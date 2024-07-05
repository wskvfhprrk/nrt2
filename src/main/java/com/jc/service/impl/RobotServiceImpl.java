package com.jc.service.impl;

import com.jc.netty.client.NettyClientConfig;
import com.jc.service.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RobotServiceImpl implements RobotService {

    @Autowired
    NettyClientConfig nettyClientConfig;

    // TODO: 2024/6/22 先判断机器人是否在原点后再执行 
    @Override
    public void reset() {
        try {
            nettyClientConfig.connectAndSendData("run(reset.jspf)");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void takeBowl() {
        //向机器人发送取碗指令
        try {
            nettyClientConfig.connectAndSendData("run(takeABowl.jspf)");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void putBowl() {
        try {
            nettyClientConfig.connectAndSendData("run(putBowl.jspf)");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getFans() {

    }
}
