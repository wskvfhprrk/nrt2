package com.jc.service.impl;

import com.jc.config.IpConfig;
import com.jc.constants.Constants;
import com.jc.enums.SignalLevel;
import com.jc.netty.server.NettyServerHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 转台步进电机控制器
 */
@Service
@Slf4j
public class TurntableService {

    private final NettyServerHandler nettyServerHandler;
    private final IODeviceService ioDeviceService;
    private final IpConfig ipConfig;
    private final StepperMotorService stepperMotorService;
    //工位值——当为原点时为0，共6个工位
    private int turntableNumber;
    //当前转台的工位状态
    private String turntableStatus;


    @Autowired
    public TurntableService(NettyServerHandler nettyServerHandler,
                            IODeviceService ioDeviceService,
                            StepperMotorService stepperMotorService, IpConfig ipConfig) {
        this.nettyServerHandler = nettyServerHandler;
        this.ioDeviceService = ioDeviceService;
        this.stepperMotorService = stepperMotorService;
        this.ipConfig = ipConfig;
    }

    /**
     * 碗回原点
     *
     * @return
     */
    public String turntableReset() {
        log.info("转台复位");
        // 获取传感器状态
        String ioStatus = ioDeviceService.getStatus();
        //如果已经在原点
        if (ioStatus.split(",")[Constants.ROTARY_TABLE_RESET_SENSOR].equals(SignalLevel.HIGH.getValue())) {
            log.info("转盘已经在原点位置！");
            return "ok";
        }
        //发送转盘的转速为最大值为40,太大IO感应不到，会超过原点位置
        stepperMotorService.modificationSpeed(Constants.ROTARY_TABLE_STEPPER_MOTOR, Constants.TURNTABLE_SPEED);
        //如果不在原点，转动
        if (ioStatus.split(",")[Constants.ROTARY_TABLE_RESET_SENSOR].equals(SignalLevel.LOW.getValue())) {
            try {
                Thread.sleep(Constants.SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //发送转动转盘指令至到为高电平
            stepperMotorService.startStepperMotor(Constants.ROTARY_TABLE_STEPPER_MOTOR, true, 0);
            Boolean flag = true;
            while (flag) {
                String newIoStatus = ioDeviceService.getIoStatus().split(",")[Constants.ROTARY_TABLE_RESET_SENSOR];
                if (newIoStatus.equals(SignalLevel.HIGH.getValue())) {
                    stepperMotorService.stop(Constants.ROTARY_TABLE_STEPPER_MOTOR);
                    flag = false;
                    //工位为0
                    turntableNumber = 0;
                    log.info("设置工位值为1");
                }
            }
        }
        return "ok";
    }

    public String feeding() {
        //先复位
        String ioStatus = ioDeviceService.getIoStatus();
        while (ioStatus.equals(Constants.NOT_INITIALIZED)) {
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
        if (ioStatus.split(",")[Constants.ROTARY_TABLE_RESET_SENSOR].equals(SignalLevel.HIGH.getValue())) {
            stepperMotorService.startStepperMotor(Constants.ROTARY_TABLE_STEPPER_MOTOR, true, 1600);
            return "ok";
        }
        if (ioStatus.split(",")[Constants.ROTARY_TABLE_RESET_SENSOR].equals(SignalLevel.LOW.getValue())) {
            //发送转动转盘指令至到为高电平
            stepperMotorService.startStepperMotor(Constants.ROTARY_TABLE_STEPPER_MOTOR, true, 0);
            Boolean flag = true;
            while (flag) {
                String newIoStatus = ioDeviceService.getIoStatus().split(",")[Constants.ROTARY_TABLE_RESET_SENSOR];
                if (newIoStatus.equals(SignalLevel.HIGH.getValue())) {
                    //步进电机转半圈
                    stepperMotorService.startStepperMotor(Constants.ROTARY_TABLE_STEPPER_MOTOR, true, 1600);
                    flag = false;
                }
                try {
                    Thread.sleep(Constants.SLEEP_TIME_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return "ok";
    }

    /**
     * 转盘6个工位功能分工，从原点开始
     * 第一个工位放置碗
     * 第二个工位待用;
     * 第三个工位为放牛肉和菜
     * 第四个工位为加蒸汽工位
     * 第五个工位为加汤和调料工位
     * 第六个工位为出汤工位
     * <p>
     * 检查每个工位功能当前状态，
     * 如果完成所有状态则运行到下一工位
     * 单独完成下一个的功能
     */
    //记录每个工位状态——第二个状态为空值直接为true
    private boolean[] stationStatus = {false, true, false, false, false, false};
    //每个位置是否有碗
    private boolean[] isThereABowlInPlace = {false, false, false, false, false, false};
    //当前原点对应第工位数
    private int originStation = 0;


    public boolean checkStationStatus() {
        for (boolean status : stationStatus) {
            if (!status) {
                return false;
            }
        }
        return true;
    }

    public String reset() {
        stationStatus = new boolean[]{false, true, false, false, false, false};
        isThereABowlInPlace = new boolean[]{false, false, false, false, false, false};
        originStation = 0;
        return "ok";
    }



    /**
     * 转到下一个工位
     */
    public void moveToNext() {
        log.info("移动到下一个工位");
        //发送转盘的转速为最大值为40,太大IO感应不到，会超过原点位置
        stepperMotorService.modificationSpeed(Constants.ROTARY_TABLE_STEPPER_MOTOR, Constants.TURNTABLE_SPEED);
        try {
            Thread.sleep(Constants.SLEEP_TIME_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //转动电机
        stepperMotorService.startStepperMotor(Constants.ROTARY_TABLE_STEPPER_MOTOR, true, 0);
        Boolean flag = true;
        //值变化
        Boolean dataChanges = false;
        while (flag) {
            try {
                Thread.sleep(Constants.SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String newIoStatus = ioDeviceService.getStatus().split(",")[Constants.ROTARY_TABLE_STATION_SENSOR];
            if (turntableStatus == null) {
                turntableStatus = newIoStatus;
                continue;
            }
            if (!newIoStatus.equals(turntableStatus) && turntableStatus != null) {
                dataChanges = true;
            }
            turntableStatus = newIoStatus;
            if (dataChanges && newIoStatus.equals(SignalLevel.HIGH.getValue())) {
                flag = false;
                stepperMotorService.stop(Constants.ROTARY_TABLE_STEPPER_MOTOR);
                turntableNumber += 1;
                log.info("工位数值：{}", turntableNumber);
            }
        }
    }

    /**
     * 转到某一个工位停止
     *
     * @param number
     */
    public void moveNumber(int number) {
        //如果为0表示初始化
        if (turntableNumber==0){
            this.turntableReset();
        }
        log.info("移动到第{}工位", number);
        //除以6求余为工位
        if (turntableNumber % 6 == number) {
            log.info("已经在{}位上", number);
            return;
        }
        //发送转盘的转速为最大值,太大IO感应不到，会超过原点位置
        stepperMotorService.modificationSpeed(Constants.ROTARY_TABLE_STEPPER_MOTOR, Constants.TURNTABLE_SPEED);
        try {
            Thread.sleep(Constants.SLEEP_TIME_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //转动电机
        stepperMotorService.startStepperMotor(Constants.ROTARY_TABLE_STEPPER_MOTOR, true, 0);
        Boolean flag = true;
        //值变化
        Boolean dataChanges = false;
        while (flag) {
            try {
                Thread.sleep(Constants.SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String newIoStatus = ioDeviceService.getStatus().split(",")[Constants.ROTARY_TABLE_STATION_SENSOR];
            if (turntableStatus == null) {
                turntableStatus = newIoStatus;
                continue;
            }
            if (!newIoStatus.equals(turntableStatus) && turntableStatus != null) {
                dataChanges = true;
            }
            turntableStatus = newIoStatus;
            if (dataChanges && newIoStatus.equals(SignalLevel.HIGH.getValue())) {
                flag = false;
                turntableNumber += 1;
                log.info("工位数值：{}", turntableNumber);
            }
            //当等于转台值时停止
            if (turntableNumber % 6 == number) {
                stepperMotorService.stop(Constants.ROTARY_TABLE_STEPPER_MOTOR);
            }
        }
    }
}
