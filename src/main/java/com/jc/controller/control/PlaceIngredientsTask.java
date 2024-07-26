package com.jc.controller.control;

import com.jc.config.Result;
import com.jc.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

/**
 * 放置配料任务
 */
@Slf4j
public class PlaceIngredientsTask implements Callable<Result> {

    private final Order order;

    public PlaceIngredientsTask(Order order) {
        this.order = order;
    }
    @Override
    public Result call() throws Exception {
        // 模拟任务处理
        Thread.sleep(1000);
        log.info("放置配料任务");
        return Result.success();
    }
}