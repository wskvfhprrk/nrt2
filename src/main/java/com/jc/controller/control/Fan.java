package com.jc.controller.control;

import com.jc.config.Result;
import com.jc.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 粉丝任务
 */
@Service
@Slf4j
public class Fan {

    public Result start(Order order) {
        // 模拟任务处理
        log.info("粉丝任务");
        return Result.success();
    }
}