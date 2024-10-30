package com.jc.utils;

/**
 * 解析重量数据的方法
 */
public class WeightParserUtils {
    public static void main(String[] args) {
        // 示例数据字符串
        String dataString = "01f40000";
//        String dataString = "01f50000";

        // 解析重量
        int weight = parseWeight(dataString);

        // 输出结果
        System.out.println("解析的重量值为: " + weight + "g");
    }

    /**
     * 解析重量数据的方法——大端
     * @param dataString 包含重量数据的16进制字符串
     * @return 返回解析出的十进制重量值
     */
    public static int parseWeight(String dataString) {
        if (dataString.length() < 4) {
            throw new IllegalArgumentException("数据长度不足，无法解析重量");
        }

        // 解析前两个字节，将其视为高字节和低字节
        int highByte = Integer.parseInt(dataString.substring(0, 2), 16);
        int lowByte = Integer.parseInt(dataString.substring(2, 4), 16);

        // 按大端格式将高低字节组合成一个16位整数
        int weight = (highByte << 8) | lowByte;

        return weight;
    }
}
