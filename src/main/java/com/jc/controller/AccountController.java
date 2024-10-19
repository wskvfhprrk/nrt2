package com.jc.controller;

import com.alibaba.fastjson.JSON;
import com.hejz.util.service.SignService;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.mqtt.MqttConsumerConfig;
import com.jc.mqtt.MqttProviderConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

@RestController
@RequestMapping("setAccount")
@Slf4j
public class AccountController {

    @Value("${machineCode}")
    private String machineCode;

    @Autowired
    private MqttProviderConfig mqttProviderConfig;

    @GetMapping("getCode")
    public Result getCode(){
        return Result.success(machineCode);
    }

    @PostMapping("getKey")
    public Result getKey(@RequestBody Map map){
//        Map map=new HashMap<>();
//        map.put("code",code);
//        map.put("password",password);
        //发送信息
        mqttProviderConfig.publish(0,false,"getKey/"+machineCode, JSON.toJSONString(map));
        return Result.success();
    }

}
