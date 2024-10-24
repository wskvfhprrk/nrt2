package com.jc.service;

import com.jc.config.Result;

/**
 * 机器人取碗
 */
public interface RobotService {
    Result robotReset();
    Result robotTakeBowl();
    Result putBowl();

    Result getFans();

    Result robotTakeFans();

    Result robotDeliverMeal();
}
