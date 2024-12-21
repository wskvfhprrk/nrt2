package com.jc.service.impl;

import com.jc.config.DataConfig;
import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.enums.SignalLevel;
import com.jc.utils.DecimalToHexConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 粉丝仓逻辑处理类
 */
@Service
@Slf4j
public class FansService {

    @Autowired
    private StepServoDriverGatewayService stepServoDriverGatewayService; // 发送指令服务
    @Autowired
    private SignalAcquisitionDeviceGatewayService signalAcquisitionDeviceGatewayService; // IO设备服务
    @Autowired
    private PubConfig pubConfig; // 公共配置
    @Autowired
    private DataConfig dataConfig;

    // 当前粉丝仓是否复位的标志
    private boolean isFansReset = false;

    /**
     * 移动到右限位
     */
    private Result moveToRightLimit() {
        if (signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FAN_COMPARTMENT_RIGHT_LIMIT) == SignalLevel.HIGH.ordinal()) {
            return Result.success();
        }
        log.info("右限位信号是低电平");
        String hex = "040600050055"; // 发送速度指令
        stepServoDriverGatewayService.sendOrder(hex);

        hex = "040600070000"; // 发送脉冲数指令
        stepServoDriverGatewayService.sendOrder(hex);

        hex = "040600010001"; // 发送开始指令
        stepServoDriverGatewayService.sendOrder(hex);
        while (signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FAN_COMPARTMENT_RIGHT_LIMIT) == SignalLevel.LOW.ordinal()) {
            try {
                Thread.sleep(Constants.COMMAND_INTERVAL_POLLING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return Result.success();
    }

    /**
     * 粉丝仓复位——移动至第一个仓
     *
     * @return 执行结果
     */
    public Result moveToLeftLimit() {
        // 检查是否已经复位
        if (!isFansReset()) {
            Result result = this.pushRodOpen(); // 推杆复位操作
            if (result.getCode() != 200) {
                return result; // 如果推杆复位失败，返回错误
            }
        }
        log.info("检查左限位信号是否高电平");
        // 检查左限位信号是否高电平
        if (signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FAN_COMPARTMENT_LEFT_LIMIT) == SignalLevel.HIGH.ordinal()) {
            log.info("左限位信号是高电平");
            pubConfig.setCurrentFanBinNumber(1); // 设置当前仓编号为1
            return Result.success(); // 返回成功
        }

        // 如果左限位信号是低电平，发指令进行移动
        if (signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FAN_COMPARTMENT_LEFT_LIMIT) == SignalLevel.LOW.ordinal()) {
            log.info("左限位信号是低电平");
            String hex = "040600050055"; // 发送速度指令
            stepServoDriverGatewayService.sendOrder(hex);

            hex = "040600070000"; // 发送脉冲数指令
            stepServoDriverGatewayService.sendOrder(hex);

            hex = "040600000001"; // 发送开始指令
            stepServoDriverGatewayService.sendOrder(hex);
        }

        // 阻塞线程，直到检测到高电平（左限位信号）
        while (signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FAN_COMPARTMENT_LEFT_LIMIT) == SignalLevel.LOW.ordinal()) {
            try {
                Thread.sleep(50L); // 每50ms检查一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return Result.success(); // 返回成功
    }

    /**
     * 推杆向前推送
     *
     * @return 执行结果
     */
    public synchronized Result noodleBinDeliver() {
        log.info("推杆向前推送");
        if (signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.LOW.ordinal()) {
            return Result.success();
        }
        String hex = "0106000503E8"; // 发送速度指令
        stepServoDriverGatewayService.sendOrder(hex);

        hex = "01060007" + DecimalToHexConverter.decimalToHex(dataConfig.getFanPushRodThrustDistanceValue()); // 发送脉冲数指令
        stepServoDriverGatewayService.sendOrder(hex);

        hex = "010600000001"; // 发送开始指令
        stepServoDriverGatewayService.sendOrder(hex);

        long startTime = System.currentTimeMillis();
        boolean isTimedOut = false;

        // 每50毫秒检查一次状态，超时6分钟退出
        while (signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.HIGH.ordinal()) {
            if (System.currentTimeMillis() - startTime > 60000) {
                isTimedOut = true;
                break;
            }
            try {
                Thread.sleep(50); // 每50毫秒检查一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (isTimedOut) {
            log.error("推杆前推时，粉丝推杆伺服在1分钟后仍未工作");
            return Result.error("推杆在1分钟后仍未复位");
        }
        //伺服要等2秒钟后才会停
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isFansReset = false; // 标记推杆不在复位状态
        return Result.success();
    }


    /**
     * 移动到指定的粉丝仓位
     *
     * @param i 粉丝仓编号
     * @return 执行结果
     */
    public Result moveFanBin(int i) {
        if (signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.LOW.ordinal()) {
            Result result = pushRodOpen(); // 推杆复位操作
            if (result.getCode() != 200) {
                return result; // 如果推杆复位失败，返回错误
            }
        }
        if (signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.LOW.ordinal()) {
            log.error("没有拉开推拉杆");
            return Result.error("没有拉开推拉杆");
        }
        Result result = this.moveToLeftLimit(); // 粉丝仓复位操作
        if (result.getCode() == 200) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            switch (i) {
                case 1:
                    // 已在第一个仓，不需额外操作
                    break;
                case 2:
                    String hex = "0406000503E8"; // 发送转向指令
                    stepServoDriverGatewayService.sendOrder(hex);
                    hex = "040600070180"; // 发送脉冲数指令
                    stepServoDriverGatewayService.sendOrder(hex);
                    hex = "040600010001"; // 发送速度指令
                    stepServoDriverGatewayService.sendOrder(hex);
                    try {
                        Thread.sleep(2000L); // 等待2秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("执行到达第 {} 粉丝仓位", i);
                    break;
                case 3:
                    hex = "0406000503E8"; // 发送转向指令
                    stepServoDriverGatewayService.sendOrder(hex);
                    hex = "040600070300"; // 发送脉冲数指令
                    stepServoDriverGatewayService.sendOrder(hex);
                    hex = "040600010001"; // 发送速度指令
                    stepServoDriverGatewayService.sendOrder(hex);
                    try {
                        Thread.sleep(1000L); // 等待2秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("执行到达第 {} 粉丝仓位", i);
                    break;
                case 4:
                    hex = "0406000503E8"; // 发送转向指令
                    stepServoDriverGatewayService.sendOrder(hex);
                    hex = "040600070480"; // 发送脉冲数指令
                    stepServoDriverGatewayService.sendOrder(hex);
                    hex = "040600010001"; // 发送速度指令
                    stepServoDriverGatewayService.sendOrder(hex);
                    try {
                        Thread.sleep(3000L); // 等待3秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("执行到达第 {} 粉丝仓位", i);
                    break;
                case 5:
                    hex = "0406000503E8"; // 发送转向指令
                    stepServoDriverGatewayService.sendOrder(hex);
                    hex = "040600070600"; // 发送脉冲数指令
                    stepServoDriverGatewayService.sendOrder(hex);
                    hex = "040600010001"; // 发送速度指令
                    stepServoDriverGatewayService.sendOrder(hex);
                    try {
                        Thread.sleep(3000L); // 等待3秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("执行到达第 {} 粉丝仓位", i);
                    break;
                default:
                    return Result.error("只有1-5"); // 返回错误，输入超出范围
            }
        }
        pubConfig.setCurrentFanBinNumber(i); // 设置当前仓位号
        return Result.success();
    }

    /**
     * 推杆后拉操作
     *
     * @return 执行结果
     */
    public synchronized Result pushRodOpen() {
        if ((signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.HIGH.ordinal())) {
            return Result.success();
        }
        log.info("推杆向后拉");
        String hex = "0106000503E8"; // 发送速度指令
        stepServoDriverGatewayService.sendOrder(hex);

        hex = "01060007" + DecimalToHexConverter.decimalToHex(dataConfig.getFanPushRodThrustDistanceValue()); // 发送脉冲数指令
        stepServoDriverGatewayService.sendOrder(hex);

        hex = "010600010001"; // 发送开始指令
        stepServoDriverGatewayService.sendOrder(hex);

        long startTime = System.currentTimeMillis();
        boolean isTimedOut = false;

        // 每50毫秒检查一次状态，超时6分钟退出
        while (signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.LOW.ordinal()) {
            if (System.currentTimeMillis() - startTime > 60000) { // 1分钟超时
                isTimedOut = true;
                break;
            }
            try {
                Thread.sleep(50); // 每50毫秒检查一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (isTimedOut) {
            log.error("推杆后拉时，粉丝推杆伺服在1分钟后仍未工作");
            return Result.error("推杆在1分钟后仍未复位");
        }
        //伺服要等2秒钟后才会停
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isFansReset = true; // 标记推杆已复位
        return Result.success();
    }


    /**
     * 粉丝仓回到第一个仓
     * R
     *
     * @return 执行结果
     */
    public Result fanReset() {
        // 先到右边，再到左边第一个仓
        if (signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FAN_COMPARTMENT_RIGHT_LIMIT) == SignalLevel.LOW.ordinal()) {
            Result result = pushRodOpen();
            if (result.getCode() != 200) {
                return result;
            }
            result = moveToRightLimit();
            if (result.getCode() != 200) {
                return result;
            }
        }
        if(signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FAN_COMPARTMENT_LEFT_LIMIT) == SignalLevel.LOW.ordinal()){
            Result result = moveToLeftLimit(); // 已复位但左限位低，进行复位操作
            if (result.getCode() != 200) {
                return result;
            }
        }
        return Result.success();
    }

    /**
     * 执行取粉丝操作
     *
     * @return 执行结果
     */
    public Result takeFans() {
        pubConfig.setAreTheFansReady(false);
        Boolean flag = determineFanStatus(); // 判断粉丝状态
        if (flag) {
            return Result.success(); // 如果感应到粉丝，返回成功
        }
        Result result = resendFromCurrentPush(); // 推杆推送当前仓位
        if (result.getCode() != 200) {
            return result; // 推送失败返回错误
        }
        flag = determineFanStatus(); // 再次判断粉丝状态
        if (flag) {
            return Result.success(); // 如果感应到粉丝，返回成功
        }
        result = pushRodOpen(); // 推杆后拉
        if (result.getCode() != 200) {
            return result; // 推送失败返回错误
        }
        result = moveToNextOne(); // 移动到下一个粉丝仓
        if (result.getCode() != 200) {
            return result; // 移动失败返回错误
        }
        takeFans(); // 递归调用取粉丝操作
        return Result.success();
    }

    /**
     * 移动到下一个粉丝仓
     */
    private Result moveToNextOne() {
        if (pubConfig.getCurrentFanBinNumber() == 5) {
            moveToLeftLimit(); // 如果当前仓是4，执行复位操作
            log.error("没有粉丝了！");
            return Result.error("没有粉丝了！"); // 返回错误提示
        }
        moveFanBin(pubConfig.getCurrentFanBinNumber() + 1); // 移动到下一个仓
        return Result.success();
    }

    /**
     * 从当前仓位推送
     *
     * @return 执行结果
     */
    public Result resendFromCurrentPush() {
        log.info("高低电平值：{}", signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN));
        if (signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.HIGH.ordinal()) {
            log.info("推杆高电平");
            Result result = noodleBinDeliver(); // 如果已复位，直接推送
            if (result.getCode() != 200) {
                return result;
            }
            return Result.success();
        } else {
            log.info("推杆低电平");
            Result result = pushRodOpen(); // 推杆复位操作
            if (result.getCode() != 200) {
                return result; // 推杆复位失败返回错误
            }
            result = noodleBinDeliver(); // 推杆复位操作
            if (result.getCode() != 200) {
                return result; // 推杆复位失败返回错误
            }
        }
        return Result.success();
    }


    /**
     * 判断粉丝状态
     *
     * @return 是否感应到粉丝
     */
    private Boolean determineFanStatus() {
        boolean b = signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FANS_WAREHOUSE_1) == SignalLevel.LOW.ordinal()
                && signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.LOW.ordinal();
        if (b) {
            pubConfig.setAreTheFansReady(true);
        }
        return b;
    }

    /**
     * 判断推杆是否已复位
     *
     * @return 是否已复位
     */
    private Boolean isFansReset() {
        return isFansReset || signalAcquisitionDeviceGatewayService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.HIGH.ordinal();
    }
}
