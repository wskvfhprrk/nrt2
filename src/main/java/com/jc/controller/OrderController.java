package com.jc.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.config.ClientConfig;
import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.entity.Order;
import com.jc.enums.OrderStatus;
import com.jc.mqtt.MqttProviderConfig;
import com.jc.vo.OrderPayMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单控制器
 */
@Slf4j
@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:8080") // 允许来自该域的请求
public class OrderController {

    @Autowired
    private ClientConfig clientConfig;
    @Autowired
    private PubConfig pubConfig;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MqttProviderConfig mqttProviderConfig;
    @Value("${machineCode}")
    private String machineCode;

    /**
     * 提交订单
     *
     * @param order 订单对象
     * @return ResponseEntity<String> 响应实体
     */
    @PostMapping
    public ResponseEntity<String> submitOrder(@RequestBody Order order) throws Exception {
        //发送mqtt消息
        String topic = "order/" + machineCode;
        mqttProviderConfig.publishSign(0, false, topic, JSON.toJSONString(order));
        return new ResponseEntity<>("订单提交成功,请根据支付订单号取餐", HttpStatus.OK);
    }

    @GetMapping("serverStatus")
    public String serverStatus() {
        Map<String, String> statusMap = checkDeviceStatus();

        String jsonResponse;
        jsonResponse = JSON.toJSONString(statusMap);
        return jsonResponse;
    }

    private Map<String, String> checkDeviceStatus() {
        Map<String, String> statusMap = new HashMap<>();

        boolean allDevicesConnected = clientConfig.getSend485Order() && clientConfig.getDocuOnLine() &&
                clientConfig.getIOdevice() && clientConfig.getReceive485Singal() &&
                clientConfig.getRelayDevice();

        if (false) { //todo 如果有订单制作时
            //todo 轮播待制作，正在制作和已经做好的订单
        } else if (allDevicesConnected && pubConfig.getAllDevicesConnectedStatus()) {
            statusMap.put("color", "green");
            statusMap.put("message", "请您点餐！");
        } else if (allDevicesConnected) {
            statusMap.put("color", "orange");
            statusMap.put("message", "设备自检中，请稍后……");
        } else {
            statusMap.put("color", "green");
//            statusMap.put("color", "red");
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

    @GetMapping("/status")
    public Result<OrderStatusResponse> getOrderStatus() {
        try {
            // 读取Redis队列中的内容
            List<String> pendingOrders = redisTemplate.opsForList().range(Constants.PENDING_ORDER_REDIS_PRIMARY_KEY, 0, -1);
            List<String> inProgressOrders = redisTemplate.opsForList().range(Constants.ORDER_REDIS_PRIMARY_KEY_IN_PROGRESS, 0, -1);
            List<String> completedOrders = redisTemplate.opsForList().range(Constants.COMPLETED_ORDER_REDIS_PRIMARY_KEY, 0, -1);

            List<Order> pendingOrdersList = pendingOrders.stream().map(s -> {
                return JSON.parseObject(s, Order.class);
            }).collect(Collectors.toList());
            List<Order> inProgressOrdersList = inProgressOrders.stream().map(s -> {
                return JSON.parseObject(s, Order.class);
            }).collect(Collectors.toList());
            List<Order> completedOrdersList = completedOrders.stream().map(s -> {
                return JSON.parseObject(s, Order.class);
            }).collect(Collectors.toList());
            // 创建OrderStatusResponse对象
            OrderStatusResponse orderStatusResponse = new OrderStatusResponse(pendingOrdersList, inProgressOrdersList, completedOrdersList);

            // 使用Result.success()方法返回结果
            return Result.success(orderStatusResponse);

        } catch (Exception e) {
            // 如果发生异常，返回错误信息
            return Result.error(500, "获取订单数据失败");
        }
    }

    public static class OrderStatusResponse {
        private List<Order> pendingOrders;
        private List<Order> inProgressOrders;
        private List<Order> completedOrders;

        public OrderStatusResponse(List<Order> pendingOrders, List<Order> inProgressOrders, List<Order> completedOrders) {
            this.pendingOrders = pendingOrders;
            this.inProgressOrders = inProgressOrders;
            this.completedOrders = completedOrders;
        }

        public List<Order> getPendingOrders() {
            return pendingOrders;
        }

        public void setPendingOrders(List<Order> pendingOrders) {
            this.pendingOrders = pendingOrders;
        }

        public List<Order> getInProgressOrders() {
            return inProgressOrders;
        }

        public void setInProgressOrders(List<Order> inProgressOrders) {
            this.inProgressOrders = inProgressOrders;
        }

        public List<Order> getCompletedOrders() {
            return completedOrders;
        }

        public void setCompletedOrders(List<Order> completedOrders) {
            this.completedOrders = completedOrders;
        }
    }

    @GetMapping("qrcode")
    public Result qrcode() {
        Object o = redisTemplate.opsForValue().get(Constants.PAY_DATA);
        if(o!=null){
            OrderPayMessage orderPayMessage = JSON.parseObject(o.toString(), OrderPayMessage.class);
            return Result.success(orderPayMessage);
        }
        return Result.success(o);
    }

    @GetMapping("test")
    public Result test(){
        String s="{\"isPaymentCompleted\":false,\"orderId\":\"A0000\",\"payMethod\":\"wechat\",\"qrCodeText\":\"weixin://wxpay/bizpayurl?pr=O8AnTMgz3\"}";
        redisTemplate.opsForValue().set(Constants.PAY_DATA+"::A0000001",s);
        Map<String, Object> result = new HashMap<>();

        // 构建 ScanOptions 来匹配以指定前缀开头的键
        ScanOptions scanOptions = ScanOptions.scanOptions().match(Constants.PAY_DATA + "*").count(100).build();

        // 使用 RedisConnection 执行 scan 操作
        redisTemplate.execute((RedisCallback) connection -> {
            // 使用 RedisConnection 的 scan 方法获取匹配的键
            Cursor<byte[]> cursor = connection.scan(scanOptions);
            while (cursor.hasNext()) {
                String key = new String(cursor.next());
                // 使用 RedisTemplate 获取键对应的值
                Object value = redisTemplate.opsForValue().get(key);
                result.put(key, value);
            }
            return null;
        });

        return Result.success(result);
    }
}
