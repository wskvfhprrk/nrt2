package com.jc.service.impl;

import com.jc.config.IpConfig;
import com.jc.config.PubConfig;
import com.jc.constants.Constants;
import com.jc.enums.SignalLevel;
import com.jc.netty.server.NettyServerHandler;
import com.jc.service.DeviceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * IO设备处理类——根据传感器自动控制设备
 * 实现了DeviceHandler接口，提供了处理IO设备消息的功能
 */
@Service
@Slf4j
public class SignalAcquisitionDeviceGatewayService implements DeviceHandler {

    @Autowired
    @Lazy
    private Relay1DeviceGatewayService relay1DeviceGatewayService;
    @Autowired
    private NettyServerHandler nettyServerHandler;
    @Autowired
    private IpConfig ipConfig;
    //工位值——当为原点时为0，共6个工位
    // 类级变量，用于保存上一次的高低电平状态
    private StringBuffer previousLevels = null;

    private String ioStatus = null;
    @Autowired
    private StepServoDriverGatewayService stepServoDriverGatewayService;
    @Autowired
    private PubConfig pubConfig;
    public Boolean sendOrderStatus = false;

    /**
     * 处理消息
     *
     * @param message 消息内容
     * @param isHex   是否为16进制消息
     */
    @Override
    public void handle(String message, boolean isHex) {
//        log.info("收到信号");
        sendOrderStatus = false;
        if (isHex) {
            // 查中间8位，从第6位开始查询
            String[] split = message.split(" ");
            // 将字符串分割为8个部分，每个部分4个字符
            StringBuffer sb = new StringBuffer();
            for (int i = 1; i <= 32; i++) {
                if (i % 4 == 0) {
                    // 解析高低电平
                    heightOrLow(split[3 + i / 4], sb);
                }
            }
//            log.info("传感器的高低电平：{}", sb);
            ioStatus = sb.toString();
            highAndLowLevelsChange(sb);
        } else {
            log.info("IO设备——普通消息: {}", message);
        }
    }

    //    @Scheduled(fixedRate = 10)
    public void performTask() {
        log.info("io值初始化…………");
        try {
            // 强制初始化相关依赖
            boolean allDevicesConnected = pubConfig.getAllDevicesConnectedStatus();
            Objects.requireNonNull(ipConfig.getSignalAcquisitionDeviceGateway(), "SignalAcquisitionDeviceGateway 未正确初始化");

            // 任务逻辑
            if (allDevicesConnected && !sendOrderStatus) {
                nettyServerHandler.sendMessageToClient(ipConfig.getSignalAcquisitionDeviceGateway(), Constants.RESET_COMMAND, true);
                sendOrderStatus = true;
            }
        } catch (Exception e) {
            // 捕获异常，记录日志
            log.error("定时任务执行异常", e);
        }
    }


    /**
     * 打印高低电平变化
     *
     * @param currentLevels
     */
    private void highAndLowLevelsChange(StringBuffer currentLevels) {
        if (previousLevels == null) {
        } else {
            // 与上次的高低电平进行对比
            String[] previousLevelStr = previousLevels.toString().split(",");
            String[] currentLevelStr = currentLevels.toString().split(",");

            // 对比每一位的高低电平变化
            for (int i = 0; i < currentLevelStr.length; i++) {
                if (!previousLevelStr[i].equals(currentLevelStr[i])) {
                    log.info("第 X{} 位电平发生变化，从 {} 变为 {}", i + 1, previousLevelStr[i], currentLevelStr[i]);
                    passaDta(i + 1, previousLevelStr[i], currentLevelStr[i]);
                }
            }
        }
        // 更新上一次的电平状态
        previousLevels = new StringBuffer(currentLevels);
    }

    private void passaDta(int i, String s, String s1) {
        //有碗信号感应到时
        if (i == Constants.X_PLACE_BOWL_SIGNAL) {
            relay1DeviceGatewayService.chuWanStop();
        }
        //如果碗报警信号
        if (i == Constants.X_PLACE_BOWL_SIGNAL && s.equals("0") && s1.equals("1")) {
            // TODO: 2024/10/23 报警给服务器通知管理人员上碗 ，订单最多再接几单
            log.warn("碗快没有了，要放碗了");
        }
        //蒸汽原位停止
        if (i == Constants.X_STEAM_ORIGIN && s.equals("0") && s1.equals("1")) {
            relay1DeviceGatewayService.relayClosing(Constants.Y_TELESCOPIC_ROD_SWITCH_CONTROL);
        }
        //蒸汽下限位停止
        if (i == Constants.X_STEAM_LOWER_LIMIT ) {
            relay1DeviceGatewayService.relayClosing(Constants.Y_TELESCOPIC_ROD_SWITCH_CONTROL);
        }
        //蒸汽限上位停止
        if (i == Constants.X_STEAM_UPPER_LIMIT && pubConfig.getAddSteam()) {
            relay1DeviceGatewayService.relayClosing(Constants.Y_TELESCOPIC_ROD_SWITCH_CONTROL);
            relay1DeviceGatewayService.relayClosing(Constants.Y_TELESCOPIC_ROD_DIRECTION_CONTROL);
        }
        if (i == Constants.X_SOUP_INGREDIENT_SENSOR && s.equals("0") && s1.equals("1")) {
            log.info("菜勺转正停止");
            //停止
            String hex = "030600020001";
            stepServoDriverGatewayService.sendOrder(hex);
        }
        //粉丝仓左限位
        if (i == Constants.X_FAN_COMPARTMENT_LEFT_LIMIT && s.equals("0") && s1.equals("1")) {
            //停止
            String hex = "040600020001";
            stepServoDriverGatewayService.sendOrder(hex);
            //设置原点为1仓
            pubConfig.setCurrentFanBinNumber(1);
        }
        //粉丝仓右限位
        if (i == Constants.X_FAN_COMPARTMENT_RIGHT_LIMIT && s.equals("0") && s1.equals("1")) {
            //停止
            String hex = "040600020001";
            stepServoDriverGatewayService.sendOrder(hex);
        }
        //倒菜伺服到位——汤右限位
        if (i == Constants.X_SOUP_RIGHT_LIMIT && s.equals("0") && s1.equals("1")) {
            //停止
            String hex = "020600020001";
            stepServoDriverGatewayService.sendOrder(hex);
        }
        //倒菜伺服到位——汤左限位
        if (i == Constants.X_SOUP_LEFT_LIMIT && s.equals("0") && s1.equals("1")) {
            //停止
            String hex = "020600020001";
            stepServoDriverGatewayService.sendOrder(hex);
        }
        //切肉机数量加1
        if (i == Constants.X_MEAT_SLICER_SENSOR && s.equals("0") && s1.equals("1")) {
            pubConfig.setMeatSlicingQuantity(pubConfig.getMeatSlicingQuantity() + 1);
            log.info("切肉数量：{}", pubConfig.getMeatSlicingQuantity());
        }
        //水流脉冲计数
        if (i == Constants.X_FLOWMETER_PULSE_COUNT && s.equals("0") && s1.equals("1")) {
            pubConfig.setFlowmeterPulseCount(pubConfig.getFlowmeterPulseCount() + 1);
            log.info("流量计脉冲数量：{}", pubConfig.getFlowmeterPulseCount());
        }
    }

    /**
     * 查询当前io所有状态值——主动查询状态值
     *
     * @return
     */
    public String getStatus() {
        while (ioStatus == null) {
            performTask();
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return ioStatus;
    }

    /**
     * 发起主动查询
     */
    public void sendSearch() {
        nettyServerHandler.sendMessageToClient(ipConfig.getSignalAcquisitionDeviceGateway(), Constants.RESET_COMMAND, true);
    }

    public int getStatus(int i) {
        String[] split = getStatus().split(",");
        String str = split[i - 1];
        return Integer.valueOf(str);
    }


    /**
     * 解析引脚的高低电平
     *
     * @param hexStr 16进制字符串
     * @param sb     保存解析结果的StringBuffer
     * @return 解析后的StringBuffer
     */
    private StringBuffer heightOrLow(String hexStr, StringBuffer sb) {
        // 检查 hexStr 是否为空或长度是否小于2
        if (hexStr == null || hexStr.length() < 2) {
            log.error("无效的 hexStr 输入");
            return null;
        }

        // 提取 hexStr 的第一位和第二位字符
        char firstChar = hexStr.charAt(0);
        char secondChar = hexStr.charAt(1);

        // 根据第二位字符判断 startIo 是否为高电平——智嵌IO输入自定义协议
        sb.append((secondChar == '1' || secondChar == '5') ? SignalLevel.HIGH.getValue() : SignalLevel.LOW.getValue()).append(",");
        // 根据第二位字符判断 startIo + 1 是否为高电平
        sb.append((secondChar == '4' || secondChar == '5') ? SignalLevel.HIGH.getValue() : SignalLevel.LOW.getValue()).append(",");
        // 根据第一位字符判断 startIo + 2 是否为高电平
        sb.append((firstChar == '1' || firstChar == '5') ? SignalLevel.HIGH.getValue() : SignalLevel.LOW.getValue()).append(",");
        // 根据第一位字符判断 endIo 是否为高电平
        sb.append((firstChar == '4' || firstChar == '5') ? SignalLevel.HIGH.getValue() : SignalLevel.LOW.getValue()).append(",");
        return sb;
    }

    //    /**
//     * 配菜电机（KG）
//     *
//     * @param i
//     * @param number
//     * @return
//     */
//    public Result vegetable1Motor(int i, Integer number) {
//        pubConfig.setDishesAreReady(false);
//        //清零
//        //02 06 00 26 00 01 A9 F2
////        nettyServerHandler.sendMessageToClient(ipConfig.getReceive485Signal(), Constants.ZEROING_CALIBRATION, true);
////        Thread.sleep(Constants.SLEEP_TIME_MS);
//        //称重前准备
//        Result result = vegetableMotor(i);
//        if (result.getCode() != 200) {
//            return result;
//        }
//        //查看是否够重量
//        Boolean flag = true;
//        while (flag) {
//            //发送称重指令
//            sendOrder(Constants.READ_WEIGHT_VALUE);
//            if (pubConfig.getCalculateWeight().length > 0 && pubConfig.getCalculateWeight()[i - 1] >= number) {
//                flag = false;
//            }
//        }
//        //停目
//        result = vegetableMotorStop(i);
//        if (result.getCode() != 200) {
//            return result;
//        }
//        //打开称重盒
//        result = closeWeighingBox(i);
//        if (result.getCode() != 200) {
//            return result;
//        }
//        pubConfig.setDishesAreReady(true);
//        log.info("第{}个配菜已经配好{}g", i, number);
//        return Result.success();
//    }
}
