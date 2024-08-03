package com.jc.controller.control;

import com.jc.config.Result;
import com.jc.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 配料准备任务
 */
@Service
@Slf4j
public class IngredientPreparation{

    public Result start(Order order) {
        log.info("配料准备任务");
        return Result.success();
    }
}