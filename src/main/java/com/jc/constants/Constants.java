package com.jc.constants;

public class Constants {
    /**
     * 重置命令
     */
    public static final String RESET_COMMAND = "48 3A 01 52 00 00 00 00 00 00 00 00 D5 45 44";
    /**
     * 睡眠时间（毫秒）
     */
    public static final long SLEEP_TIME_MS = 50L;
    /**
     * 最大步进电机编号
     */
    public static final int MAX_MOTOR_NO = 5;
    /**
     * 最大速度
     */
    public static final int MAX_SPEED = 500;
    /**
     * 传感器无值状态为2
     */
    public static final String NOT_INITIALIZED = "2";
    /**
     * 转台行进速度
     */
    public static final int TURNTABLE_SPEED = 30;


    /** 传感器的值 */
    /**
     * 转盘复位传感器X1
     */
    public static final int ROTARY_TABLE_RESET_SENSOR = 0;
    /**
     * 转盘工位传感器X2
     */
    public static final int ROTARY_TABLE_STATION_SENSOR = 1;
    /**
     * 碗下限位传感器X3
     */
    public static final int BOWL_LOWER_LIMIT_SENSOR = 2;
    /**
     * 碗上限位传感器X4
     */
    public static final int BOWL_UPPER_LIMIT_SENSOR = 3;
    /**
     * 自动升碗到位传感器X5
     */
    public static final int AUTO_BOWL_LIFT_POSITION_SENSOR = 4;
    /**
     * 机器人放空碗传感器X6
     */
    public static final int ROBOT_EMPTY_BOWL_SENSOR = 5;
    /**
     * 蒸器发生器液位传感器X7
     */
    public static final int STEAM_GENERATOR_LEVEL_SENSOR = 6;
    /**
     * 蒸汽发生器最高温度传感器
     */
    public static final int STEAM_GENERATOR_HIGHEST_TEMPERATURE_SENSOR = 12;
    /**
     * 蒸汽发生器最低温度传感器
     */
    public static final int STEAM_GENERATOR_LOWEST_TEMPERATURE_SENSOR = 13;


    /** 继电器 */
    /**
     * 出餐口向下
     */
    public static final int THE_FOOD_OUTLET_IS_FACING_DOWNWARDS_SWITCH = 1;
    /**
     * 出餐口向上
     */
    public static final int THE_FOOD_OUTLET_IS_FACING_UPWARDS_SWITCH = 2;
    /**
     * 出料开仓出料
     */
    public static final int DISCHARGING_FROM_WAREHOUSE_SWITCH = 3;
    /**
     * 出料关仓禁止出料
     */
    public static final int DISCHARGING_IS_PROHIBITED_AFTER_CLOSING_THE_WAREHOUSE_SWITCH = 4;
    /**
     * 柜体排气扇
     */
    public static final int CABINET_EXHAUST_FAN_SWITCH = 6;
    /**
     * 蒸汽
     */
    public static final int STEAM_SWITCH = 7;
    /**
     * 震动器
     */
    public static final int SHAKER_SWITCH = 13;
    /**
     * 抽汤泵
     */
    public static final int SOUP_PUMP_SWITCH = 17;
    /**
     * 抽水泵
     */
    public static final int WATER_PUMP_SWITCH = 30;
    /**
     * 碗交流电机N极
     */
    public static final int BOWL_N_SWITCH = 31;
    /**
     * 碗交流电机L极
     */
    public static final int BOWL_L_SWITCH = 32;


    /** 485设备 */
    /**
     * 转台步进电机控制器485地址
     */
    public static final int ROTARY_TABLE_STEPPER_MOTOR = 1;
    /**
     * 调味机485地址
     */
    public static final int SEASONING_MACHINE = 2;
    /**
     * 碗步进电机控制器485地址
     */
    public static final int BOWL_STEPPER_MOTOR = 3;
    /**
     * 粉丝步进电机控制器485地址
     */
    public static final int FAN_STEPPER_MOTOR = 4;
    /**
     * 转台工位总数
     */
    public static final int WORKSTATION_NUMBER = 6;
    /**
     * 蒸汽发生器保温状态
     */
    public static final int STEAM_GENERATOR_INSULATION_STATUS = 1;
    /**
     * 蒸汽发生器蒸汽状态
     */
    public static final int STEAM_GENERATOR_STEAM_STATUS = 2;
}
