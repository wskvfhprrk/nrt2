package com.jc.controller.control;

import com.jc.config.Result;
import com.jc.entity.Order;

import java.util.concurrent.Callable;

public class Task2 implements Callable<Result> {

    private final Order order;

    public Task2(Order order) {
        this.order = order;
    }
    @Override
    public Result call() throws Exception {
        // 模拟任务处理
        Thread.sleep(1000);
        return Result.error(500,order.getOrderId());
    }
}