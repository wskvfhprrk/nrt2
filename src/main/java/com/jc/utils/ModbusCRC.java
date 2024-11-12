package com.jc.utils;

public class ModbusCRC {
    /**
     * 机器人夹手位置计算
     * @param args
     */
    public static void main(String[] args) {
        // 原始十六进制字符串——09 DD 00 FF FF其中DD是位置，第一个FF是力矩，第二个FF是速度
        String hexString = "09 10 03 E8 00 01 02 00 01";
        // 将字符串转换为字节数组
        String[] hexArray = hexString.split(" ");
        byte[] data = new byte[hexArray.length];
        for (int i = 0; i < hexArray.length; i++) {
            data[i] = (byte) Integer.parseInt(hexArray[i], 16);
        }

        // 计算 CRC
        int crc = calculateModbusCRC(data);
        byte crcLow = (byte) (crc & 0xFF);
        byte crcHigh = (byte) ((crc >> 8) & 0xFF);

        // 将 CRC 添加到数据帧
        byte[] fullData = new byte[data.length + 2];
        System.arraycopy(data, 0, fullData, 0, data.length);
        fullData[fullData.length - 2] = crcLow;
        fullData[fullData.length - 1] = crcHigh;

        // 以逗号分隔的十进制数据输出
        System.out.print("十进制数据帧: ");
        for (int i = 0; i < fullData.length; i++) {
            System.out.print((fullData[i] & 0xFF));
            if (i < fullData.length - 1) {
                System.out.print(", ");
            }
        }
    }

    // 计算 Modbus CRC-16 校验
    public static int calculateModbusCRC(byte[] data) {
        int crc = 0xFFFF;
        for (byte b : data) {
            crc ^= (b & 0xFF);
            for (int i = 0; i < 8; i++) {
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
}
