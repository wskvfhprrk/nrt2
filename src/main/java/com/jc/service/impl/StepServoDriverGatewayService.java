package com.jc.service.impl;

import com.jc.config.IpConfig;
import com.jc.netty.server.NettyServerHandler;
import com.jc.service.DeviceHandler;
import com.jc.utils.CRC16;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


/**
 * 发送485指令——步进电机和伺服驱
 */
@Service
@Slf4j
public class StepServoDriverGatewayService implements DeviceHandler {

    @Autowired
    @Lazy
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
        log.info("步进伺服指令返回的HEX消息: {}", message);
    }


    /**
     * 发送485指令
     *
     * @param hexStr
     */
    public void sendOrder(String hexStr) {
        String modbusrtuString = CRC16.getModbusrtuString(hexStr);
        hexStr = hexStr + modbusrtuString;
        while (sendHexStatus) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sendHexStatus = true;
        log.info("向步进伺服发送指令:{}", hexStr);
        nettyServerHandler.sendMessageToClient(ipConfig.getStepServoDriverGateway(), hexStr, true);
    }

}