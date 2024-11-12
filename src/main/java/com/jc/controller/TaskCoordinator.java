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
    private SoupHeatingManagement soupHeatingManagement;
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
    @Autowired
    private IODeviceService iODeviceService;

    // 创建一个固定大小的线程池，线程池大小为 5
    ExecutorService executorService = Executors.newFixedThreadPool(5);


    public void executeTasks() throws Exception {
        log.info("开始处理订单");
        Order order = redisQueueService.dequeue();
        String orderJson = JSON.toJSONString(order);
        //加入正在做的订单redis队列中
        order.setStatus(OrderStatus.PROCESSING);
        redisTemplate.opsForList().rightPush(Constants.ORDER_REDIS_PRIMARY_KEY_IN_PROGRESS, orderJson);
        //同时处理的事
        log.info("开始取餐口复位关闭");
        // TODO: 2024/11/8 开始取餐口复位关闭
        log.info("开始粉丝出粉丝");
        executorService.submit(() -> {
            fansService.takeFans();
        });
        log.info("开始机器人取碗");
        executorService.submit(() -> {
            robotService.robotTakeBowl();
        });
        log.info("开始开始汤加热");
        executorService.submit(() -> {
            relayDeviceService.soupHeatTo(beefConfig.getSoupHeatingTemperature());
        });
        log.info("开始称重第一种配菜");
        // TODO: 2024/11/8 开始称重第一种配菜
        executorService.submit(() -> {
            relayDeviceService.vegetable1Motor(1, 10);
        });
        while (!pubConfig.getIsRobotStatus()) {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("开始放碗……");
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robotService.robotPlaceBowl();
        //同时处理
        log.info("开始拿粉丝");
        //必须机器人和粉丝准备到位才可以
        while (!pubConfig.getIsRobotStatus() && pubConfig.getAreTheFansReady()) {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robotService.robotTakeFans();
        log.info("开始取配菜");
        robotService.robotTakeBasket();
//        log.info("开始称重第二种配菜");
//        log.info("开始处理订单");
        //制作汤
        log.info("开始倒菜");
        log.info("开始加蒸汽和水");
        //从正在做的队列中取出放到已经完成的
        order.setStatus(OrderStatus.PROCESSING);
        redisTemplate.opsForList().leftPop(Constants.ORDER_REDIS_PRIMARY_KEY_IN_PROGRESS);
        order.setStatus(OrderStatus.COMPLETED);
        orderJson = JSON.toJSONString(order);
        redisTemplate.opsForList().rightPush(Constants.COMPLETED_ORDER_REDIS_PRIMARY_KEY, orderJson);
        Thread.sleep(2000L);
        //出汤
        log.info("开始出汤");
        log.info("开始出餐口打开出餐");
        //从已经完成队列中移除
        redisTemplate.opsForList().leftPop(Constants.COMPLETED_ORDER_REDIS_PRIMARY_KEY);
    }
}
