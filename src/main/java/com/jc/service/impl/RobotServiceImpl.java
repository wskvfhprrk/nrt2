package com.jc.service.impl;

import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.netty.client.NettyClientConfig;
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
    private Relay1DeviceGatewayService relay1DeviceGatewayService;

    @Override
    public Result robotReset() {
        try {
            nettyClientConfig.connectAndSendData("run(jiaZhua/reset.jspf)");
            if (!pubConfig.getIsRobotStatus()) {
                Thread.sleep(1000L);
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
            return Result.error("机器人未复位！");
        }
        pubConfig.setIsRobotStatus(false);
        try {
            nettyClientConfig.connectAndSendData("run(bowl/getBowl.jspf)");
            if (!pubConfig.getIsRobotStatus()) {
                Thread.sleep(300L);
            }
            log.info("机器人复位自检成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (!pubConfig.getGetBowl()) {
            try {
                Thread.sleep(Constants.COMMAND_INTERVAL_POLLING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //出碗指令
        Result result = relay1DeviceGatewayService.deliverBowl();
        if (result.getCode() == 200) {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            return result;
        }
        pubConfig.setGetBowl(false);
        pubConfig.setIsRobotStatus(true);
        log.info("机器人状态赋值为true");
        return Result.success();
    }

    @Override
    public Result robotPlaceBowl() {
        //检测机器人是否加home点
        if (!pubConfig.getIsRobotStatus()) {
            log.error("机器人未复位");
            return Result.error("机器人未复位！");
        }
        pubConfig.setIsRobotStatus(false);
        nettyClientConfig.connectAndSendData("run(bowl/putBowl.jspf)");
        pubConfig.setIsRobotStatus(false);
        return Result.success();
    }

    @Override
    public Result robotDeliverMeal() {
        //检测机器人是否加home点
        if (!pubConfig.getIsRobotStatus()) {
            log.error("机器人未复位");
            return Result.error("机器人未复位！");
        }
        pubConfig.setIsRobotStatus(false);
        nettyClientConfig.connectAndSendData("run(bowl/pickUpSoupBowl.jspf)");
        pubConfig.setIsRobotStatus(false);
        return Result.success();
    }

    @Override
    public Result robotTakeBasket() {
        //检测机器人是否加home点
        if (!pubConfig.getIsRobotStatus()) {
            log.error("机器人未复位");
            return Result.error("机器人未复位！");
        }
        pubConfig.setIsRobotStatus(false);
        nettyClientConfig.connectAndSendData("run(pieCai/main.jspf)");
        pubConfig.setIsRobotStatus(false);
        return Result.success();
    }


    @Override
    public Result robotTakeFans() {
        //检测机器人是否加home点
        if (!pubConfig.getIsRobotStatus()) {
            log.error("机器人未复位");
            return Result.error("机器人未复位！");
        }
        pubConfig.setIsRobotStatus(false);
        //根据出粉丝进行判断
        int currentFanBinNumber = pubConfig.getCurrentFanBinNumber();
        switch (currentFanBinNumber) {
            case 1:
                nettyClientConfig.connectAndSendData("run(fenSi/fen1.jspf)");
                break;
            case 2:
                nettyClientConfig.connectAndSendData("run(fenSi/fen2.jspf)");
                break;
            case 3:
                nettyClientConfig.connectAndSendData("run(fenSi/fen3.jspf)");
                break;
            case 4:
                nettyClientConfig.connectAndSendData("run(fenSi/fen4.jspf)");
                break;
            case 5:
                nettyClientConfig.connectAndSendData("run(fenSi/fen5.jspf)");
                break;
            default:
                return Result.error(500, "没有粉丝仓！");
        }
        pubConfig.setIsRobotStatus(false);
        return Result.success();
    }

    /**
     * 发送检测复位指令
     * enable
     * 执行成功反馈:enable success
     * 执行失败反馈:enable fail
     * 机器人上使能，若上使能成功，返menable success若失败返回 enable fai,若当前是使能状态，返口already enable
     */
    // TODO: 2024/12/9 没有接收状态
    public void sendDetectionResetCommand() {
        nettyClientConfig.connectAndSendData("enable");
    }
}
