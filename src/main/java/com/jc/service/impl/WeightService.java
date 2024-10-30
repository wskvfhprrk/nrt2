package com.jc.service.impl;

import com.jc.config.IpConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.netty.server.NettyServerHandler;
import com.jc.utils.ModbusCalibration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WeightService {

    @Autowired
    private NettyServerHandler nettyServerHandler;
    @Autowired
    private IpConfig ipConfig;

    public Result clearAll() {
        nettyServerHandler.sendMessageToClient(ipConfig.getReceive485Signal(), Constants.ZEROING_CALIBRATION, true);
        return Result.success();
    }

    public Result calibrateWeight(int i) {
        String s = ModbusCalibration.generateCalibrationMessage(i, 500);
        s=s.replaceAll(" ","");
        log.info("发送指令：{}",s);
        nettyServerHandler.sendMessageToClient(ipConfig.getReceive485Signal(), s, true);
        return Result.success();
    }
}
