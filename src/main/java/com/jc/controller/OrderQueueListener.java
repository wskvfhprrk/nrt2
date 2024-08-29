package com.jc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.config.PubConfig;
import com.jc.constants.Constants;
import com.jc.controller.control.SoupHeatingManagement;
import com.jc.controller.control.TaskCoordinator;
import com.jc.entity.Order;
import com.jc.enums.OrderStatus;
import com.jc.service.impl.RedisQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


/**
 * 订单队列监听器类，定期检查队列中的未处理订单并处理它们
 */
@Service
@Slf4j
public class OrderQueueListener {

    @Autowired
    private RedisQueueService redisQueueService; // 业务逻辑服务
    @Autowired
    private PubConfig pubConfig;
    @Autowired
    private TaskCoordinator taskCoordinator;
    @Autowired
    private SoupHeatingManagement soupHeatingManagement;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;


    // 每秒钟检查一次队列中的订单
    @Scheduled(fixedRate = 1000) // 1秒
    public void checkAndProcessOrders()  {
        //取出订单时机——有订单并且在编号为1或4工位时
        if (redisQueueService.getQueueSize() > 0) {
            //todo 当id等于4时也可以 还有一个条件温度达到时才可以
            //如果汤没有烧到最低温度不充许开始订单
            if (pubConfig.getSoupTemperatureValue() < Constants.SOUP_MINIMUM_TEMPERATURE_VALUE) {
                log.error("汤在温度不够，加热汤至汤的温度");
                soupHeatingManagement.heatSoupToMaximumTemperature();
            }
            if (pubConfig.getTurntableNumber() % 6 == 1) {
                Order order = redisQueueService.dequeue();
                log.info("正在处理订单：{}",order.toString());
                //加入当前正在做的订单redis中
                order.setStatus(OrderStatus.PROCESSING);
                String orderJson = null;
                try {
                    orderJson = objectMapper.writeValueAsString(order);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                redisTemplate.opsForList().rightPushIfPresent(Constants.ORDER_REDIS_PRIMARY_KEY_IN_PROGRESS,orderJson);
                try {
                    taskCoordinator.executeTasks(order);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}