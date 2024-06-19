package com.jc.service.impl;

import com.jc.config.RobotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// @Service注解用于将此类注册为Spring容器中的服务bean
@Service
@Slf4j
public class RobotService {


    private final RobotConfig robotConfig;

    // 构造函数注入RobotConfig
    @Autowired
    public RobotService(RobotConfig robotConfig) {
        this.robotConfig = robotConfig;
    }

    // 业务逻辑处理方法
    public void performOperations() {
        if (robotConfig.isReset()) {
            log.info("机器人正在复位。");
        }
        if (robotConfig.isBowlPickupSuccess()) {
            log.info("取碗成功。");
        }
        if (robotConfig.isSoupOutSuccess()) {
            log.info("出汤成功。");
        }
        if (robotConfig.isBeefReady()) {
            log.info("牛肉准备完毕。");
        }
        if (robotConfig.isBeefStorageAdded()) {
            log.info("牛肉仓加入完毕。");
        }
        if (robotConfig.isSteamReady()) {
            log.info("蒸汽准备完毕。");
        }
        if (robotConfig.isSteamAdded()) {
            log.info("蒸汽加入完毕。");
        }
        if (robotConfig.isSeasoningAdded()) {
            log.info("调料加入完毕。");
        }
        if (robotConfig.isSoupAdded()) {
            log.info("汤加入完毕。");
        }
    }
}
