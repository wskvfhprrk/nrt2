package com.jc.mqtt;

import com.alibaba.fastjson.JSON;
import com.hejz.pay.wx.WxNativePayTemplate;
import com.jc.constants.Constants;
import com.jc.entity.Order;
import com.jc.enums.OrderStatus;
import com.jc.service.impl.RedisQueueService;
import com.jc.sign.MqttSignUtil;
import com.jc.sign.SignUtil;
import com.jc.vo.OrderPayMessage;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class MqttConsumerCallBack implements MqttCallback {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    @Lazy
    private MqttConsumerConfig mqttConsumerConfig;

    private static final int MAX_RETRIES = 3; // 最大重试次数
    private static final int RETRY_INTERVAL_MS = 3000; // 每次重试间隔，单位为毫秒
    private int retryCount = 0; // 当前重试次数
    @Autowired
    private RedisQueueService queueService;

    @Autowired
    private WxNativePayTemplate wxNativePayTemplate;
    @Autowired
    private MqttProviderConfig mqttProviderConfig;
    @Value("${machineCode}")
    private String machineCode;

    /**
     * 客户端断开连接的回调
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("与服务器断开连接，正在新重连");
        reconnect();
    }

    private void reconnect() {
        while (retryCount < MAX_RETRIES) {
            try {
                mqttConsumerConfig.connect();
                log.info("重连成功");
                retryCount = 0; // 重置重试计数器
                break;
            } catch (Exception e) {
                retryCount++;
                log.error("重连失败，第 " + retryCount + " 次尝试");

                if (retryCount >= MAX_RETRIES) {
                    log.error("达到最大重连次数，停止重连");
                    break;
                }

                try {
                    Thread.sleep(RETRY_INTERVAL_MS); // 等待指定时间后再重试
                } catch (InterruptedException ie) {
                    log.error("重连等待期间被打断", ie);
                    Thread.currentThread().interrupt(); // 恢复线程的中断状态
                    break;
                }
            }
        }
    }

    /**
     * 消息到达的回调
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
//        log.info(String.format("接收消息主题 : %s", topic));
//        log.info(String.format("接收消息Qos : %d", message.getQos()));
//        log.info(String.format("接收消息内容 : %s", new String(message.getPayload())));
//        log.info(String.format("接收消息retained : %b", message.isRetained()));
        Map map = JSON.parseObject(String.valueOf(message), Map.class);
        Boolean flag = MqttSignUtil.verifySign(map);
        if (!flag) {
            return;
        }
        log.info("通过签名验证，处理业务");
        //订单支付消息
        if (topic.split("/")[0].equals("pay")) {
            String data = map.get("data").toString();
            Map map1 = JSON.parseObject(data, Map.class);
            OrderPayMessage payMessage = JSON.parseObject(map1.get("data").toString(), OrderPayMessage.class);
            //如果支付完成就删除缓存中订单，同时增加已经支付订单
            if (payMessage.getIsPaymentCompleted()) {
                redisTemplate.delete(Constants.PAY_DATA+"::"+payMessage.getOutTradeNo());
                Order order = new Order();
                order.setCustomerName(payMessage.getOrderId());
                order.setStatus(OrderStatus.PENDING);
                queueService.enqueue(order);
                return;
            }
            //20秒内支付完成，否则二维码不见
            redisTemplate.opsForValue().set(Constants.PAY_DATA+"::"+payMessage.getOutTradeNo(), map1.get("data").toString(), Duration.ofSeconds(20));
        }
        if (topic.split("/")[0].equals("message")) {
            // TODO: 2024/10/10 处理其它消息业务逻辑
        }

        //以下是——服务端代码
        if (topic.split("/")[0].equals("order")) {
            //todo 制作订单号
            Long increment = redisTemplate.opsForValue().increment(Constants.ORDER_ID + "::" + topic.split("/")[1]) + 1000;
            // 将序列号转换为字符串并取前4位
            // 若不足4位，可以根据业务需要，决定是否进行补位（例如补0）
            String id = String.format("%04d", increment);
            String orderId = "A" + id;
            //交易订单id
            String outTradeNo = UUID.randomUUID().toString().replace("-", "");
            // TODO: 2024/10/10 钱根据实际支付测试时都为一分钱
            String s = wxNativePayTemplate.createOrder(1, outTradeNo, "取餐号："+orderId);
            System.out.println(s);
            if (s.split(",")[0].equals("200")) {
                Map map1 = JSON.parseObject(s.split(",")[1], Map.class);
                String codeUrl = map1.get("code_url").toString();
                //二维码发进mqtt中
                OrderPayMessage orderPayMessage = new OrderPayMessage();
                orderPayMessage.setPayMethod("wechat");
                orderPayMessage.setOrderId(orderId);
                orderPayMessage.setIsPaymentCompleted(false);
                orderPayMessage.setQrCodeText(codeUrl);
                orderPayMessage.setOutTradeNo(outTradeNo);
                orderPayMessage.setMachineCode(topic.split("/")[1]);
                //发送mqtt消息给客户端
                mqttProviderConfig.publishSign(0, false, "pay/" + topic.split("/")[1], SignUtil.sendSignStr(JSON.toJSONString(orderPayMessage)));
                //缓存下订单，等成功能后根据订单id再删除
                redisTemplate.opsForValue().set(Constants.PAY_ORDER_ID + "::" + orderPayMessage.getOutTradeNo(), JSON.toJSONString(orderPayMessage));
            }
        }
    }

    /**
     * 消息发布成功的回调
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

}