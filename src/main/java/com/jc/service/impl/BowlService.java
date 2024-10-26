package com.jc.service.impl;

import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.enums.SignalLevel;
import com.jc.service.DeviceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 碗控制服务实现类，负责碗的升降操作和状态检查
 */
@Service
@Slf4j
public class BowlService implements DeviceHandler {

    @Autowired
    private Send485OrderService send485OrderService;
    @Autowired
    private IODeviceService ioDeviceService;

    /**
     * 处理消息
     *
     * @param message 消息内容
     * @param isHex   是否为16进制消息
     */
    @Override
    public void handle(String message, boolean isHex) {
        if (isHex) {
            log.info("碗控制服务——HEX: {}", message);
        } else {
            log.info("碗控制服务——普通消息: {}", message);
            // 在这里添加处理普通字符串消息的逻辑
        }
    }

    /**
     * 装菜勺复位
     *
     * @return
     */
    public Result spoonReset() {
        if (ioDeviceService.getStatus(Constants.X_SOUP_RIGHT_LIMIT) == SignalLevel.LOW.ordinal()) {
            return Result.error(500, "没有到达倒菜位置！");
        }
        if (ioDeviceService.getStatus(Constants.X_SOUP_INGREDIENT_SENSOR) == SignalLevel.HIGH.ordinal()) {
            return Result.success();
        }
        //先发送脉冲数，再发送指令
        String hex = "030600070480";
        send485OrderService.sendOrder(hex);
        hex = "030600050080";
        send485OrderService.sendOrder(hex);
        //先发送脉冲数，再发送指令
        hex = "030600000001";
        send485OrderService.sendOrder(hex);
        //如果转动后还没有在位置上就让其慢转到原点
        if (ioDeviceService.getStatus(Constants.X_SOUP_INGREDIENT_SENSOR) == SignalLevel.LOW.ordinal()) {
            //先发送脉冲数，再发送指令
            hex = "030600070000";
            send485OrderService.sendOrder(hex);
            hex = "03060005010";
            send485OrderService.sendOrder(hex);
            //先发送脉冲数，再发送指令
            hex = "030600000001";
            send485OrderService.sendOrder(hex);
        }
        return Result.success();
    }

    /**
     * 装菜勺倒菜
     *
     * @return
     */
    public Result spoonPour() {
        //如果没有到达位置倒菜勺——汤右限位
        if (ioDeviceService.getStatus(Constants.X_SOUP_RIGHT_LIMIT) == SignalLevel.LOW.ordinal()) {
            return Result.error(500, "没有到达倒菜位置！");
        }
        //先发送脉冲数，再发送指令
        String hex = "030600070480";
        send485OrderService.sendOrder(hex);
        //倒菜时速度
        hex = "030600050100";
        send485OrderService.sendOrder(hex);
        //先发送脉冲数，再发送指令
        hex = "030600010001";
        send485OrderService.sendOrder(hex);
        return Result.success();
    }
}
