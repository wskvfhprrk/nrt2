package com.jc.service.impl;

import com.hejz.pay.wx.service.RefundSuccessService;
import org.springframework.stereotype.Service;

@Service
public class RefundSuccessServiceImpl implements RefundSuccessService {
    @Override
    public void success(String outTradeNo) {
        System.out.println("退款成功："+outTradeNo);
    }
}
