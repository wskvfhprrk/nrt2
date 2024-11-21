package com.jc.constants;

public class Constants {
    /**
     * 重置命令——查询传感器io数据
     */
    public static final String RESET_COMMAND = "48 3A 01 52 00 00 00 00 00 00 00 00 D5 45 44";
    /**
     * 睡眠时间（毫秒）
     */
    public static final long SLEEP_TIME_MS = 200L;
    /**
     * 最大步进电机编号
     */
    public static final int MAX_MOTOR_NO = 5;
    /**
     * 最大速度
     */
    public static final int MAX_SPEED = 500;


    /** 传感器的值 */
    /**
     * 粉丝仓一
     */
    public static final int X_FANS_WAREHOUSE_1 = 1;
    /**
     * 粉丝仓二
     */
    public static final int X_FANS_WAREHOUSE_2 = 2;
    /**
     * 粉丝仓三
     */
    public static final int X_FANS_WAREHOUSE_3 = 3;
    /**
     * 粉丝仓四
     */
    public static final int X_FANS_WAREHOUSE_4 = 4;
    /**
     * 粉丝仓五
     */
    public static final int X_FANS_WAREHOUSE_5 = 5;
    /**
     * 粉丝仓原点——推植复位点
     */
    public static final int X_FAN_COMPARTMENT_ORIGIN = 6;
    /**
     * 粉丝仓右限位
     */
    public static final int X_FAN_COMPARTMENT_RIGHT_LIMIT = 7;
    /**
     * 粉丝仓左限位
     */
    public static final int X_FAN_COMPARTMENT_LEFT_LIMIT = 8;
    /**
     * 有碗信号
     */
    public static final int X_BOWL_PRESENT_SIGNAL = 9;
    /**
     * 空碗信号
     */
    public static final int X_EMPTY_BOWL_SIGNAL = 10;
    /**
     * 放碗信号
     */
    public static final int X_PLACE_BOWL_SIGNAL = 11;

    /**
     * 汤左限位——装菜位
     */
    public static final int X_SOUP_LEFT_LIMIT = 12;
    /**
     * 汤右限位——蒸汽位
     */
    public static final int X_SOUP_RIGHT_LIMIT = 13;
    /**
     * 汤原点——倒菜位置
     */
    public static final int X_SOUP_ORIGIN = 14;
    /**
     * 蒸汽上限位
     */
    public static final int X_STEAM_UPPER_LIMIT = 15;
    /**
     * 蒸汽原点
     */
    public static final int X_STEAM_ORIGIN = 16;
    /**
     * 蒸汽下限位
     */
    public static final int X_STEAM_LOWER_LIMIT = 17;
    /**
     * 汤料传感器
     */
    public static final int X_SOUP_INGREDIENT_SENSOR = 18;


    /** 32位继电器 */
    /**
     * 蒸汽发生器继电器
     */
    public static final int Y_STEAM_SWITCH = 20;
//    /**
//     * 待用
//     */
//    public static final int TO_BE_USED = 15;
    /**
     * 抽汤泵
     */
    public static final int Y_SOUP_PUMP_SWITCH = 21;
    /**
     * 碗蒸汽电磁阀
     */
    public static final int Y_BOWL_STEAM_SOLENOID_VALVE = 22;
    /**
     * 配料蒸汽电磁阀
     */
    public static final int Y_BATCHING_STEAM_SOLENOID_VALVE = 24;
    /**
     * 汤蒸汽开关
     */
    public static final int Y_SOUP_STEAM_SOLENOID_VALVE = 25;
    /**
     * 汤循环开关
     */
    public static final int Y_SOUP_SWITCH = 23;
    /**
     * 震动器
     */
    public static final int Y_SHAKER_SWITCH_1 = 17;
//    /**
//     * 配料电机4
//     */
//    public static final int INGREDIENT_MOTOR4 = 27;
//    /**
//     * 配料电机3
//     */
//    public static final int INGREDIENT_MOTOR3 = 28;
//    /**
//     * 配料电机2
//     */
//    public static final int INGREDIENT_MOTOR2 = 29;
//    /**
//     * 配料电机1
//     */
//    public static final int INGREDIENT_MOTOR1 = 30;
//    /**
//     * 碗交流电机N极
//     */
//    public static final int BOWL_N_SWITCH = 31;
//    /**
//     * 碗交流电机L极
//     */
//    public static final int BOWL_L_SWITCH = 32;


    /** 485设备指令 */
    /**
     * 转台步进电机控制器485地址
     */
    public static final int ROTARY_TABLE_STEPPER_MOTOR = 3;
    /**
     * 调味机485地址
     */
    public static final int SEASONING_MACHINE = 2;

    /**
     * 读取485设备数据
     */
    /**
     * 汤温度传感器
     */
    public static final int SOUP_TEMPERATURE_SENSOR = 1;
    /**
     * 重量传感器一托四
     */
    public static final int WEIGHT_SENSOR_ONE_TO_FOUR = 2;


//    /**
//     * 转台工位总数
//     */
//    public static final int WORKSTATION_NUMBER = 6;
    /**
     * 蒸汽发生器保温状态
     */
    public static final int STEAM_GENERATOR_INSULATION_STATUS = 1;
    /**
     * 蒸汽发生器蒸汽状态
     */
    public static final int STEAM_GENERATOR_STEAM_STATUS = 2;
    /**
     * 机器人到达home发送指令
     */
    public static final String ROBOT_HOME = "686F6D65";
    /**
     * 机器人酸菜完成到达发送指令
     */
    public static final String PEI_CAI = "706569636169";
    /**
     * 读取汤温度指令
     */
    public static final String READ_SOUP_TEMPERATURE_COMMAND = "01 03 00 04 00 01 C5 CB";
    /**
     * 读取所有重量的值
     */
    public static final String READ_WEIGHT_VALUE = "02 03 00 00 00 08 44 3F";
    /**
     * 称重置零
     */
    public static final String TARE_WEIGHT = "03 06 00 26 00 01 A8 23";
    /**
     * 置零校准
     */
    public static final String ZEROING_CALIBRATION = "02 06 00 26 00 01 A9 F2";
    /**
     * 货道通电2秒
     */
    public static final int GOODS_AISLE_POWER_ON2_SECONDS = 2;
    /**
     * 机器人出汤放碗成功
     */
    public static final String SERVING_COMPLETED = "6F70656E";
    /**
     * 机器人执行成功命令
     */
    public static final String ROBOT_EXECUTE_SUCCESS_COMMAND = "run start\n";
    /**
     * 机器人执行失败命令
     */
    public static final String ROBOT_EXECUTE_FAILURE_COMMAND = "run fail\n";
    /**
     * 汤最小温度值
     */
    public static final Double SOUP_MINIMUM_TEMPERATURE_VALUE = 80.0;
    /**
     * 汤最大温度值(保温时的最大)
     */
    public static final Double SOUP_MAXIMUM_TEMPERATURE_VALUE = 100.0;
    /**
     * 正在做订单redis主键
     */
    public static final String ORDER_REDIS_PRIMARY_KEY_IN_PROGRESS = "orderRedisPrimaryKeyInProgress";
    /**
     * 已完成订单redis主键
     */
    public static final String COMPLETED_ORDER_REDIS_PRIMARY_KEY = "completedOrderRedisPrimaryKey";
    /**
     * 待制作订单redis主键
     */
    public static final String PENDING_ORDER_REDIS_PRIMARY_KEY = "pendingOrderRedisPrimaryKey";
    /**
     * sign密钥
     */
    public static final String APP_SECRET_REDIS_KEY = "appSecret";
    /**
     * 已经支付信息
     */
    public static final String PAY_DATA = "payData";
    public static final String PAY_ORDER_ID = "payOrderId";
    public static final String ORDER_ID = "orderId";
    /**
     * 伸缩杆方向控制——通电收缩，不通电伸开
     */
    public static final int Y_TELESCOPIC_ROD_DIRECTION_CONTROL = 1;
    /**
     * 伸缩杆开关控制
     */
    public static final int Y_TELESCOPIC_ROD_SWITCH_CONTROL = 2;
    /**
     * 碗电机
     */
    public static final int Y_CHU_WAN = 15;
    /**
     * 震动器2
     */
    public static final int Y_SHAKER_SWITCH_2 = 18;
    /**
     * 出料3
     */
    public static final int Y_DISCHARGE_BIN_3 = 16;
    /**
     * 一号仓门方向控制
     */
    public static final int Y_FIRST_BIN_DIRECTION_CONTROL = 3;
    /**
     * 一号仓门开关控制
     */
    public static final int Y_HOPPER1_SWITCH_CONTROL = 4;
    public static final int Y_SECOND_BIN_DIRECTION_CONTROL = 5;
    public static final int Y_HOPPER2_SWITCH_CONTROL = 6;
    public static final int Y_THIRD_BIN_DIRECTION_CONTROL = 7;
    public static final int Y_HOPPER3_SWITCH_CONTROL = 8;

}
