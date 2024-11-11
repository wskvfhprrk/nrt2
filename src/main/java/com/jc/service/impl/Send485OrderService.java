package com.jc.service.impl;

import com.jc.config.IpConfig;
import com.jc.config.RobotConfig;
import com.jc.constants.Constants;
import com.jc.netty.server.NettyServerHandler;
import com.jc.service.DeviceHandler;
import com.jc.utils.CRC16;
import com.jc.utils.HexConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


/**
 * 发送485指令——步进电机和伺服驱
 */
@Service
@Slf4j
public class Send485OrderService implements DeviceHandler {

    @Autowired
    @Lazy
    private NettyServerHandler nettyServerHandler;
    @Autowired
    private IpConfig ipConfig;

    /**
     * 处理消息
     *
     * @param message 消息内容
     * @param isHex   是否为16进制消息
     */
    @Override
    public void handle(String message, boolean isHex) {
        if (isHex) {
//            log.info("发送485指令返回的HEX消息: {}", message);
        } else {
            log.info("发送485指令返回的普通消息: {}", message);
            // 在这里添加处理普通字符串消息的逻辑
        }
    }


    /**
     * 发送485指令
     *
     * @param HexStr
     */
    public void sendOrder(String HexStr) {
        String modbusrtuString = CRC16.getModbusrtuString(HexStr);
        nettyServerHandler.sendMessageToClient(ipConfig.getSend485Order(), HexStr+modbusrtuString, true);
    }

}