package com.jc.controller.control;

import com.jc.config.Result;
import com.jc.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 蒸汽准备任务
 */
@Service
@Slf4j
public class SteamPreparation {

    public Result start(Order order) {
        log.info("蒸汽准备任务");
        return Result.success();
    }
}