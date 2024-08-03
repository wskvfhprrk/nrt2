package com.jc.entity;

public class ModbusRTUExample {
    public static void main(String[] args) {
        String hexFrame = "010302000AF884"; // Example hex frame

        try {
            ModbusRTUFrame modbusFrame = ModbusRTUFrame.parseFrame(hexFrame);
            System.out.println(modbusFrame);
        } catch (IllegalArgumentException e) {
            System.err.println("Failed to parse frame: " + e.getMessage());
        }
    }
}
