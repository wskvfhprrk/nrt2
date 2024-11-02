package com.jc.service.impl;


import com.jc.config.BeefConfig;
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
    //当前粉丝仓编号
    private int currentFanBinNumber = 0;

    /**
     * 粉丝仓复位
     *
     * @return
     */
    public Result noodleBinReset() {
        if (ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_LEFT_LIMIT) == SignalLevel.HIGH.ordinal()) {
            currentFanBinNumber = 1;
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

    public Result noodleBinDeliver() {
        //先发速度
        String hex = "0106000503E8";
        send485OrderService.sendOrder(hex);
        hex="0106000703E8";
        send485OrderService.sendOrder(hex);
        hex="010600000001";
        send485OrderService.sendOrder(hex);
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        hex="010600010001";
        send485OrderService.sendOrder(hex);
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.success();
    }

    public Result moveFanBin(int i) {
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
                    currentFanBinNumber = 1;
                    return Result.success();
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
                    currentFanBinNumber = 2;
                    return Result.success();
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
                    currentFanBinNumber = 3;
                    return Result.success();
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
                    currentFanBinNumber = 4;
                    return Result.success();
                case 5:
                    //先发送脉冲数
                    hex = "040600070600";
                    send485OrderService.sendOrder(hex);
                    hex = "040600010001";
                    send485OrderService.sendOrder(hex);
                    try {
                        Thread.sleep(3000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentFanBinNumber = 5;
                    return Result.success();
                default:
                    return Result.error(500, "不是1-5数字");
            }
        }
        return Result.success();
    }
}
