package com.jc.controller.control;

import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.entity.Order;
import com.jc.service.RobotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 机器人放置空碗任务
 */
@Service
@Slf4j
public class RobotPlaceEmptyBowl {

    @Autowired
    private RobotService robotService;
    @Autowired
    private PubConfig pubConfig;

    public Result start(Order order) {
        // 需要机器人把碗放置到工位上，传感器检测到即为完成任务，如果没有检测到再次抓碗放置到工位上
        // 执行碗重置的操作逻辑
        Result result = robotService.takeBowl();
        return result;
    }
}