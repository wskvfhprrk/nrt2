package com.jc.controller.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.config.BeefConfig;
import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.entity.Order;
import com.jc.enums.OrderStatus;
import com.jc.enums.SignalLevel;
import com.jc.netty.server.NettyServerHandler;
import com.jc.service.impl.IODeviceService;
import com.jc.service.impl.RedisQueueService;
import com.jc.service.impl.RelayDeviceService;
import com.jc.service.impl.TurntableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 任务中心管理器
 */
@Service
@Slf4j
public class TaskCoordinator {

    @Autowired
    private PubConfig pubConfig;
    private Order order1;
    private Order order4;

    @Autowired
    private RobotPlaceEmptyBowl robotPlaceEmptyBowl;
    @Autowired
    private SoupHeatingManagement soupHeatingManagement;
    @Autowired
    private TurntableService turntableService;
    @Autowired
    private BeefConfig beefConfig;
    @Autowired
    private RelayDeviceService relayDeviceService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedisQueueService redisQueueService;
    @Autowired
    private IODeviceService iODeviceService;

    public void executeTasks() throws Exception {
        log.info("开始处理订单");
        //todo 判断转台是否在1和4两个工位才能够放置空碗
        if (pubConfig.getTurntableNumber() % 6 != 1) {
            log.info("转盘不到位，不能开始订单");
            return;
        }
        if (!pubConfig.getIsRobotStatus()) {
            log.info("机器人未初始化，不能开始订单");
            return;
        }
        if (!pubConfig.getIsResetBowl()) {
            log.info("碗未复位，不能开始订单");
            return;
        }
        //如果汤没有烧到最低温度不充许开始订单
        //先读取温度
        relayDeviceService.readTemperature();
        if (pubConfig.getSoupTemperatureValue() < Constants.SOUP_MINIMUM_TEMPERATURE_VALUE) {
            log.info("汤温度不够，不能开始订单");
            soupHeatingManagement.heatSoupToMinimumTemperature();
            return;
        }
        //汤加热到出汤温度
        soupHeatingManagement.heatSoupToMaximumTemperature();
        //初始化加碗完成
        pubConfig.setIsServingCompleted(false);
        //机器人取碗
        log.info("机器人取碗");
        Result result1 = robotPlaceEmptyBowl.takeBowl();
        //只要机器人把碗放到台上复位即可
        String status = iODeviceService.getStatus();
        String[] split = status.split(",");
        //只有当传感器感到碗放到转台上才接下来动作
        boolean flag = split[Constants.ROBOT_EMPTY_BOWL_SENSOR].equals(SignalLevel.HIGH.getValue());
        if (result1.getCode() == 200 || flag) {
            log.info("碗已经放到转台上，开始执行其它动作！");
            //从待做订单队列中取出一个订单
            Order order = redisQueueService.dequeue();
            log.info("正在处理订单：{}", order.toString());
            //加入正在做的订单redis队列中
            order.setStatus(OrderStatus.PROCESSING);
            try {
                String orderJson = objectMapper.writeValueAsString(order);
                redisTemplate.opsForList().rightPush(Constants.ORDER_REDIS_PRIMARY_KEY_IN_PROGRESS, orderJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            //todo 称重
            //送到第三个转台
            log.info("送到第三个转台");
            turntableService.alignToPosition(3);
            //下一个粉丝
            //打开所有称重盒
            //打开震动哭器下料
//                relayDeviceService.vibratorTest(beefConfig.getVibratorTime());
            //阻塞震动器震动时间
            Thread.sleep((beefConfig.getVibratorTime() + 1) * 1000);
            //转到碗加蒸汽位置
            turntableService.alignToPosition(4);
            //加蒸汽
            relayDeviceService.bowlSteam(beefConfig.getBowlSteamTime());
            Thread.sleep(1000L);
            //从正在做的队列中取出放到已经完成的
            order.setStatus(OrderStatus.PROCESSING);
            redisTemplate.opsForList().leftPop(Constants.ORDER_REDIS_PRIMARY_KEY_IN_PROGRESS);
            order.setStatus(OrderStatus.COMPLETED);
            try {
                String orderJson = objectMapper.writeValueAsString(order);
                redisTemplate.opsForList().rightPush(Constants.COMPLETED_ORDER_REDIS_PRIMARY_KEY, orderJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            //加完蒸转到第5个工位放汤
            turntableService.alignToPosition(5);
            //出汤
            relayDeviceService.soupPump(beefConfig.getSoupExtractionTime());
            //停留加汤时间
            Thread.sleep((beefConfig.getSoupExtractionTime() + 5) * 1000);
            //转到第6工位出汤
            turntableService.alignToPosition(0);
            //如果出餐口没有升起，并且出餐口有碗，出餐口碗没有取走，则开始下面操作。
//            while (!pubConfig.getServingWindowResetSensor() || !pubConfig.getThereIsABowlAtTheServingWindow() || !pubConfig.getTheBowlWasNotTakenFromTheServingWindow()) {
//                Thread.sleep(Constants.SLEEP_TIME_MS);
//                if (!pubConfig.getTheBowlWasNotTakenFromTheServingWindow()) {
//                    log.info("出餐口还有上一碗汤没有取走");
//                }
//                if (!pubConfig.getServingWindowResetSensor()) {
//                    log.info("出餐口没有复位");
//                }
//                if (!pubConfig.getThereIsABowlAtTheServingWindow()) {
//                    log.info("出餐口有碗，请取走");
//                }
//            }
            robotPlaceEmptyBowl.putBowl();
            while (!pubConfig.getIsServingCompleted()) {
                Thread.sleep(Constants.SLEEP_TIME_MS);
                continue;
            }
            //归位
            turntableService.alignToPosition(1);
            //出汤
            soupServingLogic();
            //从已经完成队列中移除
            redisTemplate.opsForList().leftPop(Constants.COMPLETED_ORDER_REDIS_PRIMARY_KEY);
        }
    }

    /**
     * 出汤逻辑
     */
    private void soupServingLogic() {
        new Thread(() -> {
            relayDeviceService.theFoodOutletIsFacingDownwards();
            //等待汤出完后才能升起
            while (pubConfig.getTheBowlWasNotTakenFromTheServingWindow()){
                log.info("上一碗汤没有取走！");
                try {
                    Thread.sleep(Constants.SLEEP_TIME_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            relayDeviceService.theFoodOutletIsFacingUpwards();
        }).start();
    }
}
