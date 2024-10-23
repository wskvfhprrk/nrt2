package com.jc.enums;

/**
 * 高低电平枚举类
 */
public enum SignalLevel {
    LOW("0"),HIGH("1");

    private final String value;

    SignalLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SignalLevel fromValue(String value) {
        for (SignalLevel level : values()) {
            if (level.value.equals(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
