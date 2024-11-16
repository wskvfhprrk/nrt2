package com.jc.utils;

/**
 * 将十进制数字转换为十六进制，并在不足4位时在前面补零
 */
public class DecimalToHexConverter {
    public static String decimalToHex(int decimal) {
        // 将十进制数字转换为十六进制
        String hex = Integer.toHexString(decimal).toUpperCase();
        // 使用String.format补全到4位，不足补0
        return String.format("%04X", Integer.parseInt(hex, 16));
    }

    public static void main(String[] args) {
        int decimalNumber = 1000;
        String hexResult = decimalToHex(decimalNumber);
        System.out.println("十进制: " + decimalNumber + " 转为十六进制: " + hexResult);
    }
}
