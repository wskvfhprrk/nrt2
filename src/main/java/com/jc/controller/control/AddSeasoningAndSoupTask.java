package com.jc.controller.control;

import com.jc.config.Result;
import com.jc.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

/**
 * 加调料和汤任务
 */
@Service
@Slf4j
public class AddSeasoningAndSoupTask implements Callable<Result> {
    private final Order order;
    public AddSeasoningAndSoupTask(Order order) {
        this.order = order;
    }
    @Override
    public Result call() throws Exception {
        // 模拟任务处理
        Thread.sleep(1000);
        return Result.error(500,"加调料和汤任务");
    }
}