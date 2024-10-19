package com.jc.mqtt;

import com.alibaba.fastjson.JSON;
import com.hejz.util.service.SignService;
import com.hejz.util.vo.SignVo;
import com.jc.constants.Constants;
import com.jc.entity.Order;
import com.jc.enums.OrderStatus;
import com.jc.service.impl.RedisQueueService;
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

@Component
@Slf4j
public class MqttConsumerCallBack implements MqttCallback {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SignService signService;
    @Autowired
    @Lazy
    private MqttConsumerConfig mqttConsumerConfig;

    private static final int MAX_RETRIES = 3; // 最大重试次数
    private static final int RETRY_INTERVAL_MS = 3000; // 每次重试间隔，单位为毫秒
    private int retryCount = 0; // 当前重试次数
    @Autowired
    private RedisQueueService queueService;

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
        log.info(String.format("接收消息主题 : %s", topic));
        log.info(String.format("接收消息Qos : %d", message.getQos()));
        log.info(String.format("接收消息内容 : %s", new String(message.getPayload())));
        log.info(String.format("接收消息retained : %b", message.isRetained()));
        String[] split = topic.split("/");
        if (split.length == 2 && !topic.split("/")[1].equals(machineCode)) {
            //不是此服务器的，不做处理
            return;
        }
        if (split.length == 3 && !topic.split("/")[2].equals(machineCode)) {
            //不是此服务器的，不做处理
            return;
        }
        if (topic.split("/")[0].equals("key")) {
            //获取密钥返回值
            Map map = JSON.parseObject(String.valueOf(message), Map.class);
            if (map.get("code").equals(200)) {
                redisTemplate.opsForValue().set(Constants.APP_SECRET_REDIS_KEY, map.get("msg").toString());
                return;
            }
        }
        Object o = redisTemplate.opsForValue().get(Constants.APP_SECRET_REDIS_KEY);
        if (o == null) {
            log.error("查找不到密钥");
            return;
        }
        Boolean flag = signService.verifyData(String.valueOf(message), String.valueOf(o));
        if (!flag) {
            log.error("未通过签名验证");
            return;
        }

        //订单支付消息
        if (topic.split("/")[1].equals("pay")) {
            String data = JSON.parseObject(String.valueOf(message), SignVo.class).getData();
            OrderPayMessage payMessage = JSON.parseObject(data, OrderPayMessage.class);
            //如果支付完成就删除缓存中订单，同时增加已经支付订单——通知机器制作
            redisTemplate.opsForValue().set(Constants.PAY_DATA + "::" + payMessage.getOutTradeNo(), data, Duration.ofSeconds(20));
        }
        if (topic.split("/")[1].equals("paySuccess")) {
            String data = JSON.parseObject(String.valueOf(message), SignVo.class).getData();
            Order order = JSON.parseObject(data, Order.class);
            //如果支付完成就删除缓存中订单，同时增加已经支付订单——通知机器制作
            //删除二维码缓存
            redisTemplate.delete(Constants.PAY_DATA + "::" + order.getOrderId());
            queueService.enqueue(order);

        }
    }

    /**
     * 消息发布成功的回调
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

}