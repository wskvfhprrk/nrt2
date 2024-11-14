package com.jc.service;

import com.jc.config.Result;

/**
 * 机器人取碗
 */
public interface RobotService {

    // 重置机器人状态
    Result robotReset();

    // 机器人拿碗
    Result robotTakeBowl();

    // 机器人放碗
    Result robotPlaceBowl();

    // 机器人拿风扇
    Result robotTakeFans();

    // 机器人取餐
    Result robotDeliverMeal();

    // 机器人拿篮子
    Result robotTakeBasket();
}
