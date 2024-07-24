package com.jc.service.impl;

import com.jc.config.IpConfig;
import com.jc.config.PubConfig;
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
     * @param no     继电器编号，范围为1-32
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
        log.info("关闭所有继电器");
        // 发送关闭所有继电器的指令
        nettyServerHandler.sendMessageToClient(ipConfig.getRelay(), "48 3A 01 57 00 00 00 00 00 00 00 00 DA 45 44", true);
    }

    /**
     * 打开所有继电器
     */
    public void openAll() {
        log.info("打开所有继电器");
        // 发送打开所有继电器的指令
        nettyServerHandler.sendMessageToClient(ipConfig.getRelay(), "48 3A 01 57 55 55 55 55 55 55 55 55 82 45 44", true);
    }

    /**
     * 出餐口向下
     *
     * @return
     */
    public String theFoodOutletIsFacingDownwards() {
        log.info("出餐口向下");
        relayClosing(Constants.THE_FOOD_OUTLET_IS_FACING_UPWARDS_SWITCH);
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        openClose(Constants.THE_FOOD_OUTLET_IS_FACING_DOWNWARDS_SWITCH, 15);
        //出完后盖板盖上
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.coverClosed();
        return "ok";
    }

    /**
     * 出餐口向上
     *
     * @return
     */
    public String theFoodOutletIsFacingUpwards() {
        log.info("出餐口向上");
        //先打开盖板
        this.coverOpen();
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        relayClosing(Constants.THE_FOOD_OUTLET_IS_FACING_DOWNWARDS_SWITCH);
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        openClose(Constants.THE_FOOD_OUTLET_IS_FACING_UPWARDS_SWITCH, 15);
        return "ok";
    }

    /**
     * 出料开仓出料
     *
     * @return
     */
    public String coverOpen() {
        log.info("出料开仓出料");
        relayClosing(Constants.DISCHARGING_IS_PROHIBITED_AFTER_CLOSING_THE_WAREHOUSE_SWITCH);
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        openClose(Constants.DISCHARGING_FROM_WAREHOUSE_SWITCH, 15);
        return "ok";
    }

    /**
     * 出料关仓禁止出料
     *
     * @return
     */
    public String coverClosed() {
        log.info("出料关仓禁止出料");
        relayClosing(Constants.DISCHARGING_FROM_WAREHOUSE_SWITCH);
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        openClose(Constants.DISCHARGING_IS_PROHIBITED_AFTER_CLOSING_THE_WAREHOUSE_SWITCH, 15);
        return "ok";
    }

    /**
     * 蒸汽测试,打开5秒后关闭
     */
    public void steam() {
        log.info("蒸汽测试,打开5秒后关闭");
        //继电器7打开15秒关闭
        this.openClose(Constants.STEAM_SWITCH, 5);
    }


    /**
     * 打开柜体排气风扇
     */
    public String openFan() {
        log.info("打开柜体排气风扇");
        relayOpening(Constants.CABINET_EXHAUST_FAN_SWITCH);
        return "ok";
    }

    /**
     * 关闭柜体排气风扇
     */
    public String closeFan() {
        log.info("关闭柜体排气风扇");
        relayClosing(Constants.CABINET_EXHAUST_FAN_SWITCH);
        return "ok";
    }

    /**
     * 停止碗开关
     */
    public void stopBowl() {
        log.info("停止碗开关");
        relayClosing(Constants.BOWL_L_SWITCH);
    }

    /**
     * 打碗开关
     */
    public void openBowl() {
        log.info("打碗开关");
        relayOpening(Constants.BOWL_L_SWITCH);
    }

    /**
     * 碗上升
     */
    public void bowlRise() {
        log.info("碗上升");
        relayOpening(Constants.BOWL_N_SWITCH);
    }

    /**
     * 碗下降
     */
    public void bowlDrop() {
        log.info("碗下降");
        relayClosing(Constants.BOWL_N_SWITCH);
    }

    /**
     * 打开抽汤泵10秒钟时间
     */
    public void soupPump() {
        log.info("打开抽汤泵10秒钟时间");
        openClose(Constants.SOUP_PUMP_SWITCH, 10);
    }

    /**
     * 蒸汽打开
     */
    public void steamOpen() {
        log.info("蒸汽打开");
        relayOpening(Constants.STEAM_SWITCH);
    }

    /**
     * 蒸汽关闭
     */
    public void steamClose() {
        log.info("蒸汽关闭");
        relayClosing(Constants.STEAM_SWITCH);
    }

    /**
     * 抽水机打开3分钟——最大抽水时间，如果抽不上来就不抽了
     */
    public void pumpStart() {
        //抽180秒后检测一下
        openClose(Constants.WATER_PUMP_SWITCH, 300);
        //三分钟后检测
        affterTest();
    }

    private void affterTest() {
        new Thread(() -> {
            try {
                Thread.sleep(300L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //如果还抽不上水就说明抽水泵坏了
            if (!pubConfig.getSteamGeneratorWaterStatus()) {
                log.error("请检查水路，无法出上水！");
            }
        }).start();
    }

    /**
     * 水到液位处再加半分钟
     */
    public void pumpStop() {
        openClose(Constants.WATER_PUMP_SWITCH, 30);
    }
}
