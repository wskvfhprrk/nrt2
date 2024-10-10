package com.jc.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderPayMessage implements Serializable {
    /**
     * 订单id——一般为一个开头字母+四位数字如A0000
     */
    private String orderId;
    /**
     * 支付二维码
     */
    private String qrCodeText;
    /**
     * 支付方式——微信或支付宝支付
     */
    private String payMethod;
    /**
     * 是否支付完成——当qrCodeText有值时为未支付完居，当没有值时为没有支付完成
     */
    private Boolean isPaymentCompleted;
}
