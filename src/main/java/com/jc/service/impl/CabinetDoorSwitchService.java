package com.jc.service.impl;

import com.jc.config.Result;
import com.jc.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 柜门开关
 */
@Service
@Slf4j
public class CabinetDoorSwitchService {
    @Autowired
    private Relay1DeviceGatewayService relay1DeviceGatewayService;


    /**
     * 打开右上门
     */
    public Result openUpperRightDoor() {
        relay1DeviceGatewayService.relayClosing(Constants.Y_RIGHT_DOOR);
        return Result.success();
    }
    /**
     * 关闭右上门
     */
    public Result closeUpperRightDoor() {
        relay1DeviceGatewayService.relayClosing(Constants.Y_RIGHT_DOOR);
        return Result.success();
    }
    /**
     * 打开中间左边上门
     */
    public Result openMiddleLeftUpperDoor(){
        relay1DeviceGatewayService.relayClosing(Constants.Y_RIGHT_DOOR);
        return Result.success();
    }

}
