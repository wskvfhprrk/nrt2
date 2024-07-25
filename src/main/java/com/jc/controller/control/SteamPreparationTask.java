package com.jc.controller.control;

import com.jc.config.Result;
import com.jc.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

/**
 * 蒸汽准备任务
 */
@Slf4j
public class SteamPreparationTask implements Callable<Result> {

    private final Order order;

    public SteamPreparationTask(Order order) {
        this.order = order;
    }
    @Override
    public Result call() throws Exception {
        // 模拟任务处理
        Thread.sleep(1000);
        log.info("蒸汽准备任务");
        return Result.error(500,"蒸汽准备任务");
    }
}