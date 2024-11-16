package com.jc.controller;

import com.alibaba.fastjson.JSON;
import com.jc.config.BeefConfig;
import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.entity.Order;
import com.jc.enums.OrderStatus;
import com.jc.service.RobotService;
import com.jc.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 任务中心管理器
 */
@Service
@Slf4j
public class TaskCoordinator {

    @Autowired
    private PubConfig pubConfig;
    @Autowired
    private RobotService robotService;
    @Autowired
    private FansService fansService;
    @Autowired
    private BeefConfig beefConfig;
    @Autowired
    private RelayDeviceService relayDeviceService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisQueueService redisQueueService;

    // 创建一个固定大小的线程池，线程池大小为 5
    ExecutorService executorService = Executors.newFixedThreadPool(5);
    @Autowired
    private BowlService bowlService;


    public Result executeTasks() throws Exception {
        log.info("开始处理订单");
        // TODO: 2024/11/15 先把上一次未处理完的订单作为异常订单处理退款
        Long size = redisTemplate.opsForList().size(Constants.ORDER_REDIS_PRIMARY_KEY_IN_PROGRESS);
        if (size > 0) {
            handleFaultyOrder();
        }
        Long start = System.currentTimeMillis();
        Order order = redisQueueService.dequeue();
        String orderJson = JSON.toJSONString(order);
        //加入正在做的订单redis队列中
        order.setStatus(OrderStatus.PROCESSING);
        redisTemplate.opsForList().rightPush(Constants.ORDER_REDIS_PRIMARY_KEY_IN_PROGRESS, orderJson);
        //同时处理
        // 取粉丝
        log.info("开始粉丝出粉丝");
        Result result = fansService.takeFans();
        if (result.getCode() != 200) {
            return result;
        }
        //第一种配菜
        pubConfig.setDishesAreReady(false);
        executorService.submit(() -> {
            log.info("开始称重第一种配菜");
            relayDeviceService.vegetable1Motor(1, 10);
        });
        //加蒸汽
        executorService.submit(() -> {
            addSteamToSideDishes();
        });
        executorService.submit(() -> {
            log.info("开始开始汤加热");
            relayDeviceService.soupHeatTo(beefConfig.getSoupHeatingTemperature());
        });
        //必须机器人和粉丝准备到位才可以
        while (!pubConfig.getIsRobotStatus() || !pubConfig.getAreTheFansReady()) {
            Thread.sleep(500L);
        }
        log.info("机器人拿粉丝");
        while (pubConfig.getCurrentFanBinNumber() > 4 || pubConfig.getCurrentFanBinNumber() < 0) {
            Thread.sleep(500L);
        }
        result = robotService.robotTakeFans();
        if (result.getCode() != 200) {
            return result;
        }
        //倒菜蓝
        while (!pubConfig.getIsRobotStatus() || !pubConfig.getDishesAreReady()) {
            Thread.sleep(500L);
        }
        log.info("机器人取配菜");
        result = robotService.robotTakeBasket();
        if (result.getCode() != 200) {
            return result;
        }
        //开始机器人取碗
        while (!pubConfig.getIsRobotStatus()) {
            Thread.sleep(500L);
        }
        log.info("开始机器人取碗");
        result = robotService.robotTakeBowl();
        if (result.getCode() != 200) {
            return result;
        }
        //开始放碗
        while (!pubConfig.getIsRobotStatus()) {
            Thread.sleep(Constants.SLEEP_TIME_MS);
        }
        Thread.sleep(2000L);
        log.info("开始放碗……");
        result = robotService.robotPlaceBowl();
        if (result.getCode() != 200) {
            return result;
        }
        //等加完蒸汽和机器人停止状态
        while (!pubConfig.getIsRobotStatus() || !pubConfig.getFinishAddingSteam()) {
            Thread.sleep(Constants.SLEEP_TIME_MS);
        }
        log.info("倒菜");
        result = bowlService.spoonPour();
        if (result.getCode() != 200) {
            return result;
        }

        log.info("移到装菜");
        result = bowlService.spoonLoad();
        if (result.getCode() != 200) {
            return result;
        }

        log.info("开始加汤");
        result = relayDeviceService.soupAdd(beefConfig.getSoupExtractionTime());
        if (result.getCode() != 200) {
            return result;
        }
        log.info("开始出餐");
        robotService.robotDeliverMeal();
        //从正在做的队列中取出放到已经完成的
        order.setStatus(OrderStatus.PROCESSING);
        //从队列中取出
        redisTemplate.opsForList().leftPop(Constants.ORDER_REDIS_PRIMARY_KEY_IN_PROGRESS);
        //放进做完队列中
        order.setStatus(OrderStatus.COMPLETED);
        orderJson = JSON.toJSONString(order);
        redisTemplate.opsForList().rightPush(Constants.COMPLETED_ORDER_REDIS_PRIMARY_KEY, orderJson);
        //出汤
        log.info("开始出汤");
        log.info("开始出餐口打开出餐");
        //从已经完成队列中移除
        redisTemplate.opsForList().leftPop(Constants.COMPLETED_ORDER_REDIS_PRIMARY_KEY);
        long l = (System.currentTimeMillis() - start) / 1000;
        log.info("出单时间：{}秒", l);
        return Result.success();
    }

    /**
     * 配菜加蒸汽
     *
     * @return
     */
    private Result addSteamToSideDishes() {
        Long begin = System.currentTimeMillis();
        Boolean flag = false;
        //当配菜弄完时开始加蒸汽
        while (!pubConfig.getSideDishesCompleted()) {
            try {
                Thread.sleep(Constants.SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() - begin > 180000) {
                flag = true;
                break;
            }
            if (flag) {
                log.error("3分钟机器人未完成配菜");
                return Result.error("3分钟机器人未完成配菜");
            }
        }
        log.info("到复位位置");
        Result result = bowlService.spoonReset();
        if (result.getCode() != 200) {
            return result;
        }
        log.info("加蒸汽");
        result = relayDeviceService.bowlSteamAdd(beefConfig.getBowlSteamTime());
        if (result.getCode() != 200) {
            return result;
        }
        pubConfig.setSideDishesCompleted(false);
        return Result.success();
    }

    /**
     * 处理异常订单
     */
    private void handleFaultyOrder() {
        //删除缓存中的正在做的数据
        Object o = redisTemplate.opsForList().leftPop(Constants.ORDER_REDIS_PRIMARY_KEY_IN_PROGRESS);
        if (o == null) {
            return;
        }
        //记录本地并上报服务器数据
        Order order = JSON.parseObject(o.toString(), Order.class);
        // TODO: 2024/11/15 处理异常订单
        log.info("退款订单：{}", order.getCustomerName());
        handleFaultyOrder();
    }
}
