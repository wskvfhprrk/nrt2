package com.jc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.config.ClientConfig;
import com.jc.config.IpConfig;
import com.jc.entity.Order;
import com.jc.netty.server.NettyServerHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * 提交订单
     *
     * @param order 订单对象
     * @return ResponseEntity<String> 响应实体
     */
    @PostMapping
    public ResponseEntity<String> submitOrder(@RequestBody Order order) {
        // 在这里处理订单逻辑，例如保存到数据库或其他操作
        log.info("收到订单: " + order);
        // 发送订单信息到设备
        nettyServerHandler.sendMessageToClient(ipConfig.getSend485Order(), order.toString(), false);
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

        if (allDevicesConnected) {
            statusMap.put("color", "green");
            statusMap.put("message", "所有设备已经连接，可以正常工作！");
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
