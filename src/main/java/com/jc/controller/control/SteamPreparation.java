package com.jc.controller.control;

import com.jc.config.BeefConfig;
import com.jc.config.Result;
import com.jc.entity.Order;
import com.jc.service.impl.RelayDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;


/**
 * 汤加热准备任务
 */
@Service
@Slf4j
public class SteamPreparation {
    @Autowired
    private RelayDeviceService relayDeviceService;
    @Autowired
    private BeefConfig beefConfig;

    public Result start() {
        relayDeviceService.soupHeating(beefConfig.getSoupHeatingTemperature());
        // TODO: 2024/8/26 汤加热不到温度阻塞进程
        return Result.success();
    }

}