package com.jc.service.impl;

import com.jc.config.IpConfig;
import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.enums.SignalLevel;
import com.jc.netty.client.NettyClientConfig;
import com.jc.netty.server.NettyServerHandler;
import com.jc.service.RobotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RobotServiceImpl implements RobotService {

    @Autowired
    NettyClientConfig nettyClientConfig;
    @Autowired
    private PubConfig pubConfig;
    private int takeBowlNumber = 0;
    @Autowired
    private RelayDeviceService relayDeviceService;
    @Autowired
    private FansService fansService;

    @Override
    public Result robotReset() {
        try {
            nettyClientConfig.connectAndSendData("run(jiaZhua/reset.jspf)");
            if (!pubConfig.getIsRobotStatus()) {
                Thread.sleep(Constants.SLEEP_TIME_MS);
            }
            log.info("机器人复位自检成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.success();
    }

    @Override
    public Result robotTakeBowl() {
        //检测机器人是否加home点
        if (!pubConfig.getIsRobotStatus()) {
            log.error("机器人未复位");
            return Result.error(500, "机器人未复位！");
        }
        pubConfig.setIsRobotStatus(false);
        try {
            nettyClientConfig.connectAndSendData("run(bowl/getBowl.jspf)");
            if (!pubConfig.getIsRobotStatus()) {
                Thread.sleep(Constants.SLEEP_TIME_MS);
            }
            log.info("机器人复位自检成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //出碗指令
        Result result = relayDeviceService.deliverBowl();
        if (result.getCode() == 20) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            return result;
        }
        pubConfig.setIsRobotStatus(true);
        return Result.success();
    }

    @Override
    public Result robotPlaceBowl() {
        //检测机器人是否加home点
        if (!pubConfig.getIsRobotStatus()) {
            log.error("机器人未复位");
            return Result.error(500, "机器人未复位！");
        }
        pubConfig.setIsRobotStatus(false);
        try {
            nettyClientConfig.connectAndSendData("run(bowl/putBowl.jspf)");
            pubConfig.setIsRobotStatus(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.success();
    }

    @Override
    public Result robotDeliverMeal() {
        //检测机器人是否加home点
        if (!pubConfig.getIsRobotStatus()) {
            log.error("机器人未复位");
            return Result.error(500, "机器人未复位！");
        }
        pubConfig.setIsRobotStatus(false);
        try {
            nettyClientConfig.connectAndSendData("run(bowl/pickUpSoupBowl.jspf)");
            pubConfig.setIsRobotStatus(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.success();
    }

    @Override
    public Result robotTakeBasket() {
        //检测机器人是否加home点
        if (!pubConfig.getIsRobotStatus()) {
            log.error("机器人未复位");
            return Result.error(500, "机器人未复位！");
        }
        pubConfig.setIsRobotStatus(false);
        try {
            nettyClientConfig.connectAndSendData("run(pieCai/main.jspf)");
            pubConfig.setIsRobotStatus(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.success();
    }


    @Override
    public Result robotTakeFans() {
        //检测机器人是否加home点
        if (!pubConfig.getIsRobotStatus()) {
            log.error("机器人未复位");
            return Result.error(500, "机器人未复位！");
        }
        pubConfig.setIsRobotStatus(false);
        try {
            // TODO: 2024/11/7 根据出粉丝进行判断
            nettyClientConfig.connectAndSendData("run(fenSi/fen1.jspf)");
            pubConfig.setIsRobotStatus(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.success();
    }

    public Result takeFans() {
        return fansService.takeFans();
    }
}
