package com.jc.service.impl;


import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.enums.SignalLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 粉丝仓逻辑
 */
@Service
@Slf4j
public class FansService {

    @Autowired
    private Send485OrderService send485OrderService;
    @Autowired
    private IODeviceService ioDeviceService;
    @Autowired
    private PubConfig pubConfig;
    //当前粉丝仓编号
    private boolean isFansReset = false;

    /**
     * 粉丝仓复位
     *
     * @return
     */
    public Result noodleBinReset() {
        if (!isFansReset()) {
            Result result = this.pushRodOpen();
            if (result.getCode() != 200) {
                return result;
            }
        }
        if (ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_LEFT_LIMIT) == SignalLevel.HIGH.ordinal()) {
            pubConfig.setCurrentFanBinNumber(1);
            return Result.success();
        }
        while (ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_LEFT_LIMIT) == SignalLevel.LOW.ordinal()) {
            //先发速度
            String hex = "040600050020";
            send485OrderService.sendOrder(hex);
            //先发送脉冲数，再发送指令
            hex = "040600070000";
            send485OrderService.sendOrder(hex);
            hex = "040600000001";
            send485OrderService.sendOrder(hex);
        }
        return Result.success();
    }

    /**
     * 推杆向推向前
     *
     * @return
     */
    public Result noodleBinDeliver() {
        //先发速度
        String hex = "0106000503E8";
        send485OrderService.sendOrder(hex);
        hex = "0106000703E8";
        send485OrderService.sendOrder(hex);
        hex = "010600000001";
        send485OrderService.sendOrder(hex);
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isFansReset = false;
        return Result.success();
    }

    public Result moveFanBin(int i) {
        if (!isFansReset()) {
            log.warn("推杆没有复位");
            Result result = pushRodOpen();
            if (result.getCode() != 200) {
                return result;
            }
        }
        //先回原蹼再发脉冲
        Result result = this.noodleBinReset();
        if (result.getCode() == 200) {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //先发速度
            String hex = "0406000503E8";
            send485OrderService.sendOrder(hex);
            switch (i) {
                //先发送脉冲数，再发送指令
                case 1:
                    //原点位置为第一个室
                    break;
                case 2:
                    //先发送脉冲数
                    hex = "040600070180";
                    send485OrderService.sendOrder(hex);
                    hex = "040600010001";
                    send485OrderService.sendOrder(hex);
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    //先发送脉冲数
                    hex = "040600070300";
                    send485OrderService.sendOrder(hex);
                    hex = "040600010001";
                    send485OrderService.sendOrder(hex);
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    log.info("执行到达第 {} 粉丝仓位", i);
                    //先发送脉冲数
                    hex = "040600070480";
                    send485OrderService.sendOrder(hex);
                    hex = "040600010001";
                    send485OrderService.sendOrder(hex);
                    try {
                        Thread.sleep(3000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    return Result.error(500, "不是1-4数字");
            }
        }
        pubConfig.setCurrentFanBinNumber(i);
        return Result.success();
    }

    /**
     * 推杆后拉
     *
     * @return
     */
    private Result pushRodOpen() {
        //先发速度
        String hex = "0106000503E8";
        send485OrderService.sendOrder(hex);
        hex = "0106000703E8";
        send485OrderService.sendOrder(hex);
        hex = "010600010001";
        send485OrderService.sendOrder(hex);
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isFansReset = true;
        return Result.success();
    }

    public Result FanReset() {
        //原点传感器
        boolean b = ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_LEFT_LIMIT) == SignalLevel.HIGH.ordinal();
        if (isFansReset() && b) {
            return Result.success();
        }
        if (isFansReset() && !b) {
            return moveFanBin(1);
        }
        if (!b) {
            //推杆拉开移动到第一个
            Result result = pushRodOpen();
            if (result.getCode() != 200) {
                return result;
            }
            return moveFanBin(1);
        }
        return Result.error(500, "未知错误");
    }

    public Result takeFans() {
        //判断粉丝状态
        Boolean falg = determineFanStatus();
        if (falg) {
            return Result.success();
        }
        //如果没有就复位再推送从当前推送一个
        Result result = resendFromCurrentPush();
        if (result.getCode() != 200) {
            return result;
        }
        //再检查一下，如果粉丝传感器感应到，并且推杆推到位即可
        falg = determineFanStatus();
        if (falg) {
            return Result.success();
        }
        //否则移动到下一个，递归
        result = moveToNextOne();
        if (result.getCode() != 200) {
            return result;
        }

        takeFans();
        return null;
    }

    /**
     * 移动到下一个粉丝仓
     */
    private Result moveToNextOne() {
        if (pubConfig.getCurrentFanBinNumber() == 4) {
            moveFanBin(1);
            return Result.error(500, "没有粉丝了！");
        }
        moveFanBin(pubConfig.getCurrentFanBinNumber() + 1);
        return Result.success();
    }

    /**
     * 推杆向前推
     *
     * @return
     */
    private Result resendFromCurrentPush() {
        //如果是复位就直接推
        if (isFansReset()) {
            noodleBinDeliver();
            return Result.success();
        }
        Result result = pushRodOpen();
        if (result.getCode() != 200) {
            return result;
        }
        noodleBinDeliver();
        return Result.success();
    }

    private Boolean determineFanStatus() {
        //如果粉丝传感器感应到，并且推杆推到位根据位置判断哪里返回
        if (ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.LOW.ordinal() && ioDeviceService.getStatus(Constants.X_FANS_WAREHOUSE_1) == SignalLevel.LOW.ordinal()) {
            return true;
        }
        return false;
    }

    private Boolean isFansReset() {
        if (isFansReset) {
            return true;
        }
        return ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.HIGH.ordinal();
    }
}
