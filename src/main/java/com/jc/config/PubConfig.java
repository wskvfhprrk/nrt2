package com.jc.config;

import com.jc.enums.SteamGeneratorState;
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
     * 机器人在待机状态
     */
    private Boolean robotStatus = false;
    /**
     * 机器人成功执行命令
     */
    public Boolean robotExecutionNaming = false;

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
     * 加碗完成
     */
    private Boolean addingBowlCompleted = false;
    /**
     * 汤加热完成
     */
    private Boolean soupHeatingComplete = false;
    /**
     * 加汤完成
     */
    private Boolean addingSoupCompleted = false;
    /**
     * 出餐完成
     */
    private Boolean servingCompleted = false;
    /**
     * 有碗工位号
     */
    private String bowlStationNumber;
    /**
     * 订单完成状态
     */
    private Boolean orderCompletedStatus = false;
    /**
     * 蒸汽发生器当前状态：1表示保湿状态、2表示蒸汽状态
     */
    private int steamGeneratorCurrentState = SteamGeneratorState.HUMIDIFYING.getValue();
    /**
     * 蒸汽发生器是否有水
     */
    private Boolean steamGeneratorWaterStatus = false;
    /**
     * 所有设备连接状态
     */
    private Boolean allDevicesConnectedStatus = false;
    /**
     * 设备自检完成
     */
    public boolean deviceSelfCheckComplete = false;

    /**
     * 汤的温度
     */
    public Double soupTemperatureValue = 0.0;

    /**
     * 称重传感器
     */
    public int[] calculateWeight = new int[4];
    /**
     * 是否开启定时任务
     */
    public Boolean executeTask = false;

}
