package com.jc.service.impl;

import com.jc.config.IpConfig;
import com.jc.config.PubConfig;
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
    @Autowired
    private PubConfig pubConfig;
    //发送中状态——true正在发送中，false已经有返回值
//    private Boolean send = false;

    /**
     * 处理消息
     *
     * @param message 消息内容
     * @param isHex   是否为16进制消息
     */
    @Override
    public void handle(String message, boolean isHex) {
        pubConfig.setSend485Status(false);
        if (isHex) {
            log.info("步进伺服指令返回的HEX消息: {}", message);
        } else {
            log.info("发送485指令返回的普通消息: {}", message);
            // 在这里添加处理普通字符串消息的逻辑
        }
    }


    /**
     * 发送485指令
     *
     * @param hexStr
     */
    public void sendOrder(String hexStr) {
        String modbusrtuString = CRC16.getModbusrtuString(hexStr);
        String s = hexStr + modbusrtuString;
        while (pubConfig.getSend485Status()) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        pubConfig.setSend485Status(true);
        log.info("向步进伺服发送指令");
        nettyServerHandler.sendMessageToClient(ipConfig.getSend485Order(), s, true);
    }

}