package com.jc.service.impl;

import com.jc.config.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IngredientsSystemService {

    /**
     * 打开弹簧货道
     *
     * @param number （编号）
     */
    public Result springChannel(Integer number) {
        return Result.success();
    }

    public Result openWeighingBox(Integer number) {
        return Result.success();
    }

    public Result closeWeighingBox(Integer number) {
        return Result.success();
    }

    public Result vegetableMotor(Integer number) {
        return Result.success();
    }

    public Result selectionTestOpen() {
        return Result.success();
    }

    public Result selectionTestClose() {
        return Result.success();
    }

    public Result vegetableMotorInKg(Integer number) {
        return Result.success();
    }
}
