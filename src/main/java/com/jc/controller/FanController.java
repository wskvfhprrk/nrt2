package com.jc.controller;

import com.jc.config.Result;
import com.jc.service.impl.FansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FanController {
    @Autowired
    private FansService fansService;

    @GetMapping("noodleBinDeliver")
    public Result noodleBinDeliver(){
        return fansService.noodleBinDeliver();
    }

    @GetMapping("pushRodOpen")
    public Result pushRodOpen(){
        return fansService.pushRodOpen();
    }
    @GetMapping("all")
    public Result all() throws InterruptedException {
        for (int i = 0; i < 11; i++) {
            i++;
            Result result = fansService.noodleBinDeliver();
            if(result.getCode()!=200){
                return Result.error(500,"error");
            }
            result=fansService.pushRodOpen();
            if(result.getCode()!=200){
                return Result.error(500,"error");
            }
        }
        return Result.success();
    }

}
