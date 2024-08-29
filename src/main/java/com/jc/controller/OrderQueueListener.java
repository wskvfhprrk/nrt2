package com.jc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.config.PubConfig;
import com.jc.controller.control.TaskCoordinator;
import com.jc.entity.Order;
import com.jc.enums.OrderStatus;
import com.jc.service.impl.RedisQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    // 每秒钟检查一次队列中的订单
    @Scheduled(fixedRate = 1000) // 1秒
    public void checkAndProcessOrders() {
        //取出订单时机——有订单并且在编号为1或4工位时
        if (redisQueueService.getQueueSize() > 0) {
            //todo 当id等于4时也可以
            if (pubConfig.getTurntableNumber() % 6 == 1) {
                Order order = redisQueueService.dequeue();
                log.info("正在处理订单：{}",order.toString());
                try {
                    taskCoordinator.executeTasks(order);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
