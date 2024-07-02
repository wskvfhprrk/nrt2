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
 * 发送485指令
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
    private RobotConfig robotConfig;

    /**
     * 处理消息
     *
     * @param message 消息内容
     * @param isHex   是否为16进制消息
     */
    @Override
    public void handle(String message, boolean isHex) {
        if (isHex) {
            log.info("发送485指令返回的HEX消息: {}", message);
            theDischargeMachineIsCompletedToJudge(message);
        } else {
            log.info("发送485指令返回的普通消息: {}", message);
            // 在这里添加处理普通字符串消息的逻辑
        }
    }
    /**
     * 对于收到出料机出料完成进行判断
     */
    private void theDischargeMachineIsCompletedToJudge(String message) {
        //02 01 01 01 90 0C
        boolean validateCRC = CRC16.validateCRC(HexConvert.hexStringToBytes(message.replaceAll(" ","")));
        if(!validateCRC) return;
        //根据前两位判断出料机信息
        String[] s = message.split(" ");
        int address=Integer.valueOf(s[0]);
        if(address== Constants.SEASONING_MACHINE){
            if(s[3].equals("00")){
                robotConfig.setEjectionIsComplete(true);
                log.info("出料机出料完成！");
            }else {
                robotConfig.setEjectionIsComplete(false);
            }
        }
    }

    /**
     * 发送485指令
     * @param HexXtr
     */
    public void sendOrder(String HexXtr) {
        nettyServerHandler.sendMessageToClient(ipConfig.getSend485Order(), HexXtr, true);
    }
}
