package com.jc.mqtt;


import com.alibaba.fastjson.JSON;
import com.hejz.util.SignUtil;
import com.hejz.util.dto.SignDto;
import com.hejz.util.service.SignService;
import com.jc.config.PubConfig;
import com.jc.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 创建控制器测试发布信息
 */
@Controller
@Slf4j
public class SendController {
    @Autowired
    private MqttProviderConfig providerClient;
    @Value("${machineCode}")
    private String machineCode;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SignService signService;
    @Autowired
    private PubConfig pubConfig;

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
//    @Scheduled(cron = "0 0/1 * * * ? ")
    public void heartbeat() {
        //mqtt没有连接向服务器发送
        if(!pubConfig.getMqttConnectStatus()){
            return;
        }
        Object o = redisTemplate.opsForValue().get(Constants.APP_SECRET_REDIS_KEY);
        if(o==null){
            log.error("没有密钥");
            return;
        }
        SignDto dto=new SignDto();
        dto.setData(null);
        dto.setTimestamp(System.currentTimeMillis());
        dto.setNonce(SignUtil.generateNonce(16));
        try {
            sendMessage(0, false, "heartbeat/" + machineCode, JSON.toJSONString(signService.signDataToVo(dto,String.valueOf(o))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}