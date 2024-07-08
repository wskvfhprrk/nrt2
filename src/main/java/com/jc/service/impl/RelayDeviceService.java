package com.jc.service.impl;

import com.jc.config.IpConfig;
import com.jc.constants.Constants;
import com.jc.netty.server.NettyServerHandler;
import com.jc.service.DeviceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 继电器设备处理类
 * 实现了DeviceHandler接口，提供了继电器的打开、关闭及定时关闭功能
 */
@Service
@Slf4j
public class RelayDeviceService implements DeviceHandler {

    @Autowired
    private NettyServerHandler nettyServerHandler;

    @Autowired
    private IpConfig ipConfig;

    /**
     * 处理消息
     *
     * @param message 消息内容
     * @param isHex 是否为16进制消息
     */
    @Override
    public void handle(String message, boolean isHex) {
        if (isHex) {
            log.info("继电器设备——HEX消息: {}", message);
        } else {
            log.info("继电器设备——普通消息: {}", message);
            // 在这里添加处理普通字符串消息的逻辑
        }
    }

    /**
     * 根据编号打开继电器
     *
     * @param no 继电器编号，范围为1-32
     */
    public void relayOpening(int no) {
        if (no <= 0 || no > 32) {
            log.error("编号{}继电器不存在！", no);
            return; // 添加return，防止继续执行
        }
        // 将编号转换为16进制字符串
        String hexString = Integer.toHexString(no).toUpperCase();
        // 如果字符串长度不足两位，则在前面补0
        if (hexString.length() < 2) {
            hexString = "0" + hexString;
        }
        // 构造指令字符串
        StringBuffer sb = new StringBuffer("48 3A 01 70 ");
        sb.append(hexString);
        sb.append(" 01 00 00 45 44");
        // 发送指令
        nettyServerHandler.sendMessageToClient(ipConfig.getRelay(), sb.toString(), true);
    }

    /**
     * 根据编号关闭继电器
     *
     * @param no 继电器编号，范围为1-32
     */
    public void relayClosing(int no) {
        if (no <= 0 || no > 32) {
            log.error("编号{}继电器不存在！", no);
            return; // 添加return，防止继续执行
        }
        // 将编号转换为16进制字符串
        String hexString = Integer.toHexString(no).toUpperCase();
        // 如果字符串长度不足两位，则在前面补0
        if (hexString.length() < 2) {
            hexString = "0" + hexString;
        }
        // 构造指令字符串
        StringBuffer sb = new StringBuffer("48 3A 01 70 ");
        sb.append(hexString);
        sb.append(" 00 00 00 45 44");
        // 发送指令
        nettyServerHandler.sendMessageToClient(ipConfig.getRelay(), sb.toString(), true);
    }

    /**
     * 继电器打开一段时间后自动关
     *
     * @param no 继电器编号，范围为1-32
     * @param second 延迟关闭的时间，单位为秒，范围为1-177777
     */
    public void openClose(int no, int second) {
        if (no <= 0 || no > 32) {
            log.error("编号{}继电器不存在！", no);
            return; // 添加return，防止继续执行
        }
        if (second <= 0 || second > 177777) {
            log.error("时间值不能限定", second);
            return; // 添加return，防止继续执行
        }
        // 将编号转换为16进制字符串
        String hexString = Integer.toHexString(no).toUpperCase();
        // 如果字符串长度不足两位，则在前面补0
        if (hexString.length() < 2) {
            hexString = "0" + hexString;
        }
        // 构造指令字符串
        StringBuffer sb = new StringBuffer("48 3A 01 70 ");
        sb.append(hexString);
        sb.append(" 01 ");
        // 将时间转换为16进制字符串
        String hexTime = String.format("%04X", second).toUpperCase();
        sb.append(hexTime);
        sb.append(" 45 44");
        // 发送指令
        nettyServerHandler.sendMessageToClient(ipConfig.getRelay(), sb.toString(), true);
    }

    /**
     * 关闭所有继电器
     */
    public void closeAll() {
        // 发送关闭所有继电器的指令
        nettyServerHandler.sendMessageToClient(ipConfig.getRelay(), "48 3A 01 57 00 00 00 00 00 00 00 00 DA 45 44", true);
    }

    /**
     * 打开所有继电器
     */
    public void openAll() {
        // 发送打开所有继电器的指令
        nettyServerHandler.sendMessageToClient(ipConfig.getRelay(), "48 3A 01 57 55 55 55 55 55 55 55 55 82 45 44", true);
    }

    /**
     * 出餐口向下
     * @return
     */
    public String theFoodOutletIsFacingDownwards(){
        relayClosing(Constants.THE_FOOD_OUTLET_IS_FACING_UPWARDS);
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        relayOpening(Constants.THE_FOOD_OUTLET_IS_FACING_DOWNWARDS);
        return "ok";
    }
    /**
     * 出餐口向上
     * @return
     */
    public String theFoodOutletIsFacingUpwards(){
        relayClosing(Constants.THE_FOOD_OUTLET_IS_FACING_DOWNWARDS);
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        relayOpening(Constants.THE_FOOD_OUTLET_IS_FACING_UPWARDS);
        return "ok";
    }
    /**
     * 出料开仓出料
     * @return
     */
    public String dischargingFromWarehouse(){
        relayClosing(Constants.DISCHARGING_IS_PROHIBITED_AFTER_CLOSING_THE_WAREHOUSE);
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        relayOpening(Constants.DISCHARGING_FROM_WAREHOUSE);
        return "ok";
    }
    /**
     * 出料关仓禁止出料
     * @return
     */
    public String dischargingIsProhibitedAfterClosingTheWarehouse(){
        relayClosing(Constants.DISCHARGING_FROM_WAREHOUSE);
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        relayOpening(Constants.DISCHARGING_IS_PROHIBITED_AFTER_CLOSING_THE_WAREHOUSE);
        return "ok";
    }
    //蒸汽测试,打开10秒后关闭
    public void steam() {
        //继电器7打开10秒关闭
        this.openClose(7,10);
    }
}
