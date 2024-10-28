package com.jc.utils;

/**
 * 根据寄存器地址和标重重量生成 Modbus 标定重量报文
 */
public class ModbusCalibration {

    /**
     * 根据寄存器地址和标重重量生成 Modbus 标定重量报文
     * @param registerAddressIndex 寄存器索引（1 表示 0x0074，2 表示 0x0076，依此类推）
     * @param weight 标定重量值（例如：500）
     * @return 生成的 Modbus 报文字符串
     */
    public static String generateCalibrationMessage(int registerAddressIndex, int weight) {
        // 设备地址（例如：02）
        byte deviceAddress = 0x02;

        // 功能码（10 表示写多个寄存器）
        byte functionCode = 0x10;

        // 根据 registerAddressIndex 计算实际的寄存器地址
        int baseAddress = 0x0074;
        int calculatedAddress = baseAddress + (registerAddressIndex - 1) * 2;

        // 将计算得到的寄存器地址分成两个字节
        byte registerHigh = (byte) (calculatedAddress >> 8);
        byte registerLow = (byte) (calculatedAddress & 0xFF);

        // 写两个寄存器，因此寄存器数量为 0x0002
        byte registerCountHigh = 0x00;
        byte registerCountLow = 0x02;

        // 数据字节长度为 4（2 字节的重量数据 + 2 字节填充）
        byte dataLength = 0x04;

        // 将重量值转换为大端格式的 2 字节，并在后面补两个字节 `00 00`
        byte[] weightBytes = new byte[4];
        weightBytes[0] = (byte) (weight >> 8);   // 高字节
        weightBytes[1] = (byte) (weight & 0xFF); // 低字节
        weightBytes[2] = 0x00;                   // 填充字节
        weightBytes[3] = 0x00;                   // 填充字节

        // 拼接 CRC 校验前的完整报文
        byte[] messageWithoutCRC = {
                deviceAddress, functionCode, registerHigh, registerLow,
                registerCountHigh, registerCountLow, dataLength,
                weightBytes[0], weightBytes[1], weightBytes[2], weightBytes[3]
        };

        // 计算 CRC 校验码
        byte[] crc = calculateCRC(messageWithoutCRC);

        // 将报文与 CRC 合并
        byte[] finalMessage = new byte[messageWithoutCRC.length + crc.length];
        System.arraycopy(messageWithoutCRC, 0, finalMessage, 0, messageWithoutCRC.length);
        System.arraycopy(crc, 0, finalMessage, messageWithoutCRC.length, crc.length);

        // 将报文转换为十六进制字符串以便查看
        StringBuilder sb = new StringBuilder();
        for (byte b : finalMessage) {
            sb.append(String.format("%02X ", b));
        }

        return sb.toString().trim();
    }

    /**
     * 计算 Modbus CRC 校验码
     * @param data 要计算 CRC 的字节数组
     * @return 两字节的 CRC 校验码
     */
    private static byte[] calculateCRC(byte[] data) {
        int crc = 0xFFFF;
        for (byte b : data) {
            crc ^= b & 0xFF;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x0001) != 0) {
                    crc >>= 1;
                    crc ^= 0xA001;
                } else {
                    crc >>= 1;
                }
            }
        }
        return new byte[] { (byte) (crc & 0xFF), (byte) ((crc >> 8) & 0xFF) };
    }

    public static void main(String[] args) {
        // 示例：寄存器索引 1 和 2，标重 500g
        System.out.println("Calibration Message for register 1: " + generateCalibrationMessage(1, 500));
        System.out.println("Calibration Message for register 2: " + generateCalibrationMessage(2, 500));
        System.out.println("Calibration Message for register 3: " + generateCalibrationMessage(3, 500));
        System.out.println("Calibration Message for register 4: " + generateCalibrationMessage(4, 500));
    }
}
