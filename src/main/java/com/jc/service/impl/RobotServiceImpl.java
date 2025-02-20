package com.jc.service.impl;

import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.enums.SignalLevel;
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
    @Autowired
    private SignalAcquisitionDeviceGatewayService signalAcquisitionDeviceGatewayService;

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
        checkBowl();
        return Result.success();
    }

    private int getBowlNumber = 0;

    private Result checkBowl() {
        while (!pubConfig.getIsRobotStatus()) {
            try {
                Thread.sleep(Constants.COMMAND_INTERVAL_POLLING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        pubConfig.setIsRobotStatus(false);
        //取碗后拿到进行检查
        nettyClientConfig.connectAndSendData("run(bowl/checkBowl.jspf)");
        while (!pubConfig.getIsRobotStatus()) {
            try {
                Thread.sleep(Constants.COMMAND_INTERVAL_POLLING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //直到机器人停止
        while (!pubConfig.getIsRobotStatus()) {
            try {
                Thread.sleep(Constants.COMMAND_INTERVAL_POLLING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //检查
        if (signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FANS_WAREHOUSE_4) == SignalLevel.HIGH.ordinal()) {
            getBowlNumber = 0;
            return Result.success();
        }
        getBowlNumber += 1;
        log.info("getBowlNumber==={}",getBowlNumber);
        if(getBowlNumber>=3){
            return Result.error("取碗失败！");
        }
        return robotTakeBowl();
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

    /**
     * 机器人拿篮子次数
     */
    private int naLanNumber = 0;

    @Override
    public Result robotTakeBasket() {
        //检测机器人是否加home点
        if (!pubConfig.getIsRobotStatus()) {
            log.error("机器人未复位");
            return Result.error("机器人未复位！");
        }
        pubConfig.setIsRobotStatus(false);
        nettyClientConfig.connectAndSendData("run(pieCai/naLan.jspf)");
        while (!pubConfig.getIsRobotStatus()) {
            try {
                Thread.sleep(Constants.COMMAND_INTERVAL_POLLING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //检查是否取走菜篮
        if (signalAcquisitionDeviceGatewayService.getStatus(Constants.X_BASKET_RESET) == SignalLevel.HIGH.ordinal()) {
            naLanNumber = 0;
            nettyClientConfig.connectAndSendData("run(pieCai/main.jspf)");
            return Result.success();
        }
        naLanNumber += 1;
        if (naLanNumber >= 3) {
            log.error("机器人取菜篮不成功，请人工检查并给菜篮复位！");
            nettyClientConfig.connectAndSendData("run(pieCai/goHome.jspf)");
            return Result.error("机器人取菜篮不成功，请人工检查并给菜篮复位！");
        }
        return this.robotTakeBasket();
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

}
