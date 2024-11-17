package com.jc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 参数配置
 */

@Configuration
@ConfigurationProperties(prefix = "data")
@Data
public class BeefConfig {
    /**
     * 10元牛肉
     */
    private int beef10;
    /**
     * 15元牛肉
     */
    private int beef15;
    /**
     * 20元牛肉
     */
    private int beef20;
    /**
     * 香菜配置
     */
    private int cilantro;
    /**
     * 葱花配置
     */
    private int choppedGreenOnion;
    /**
     * 抽汤时间
     */
    private int soupExtractionTime;
    /**
     * 震动器时间
     */
    private int vibratorTime;
    /**
     * 汤加热温度
     */
    private int soupHeatingTemperature;
    /**
     * 汤加保温温度
     */
    private int soupInsulationTemperature;
    /**
     * 加蒸汽时间（秒）
     */
    private int steamAdditionTimeSeconds;
    /**
     * 抽汤排气时间
     */
    public int soupExhaustTime;
    /**
     * 菜勺走动距离值
     */
    public int ladleWalkingDistanceValue ;
    /**
     * 菜勺倒菜转动值
     */
    public int ladleDishDumpingRotationValue ;
    /**
     * 粉丝推杆推动距离值
     */
    public int fanPushRodThrustDistanceValue ;
}
