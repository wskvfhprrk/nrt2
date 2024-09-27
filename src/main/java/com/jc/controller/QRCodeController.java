package com.jc.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成程序
 */
@RestController
public class QRCodeController {

    @GetMapping("/qrcode")
    public ResponseEntity<byte[]> generateQRCode(@RequestParam String text, @RequestParam String paymentMethod) {
        try {
            int width = 300;
            int height = 300;
            String fileType = "png";

            // 创建编码选项
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // 创建 QR 码生成器
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hintMap);

            // 设置颜色，绿色背景为微信支付，蓝色背景为支付宝支付
            int foregroundColor = 0xFF000000;
            if ("alipay".equalsIgnoreCase(paymentMethod)) {
                foregroundColor = 0xFFFFFFFF;
            }
            int backgroundColor = 0xFFFFFFFF; // 默认白色背景;
            if ("wechat".equalsIgnoreCase(paymentMethod)) {
                backgroundColor = 0xFF07C160; // 绿色背景
            } else if ("alipay".equalsIgnoreCase(paymentMethod)) {
                backgroundColor = 0xFF10AEFF; // 蓝色背景
            }

            // 使用 MatrixToImageConfig 来自定义颜色
            MatrixToImageConfig config = new MatrixToImageConfig(foregroundColor, backgroundColor);

            // 将 BitMatrix 转换为字节数组
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, fileType, outputStream, config);
            byte[] qrCodeImage = outputStream.toByteArray();

            // 返回二维码图像
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"qrcode.png\"")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrCodeImage);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
