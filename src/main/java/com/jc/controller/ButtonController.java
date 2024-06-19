package com.jc.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buttonAction")
public class ButtonController {

    @GetMapping("/{id}")
    public String handleButtonAction(@PathVariable int id) {
        // 处理不同按钮的逻辑
        return "按钮 " + id + " 的操作已处理";
    }
}
