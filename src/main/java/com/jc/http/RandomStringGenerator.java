package com.jc.http;

import java.security.SecureRandom;

public class RandomStringGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 12;
    
    public static void main(String[] args) {
        String randomString = generateRandomString(LENGTH);
        System.out.println("随机生成的12位字符串: " + randomString);
    }
    public static String generateRandomString() {
        return generateRandomString(LENGTH);
    }
    public static String generateRandomString(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }

        return sb.toString();
    }
}
