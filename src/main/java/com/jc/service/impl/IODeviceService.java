package com.jc.service.impl;

import com.jc.config.IpConfig;
import com.jc.config.PubConfig;
import com.jc.constants.Constants;
import com.jc.enums.SignalLevel;
import com.jc.netty.server.NettyServerHandler;
import com.jc.service.DeviceHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * IO设备处理类
 * 实现了DeviceHandler接口，提供了处理IO设备消息的功能
 */
@Service
@Slf4j
public class IODeviceService implements DeviceHandler {

    @Autowired
    @Lazy
    private RelayDeviceService relayDeviceService;

    private String ioStatus;
    @Autowired
    private NettyServerHandler nettyServerHandler;
    @Autowired
    private IpConfig ipConfig;
    //工位值——当为原点时为0，共6个工位
    @Autowired
    private PubConfig pubConfig;
    //当前转台的工位状态
    private String turntableStatus;

    public IODeviceService() {
        this.ioStatus = Constants.NOT_INITIALIZED;
    }

    public String getIoStatus() {
        return ioStatus;
    }

    /**
     * 处理消息
     *
     * @param message 消息内容
     * @param isHex   是否为16进制消息
     */
    @Override
    public void handle(String message, boolean isHex) {
        if (isHex) {
            log.info("IO设备——HEX消息: {}", message);
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
            log.info("传感器的高低电平：{}", sb);
            // ioStatus赋值，以便其它类看到
            this.ioStatus = sb.toString();
            sensorInstructionProcessing(sb);
        } else {
            log.info("IO设备——普通消息: {}", message);
        }
    }

    /**
     * 查询当前io所有状态值——如果为初始值主动查询
     *
     * @return
     */
    public String getStatus() {
        String ioStatus = this.ioStatus;
        while (ioStatus.equals(Constants.NOT_INITIALIZED)) {
            log.error("无法获取传感器的值！");
            // 先重置传感器
            nettyServerHandler.sendMessageToClient(ipConfig.getIo(), Constants.RESET_COMMAND, true);
            try {
                // 等待指定时间，确保传感器完成重置
                Thread.sleep(Constants.SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 重新获取传感器状态
            ioStatus = this.ioStatus;
            if (ioStatus.equals(Constants.NOT_INITIALIZED)) {
                log.error("没有发现传感器的值！");
            }
        }
        return ioStatus;
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

    /**
     * 处理传感器指令
     *
     * @param sb 传感器状态信息
     */
    private void sensorInstructionProcessing(StringBuffer sb) {
        String[] split = sb.toString().split(",");
        // 如果碗的极限传感器高电平，要停止碗步进电机
        if (split[Constants.BOWL_LOWER_LIMIT_SENSOR].equals(SignalLevel.HIGH.getValue()) || split[Constants.BOWL_UPPER_LIMIT_SENSOR].equals(SignalLevel.HIGH.getValue())) {
            log.info("到达限位点，停止碗升降的步进电机");
            relayDeviceService.stopBowl();
        }
        //计算工位的值
        calculateWorkstationValue(split);
        //蒸汽发生器温度控制
        steamGeneratorReachesTemperature(split);
    }

    /**
     * 蒸汽发生器温度控制——到最低加热打开加热半分钟,达到最高温度就关闭
     *
     * @param split
     */
    private void steamGeneratorReachesTemperature(String[] split) {
        //温度到了最低就加热半分钟
        if (split[Constants.STEAM_GENERATOR_LOWEST_TEMPERATURE_SENSOR].equals(SignalLevel.HIGH.getValue())) {
            //打开半分钟后关闭
            relayDeviceService.openClose(Constants.STEAM, 30);
        }
        //如果是保湿情况下到了最高温度就关闭
        if (pubConfig.getSteamGeneratorCurrentState() == 1 && split[Constants.STEAM_GENERATOR_HIGHEST_TEMPERATURE_SENSOR].equals(SignalLevel.HIGH.getValue())) {
            //打开一分钟后关闭
            relayDeviceService.steamClose();
        }
    }

    /**
     * 计算工位的值
     *
     * @param split
     */
    private void calculateWorkstationValue(String[] split) {
        // 如果转台复位传感器信号为高电平
        if (split[Constants.ROTARY_TABLE_RESET_SENSOR].equals(SignalLevel.HIGH.getValue())) {
            // 将转台的工位数设为0
            pubConfig.setTurntableNumber(0);
            pubConfig.setTurntableReset(true);
        }

        // 如果转台状态为空（首次调用时可能为空）
        if (turntableStatus == null) {
            // 将转台状态设置为当前传感器信号值
            turntableStatus = split[Constants.ROTARY_TABLE_STATION_SENSOR];
            // 结束方法，不进行后续操作
            return;
        }

        log.info("turntableStatus:{}", turntableStatus);
        // 如果转台状态改变且当前传感器信号为高电平
        if (turntableStatus.equals(SignalLevel.LOW.getValue()) && split[Constants.ROTARY_TABLE_STATION_SENSOR].equals(SignalLevel.HIGH.getValue())) {
            // 将转台的工位数加1
            pubConfig.setTurntableNumber(pubConfig.getTurntableNumber() + 1);
        }
        turntableStatus = split[Constants.ROTARY_TABLE_STATION_SENSOR];
        // 通过日志记录当前的转台工位数
        log.info("TurntableNumber:{}", pubConfig.getTurntableNumber());
    }


}
