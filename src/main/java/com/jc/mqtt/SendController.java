package com.jc.mqtt;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

/**
 * 创建控制器测试发布信息
 */
@Controller
public class SendController {
    @Autowired
    private MqttProviderConfig providerClient;

    @RequestMapping("/sendMessage")
    @ResponseBody
    public String sendMessage(int qos,boolean retained,String topic,String message){
        try {
            providerClient.publish(qos, retained, topic, message);
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "发送失败";
        }
    }
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void init(){
            sendMessage(1,true,"topic5", "{\n" +
                    "  \"date\":" +LocalDateTime.now()+
                    "\n}");
    }
}