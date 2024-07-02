package com.jc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// @Component注解用于将此类注册为Spring容器中的bean
@Component
// @ConfigurationProperties注解用于将属性文件中的配置注入到此类中
@ConfigurationProperties(prefix = "robot")
@Data
public class RobotConfig {

    private boolean reset;
    private boolean bowlPickupSuccess;
    private boolean soupOutSuccess;
    private boolean beefReady;
    private boolean beefStorageAdded;
    private boolean steamReady;
    private boolean steamAdded;
    private boolean seasoningAdded;
    private boolean soupAdded;
    //出料机是否完成出料
    private boolean ejectionIsComplete;

}
