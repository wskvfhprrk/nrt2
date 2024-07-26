package com.jc.controller.control;

import com.jc.config.Result;
import com.jc.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

/**
 * 取牛肉汤
 */
@Slf4j
public class RetrieveBeefSoup implements Callable<Result> {

    private final Order order;
    public RetrieveBeefSoup(Order order) {
        this.order = order;
    }
    @Override
    public Result call() throws Exception {
        // 模拟任务处理
        Thread.sleep(1000);
        log.info("取牛肉汤");
        return Result.success();
    }
}