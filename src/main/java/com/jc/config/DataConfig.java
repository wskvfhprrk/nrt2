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
public class DataConfig {
    /**
     * 抽汤时间
     */
    private int soupExtractionTime;
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
    private int soupExhaustTime;
    /**
     * 菜勺加蒸汽距离值
     */
    private int ladleWalkingDistanceValue;
    /**
     * 菜勺倒菜转动值
     */
    private int ladleDishDumpingRotationValue;
    /**
     * 菜勺倒菜距离（脉冲值）
     */
    private int ladleDishDumpingDistancePulseValue;
    /**
     * 粉丝推杆推动距离值
     */
    private int fanPushRodThrustDistanceValue;
    /**
     * 配料1的值
     */
    private int ingredient1Value;
    /**
     * 配料2的值
     */
    private int ingredient2Value;
    /**
     * openFanTime(秒)
     */
    private int openFanTime;
    /**
     * 是否使用称重
     */
    private Boolean isUseWeighing;
    /**
     * 称重默认值
     */
    private int defaultWeighingValue;
    /**
     * 抽汤脉冲值
     */
    private int dispenseSoupByPulseCount = 10;
}
