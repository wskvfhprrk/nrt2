package com.jc.http;

public class TimeBasedNumberGenerator {

    public static void main(String[] args) {
        String timeBasedNumber = generateTimeBasedNumber();
        System.out.println("基于当前时间的10位数字: " + timeBasedNumber);
    }

    public static String generateTimeBasedNumber() {
        long currentTimeMillis = System.currentTimeMillis();
        
        // 转换为秒级别的时间戳（去掉最后三位毫秒部分）
        long timeInSeconds = currentTimeMillis / 1000;

        // 如果长度不足10位，在末尾添加随机数字
        String timeBasedString = String.valueOf(timeInSeconds);
        while (timeBasedString.length() < 10) {
            timeBasedString += (int)(Math.random() * 10);
        }

        // 如果长度超过10位，截取前10位
        if (timeBasedString.length() > 10) {
            timeBasedString = timeBasedString.substring(0, 10);
        }

        return timeBasedString;
    }
}
