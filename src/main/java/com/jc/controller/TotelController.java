package com.jc.controller;

import com.jc.config.NettyClientConfig;
import com.jc.service.RobotService;
import com.jc.service.impl.BowlService;
import com.jc.service.impl.TurntableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TotelController {
    @Autowired
    RobotService robotService;
    @Autowired
    BowlService bowlService;
    @Autowired
    TurntableService turntableService;


    @GetMapping("reset")
    public String reset() throws Exception {
        //机器人重置命令
        robotService.reset();
        //转盘重置
        turntableService.turntableReset();
        log.info("转盘重置");
        //碗重置
        bowlService.bowlReset();
        log.info("碗重置");
        return "ok";
    }

    @GetMapping("takeBowl")
    public String takeBowl() {
        //向机器人发送取碗指令
       robotService.takeBowl();
        return "ok";
    }

    @GetMapping("putBowl")
    public String putBowl(){
        robotService.putBowl();
        return "ok";
    }
}
