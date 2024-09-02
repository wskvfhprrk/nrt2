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
    private IngredientPreparation ingredientPreparation;
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
        if(!pubConfig.getIsResetBowl()){
            log.info("碗未复位，不能开始订单");
            return;
        }
        //如果汤没有烧到最低温度不充许开始订单
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
        if (pubConfig.getTurntableNumber() == 3) {
            // TODO: 2024/7/27 打开称重传感器料仓
            // TODO: 2024/7/27 粉丝货道掉下来
            // TODO: 2024/7/27 震动器打开后自动关闭
            Thread.sleep(5000L);//模拟下配料
            //旋转到第四个工位
            turntableService.alignToPosition(4);
        }
        if (pubConfig.getTurntableNumber() == 4) {
            // TODO: 2024/7/27 盖板盖到碗上
            // TODO: 2024/7/27 打开蒸汽
            Thread.sleep(5000L);//模拟下配料
            //旋转到第起始工位
            turntableService.alignToPosition(5);
        }
        if (pubConfig.getTurntableNumber() == 5) {
            //放汤

        }
    }

    /**
     * 出汤逻辑
     */
    private void soupServingLogic() {
        new Thread(() -> {
            relayDeviceService.theFoodOutletIsFacingDownwards();
            try {
                Thread.sleep(beefConfig.getTheFoodOutletIsFacingDownwardsTime() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            relayDeviceService.theFoodOutletIsFacingUpwards();
        }).start();
    }
}
