package com.jc.service.impl;

import com.jc.config.BeefConfig;
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
    private BeefConfig beefConfig;
    @Autowired
    private FansService fansService;
    @Autowired
    private IODeviceService iODeviceService;

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
        result = relayDeviceService.soupHeatTo(beefConfig.getSoupInsulationTemperature());
        if (result.getCode() != 200) {
            log.error(result.getMessage());
            return;
        }
//        //等待几个参数完成才算自检完成
//        //1、机器人复位
//        //2、检测汤温度到达保温温度
//        //3、粉丝仓传感器到低电平
//        Boolean fanBinSensor = iODeviceService.getStatus(Constants.X_FAN_COMPARTMENT_ORIGIN) == SignalLevel.LOW.ordinal();
//        //4、菜勺到达装菜位置
//        Boolean ladleReachedDishLoadingPosition = iODeviceService.getStatus(Constants.X_SOUP_INGREDIENT_SENSOR) == SignalLevel.HIGH.ordinal();
        //达不到条件阻塞进行检测
//        while (!pubConfig.getIsRobotStatus() ||
//                !fanBinSensor ||
//                !ladleReachedDishLoadingPosition
//        ) {
//            //如果机器人不重置发送重置指令
//            if (!pubConfig.getIsRobotStatus()) {
//                log.error("机器人未重置，重新重置");
//                robotService.robotReset();
//            }
//            if( !fanBinSensor){
//                log.error("粉丝仓未重置，请检查");
//            }
//            if( !ladleReachedDishLoadingPosition){
//                log.error("装菜勺未到达装菜位置，请检查");
//            }
//            try {
//                Thread.sleep(Constants.SLEEP_TIME_MS * 10);
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

