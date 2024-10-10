package com.jc.mqtt;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.sign.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建控制器测试发布信息
 */
@Controller
public class SendController {
    @Autowired
    private MqttProviderConfig providerClient;
    @Value("${machineCode}")
    private String machineCode;

    @RequestMapping("/sendMessage")
    @ResponseBody
    public String sendMessage(int qos, boolean retained, String topic, String message) {
        try {
            providerClient.publish(qos, retained, topic, message);
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "发送失败";
        }
    }

    /**
     * 心跳——每分钟向服务器发送信息
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void heartbeat() {
        String value = SignUtil.sendSignStr("null");
        sendMessage(0, false, "heartbeat/" + machineCode, value);
    }
}