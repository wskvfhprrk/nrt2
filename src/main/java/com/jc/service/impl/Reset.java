package com.jc.service.impl;

import com.jc.config.DataConfig;
import com.jc.config.PubConfig;
import com.jc.config.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class Reset {
    @Autowired
    private RobotServiceImpl robotService;
    @Autowired
    private BowlService bowlService;
    @Autowired
    private Relay1DeviceGatewayService relay1DeviceGatewayService;
    @Autowired
    private PubConfig pubConfig;
    @Autowired
    private DataConfig dataConfig;
    @Autowired
    private FansService fansService;
    @Autowired
    private TemperatureWeighingGatewayService temperatureWeightReadingService;
    @Autowired
    private SignalAcquisitionDeviceGatewayService signalAcquisitionDeviceGatewayService;

    public synchronized void start() {
        signalAcquisitionDeviceGatewayService.sendOrderStatus = false;
        if (!pubConfig.getAllDevicesConnectedStatus()) {
            return;
        }
        log.info("设备自检复位中……");
        //粉丝仓复位
        log.info("粉丝仓复位");
        Result result = fansService.moveFanBin(2);
        if (result.getCode() != 200) {
            log.error(result.getMessage());
            return;
        }
        result = fansService.fanReset();
        if (result.getCode() != 200) {
            log.error(result.getMessage());
            return;
        }
        result = fansService.resendFromCurrentPush();
        if (result.getCode() != 200) {
            log.error(result.getMessage());
            return;
        }
        log.info("抽汤排气");
        result = relay1DeviceGatewayService.soupPipeExhaust(dataConfig.getSoupExhaustTime());
        if (result.getCode() != 200) {
            log.error(result.getMessage());
            return;
        }
        //机器人复位
        log.info("机器人复位");
        robotService.robotReset();
        //出汤口复位
        log.info("出汤口复位");
        log.info("蒸汽盖上升");
        result = relay1DeviceGatewayService.soupSteamCoverUp();
        if (result.getCode() != 200) {
            log.error(result.getMessage());
            return;
        }
        log.info("菜勺移动到装菜位置");
        result = bowlService.spoonLoad();
        if (result.getCode() != 200) {
            log.error(result.getMessage());
            return;
        }
        log.info("蒸汽给汤加热至保温温度");
        result = temperatureWeightReadingService.soupHeatTo(dataConfig.getSoupInsulationTemperature());
        if (result.getCode() != 200) {
            log.error(result.getMessage());
            return;
        }
        log.info("自检完成！");
        pubConfig.setDeviceSelfCheckComplete(true);
        /**
         * 打开系统定时任务
         */
        pubConfig.setIsExecuteTask(true);
        log.info("定时任务打开！");
        //复位后打开windows浏览器脚本全屏显示
        try {
            String scriptPath = "C:\\scripts\\open_browser.ps1";
            String command = "powershell.exe -ExecutionPolicy Bypass -File \"" + scriptPath + "\"";
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

