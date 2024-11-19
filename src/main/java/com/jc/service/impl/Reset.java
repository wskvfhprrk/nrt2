package com.jc.service.impl;

import com.jc.config.DataConfig;
import com.jc.config.PubConfig;
import com.jc.config.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Reset {
    @Autowired
    private RobotServiceImpl robotService;
    @Autowired
    private BowlService bowlService;
    @Autowired
    private RelayDeviceService relayDeviceService;
    @Autowired
    private PubConfig pubConfig;
    @Autowired
    private DataConfig dataConfig;
    @Autowired
    private FansService fansService;

    public void start() {
        if (!pubConfig.getAllDevicesConnectedStatus()) {
            return;
        }
        log.info("设备自检复位中……");
        //打开蒸汽发生器
        log.info("打开蒸汽发生器");
        relayDeviceService.openSteamGenerator();
        log.info("抽汤排气");
        relayDeviceService.soupPipeExhaust(dataConfig.getSoupExhaustTime());
        //机器人复位
        log.info("机器人复位");
        robotService.robotReset();
        //出汤口复位
        log.info("出汤口复位");
        //粉丝仓复位
        log.info("粉丝仓复位");
        Result result = fansService.resendFromCurrentPush();
        if (result.getCode() != 200) {
            log.error(result.getMessage());
            return;
        }
        result = fansService.moveFanBin(2);
        if (result.getCode() != 200) {
            log.error(result.getMessage());
            return;
        }
        result = fansService.fanReset();
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
        result = relayDeviceService.soupHeatTo(dataConfig.getSoupInsulationTemperature());
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
    }
}

