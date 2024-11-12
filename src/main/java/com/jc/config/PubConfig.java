package com.jc.config;

import com.jc.enums.SteamGeneratorState;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 共公参数
 */

@Configuration
@ConfigurationProperties(prefix = "pub")
@Data
public class PubConfig {
    /**
     * 出餐口有碗
     */
    public Boolean thereIsABowlAtTheServingWindow;
    /**
     * 出餐口没有升起
     */
    public Boolean servingWindowResetSensor;
    /**
     * 出餐口上一碗汤没有取走
     */
    public Boolean theBowlWasNotTakenFromTheServingWindow;
    /**
     * 机器人在待机状态
     */
    private Boolean isRobotStatus = false;
    /**
     * 机器人成功执行命令
     */
    public Boolean isRobotExecutionNaming = false;

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
    private Boolean isTurntableReset = false;
    /**
     * 蒸汽机中是有水
     */
    private Boolean isWaterInSteamEngine = false;
    /**
     * 碗复位
     */
    private Boolean isResetBowl = false;
    /**
     * 配料1完成
     */
    private Boolean isIngredient1Completed = false;
    /**
     * 配料2完成
     */
    private Boolean isIngredient2Completed = false;
    /**
     * 配料3完成
     */
    private Boolean isIngredient3Completed = false;
    /**
     * 配料4完成
     */
    private Boolean isIngredient4Completed = false;
    /**
     * 放置碗完成
     */
    private Boolean isPlacingBowlCompleted = false;
    /**
     * 放置粉丝完成
     */
    private Boolean isPlacingNoodlesCompleted = false;
    /**
     * 放置配料完成
     */
    private Boolean isPlacingIngredientsCompleted = false;
    /**
     * 加蒸汽完成
     */
    private Boolean isAddingSteamCompleted = false;
    /**
     * 加调料完成
     */
    private Boolean isAddingSeasoningCompleted = false;
    /**
     * 加碗完成
     */
    private Boolean isAddingBowlCompleted = false;
    /**
     * 汤加热完成
     */
    private Boolean isSoupHeatingComplete = false;
    /**
     * 加汤完成
     */
    private Boolean isAddingSoupCompleted = false;
    /**
     * 机器人出餐完成——松开夹手
     */
    private Boolean isServingCompleted = false;
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
     * 是否开启定时任务——初始化时给开启，急停关闭
     */
    public Boolean isExecuteTask = false;
    /**
     * 粉丝货道
     */
    public int currentFanBinNumber = 0;
    /**
     * 粉丝是否准备好
     */
    public Boolean areTheFansReady = false;

}
