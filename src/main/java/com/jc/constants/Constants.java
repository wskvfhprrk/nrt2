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
    /** 传感器的值 */
    /**
     * 转盘复位传感器
     */
    public static final int ROTARY_TABLE_RESET_SENSOR = 0;
    /**
     * 转台行进速度
     */
    public static final int TURNTABLE_SPEED = 30;
    /**
     * 转盘工位传感器英文
     */
    public static final int ROTARY_TABLE_STATION_SENSOR = 1;
    /**
     * 碗下限位传感器
     */
    public static final int BOWL_LOWER_LIMIT_SENSOR = 2;
    /**
     * 碗上限位传感器
     */
    public static final int BOWL_UPPER_LIMIT_SENSOR = 3;
    /**
     * 空碗传感器
     */
    public static final int EMPTY_BOWL_SENSOR = 4;
    /**
     * 牛汤碗传感器
     */
    public static final int COW_SOUP_BOWL_SENSOR = 5;
    /**
     * 出餐口传感器
     */
    public static final int FOOD_OUTLET_SENSOR = 7;
    /**
     * 蒸器发生器温度定值传感器
     */
    public static final int SOUP_AND_STEAM = 8;

    /** 继电器 */
    /**
     * 出餐口向下
     */
    public static final int THE_FOOD_OUTLET_IS_FACING_DOWNWARDS = 1;
    /**
     * 出餐口向上
     */
    public static final int THE_FOOD_OUTLET_IS_FACING_UPWARDS = 2;
    /**
     * 出料开仓出料
     */
    public static final int DISCHARGING_FROM_WAREHOUSE = 3;
    /**
     * 出料关仓禁止出料
     */
    public static final int DISCHARGING_IS_PROHIBITED_AFTER_CLOSING_THE_WAREHOUSE = 4;
    /**
     * 牛肉电机
     */
    public static final int BEEF_MOTOR = 5;
    /**
     * 柜体排气扇
     */
    public static final int CABINET_EXHAUST_FAN = 6;
    /**
     * 蒸汽
     */
    public static final int STEAM = 7;
    /**
     * 出牛肉汤电机
     */
    public static final int BEEF_SOUP_MOTOR = 8;
    /**
     * 门磁左上
     */
    public static final int DOOR_MAGNET_UPPER_LEFT = 9;
    /**
     * 门磁左下
     */
    public static final int DOOR_MAGNET_LOWER_LEFT = 10;
    /**
     * 门磁中上
     */
    public static final int DOOR_MAGNETIC_UPPER_MIDDLE = 11;
    /**
     * 门磁中下
     */
    public static final int DOOR_MAGNETIC_LOWER_MIDDLE = 12;
    /**
     * 震动器
     */
    public static final int SHAKER = 13;
    /**
     * 抽汤泵
     */
    public static final int SOUP_PUMP = 17;
    /**
     * 抽水泵
     */
    public static final int WATER_PUMP = 18;

    /**
     * 碗交流电机N极
     */
    public static final int BOWL_N = 31;
    /**
     * 碗交流电机L极
     */
    public static final int BOWL_L = 32;
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

}
