package com.jc.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.constants.Constants;
import com.jc.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Redis队列服务，用于实现FIFO队列操作
 */
@Service
@Slf4j
public class RedisQueueService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();  // 用于JSON序列化和反序列化的对象

    // 将Order对象添加到队列末尾
    public void enqueue(Order order) {
        try {
            String orderJson = objectMapper.writeValueAsString(order);
            redisTemplate.opsForList().rightPush(Constants.PENDING_ORDER_REDIS_PRIMARY_KEY, orderJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // 打印序列化错误
        }
    }

    // 从队列中取出Order对象
    public Order dequeue() {
        String orderJson = redisTemplate.opsForList().leftPop(Constants.PENDING_ORDER_REDIS_PRIMARY_KEY);
        if (orderJson != null) {
            try {
                return objectMapper.readValue(orderJson, Order.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace(); // 打印反序列化错误
            }
        }
        return null; // 如果队列为空或发生错误，返回null
    }

    // 获取队列长度
    public long getQueueSize() {
        return redisTemplate.opsForList().size(Constants.PENDING_ORDER_REDIS_PRIMARY_KEY);
    }

    // 查看队列中的内容（不取出元素）
    public List<Order> peekQueue() {
        // 获取Redis队列中的元素范围
        List<String> orderJsonList = redisTemplate.opsForList().range(Constants.PENDING_ORDER_REDIS_PRIMARY_KEY, 0, -1);
        if (orderJsonList != null) {
            // 将JSON字符串转换为Order对象列表
            return orderJsonList.stream().map(orderJson -> {
                try {
                    return objectMapper.readValue(orderJson, Order.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 增加指定 key 的值
     */
    public Long incrementValue(String key) {
        Long newValue = redisTemplate.opsForValue().increment(key, 1);
        return newValue;
    }
}
