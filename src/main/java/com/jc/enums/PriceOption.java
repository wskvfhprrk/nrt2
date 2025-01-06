package com.jc.enums;

public enum PriceOption {
    SMALL("small", 1),  // small -> meatSlicingMachine(1)
    MID("mid", 2),      // mid -> meatSlicingMachine(2)
    LARGE("large", 3),  // large -> meatSlicingMachine(3)
    ADD_MEAT("addMeat", 4); // addMeat -> meatSlicingMachine(4)

    private final String key; // 前端传递的值（如 small, mid 等）
    private final int machineParameter; // meatSlicingMachine 的参数

    PriceOption(String key, int machineParameter) {
        this.key = key;
        this.machineParameter = machineParameter;
    }

    public String getKey() {
        return key;
    }

    public int getMachineParameter() {
        return machineParameter;
    }

    // 根据 key 获取枚举值
    public static PriceOption fromKey(String key) {
        for (PriceOption option : PriceOption.values()) {
            if (option.getKey().equals(key)) {
                return option;
            }
        }
        throw new IllegalArgumentException("Unknown key: " + key);
    }
}