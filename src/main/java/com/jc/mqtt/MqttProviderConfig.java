package com.jc.mqtt;

import com.alibaba.fastjson.JSON;
import com.hejz.util.SignUtil;
import com.hejz.util.dto.SignDto;
import com.hejz.util.service.SignService;
import com.jc.config.MqttConfig;
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
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void publish(int qos, boolean retained, String topic, String message) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        mqttMessage.setPayload(message.getBytes());
        //主题的目的地，用于发布/订阅信息
        MqttTopic mqttTopic = client.getTopic(topic);
        //提供一种机制来跟踪消息的传递进度
        //用于在以非阻塞方式（在后台运行）执行发布是跟踪消息的传递进度
        MqttDeliveryToken token;
        try {
            //将指定消息发布到主题，但不等待消息传递完成，返回的token可用于跟踪消息的传递状态
            //一旦此方法干净地返回，消息就已被客户端接受发布，当连接可用，将在后台完成消息传递。
            token = mqttTopic.publish(mqttMessage);
            token.waitForCompletion();
        } catch (MqttException e) {
            if (e.getReasonCode() == 32104) {
                log.error("与服务器断开连接");
            } else {
                e.printStackTrace();
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