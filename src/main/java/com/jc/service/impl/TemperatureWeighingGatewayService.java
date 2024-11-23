package com.jc.service.impl;

import com.jc.config.IpConfig;
import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.netty.server.NettyServerHandler;
import com.jc.service.DeviceHandler;
import com.jc.utils.CRC16;
import com.jc.utils.ModbusCalibration;
import com.jc.utils.WeightParserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 收到485信号
 */
@Service
@Slf4j
public class TemperatureWeighingGatewayService implements DeviceHandler {

    @Autowired
    private PubConfig pubConfig;
    @Autowired
    private NettyServerHandler nettyServerHandler;
    @Autowired
    private Relay2DeviceGatewayService relay2DeviceGatewayService;
    @Autowired
    private IpConfig ipConfig;
    @Autowired
    private Relay1DeviceGatewayService relay1DeviceGatewayService;
    //发送中状态——true正在发送中——不允许再次发送指令，等待其返回值后才可发送，false已经有返回值
    private Boolean sendHexStatus = false;

    /**
     * 处理消息
     *
     * @param message 消息内容
     * @param isHex   是否为16进制消息
     */
    @Override
    public void handle(String message, boolean isHex) {
        sendHexStatus = false;
//        log.info("温度称重接收到HEX消息: {}", message);
        if (message == null || message.equals("null") || message.length() == 0) return;
        ParseCommand(message);
    }

    /**
     * 发送指令
     *
     * @param hexStr
     */
    private void sendOrder(String hexStr) {
        while (sendHexStatus) {
            try {
                Thread.sleep(Constants.COMMAND_INTERVAL_POLLING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sendHexStatus = true;
//        log.info("温度称重发送HEX消息: {}", hexStr);
        nettyServerHandler.sendMessageToClient(ipConfig.getTemperatureWeighingGateway(), hexStr, true);
    }

    /**
     * 解析指令
     *
     * @param message
     */
    private void ParseCommand(String message) {
        //先modbus验证，如果验证不过就不管
        try {
            if (message == null) return;
            boolean b = CRC16.validateCRC(message);
            if (!b) {
                return;
            }
            String s = message.substring(0, 2);
            Integer integer = Integer.valueOf(s);
            //计算汤的温度值
            if (integer.equals(Constants.SOUP_TEMPERATURE_SENSOR)) {
                CalculateSoupTemperatureValue(message);
            }
            //计算重量
            if (integer.equals(Constants.WEIGHT_SENSOR_ONE_TO_FOUR)) {
                calculateWeight(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
    }

    /**
     * 读取汤的温度值
     *
     * @return
     */
    public Result readTemperature() {
        sendOrder(Constants.READ_SOUP_TEMPERATURE_COMMAND);
        return Result.success(pubConfig.getSoupTemperatureValue());
    }


    /**
     * 配菜称重盒关闭打开（编号）
     *
     * @param number
     * @return
     */
    public Result vegetableMotor(Integer number) {
        //打开震动筛上面的开关
        relay1DeviceGatewayService.vibrationSwitchOn();
        relay1DeviceGatewayService.relayOpening(Constants.Y_SHAKER_SWITCH_1);
        return Result.success();
    }

    /**
     * 配菜称重盒关闭关闭（编号）
     *
     * @param number
     */
    public Result vegetableMotorStop(Integer number) {
        //关闭震动盘
        relay1DeviceGatewayService.relayClosing(Constants.Y_SHAKER_SWITCH_1);
        //打开盒子，然后关闭
        relay2DeviceGatewayService.openWeighBox(1);
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        relay2DeviceGatewayService.closeWeighBox(1);
        //震动筛上开关关闭
        relay1DeviceGatewayService.vibrationSwitchOff();
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.success();
    }

    /**
     *
     */
    public Result closeWeighingBox(int number) {
        relay2DeviceGatewayService.closeWeighBox(number);
        relay1DeviceGatewayService.openClose(Constants.Y_SHAKER_SWITCH_1, 10);
//        relay1DeviceGatewayService.openClose(Constants.Y_SHAKER_SWITCH_2, 10);
        return Result.success();
    }

    /**
     * 配菜电机（KG）
     *
     * @param i
     * @param number
     * @return
     */
    public Result vegetable1Motor(int i, Integer number) {
        pubConfig.setDishesAreReady(false);
        //清零
        //02 06 00 26 00 01 A9 F2
//        nettyServerHandler.sendMessageToClient(ipConfig.getReceive485Signal(), Constants.ZEROING_CALIBRATION, true);
//        Thread.sleep(Constants.SLEEP_TIME_MS);
        //称重前准备
        Result result = vegetableMotor(i);
        if (result.getCode() != 200) {
            return result;
        }
        //查看是否够重量
        Boolean flag = true;
        while (flag) {
            //发送称重指令
            sendOrder(Constants.READ_WEIGHT_VALUE);
            if (pubConfig.getCalculateWeight().length > 0 && pubConfig.getCalculateWeight()[i - 1] >= number) {
                flag = false;
            }
        }
        //停止
        result = vegetableMotorStop(i);
        if (result.getCode() != 200) {
            return result;
        }
        //打开盒子，然后关闭
        relay2DeviceGatewayService.openWeighBox(4);
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        relay2DeviceGatewayService.closeWeighBox(4);
        pubConfig.setDishesAreReady(true);
        log.info("第{}个配菜已经配好{}g", i, number);
        return Result.success();
    }

    /**
     * 计算重量
     *
     * @param message
     * @return
     */
    private int[] calculateWeight(String message) {
//        if (Constants.ZEROING_CALIBRATION.replaceAll(" ", "").equals(message.replaceAll(" ", ""))) {
//            log.info("所有秤已置零！");
//            return new int[4];
//        }
        //判断如果不是稳重值就不管了
        message = message.replaceAll(" ", "");
        if (!message.substring(0, 4).equals("0203")) {
            log.info(message);
            return null;
        }
        int[] sensorValues = new int[4];
        for (int i = 0; i < 4; i++) {
            int startIndex = 6 + i * 8;
            String substring = message.substring(startIndex, startIndex + 8);
            int number = WeightParserUtils.parseWeight(substring);
            sensorValues[i] = number;
            log.info("称重传感器 {} 的值为：{} g", i + 1, number);
        }
        pubConfig.setCalculateWeight(sensorValues);
        return sensorValues;
    }

    /**
     * 计算汤传感器数据
     *
     * @param message
     */
    private void CalculateSoupTemperatureValue(String message) {
        //01 03 02 01 20 B8 0C
        //截取第六位到第10位数据位
        message = message.replaceAll(" ", "");
        String substring = message.substring(6, 10);
        int i = Integer.parseInt(substring, 16);
        double soupTemperatureValue = i / 10.0;
        pubConfig.setSoupTemperatureValue(soupTemperatureValue);
        log.info("测量汤的温度为：{} 度", soupTemperatureValue);
    }

    /**
     * 汤加热多少（度）
     *
     * @param number
     * @return
     */
    public Result soupHeatTo(Integer number) {
        //初始化温度
        pubConfig.setSoupTemperatureValue(0.0);
        //发送查询温度指令
        Boolean flag = true;
        while (flag) {
            //发送温度
            readTemperature();
            Double soupTemperatureValue = pubConfig.getSoupTemperatureValue();
            //如果温度大于等于设定温度时就关闭
            if (soupTemperatureValue >= number) {
                flag = false;
                relay1DeviceGatewayService.relayClosing(Constants.Y_SOUP_STEAM_SOLENOID_VALVE);
            } else {
                relay1DeviceGatewayService.relayOpening(Constants.Y_SOUP_STEAM_SOLENOID_VALVE);
            }
        }
        return Result.success();
    }

    /**
     * 称重传感器全部清0
     *
     * @return
     */
    public Result clearAll() {
        nettyServerHandler.sendMessageToClient(ipConfig.getTemperatureWeighingGateway(), Constants.ZEROING_CALIBRATION, true);
        return Result.success();
    }

    /**
     * 标重重量500g
     *
     * @param i 第几个传感器
     * @return
     */
    public Result calibrateWeight(int i) {
        String s = ModbusCalibration.generateCalibrationMessage(i, 500);
        s = s.replaceAll(" ", "");
//        log.info("发送指令：{}", s);
        nettyServerHandler.sendMessageToClient(ipConfig.getTemperatureWeighingGateway(), s, true);
        return Result.success();
    }

    /**
     * 滚筒料称重
     *
     * @param number
     * @return
     */
    public Result vegetable1Motor2(int number) {
        pubConfig.setDishesAreReady(false);
        //清零
        //02 06 00 26 00 01 A9 F2
//        nettyServerHandler.sendMessageToClient(ipConfig.getReceive485Signal(), Constants.ZEROING_CALIBRATION, true);
//        Thread.sleep(Constants.SLEEP_TIME_MS);
        //称重前准备——打开电源
        relay1DeviceGatewayService.relayOpening(Constants.Y_DISCHARGE_BIN_3);
        //查看是否够重量
        Boolean flag = true;
        while (flag) {
            //发送称重指令
            sendOrder(Constants.READ_WEIGHT_VALUE);
            if (pubConfig.getCalculateWeight().length > 0 && pubConfig.getCalculateWeight()[4 - 1] >= number) {
                flag = false;
            }
        }
        //停止
        relay1DeviceGatewayService.relayClosing(Constants.Y_DISCHARGE_BIN_3);
        pubConfig.setDishesAreReady(true);
        log.info("第{}个配菜已经配好{}g", 2, number);
        return Result.success();
    }
}
