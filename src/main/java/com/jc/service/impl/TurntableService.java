package com.jc.service.impl;

import com.jc.config.IpConfig;
import com.jc.config.PubConfig;
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
    private final PubConfig pubConfig;


    @Autowired
    public TurntableService(NettyServerHandler nettyServerHandler,
                            IODeviceService ioDeviceService,
                            StepperMotorService stepperMotorService, IpConfig ipConfig, PubConfig pubConfig) {
        this.nettyServerHandler = nettyServerHandler;
        this.ioDeviceService = ioDeviceService;
        this.stepperMotorService = stepperMotorService;
        this.ipConfig = ipConfig;
        this.pubConfig = pubConfig;
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
            pubConfig.setTurntableNumber(1);
            pubConfig.setIsTurntableReset(true);
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
                    pubConfig.setTurntableNumber(1);
                    pubConfig.setIsTurntableReset(true);
                }
            }
        }
        return "ok";
    }

    /**
     * 转到下一个工位
     */
    public void moveToNext() {
        log.info("移动到下一个工位");
        int oldNumber = pubConfig.getTurntableNumber();
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
        while (flag) {
            try {
                Thread.sleep(Constants.SLEEP_TIME_MS / 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int newNumber = pubConfig.getTurntableNumber();
            if (oldNumber % Constants.WORKSTATION_NUMBER == 5 && newNumber == Constants.WORKSTATION_NUMBER) {
                stepperMotorService.stop(Constants.ROTARY_TABLE_STEPPER_MOTOR);
                flag = false;
                continue;
            }
            if (newNumber % Constants.WORKSTATION_NUMBER == oldNumber % Constants.WORKSTATION_NUMBER + 1) {
                stepperMotorService.stop(Constants.ROTARY_TABLE_STEPPER_MOTOR);
                flag = false;
            }
        }
        log.info("工位数值：{}", pubConfig.getTurntableNumber());

    }

    /**
     * 转到某一个工位停止
     *
     * @param number
     */
    public void alignToPosition(int number) {
        //如果没有初始化要多转一圈第一圈让重置
        //除以6求余为工位
        if (pubConfig.getIsTurntableReset() && pubConfig.getTurntableNumber() % Constants.WORKSTATION_NUMBER == number) {
            log.info("已经在{}位上", number);
            return;
        }
        //发送转盘的转速为最大值,太大IO感应不到，会超过原点位置
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
        while (flag) {
            try {
                Thread.sleep(Constants.SLEEP_TIME_MS / 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int newNumber = pubConfig.getTurntableNumber();
            if (newNumber % Constants.WORKSTATION_NUMBER == number) {
                stepperMotorService.stop(Constants.ROTARY_TABLE_STEPPER_MOTOR);
                flag = false;
            }
        }
        log.info("工位数值：{}", pubConfig.getTurntableNumber());
    }

}
