package com.jc.service.impl;


import com.jc.config.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 粉丝仓逻辑
 */
@Service
public class FansService {

    @Autowired
    private Send485OrderService send485OrderService;
    /**
     * 粉丝仓复位
     * @return
     */
    public Result noodleBinReset() {
        //先发送脉冲数，再发送指令
        String hex="030600070080";
        send485OrderService.sendOrder(hex);
        //先发送脉冲数，再发送指令
         hex="030600010001";
        send485OrderService.sendOrder(hex);
        return Result.success();
    }

    public Result noodleBinDeliver() {
        return null;
    }
}
