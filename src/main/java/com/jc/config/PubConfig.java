package com.jc.config;

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
    public int currentFanBinNumber = 1;
    /**
     * 粉丝是否准备好
     */
    public Boolean areTheFansReady = false;
    /**
     * 菜准备好
     */
    public Boolean dishesAreReady = false;
    /**
     * 倒菜完成
     */
    public Boolean servingDishesCompleted = false;
    /**
     * 加蒸汽
     */
    public Boolean addSteam = true;
    /**
     * 配菜完成
     */
    public Boolean sideDishesCompleted = false;
    /**
     * 加完蒸汽
     */
    public Boolean finishAddingSteam = false;
    /**
     * 切肉数量
     */
    public int meatSlicingQuantity;
    /**
     * 机器人获取碗的指令
     */
    public Boolean getBowl;

    /**
     * 称重1完毕
     */
    public Boolean vegetable1Motor;
    /**
     * 称重2完毕
     */
    public Boolean vegetable2Motor;
    /**
     * 机器人加使通
     */
    public Boolean robotAlreadyEnableCommand;

}
