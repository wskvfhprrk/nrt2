package com.jc.service.impl;

import com.jc.config.PubConfig;
import com.jc.config.Result;
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
        //先modbus验证，如果验证不过就不管
        boolean b = CRC16.validateCRC(HexConvert.hexStringToBytes(message));
        if(!b){
            return;
        }
        String s = message.substring(0, 2);
        Integer integer = Integer.valueOf(s);
        //计算汤的温度值
        if (integer.equals(Constants.SOUP_TEMPERATURE_SENSOR)) {
            CalculateSoupTemperatureValue(message);
        }
        //计算重量
        if(integer.equals(Constants.WEIGHT_SENSOR_ONE_TO_FOUR)){
            calculateWeight(message);
        }
    }

    /**
     * 计算重量
     * @param message
     * @return
     */
    private int[] calculateWeight(String message) {
        // 01 03 10 00 00 03 E8 00 00 07 D0 00 00 0B B8 00 00 0F A0 A8 23
        int[] sensorValues = new int[4];
        for (int i = 0; i < 4; i++) {
            int startIndex = 3 + i * 4;
            String substring = message.substring(startIndex, startIndex + 4);
            int number = Integer.parseInt(substring, 16);
            sensorValues[i]=number;
            log.info("称重传感器 {} 的值为：{} g",i,number);
        }
        pubConfig.setCalculateWeight(sensorValues);
        return sensorValues;
    }

    /**
     * 计算汤传感器数据
     * @param message
     */
    private void CalculateSoupTemperatureValue(String message) {
        //01 03 02 01 20 B8 0C
        //截取第六位到第10位数据位
        String substring = message.substring(6, 10);
        int i = Integer.parseInt(substring, 16);
        double soupTemperatureValue = i / 10.0;
        pubConfig.setSoupTemperatureValue(soupTemperatureValue);
        log.info("测量汤的温度为：{} 度", soupTemperatureValue);
    }
}
