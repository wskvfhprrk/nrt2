package com.jc.controller;

import com.jc.config.Result;
import com.jc.service.impl.Relay1DeviceGatewayService;
import com.jc.service.impl.Relay2DeviceGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private Relay1DeviceGatewayService relay1DeviceGatewayService;
    @Autowired
    private Relay2DeviceGatewayService relay2DeviceGatewayService;


    @GetMapping("openPickUpCounter")
    public Result openPickUpCounter(){
       return relay1DeviceGatewayService.openPickUpCounter();
    }
    @GetMapping("closePickUpCounter")
    public Result closePickUpCounter(){
       return relay1DeviceGatewayService.closePickUpCounter();
    }

    @GetMapping("closeWeighBox")
    public Result closeWeighBox(int i){
        Result result = relay2DeviceGatewayService.closeWeighBox(i);
        return result;
    }
    @GetMapping("openWeighBox")
    public Result openWeighBox(int i){
        Result result = relay2DeviceGatewayService.openWeighBox(i);
        return result;
    }
    @GetMapping("vibrationSwitchOn")
    public Result vibrationSwitchOn(){
        Result result = relay1DeviceGatewayService.vibrationSwitchOn() ;
        return result;
    }
    @GetMapping("vibrationSwitchOff")
    public Result vibrationSwitchOff(){
        Result result = relay1DeviceGatewayService.vibrationSwitchOff() ;
        return result;
    }
}
