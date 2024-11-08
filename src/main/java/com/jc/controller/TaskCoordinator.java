package com.jc.controller;

import com.alibaba.fastjson.JSON;
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
    private RedisQueueService redisQueueService;
    @Autowired
    private IODeviceService iODeviceService;

    public void executeTasks() throws Exception {
        log.info("开始处理订单");
        Order order = redisQueueService.dequeue();
        String orderJson = JSON.toJSONString(order);
        //加入正在做的订单redis队列中
        order.setStatus(OrderStatus.PROCESSING);
        redisTemplate.opsForList().rightPush(Constants.ORDER_REDIS_PRIMARY_KEY_IN_PROGRESS, orderJson);
        Thread.sleep(2000L);
        //同时处理的事
        log.info("开始取餐口复位关闭");
        log.info("开始机器人取碗");
        log.info("开始开始汤加热");
        log.info("开始称重第一种配菜");
        log.info("开始粉丝出粉丝");
        //同时处理
        log.info("开始拿粉丝");
        log.info("开始菜勺到装菜位置");
        log.info("开始称重第二种配菜");
        log.info("开始处理订单");
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
