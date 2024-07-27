package com.jc.controller.control;

import com.jc.config.Result;
import com.jc.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 放置配料任务
 */
@Service
@Slf4j
public class PlaceIngredients {

    public Result start(Order order) {
        // 模拟任务处理
        log.info("放置配料任务");
        return Result.success();
    }
}