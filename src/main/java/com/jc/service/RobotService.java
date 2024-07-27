package com.jc.service;

import com.jc.config.Result;

/**
 * 机器人取碗
 */
public interface RobotService {
    Result reset();
    Result takeBowl();
    Result putBowl();
}
