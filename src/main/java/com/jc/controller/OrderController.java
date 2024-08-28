package com.jc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.config.ClientConfig;
import com.jc.config.IpConfig;
import com.jc.config.PubConfig;
import com.jc.controller.control.TaskCoordinator;
import com.jc.entity.Order;
import com.jc.netty.server.NettyServerHandler;
import com.jc.service.impl.RedisQueueService;
import com.jc.service.impl.Reset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * 订单控制器
 */
@Slf4j
@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:8080") // 允许来自该域的请求
public class OrderController {

    @Autowired
    private NettyServerHandler nettyServerHandler;
    @Autowired
    private IpConfig ipConfig;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ClientConfig clientConfig;
    @Autowired
    private TaskCoordinator taskCoordinator;
    @Autowired
    private PubConfig pubConfig;
    @Autowired
    private RedisQueueService queueService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 提交订单
     *
     * @param order 订单对象
     * @return ResponseEntity<String> 响应实体
     */
    @PostMapping
    public ResponseEntity<String> submitOrder(@RequestBody Order order) throws Exception {
        // 在这里处理订单逻辑，例如保存到数据库或其他操作
        log.info("收到订单: " + order);
        // 订单排列中
        order.setOrderId(UUID.randomUUID().toString().replace("-", ""));
        Long id = redisTemplate.opsForValue().increment(LocalDate.now().toString(), 1);
        order.setCustomerName("A" + (1234 + id));
        queueService.enqueue(order);
        // TODO: 2024/8/28 处理订单需要一个监听事件
//        taskCoordinator.executeTasks(order);
        return new ResponseEntity<>("订单提交成功", HttpStatus.OK);
    }

    @GetMapping("serverStatus")
    public String serverStatus() {
        Map<String, String> statusMap = checkDeviceStatus();

        String jsonResponse;
        try {
            jsonResponse = objectMapper.writeValueAsString(statusMap);
        } catch (JsonProcessingException e) {
            jsonResponse = "{\"color\":\"red\",\"message\":\"服务器状态检查时发生错误。\"}";
        }

        return jsonResponse;
    }

    private Map<String, String> checkDeviceStatus() {
        Map<String, String> statusMap = new HashMap<>();

        boolean allDevicesConnected = clientConfig.getSend485Order() && clientConfig.getDocuOnLine() &&
                clientConfig.getIOdevice() && clientConfig.getReceive485Singal() &&
                clientConfig.getRelayDevice();

        if (allDevicesConnected && pubConfig.getAllDevicesConnectedStatus()) {
            statusMap.put("color", "green");
            statusMap.put("message", "设备正常可以正常使用");

        } else if (allDevicesConnected) {
            statusMap.put("color", "orange");
            statusMap.put("message", "设备自检中，请稍后……");
        } else {
            statusMap.put("color", "red");
            StringBuilder missingDevicesMessage = new StringBuilder();

            if (!clientConfig.getIOdevice()) {
                appendWithComma(missingDevicesMessage, "IO采集设备");
            }
            if (!clientConfig.getRelayDevice()) {
                appendWithComma(missingDevicesMessage, "继电器设备");
            }
            if (!clientConfig.getSend485Order()) {
                appendWithComma(missingDevicesMessage, "发送485指令设备");
            }
            if (!clientConfig.getReceive485Singal()) {
                appendWithComma(missingDevicesMessage, "485监听数据设备");
            }
            if (!clientConfig.getDocuOnLine()) {
                appendWithComma(missingDevicesMessage, "机器人");
            }
            statusMap.put("message", missingDevicesMessage.append("未连接").toString());
        }

        return statusMap;
    }

    private void appendWithComma(StringBuilder sb, String message) {
        if (sb.length() > 0) {
            sb.append("、");
        }
        sb.append(message);
    }
}
