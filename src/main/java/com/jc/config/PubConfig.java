package com.jc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 共公参数
 */
@Component
@ConfigurationProperties(prefix = "pub")
@Data
public class PubConfig {

    /**
     * 机器人在等机状态
     */
    private Boolean robotStatus = true;
    /**
     * 转台当前工位数
     */
    private int turntableNumber = 0;
    /**
     * 有多少个未做订单
     */
    private int orderNumber = 0;
    /**
     * 转台复位
     */
    private Boolean turntableReset = false;
    /**
     * 蒸汽机中是有水
     */
    private Boolean hasWaterInSteamEngine = false;
    /**
     * 碗复位
     */
    private Boolean resetBowl = false;
    /**
     * 配料1完成
     */
    private Boolean ingredient1Completed = false;
    /**
     * 配料2完成
     */
    private Boolean ingredient2Completed = false;
    /**
     * 配料3完成
     */
    private Boolean ingredient3Completed = false;
    /**
     * 配料4完成
     */
    private Boolean ingredient4Completed = false;
    /**
     * 放置碗完成
     */
    private Boolean placingBowlCompleted = false;
    /**
     * 放置粉丝完成
     */
    private Boolean placingNoodlesCompleted = false;
    /**
     * 放置配料完成
     */
    private Boolean placingIngredientsCompleted = false;
    /**
     * 加蒸汽完成
     */
    private Boolean addingSteamCompleted = false;
    /**
     * 加调料完成
     */
    private Boolean addingSeasoningCompleted = false;
    /**
     * 加汤完成
     */
    private Boolean addingSoupCompleted = false;
    /**
     * 出餐完成
     */
    private Boolean servingCompleted=false;
    /**
     * 有碗工位号
     */
    private String bowlStationNumber;
}
