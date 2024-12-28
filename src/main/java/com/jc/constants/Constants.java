package com.jc.constants;

public class Constants {

    /** 通用常量 */
    /**
     * 重置命令——查询传感器io数据
     */
    public static final String RESET_COMMAND = "48 3A 01 52 00 00 00 00 00 00 00 00 D5 45 44";
    /**
     * 发送指令间隔轮询时间（毫秒）
     */
    public static final long COMMAND_INTERVAL_POLLING_TIME = 50L;
    /**
     * 最大步进电机编号
     */
    public static final int MAX_MOTOR_NO = 5;
    /**
     * 最大速度
     */
    public static final int MAX_SPEED = 500;

    /** 传感器值常量 */
    /**
     * 粉丝仓
     */
    public static final int X_FANS_WAREHOUSE_1 = 1;
    public static final int X_FANS_WAREHOUSE_2 = 2;
    public static final int X_FANS_WAREHOUSE_3 = 3;
    public static final int X_FANS_WAREHOUSE_4 = 4;
    public static final int X_FANS_WAREHOUSE_5 = 5;
    public static final int X_FAN_COMPARTMENT_ORIGIN = 6;
    public static final int X_FAN_COMPARTMENT_RIGHT_LIMIT = 7;
    public static final int X_FAN_COMPARTMENT_LEFT_LIMIT = 8;
    /**
     * 菜篮复位
     */
    public static final int X_BASKET_RESET = 22;

    /**
     * 碗信号
     */
    public static final int X_BOWL_PRESENT_SIGNAL = 9;
    public static final int X_EMPTY_BOWL_SIGNAL = 10;
    public static final int X_PLACE_BOWL_SIGNAL = 11;

    /**
     * 汤相关
     */
    public static final int X_SOUP_LEFT_LIMIT = 12;
    public static final int X_SOUP_RIGHT_LIMIT = 13;
    public static final int X_SOUP_ORIGIN = 14;

    /**
     * 蒸汽相关
     */
    public static final int X_STEAM_UPPER_LIMIT = 15;
    public static final int X_STEAM_ORIGIN = 16;
    public static final int X_STEAM_LOWER_LIMIT = 17;
    /**
     * 汤料传感器
     */
    public static final int X_SOUP_INGREDIENT_SENSOR = 18;
    /**
     * 切肉机传感器
     */
    public static final int X_MEAT_SLICER_SENSOR = 20;
    /**
     * 水脉冲传感器
     */
    public static int X_FLOWMETER_PULSE_COUNT = 21;
    /**
     * 32位继电器
     */
    /**
     * 蒸汽
     */
    public static final int Y_STEAM_SWITCH = 20;
    /**
     * 抽汤泵
     */
    public static final int Y_SOUP_PUMP_SWITCH = 21;
    /**
     * 抽汤开关
     */
    public static final int Y_BOWL_STEAM_SOLENOID_VALVE = 22;
    /**
     * 抽汤排气开关
     */
    public static final int Y_SOUP_SWITCH = 23;
    /**
     * 加蒸汽电磁阀开关
     */
    public static final int Y_BATCHING_STEAM_SOLENOID_VALVE = 24;
    /**
     * 汤加热蒸汽电磁阀开关
     */
    public static final int Y_SOUP_STEAM_SOLENOID_VALVE = 25;
    /**
     * 震动器1
     */
    public static final int Y_SHAKER_SWITCH_1 = 17;
    public static final int Y_SHAKER_SWITCH_2 = 18;
    /**
     * 切肉机控制
     */
    public static final int Y_MEAT_SLICER_CONTROL = 18;
    /**
     * 出砣电机开关
     */
    public static final int Y_CHU_WAN = 15;
    /**
     * 出料仓3
     */
    public static final int Y_DISCHARGE_BIN_3 = 16;

    /**
     * 蒸汽伸缩杆控制
     */
    public static final int Y_TELESCOPIC_ROD_DIRECTION_CONTROL = 1;
    public static final int Y_TELESCOPIC_ROD_SWITCH_CONTROL = 2;

    /**
     * 出餐口开关
     */
    public static final int Y_PICK_UP_COUNTER_DIRECTION_CONTROL = 3;
    public static final int Y_PICK_UP_COUNTER = 4;
    /**
     * 震动出料器开关
     */
    public static final int Y_VIBRATION_SWITCH_DIRECTION_CONTROL = 5;
    public static final int Y_VIBRATION_SWITCH_CONTROL = 6;
    /**
     * 仓门控制
     */
//    public static final int Y_SECOND_BIN_DIRECTION_CONTROL = 5;
//    public static final int Y_HOPPER2_SWITCH_CONTROL = 6;
//    public static final int Y_THIRD_BIN_DIRECTION_CONTROL = 7;
//    public static final int Y_HOPPER3_SWITCH_CONTROL = 8;

    /**
     * 柜门控制
     */
    /**
     * 左上门
     */
    public static final int Y_LEFT_DOOR = 27;
    /**
     * 左下门
     */
    public static final int Y_OPEN_LOWER_LEFT_DOOR = 28;
    /**
     * 中间左上门
     */
    public static final int Y_MIDDLE_LEFT_UPPER_DOOR = 31;
    /**
     * 中间左下门
     */
    public static final int Y_MIDDLE_LEFT_LOWER_DOOR = 32;
    /**
     * 中间右上门
     */
    public static final int Y_MIDDLE_RIGHT_UPPER_DOOR = 29;
    /**
     * 中间右下门
     */
    public static final int Y_MIDDLE_RIGHT_LOWER_DOOR = 30;

    /** 485设备指令 */
    /**
     * 设备地址
     */
    public static final int ROTARY_TABLE_STEPPER_MOTOR = 3;
    public static final int SEASONING_MACHINE = 2;

    /**
     * 数据读取
     */
    public static final int SOUP_TEMPERATURE_SENSOR = 1;
    public static final int WEIGHT_SENSOR_ONE_TO_FOUR = 2;

    /**
     * 状态常量
     */
    public static final int STEAM_GENERATOR_INSULATION_STATUS = 1;
    public static final int STEAM_GENERATOR_STEAM_STATUS = 2;

    /**
     * 机器人相关指令
     */
    public static final String ROBOT_HOME = "686F6D65";
    public static final String PEI_CAI = "706569636169";
    public static final String SERVING_COMPLETED = "6F70656E";
    /**
     * 拿篮子——naLan
     */
    public static final String NA_LAN = "6E614C616E";
    /**
     * 放牛肉——putBeef
     */
    public static final String PUT_BEEF = "70757442656566";
    /**
     * 放粉丝——fens
     */
    public static final String FENS = "66 65 6E 73";
    /**
     * 机器人取碗——getBowl
     */
    public static final String GET_BOWL = "676574626F776C";
    /**
     * 取碗——checkBowl
     */
    public static final String CHECK_BOWL = "636865636B426F776C";
    /**
     * 放碗——putBowl
     */
    public static final String PUT_BOWL = "707574426F776C";
    /**
     * 出餐——pickUpSoupBowl
     */
    public static final String PICK_UP_SOUP_BOWL = "7069636B5570536F7570426F776C";

//    public static final String ROBOT_EXECUTE_SUCCESS_COMMAND = "run start\n";
//    public static final String ROBOT_EXECUTE_FAILURE_COMMAND = "run fail\n";
//    public static final String ROBOT_ALREADY_ENABLE_COMMAND = "already enable\n";
//    /**
//     * 机器人获取碗指令
//     */
//    public static final String GET_BOWL = "676574626F776C";

    /**
     * 数据命令
     */
    public static final String READ_SOUP_TEMPERATURE_COMMAND = "01 03 00 04 00 01 C5 CB";
    public static final String READ_WEIGHT_VALUE = "02 03 00 00 00 08 44 3F";
    public static final String TARE_WEIGHT = "03 06 00 26 00 01 A8 23";
    public static final String ZEROING_CALIBRATION = "02 06 00 26 00 01 A9 F2";

    /**
     * 温度值
     */
    public static final Double SOUP_MINIMUM_TEMPERATURE_VALUE = 80.0;
    public static final Double SOUP_MAXIMUM_TEMPERATURE_VALUE = 100.0;

    /**
     * Redis主键
     */
    public static final String ORDER_REDIS_PRIMARY_KEY_IN_PROGRESS = "orderRedisPrimaryKeyInProgress";
    public static final String COMPLETED_ORDER_REDIS_PRIMARY_KEY = "completedOrderRedisPrimaryKey";
    public static final String PENDING_ORDER_REDIS_PRIMARY_KEY = "pendingOrderRedisPrimaryKey";
    public static final String APP_SECRET_REDIS_KEY = "appSecret";
    public static final String PAY_DATA = "payData";
    public static final String PAY_ORDER_ID = "payOrderId";
    public static final String ORDER_ID = "orderId";
    /**
     * 其他
     */
    public static final int GOODS_AISLE_POWER_ON2_SECONDS = 2;
    /**
     * 风扇继电器
     */
    public static final int REAR_BOX_FAN = 13;
    /**
     * 称重盒编号
     */
    public static final int WEIGH_BOX_NUMBER = 4;

}
