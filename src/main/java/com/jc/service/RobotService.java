package com.jc.service;

import com.jc.config.Result;

/**
 * 机器人取碗
 */
public interface RobotService {
    void reset();
    Result takeBowl();
    void putBowl();
    void getFans();
}
