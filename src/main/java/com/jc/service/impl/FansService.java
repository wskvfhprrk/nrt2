package com.jc.service.impl;

import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.enums.SignalLevel;
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
    private Send485OrderService send485OrderService; // 发送指令服务
    @Autowired
    private IODeviceService ioDeviceService; // IO设备服务
    @Autowired
    private PubConfig pubConfig; // 公共配置

    // 当前粉丝仓是否复位的标志
    private boolean isFansReset = false;

    /**
     * 粉丝仓复位——移动至第一个仓
     *
     * @return 执行结果
     */
    public Result noodleBinReset() {
        // 检查是否已经复位
        if (!isFansReset()) {
            Result result = this.pushRodOpen(); // 推杆复位操作
            if (result.getCode() != 200) {
                return result; // 如果推杆复位失败，返回错误
            }
        }

        // 检查左限位信号是否高电平
        if (ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_LEFT_LIMIT) == SignalLevel.HIGH.ordinal()) {
            pubConfig.setCurrentFanBinNumber(1); // 设置当前仓编号为1
            return Result.success(); // 返回成功
        }

        // 如果左限位信号是低电平，发指令进行移动
        if (ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_LEFT_LIMIT) == SignalLevel.LOW.ordinal()) {
            String hex = "040600050015"; // 发送速度指令
            send485OrderService.sendOrder(hex);

            hex = "040600070000"; // 发送脉冲数指令
            send485OrderService.sendOrder(hex);

            hex = "040600000001"; // 发送开始指令
            send485OrderService.sendOrder(hex);
        }

        // 阻塞线程，直到检测到高电平（左限位信号）
        while (ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_LEFT_LIMIT) == SignalLevel.LOW.ordinal()) {
            try {
                Thread.sleep(50L); // 每500ms检查一次
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
    public Result noodleBinDeliver() {
        log.info("推杆向前推送");
        if (ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.LOW.ordinal()) {
            return Result.success();
        }
        String hex = "0106000503E8"; // 发送速度指令
        send485OrderService.sendOrder(hex);

        hex = "0106000703E8"; // 发送脉冲数指令
        send485OrderService.sendOrder(hex);

        hex = "010600000001"; // 发送开始指令
        send485OrderService.sendOrder(hex);

        long startTime = System.currentTimeMillis();
        boolean isTimedOut = false;

        // 每50毫秒检查一次状态，超时6分钟退出
        while (ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.HIGH.ordinal()) {
            if (System.currentTimeMillis() - startTime > 60000) { // 10000
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
            return Result.error(500, "推杆在1分钟后仍未复位");
        }
        //伺服要等2秒钟后才会停
        try {
            Thread.sleep(2000L);
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
        if (ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.LOW.ordinal()) {
//            log.warn("推杆没有复位");
            Result result = pushRodOpen(); // 推杆复位操作
            if (result.getCode() != 200) {
                return result; // 如果推杆复位失败，返回错误
            }
        }
//        if (ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.LOW.ordinal()) {
//            log.error("没有拉开推拉杆");
//            return Result.error(500, "没有拉开推拉杆");
//        }
        Result result = this.noodleBinReset(); // 粉丝仓复位操作
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
                    send485OrderService.sendOrder(hex);
                    hex = "040600070180"; // 发送脉冲数指令
                    send485OrderService.sendOrder(hex);
                    hex = "040600010001"; // 发送速度指令
                    send485OrderService.sendOrder(hex);
                    try {
                        Thread.sleep(2000L); // 等待2秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("执行到达第 {} 粉丝仓位", i);
                    break;
                case 3:
                    hex = "0406000503E8"; // 发送转向指令
                    send485OrderService.sendOrder(hex);
                    hex = "040600070300"; // 发送脉冲数指令
                    send485OrderService.sendOrder(hex);
                    hex = "040600010001"; // 发送速度指令
                    send485OrderService.sendOrder(hex);
                    try {
                        Thread.sleep(1000L); // 等待2秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("执行到达第 {} 粉丝仓位", i);
                    break;
                case 4:
                    hex = "0406000503E8"; // 发送转向指令
                    send485OrderService.sendOrder(hex);
                    hex = "040600070480"; // 发送脉冲数指令
                    send485OrderService.sendOrder(hex);
                    hex = "040600010001"; // 发送速度指令
                    send485OrderService.sendOrder(hex);
                    try {
                        Thread.sleep(3000L); // 等待3秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("执行到达第 {} 粉丝仓位", i);
                    break;
                default:
                    return Result.error(500, "只有1-4"); // 返回错误，输入超出范围
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
    public Result pushRodOpen() {
        log.info("推杆向后拉");
        if ((ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.HIGH.ordinal())) {
            return Result.success();
        }
        String hex = "0106000503E8"; // 发送速度指令
        send485OrderService.sendOrder(hex);

        hex = "0106000703E8"; // 发送脉冲数指令
        send485OrderService.sendOrder(hex);

        hex = "010600010001"; // 发送开始指令
        send485OrderService.sendOrder(hex);

        long startTime = System.currentTimeMillis();
        boolean isTimedOut = false;

        // 每50毫秒检查一次状态，超时6分钟退出
        while (ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.LOW.ordinal()) {
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
            return Result.error(500, "推杆在1分钟后仍未复位");
        }
        //伺服要等2秒钟后才会停
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isFansReset = true; // 标记推杆已复位
        return Result.success();
    }


    /**
     * 粉丝仓回到第一个仓
     *
     * @return 执行结果
     */
    public Result FanReset() {
        boolean b = ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_LEFT_LIMIT) == SignalLevel.HIGH.ordinal();
        if (isFansReset() && b) {
            return Result.success(); // 如果已复位且左限位信号高，返回成功
        }
        if (isFansReset() && !b) {
            return noodleBinReset(); // 已复位但左限位低，进行复位操作
        }
        if (!b) {
            Result result = pushRodOpen(); // 推杆复位操作
            if (result.getCode() != 200) {
                return result; // 如果推杆复位失败，返回错误
            }
            return noodleBinReset(); // 粉丝仓复位
        }
        long startTime = System.currentTimeMillis();
        long timeoutMillis = 36000;
        Boolean falg = false;

        while (!isFansReset()) {
            // 检查是否超过了超时时间
            if (System.currentTimeMillis() - startTime >= timeoutMillis) {
                log.error("复位超过了6分钟");
                falg = true;
                break;
            }

            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
                // 如果线程被中断，也可以选择跳出循环
                break;
            }
        }
        if (falg) {
            return Result.error(500, "复位超过了6分钟");
        }
//        Result result = noodleBinDeliver();
//        if (result.getCode() != 200) {
//            return result;
//        }
//        result = pushRodOpen();
//        if (result.getCode() != 200) {
//            return result;
//        }
        return Result.error(500, "未知错误"); // 返回未知错误
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
        return null;
    }

    /**
     * 移动到下一个粉丝仓
     */
    private Result moveToNextOne() {
        if (pubConfig.getCurrentFanBinNumber() == 4) {
            noodleBinReset(); // 如果当前仓是4，执行复位操作
            return Result.error(500, "没有粉丝了！"); // 返回错误提示
        }
        moveFanBin(pubConfig.getCurrentFanBinNumber() + 1); // 移动到下一个仓
        return Result.success();
    }

    /**
     * 从当前仓位推送
     *
     * @return 执行结果
     */
    private Result resendFromCurrentPush() {
        if (isFansReset()) {
            Result result = noodleBinDeliver(); // 如果已复位，直接推送
            if (result.getCode() != 200) {
                return result;
            }
            return Result.success();
        }

        Result result = pushRodOpen(); // 推杆复位操作
        if (result.getCode() != 200) {
            return result; // 推杆复位失败返回错误
        }

        result = noodleBinDeliver(); // 推送操作
        if (result.getCode() != 200) {
            return result;
        }
        return Result.success();
    }


    /**
     * 判断粉丝状态
     *
     * @return 是否感应到粉丝
     */
    private Boolean determineFanStatus() {
        boolean b = ioDeviceService.getStatus(Constants.X_FANS_WAREHOUSE_1) == SignalLevel.LOW.ordinal()
                && ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.LOW.ordinal();
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
        return isFansReset || ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.HIGH.ordinal();
    }
}
