package com.jc.service.impl;

import com.jc.config.PubConfig;
import com.jc.constants.Constants;
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

    public void start() {
        new Thread(() -> {
            Boolean falg = true;
            while (falg) {
                if (pubConfig.getAllDevicesConnectedStatus()) {
                    //机器人复位
                    robotService.reset();
                    //转台复位
                    turntableService.turntableReset();
                    //碗复位
                    bowlService.bowlReset();
                    //出汤口复位
                    relayDeviceService.theFoodOutletIsFacingUpwards();
                    falg = false;
                }
            }
            try {
                Thread.sleep(Constants.SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("系统启动已经自动重置完毕！");
        }).start();
    }

}
