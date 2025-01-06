package com.jc;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import org.json.JSONObject;

public class FomoPayDemo {

    // 生成RSA密钥对
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    // 保存密钥到文件
    public static void saveKeyToFile(String filename, byte[] key) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(key);
        }
    }

    // 从文件加载私钥
    public static PrivateKey loadPrivateKey(String filename) throws Exception {
        byte[] keyBytes = readFileBytes(filename);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    // 从文件加载公钥
    public static PublicKey loadPublicKey(String filename) throws Exception {
        byte[] keyBytes = readFileBytes(filename);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    // 读取文件字节
    public static byte[] readFileBytes(String filename) throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            return bytes;
        }
    }

    // 对请求进行签名
    public static String signRequest(String payload, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(payload.getBytes(StandardCharsets.UTF_8));
        byte[] signedBytes = signature.sign();
        return bytesToHex(signedBytes).toLowerCase();
    }

    // 验证响应签名
    public static boolean verifyResponse(String payload, String signature, PublicKey publicKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(payload.getBytes(StandardCharsets.UTF_8));
        return sig.verify(hexToBytes(signature));
    }

    // 字节数组转十六进制字符串
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // 十六进制字符串转字节数组
    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    // 发送HTTP POST请求
    public static String sendHttpPostRequest(String url, String payload, Map<String, String> headers) throws IOException {
        java.net.HttpURLConnection connection = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // 设置请求头
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }

        // 发送请求体
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // 读取响应
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    public static void main(String[] args) throws Exception {
        // 1. 生成RSA密钥对
        KeyPair keyPair = generateRSAKeyPair();
        saveKeyToFile("private_key.pem", keyPair.getPrivate().getEncoded());
        saveKeyToFile("public_key.pem", keyPair.getPublic().getEncoded());

        // 2. 加载私钥和公钥
        PrivateKey privateKey = loadPrivateKey("private_key.pem");
        PublicKey publicKey = loadPublicKey("public_key.pem");

        // 3. 构造Sale Request请求
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("0", "0200"); // Message type identifier
        requestBody.put("1", "a238408080c0880000000010001000000"); // Bitmap
        requestBody.put("3", "000000"); // Processing code
        requestBody.put("7", "1231235959"); // Transmission date & time
        requestBody.put("11", "000135"); // System trace audit number (STAN)
        requestBody.put("12", "235959"); // Local transaction time
        requestBody.put("13", "1231"); // Local transaction date
        requestBody.put("18", "0005"); // Merchant type
        requestBody.put("25", "20"); // Point of service condition code
        requestBody.put("41", "22222222"); // Terminal ID
        requestBody.put("42", "111111111111111"); // Merchant ID
        requestBody.put("49", "SGD"); // Currency code
        requestBody.put("88", "000000001000"); // Total amount of debits
        requestBody.put("104", "Payment Description"); // Transaction description

        JSONObject jsonRequest = new JSONObject(requestBody);
        String payload = jsonRequest.toString();

        // 4. 对请求进行签名
        String signature = signRequest(payload, privateKey);

        // 5. 发送HTTP POST请求
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Authentication-Version", "1.1");
        headers.put("X-Authentication-Method", "SHA256WithRSA");
        headers.put("X-Authentication-KeyId", "your_key_id"); // 替换为实际的Key ID
        headers.put("X-Authentication-Nonce", "random_nonce"); // 替换为随机数
        headers.put("X-Authentication-Timestamp", String.valueOf(System.currentTimeMillis() / 1000)); // 时间戳
        headers.put("X-Authentication-Sign", signature);
        headers.put("Content-Type", "application/json");

        String response = sendHttpPostRequest("https://uat.pos.fomopay.net/rpc", payload, headers);
        System.out.println("Response: " + response);

        // 6. 验证响应签名
        JSONObject jsonResponse = new JSONObject(response);
        String responsePayload = jsonResponse.toString();
        String responseSignature = headers.get("X-Authentication-Sign");

        boolean isVerified = verifyResponse(responsePayload, responseSignature, publicKey);
        System.out.println("Response signature verified: " + isVerified);
    }
}