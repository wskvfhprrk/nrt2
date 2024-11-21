package com.jc.service.impl;

import com.jc.config.IpConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.netty.server.NettyServerHandler;
import com.jc.service.DeviceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 亿佰特继电器设备处理类
 * 实现了DeviceHandler接口，提供了继电器的打开、关闭及定时关闭功能
 */
@Service
@Slf4j
public class Relay2DeviceGatewayService implements DeviceHandler {
    @Autowired
    private NettyServerHandler nettyServerHandler;
    @Autowired
    private IpConfig ipConfig;
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
        log.info("亿佰特继电器设备——HEX消息: {}", message);
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
        nettyServerHandler.sendMessageToClient(ipConfig.getRelay2DeviceGateway(), hexStr, true);
        sendHexStatus = true;
    }
    /**
     * 根据编号打开继电器
     */
    public Result openRelayByNumber(int number){
        //00 07 00 00 00 06 01 05 00 00 FF 00
        StringBuffer sb=new StringBuffer("000700000006010500");
        sb.append(String.format("%02d", number-1));
        sb.append("FF00");
        sendOrder(sb.toString());
        return Result.success();
    }
    /**
     * 根据编号关闭继电器
     */
    public Result closeRelayByNumber(int number){
        //00 07 00 00 00 06 01 05 00 00 FF 00
        StringBuffer sb=new StringBuffer("000700000006010500");
        sb.append(String.format("%02d", number-1));
        sb.append("0000");
        sendOrder(sb.toString());
        return Result.success();
    }

    /**
     * 根据编号称重盒打开
     */
    public Result openWeighBox(int number){
        closeRelayByNumber(number);
        openRelayByNumber(number+4);
        return Result.success();
    }
    /**
     * 根据编号称重盒关闭
     */
    public Result closeWeighBox(int number){
        openRelayByNumber(number);
        openRelayByNumber(number+4);
        return Result.success();
    }
}
