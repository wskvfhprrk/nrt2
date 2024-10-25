package com.jc.service.impl;

import com.jc.config.BeefConfig;
import com.jc.config.PubConfig;
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
    private TurntableService turntableService;
    @Autowired
    private PubConfig pubConfig;
    @Autowired
    private BeefConfig beefConfig;

    public void start() {
        if (!pubConfig.getAllDevicesConnectedStatus()) {
            return;
        }
        log.info("设备自检复位中……");
        //打开蒸汽发生器
        log.info("打开蒸汽发生器");
        relayDeviceService.openSteamGenerator();
        log.info("抽汤排气");
//        new Thread(()->relayDeviceService.soupExhaust(beefConfig.soupExhaustTime)).start();
        //机器人复位
        log.info("机器人复位");
        robotService.robotReset();
        //转台复位
        log.info("转台复位");
        turntableService.turntableReset();
        //碗复位
        log.info("碗复位");
//        bowlService.bowlReset();
        //出汤口复位
        log.info("出汤口复位");
        relayDeviceService.theFoodOutletIsFacingUpwards();
        //设备自检完成
        pubConfig.setDeviceSelfCheckComplete(true);
        log.info("自检完成！");
        /**
         * 打开系统定时任务
         */
        pubConfig.setIsExecuteTask(true);
//        log.info("定时任务打开！");
    }
}
