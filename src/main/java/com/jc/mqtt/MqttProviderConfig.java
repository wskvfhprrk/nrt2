package com.jc.mqtt;

import com.alibaba.fastjson.JSON;
import com.hejz.util.SignUtil;
import com.hejz.util.dto.SignDto;
import com.hejz.util.service.SignService;
import com.jc.config.MqttConfig;
import com.jc.config.PubConfig;
import com.jc.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * 消息发布者客户端配置
 */
@Configuration
@Slf4j
public class MqttProviderConfig {

    @Autowired
    private MqttConfig mqttConfig;
    @Autowired
    private SignService signService;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 客户端对象
     */
    private MqttClient client;

    @Autowired
    private MqttProviderCallBack mqttProviderCallBack;
    @Autowired
    private PubConfig pubConfig;


    /**
     * 客户端连接服务端
     */
    public void connect() {
        try {
            //创建MQTT客户端对象
            client = new MqttClient(mqttConfig.getUrl(), mqttConfig.getSend_id(), new MemoryPersistence());
            //连接设置
            MqttConnectOptions options = new MqttConnectOptions();
            //是否清空session，设置false表示服务器会保留客户端的连接记录（订阅主题，qos）,客户端重连之后能获取到服务器在客户端断开连接期间推送的消息
            //设置为true表示每次连接服务器都是以新的身份
            options.setCleanSession(true);
            //设置连接用户名
            options.setUserName(mqttConfig.getUsername());
            //设置连接密码
            options.setPassword(mqttConfig.getPassword().toCharArray());
            //设置超时时间，单位为秒
            options.setConnectionTimeout(100);
            //设置心跳时间 单位为秒，表示服务器每隔 1.5*20秒的时间向客户端发送心跳判断客户端是否在线
            options.setKeepAliveInterval(20);
            //设置遗嘱消息的话题，若客户端和服务器之间的连接意外断开，服务器将发布客户端的遗嘱信息
            options.setWill("willTopic", (mqttConfig.getSend_id() + "与服务器断开连接").getBytes(), 0, false);
            //设置回调
            client.setCallback(mqttProviderCallBack);
            client.connect(options);
            pubConfig.setMqttConnectStatus(true);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void publish(int qos, boolean retained, String topic, String message) {
        if (client == null || !client.isConnected()) {
            log.warn("MQTT client not connected, attempting to reconnect...");
            connect();
            if (client == null || !client.isConnected()) {
                log.error("Failed to connect MQTT client");
                return;
            }
        }

        try {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setQos(qos);
            mqttMessage.setRetained(retained);
            mqttMessage.setPayload(message.getBytes());
            
            MqttTopic mqttTopic = client.getTopic(topic);
            MqttDeliveryToken token = mqttTopic.publish(mqttMessage);
            token.waitForCompletion();
            
            log.info("Message published successfully to topic: {}", topic);
        } catch (MqttException e) {
            if (e.getReasonCode() == 32104) {
                log.error("与服务器断开连接");
                pubConfig.setMqttConnectStatus(false);
                // Attempt to reconnect
                connect();
            } else {
                log.error("MQTT publish error: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * 发送加密信息
     *
     * @param qos
     * @param retained
     * @param topic
     * @param s
     */
    public void publishSign(int qos, boolean retained, String topic, String s) throws Exception {
        Object o = redisTemplate.opsForValue().get(Constants.APP_SECRET_REDIS_KEY);
        if (o == null) {
            log.error("没有密钥");
            return;
        }
        String string = JSON.toJSONString(signService.signByData(s,String.valueOf(o)));
        try {
            this.publish(qos, retained, topic, string);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
