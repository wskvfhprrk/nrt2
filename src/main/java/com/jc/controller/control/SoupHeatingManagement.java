package com.jc.controller.control;

import com.jc.config.BeefConfig;
import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.service.impl.RelayDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;



/**
 * 汤加热管理
 */
@Service
@Slf4j
public class SoupHeatingManagement {
    @Autowired
    private RelayDeviceService relayDeviceService;
    @Autowired
    private BeefConfig beefConfig;
    @Autowired
    private PubConfig pubConfig;

    /**
     * 汤加热到最高温度
     *
     * @return
     */
    public Result heatSoupToMaximumTemperature() {
        log.info("汤加热出到汤温度");
        new Thread(() -> relayDeviceService.soupHeating(beefConfig.getSoupHeatingTemperature())).start();
        return Result.success();
    }

    /**
     * 汤加热到中间温度
     *
     * @return
     */
    public Result heatSoupToMediumTemperature() {
        log.info("汤加热至保湿最高温度");
        new Thread(()->relayDeviceService.soupHeating(Constants.SOUP_MAXIMUM_TEMPERATURE_VALUE)).start();
        return Result.success();
    }

    /**
     * 汤加热到最小温度
     *
     * @return
     */
    public Result heatSoupToMinimumTemperature() {
        log.info("汤加热至最低温度");
        new Thread(()->relayDeviceService.soupHeating(Constants.SOUP_MINIMUM_TEMPERATURE_VALUE)).start();
        return Result.success();
    }

    /**
     * 汤自动加热——定时任务
     */
    @Scheduled(fixedRate = 180000) // 每半小时检查一次
    public void soupAutoHeating() {
        if(!pubConfig.getIsExecuteTask())return;
        log.info("自动检测汤的温度");
        //读取温度
        relayDeviceService.readTemperature();
        //如果小于汤的最小温度就加热到保持最大温度即中间温度
        if (pubConfig.getSoupTemperatureValue() < Constants.SOUP_MINIMUM_TEMPERATURE_VALUE) {
            heatSoupToMediumTemperature();
        }
    }

}