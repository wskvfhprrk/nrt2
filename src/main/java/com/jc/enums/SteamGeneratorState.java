package com.jc.enums;

/**
 * 蒸汽发生器当前状态：1表示保湿状态、2表示蒸汽状态
 */
public enum SteamGeneratorState {
    HUMIDIFYING(1), // 保湿状态
    STEAMING(2);    // 蒸汽状态

    private final int value;

    // 构造函数
    SteamGeneratorState(int value) {
        this.value = value;
    }

    // 获取状态值
    public int getValue() {
        return value;
    }

    // 通过值获取状态
    public static SteamGeneratorState fromValue(int value) {
        for (SteamGeneratorState state : values()) {
            if (state.getValue() == value) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid state value: " + value);
    }
}
