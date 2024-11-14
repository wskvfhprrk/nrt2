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
    private PubConfig pubConfig;
    @Autowired
    private BeefConfig beefConfig;
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
        new Thread(() -> relayDeviceService.soupPipeExhaust(beefConfig.soupExhaustTime)).start();
        //机器人复位
        log.info("机器人复位");
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robotService.robotReset();
        //出汤口复位
        log.info("出汤口复位");
        //粉丝仓复位
        fansService.FanReset();
        log.info("粉丝仓复位");
//        relayDeviceService.theFoodOutletIsFacingUpwards();
        //设备自检完成
//        while (!pubConfig.getIsRobotStatus()) {
//            try {
//                Thread.sleep(500L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        log.info("自检完成！");
        pubConfig.setDeviceSelfCheckComplete(true);
        /**
         * 打开系统定时任务
         */
        pubConfig.setIsExecuteTask(true);
        log.info("定时任务打开！");
    }
}

