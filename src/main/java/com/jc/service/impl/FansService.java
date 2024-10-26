package com.jc.service.impl;


import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.enums.SignalLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 粉丝仓逻辑
 */
@Service
public class FansService {

    @Autowired
    private Send485OrderService send485OrderService;
    @Autowired
    private IODeviceService ioDeviceService;

    /**
     * 粉丝仓复位
     *
     * @return
     */
    public Result noodleBinReset() {
        //先发送脉冲数，再发送指令
        String hex = "040600070000";
        send485OrderService.sendOrder(hex);
        //发送速度指令
        hex = "04060005010";
        send485OrderService.sendOrder(hex);
        //先发送脉冲数，再发送指令
        //停止
        //粉丝仓左限位
        if (ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_LEFT_LIMIT) == SignalLevel.HIGH.ordinal()) {
            //停止
            hex = "040600010001";
            send485OrderService.sendOrder(hex);
        }
        //粉丝仓左限位
        if (ioDeviceService.getStatus(Constants.X_FAN_COMPARTMENT_RIGHT_LIMIT) == SignalLevel.HIGH.ordinal()) {
            //停止
            hex = "040600000001";
            send485OrderService.sendOrder(hex);
        }
        hex = "040600000001";
        send485OrderService.sendOrder(hex);
        return Result.success();
    }

    public Result noodleBinDeliver() {
        return null;
    }
}
