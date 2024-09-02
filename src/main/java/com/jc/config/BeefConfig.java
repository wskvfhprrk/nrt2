package com.jc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 参数配置
 */
@Component
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
    private Double soupHeatingTemperature;
    /**
     * 碗蒸汽时间
     */
    private int bowlSteamTime;
    /**
     * 出汤时间
     */
    private int theFoodOutletIsFacingDownwardsTime;
    /**
     * 转台速度
     */
    private int turntableSpeed;
    /**
     * 抽汤排气时间
     */
    public int soupExhaustTime;
}
