package com.jc.service.impl;

import com.alibaba.fastjson.JSON;
import com.hejz.pay.wx.WxNativePayTemplate;
import com.hejz.pay.wx.service.PaySuccessService;
import com.hejz.pay.wx.service.RefundSuccessService;
import com.jc.constants.Constants;
import com.jc.mqtt.MqttProviderConfig;
import com.jc.sign.SignUtil;
import com.jc.vo.OrderPayMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 退款成功——服务端代码
 */
@Service
@Slf4j
public class PaySucessServiceImpl implements PaySuccessService, RefundSuccessService {
    @Autowired
    private MqttProviderConfig mqttProviderConfig;
    @Autowired
    private WxNativePayTemplate wxNativePayTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${machineCode}")
    private String machineCode;


    @Override
    public void paySuccess(String outTradeNo) {
        // TODO: 2024/10/10 支付完成后就退款
        String refunds = wxNativePayTemplate.refunds(1, 1, outTradeNo);
        log.info("退款信息：{}", refunds);
        //支付完成后发送状态
        //需要使用订单号更换自定义订单号
        Object o = redisTemplate.opsForValue().get(Constants.PAY_ORDER_ID + "::" + outTradeNo);
        if (o == null) {
            log.error("没有发现订单缓存：{}", outTradeNo);
            return;
        }
        redisTemplate.delete(Constants.PAY_ORDER_ID + "::" + outTradeNo);
        OrderPayMessage orderPayMessage = JSON.parseObject(o.toString(), OrderPayMessage.class);
        orderPayMessage.setIsPaymentCompleted(true);
        String s = SignUtil.sendSignStr(JSON.toJSONString(orderPayMessage));
        // TODO: 2024/10/12 订单状态更改为已支付
        mqttProviderConfig.publishSign(0, false, "pay/" + machineCode, s);
    }

    @Override
    public void refundSuccess(String outTradeNo) {
        log.info("退款成功，退款订单号：{}", outTradeNo);
    }
    //获取订单
}
