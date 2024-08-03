package com.jc.service.impl;

import com.jc.config.PubConfig;
import com.jc.constants.Constants;
import com.jc.service.DeviceHandler;
import com.jc.utils.CRC16;
import com.jc.utils.HexConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 收到485信号
 */
@Service
@Slf4j
public class Receive485SignalService implements DeviceHandler {

    @Autowired
    private PubConfig pubConfig;
    /**
     * 处理消息
     *
     * @param message 消息内容
     * @param isHex   是否为16进制消息
     */
    @Override
    public void handle(String message, boolean isHex) {
        if (isHex) {
            log.info("收到485信号——HEX: {}", message);
            //解析指令
            ParseCommand(message);
        } else {
            log.info("收到485信号——普通消息: {}", message);
            // 在这里添加处理普通字符串消息的逻辑
        }
    }

    /**
     * 解析指令
     *
     * @param message
     */
    private void ParseCommand(String message) {
        String s = message.substring(0, 2);
        Integer integer = Integer.valueOf(s);
        //先modbus验证，如果验证不过就不管
        boolean b = CRC16.validateCRC(HexConvert.hexStringToBytes(message));
        if(!b){
            return;
        }
        //计算汤的温度值
        if (integer.equals(Constants.SOUP_TEMPERATURE_SENSOR)) {
            CalculateSoupTemperatureValue(message);
        }
    }

    /**
     * @param message
     */
    private void CalculateSoupTemperatureValue(String message) {
        //01 03 02 01 20 B8 0C
        //截取第六位到第10位数据位
        String substring = message.substring(6, 10);
        int i = Integer.parseInt(substring, 16);
        pubConfig.setSoupTemperatureValue(i/10.0);
        log.info("测量汤的温度为：{}",i/10.0);
    }
}
