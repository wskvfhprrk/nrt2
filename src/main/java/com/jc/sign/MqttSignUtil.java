package com.jc.sign;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
public class MqttSignUtil {
    public static Boolean verifySign(Map map){
        //签名验证
        boolean b = SignUtil.verifySign2(map);
        if(!b){
            log.error("签名验证不通过！");
            return false;
        }
        //时间是不是大于10分钟
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime time1 = LocalDateTime.parse(map.get("time").toString(), formatter);
        Duration duration = Duration.between(time1, LocalDateTime.now());
        long minutes = duration.toMinutes();
        if (minutes <= 10) {
//            log.info("两个时间段相差10分钟");
        } else {
            log.error("两个时间段相差超过10分钟");
            return false;
        }
        return true;
    }
}
