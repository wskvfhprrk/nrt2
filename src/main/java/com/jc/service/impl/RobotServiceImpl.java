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
    private IODeviceService ioDeviceService;
    @Autowired
    private NettyServerHandler nettyServerHandler;
    @Autowired
    private IpConfig ipConfig;
    @Autowired
    private BowlService bowlService;
    @Autowired
    private PubConfig pubConfig;
    private int takeBowlNumber = 0;

    @Override
    public Result reset() {
        // TODO: 2024/8/28 如果机器人没有执行命令成功将循环执行此发送指令任务
        try {
            nettyClientConfig.connectAndSendData("run(reset.jspf)");
            if (!pubConfig.getRobotStatus()) {
                Thread.sleep(Constants.SLEEP_TIME_MS);
            }
            log.info("机器人复位自检成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.success();
    }

    @Override
    public Result takeBowl() {
        //如果重试三次不再重试
        if (takeBowlNumber > 3) {
            log.error("重试三次无法取到碗，请检查!");
            return Result.error(500, "重试三次无法取到碗，请人工检查！");
        }
        //如果碗传感器为低电平，则重新发出碗重置命令
        String ioStatus = ioDeviceService.getIoStatus();
        while (ioStatus.equals(Constants.NOT_INITIALIZED)) {
            log.error("无法获取传感器的值！");
            // 先重置传感器
            nettyServerHandler.sendMessageToClient(ipConfig.getIo(), Constants.RESET_COMMAND, true);
            try {
                // 等待指定时间，确保传感器完成重置
                Thread.sleep(Constants.SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 重新获取传感器状态
            ioStatus = ioDeviceService.getIoStatus();
            if (ioStatus.equals(Constants.NOT_INITIALIZED)) {
                log.error("没有发现传感器的值！");
            }
        }
        // 解析传感器状态字符串
        String[] split = ioStatus.split(",");
        boolean bowlSensor = split[Constants.AUTO_BOWL_LIFT_POSITION_SENSOR].equals(SignalLevel.HIGH.getValue()); // 碗传感器状态
        //是否检测到有东西
        Boolean isObjectDetected = split[Constants.ROBOT_EMPTY_BOWL_SENSOR].equals(SignalLevel.HIGH.getValue());
        if (isObjectDetected) {
            log.error("检测放碗位置到有东西！");
            return Result.error(500, "检测放碗位置到有东西！");
        }
        //检测机器人是否加home点
        if (!pubConfig.getRobotStatus()) {
            log.error("机器人未复位");
            return Result.error(500, "机器人未复位！");
        }
        if (!bowlSensor) {
            bowlService.bowlReset();
        } else {
            pubConfig.setAddingBowlCompleted(true);
        }
        //向机器人发送取碗指令
        try {
            nettyClientConfig.connectAndSendData("run(takeABowl.jspf)");
            pubConfig.setRobotStatus(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //等待到机器人发出HOME指令才算完成
        while (!pubConfig.getRobotStatus()) {
            try {
                Thread.sleep(Constants.SLEEP_TIME_MS);
                if (pubConfig.getRobotStatus()) {
                    continue;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String status = ioDeviceService.getStatus();
        String[] split1 = status.split(",");
        if (split1[Constants.ROBOT_EMPTY_BOWL_SENSOR].equals(SignalLevel.LOW.getValue())) {
            takeBowlNumber += 1;
            this.takeBowl();
        }
        return Result.success();
    }

    @Override
    public Result putBowl() {
        //检测机器人是否加home点
        if (!pubConfig.getRobotStatus()) {
            log.error("机器人未复位");
            return Result.error(500, "机器人未复位！");
        }
        try {
            nettyClientConfig.connectAndSendData("run(putABowl.jspf)");
            pubConfig.setRobotStatus(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.success();
    }

}
