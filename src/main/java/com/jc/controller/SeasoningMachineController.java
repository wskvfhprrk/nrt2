package com.jc.controller;

import com.jc.service.impl.SeasoningMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeasoningMachineController {

    @Autowired
    private SeasoningMachineService seasoningMachineService;

    @GetMapping("formula")
        private void dischargeAccordingToFormula(int i){
            seasoningMachineService.dischargeAccordingToFormula(i);
    }
}
