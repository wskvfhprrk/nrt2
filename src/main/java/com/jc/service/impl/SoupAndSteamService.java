package com.jc.service.impl;

import com.jc.constants.Constants;
import com.jc.enums.SignalLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 汤和蒸汽控制逻辑
 */
@Service
@Slf4j
public class SoupAndSteamService {
    @Autowired
    private RelayDeviceService relayDeviceService;
    @Autowired
    private IODeviceService ioDeviceService;
    //打开蒸汽时——温度根据蒸汽罐体最大气压或者订单完成就停止，不根据蒸汽发生器上温度无法控制
    public void openSteam(){
        //给10秒钟时间
        relayDeviceService.openClose(Constants.STEAM_SWITCH,10);
    }
    //自动保温——温度根据蒸汽发生器上温度进行控制
    public void automaticInsulation(){
        //查IO状态
        String ioStatus = ioDeviceService.getStatus();
        String[] split = ioStatus.split(",");
        //如果传感器是高电平时
        if(split[Constants.STEAM_GENERATOR_LEVEL_SENSOR].equals(SignalLevel.HIGH.getValue())){
            relayDeviceService.steamClose();
        }else {
            relayDeviceService.steamOpen();
        }
    }
}
