package com.jc.entity;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ModbusRTUFrame {
    private byte slaveAddress;
    private byte functionCode;
    private byte[] data;
    private int crc;

    public ModbusRTUFrame(byte slaveAddress, byte functionCode, byte[] data, int crc) {
        this.slaveAddress = slaveAddress;
        this.functionCode = functionCode;
        this.data = data;
        this.crc = crc;
    }

    public String getSlaveAddress() {
        return String.format("%02X", slaveAddress);
    }

    public String getFunctionCode() {
        return String.format("%02X", functionCode);
    }

    public String getData() {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public String getCrc() {
        return String.format("%04X", crc);
    }

    // 解析十六进制字符串帧
    public static ModbusRTUFrame parseFrame(String hexFrame) throws IllegalArgumentException {
        byte[] frame = hexStringToByteArray(hexFrame);
        
        if (frame.length < 4) {
            throw new IllegalArgumentException("Invalid frame length.");
        }

        ByteBuffer buffer = ByteBuffer.wrap(frame);
        byte slaveAddress = buffer.get();
        byte functionCode = buffer.get();
        byte[] data = new byte[frame.length - 4];
        buffer.get(data);
        int crc = buffer.getShort() & 0xFFFF;

        if (calculateCRC(frame, frame.length - 2) != crc) {
            throw new IllegalArgumentException("CRC check failed.");
        }

        return new ModbusRTUFrame(slaveAddress, functionCode, data, crc);
    }

    // 将十六进制字符串转换为字节数组
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    // 计算CRC校验
    private static int calculateCRC(byte[] data, int length) {
        int crc = 0xFFFF;
        for (int pos = 0; pos < length; pos++) {
            crc ^= (data[pos] & 0xFF);
            for (int i = 8; i != 0; i--) {
                if ((crc & 0x0001) != 0) {
                    crc >>= 1;
                    crc ^= 0xA001;
                } else {
                    crc >>= 1;
                }
            }
        }
        return crc;
    }

    @Override
    public String toString() {
        return "ModbusRTUFrame{" +
                "slaveAddress=" + getSlaveAddress() +
                ", functionCode=" + getFunctionCode() +
                ", data=" + getData() +
                ", crc=" + getCrc() +
                '}';
    }
}
