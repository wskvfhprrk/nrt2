package com.jc.controller;

import com.jc.config.Result;
import com.jc.entity.Order;
import com.jc.service.impl.RedisQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * 队列控制器类，提供队列操作的REST接口
 */
@RestController
public class QueueController {

    @Autowired
    private RedisQueueService redisQueueService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加Order对象到队列
     *
     * @param order 要添加的Order对象
     * @return 成功添加订单的消息
     */
    @PostMapping("/enqueueOrder")
    public Result enqueueOrder(@RequestBody Order order) {
        order.setOrderId(UUID.randomUUID().toString().replace("-", ""));
        Long id = redisTemplate.opsForValue().increment(LocalDate.now().toString(), 1);
        order.setCustomerName("A" + (1234 + id));
        redisQueueService.enqueue(order);
        return Result.success(order);
    }

    /**
     * 从队列中取出Order对象
     *
     * @return 从队列中取出的Order对象或队列为空的消息
     */
    @GetMapping("/dequeueOrder")
    public Result dequeueOrder() {
        Order order = redisQueueService.dequeue();
        return Result.success(order);
    }

    /**
     * 获取当前队列的长度
     *
     * @return 队列长度的消息
     */
    @GetMapping("/queueSize")
    public String getQueueSize() {
        long size = redisQueueService.getQueueSize();
        return "队列长度: " + size;
    }

    /**
     * 查看队列中所有的订单
     *
     * @return 所有订单的列表
     */
    @GetMapping("/allOrders")
    public List<Order> getAllOrders() {
        return redisQueueService.peekQueue();
    }

    @GetMapping("/incrementValue")
    public String incrementValue(String key) {
        long id = redisQueueService.incrementValue(key);
        return "id值为: " + id;
    }
}
