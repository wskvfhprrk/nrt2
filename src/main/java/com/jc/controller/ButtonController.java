package com.jc.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buttonAction")
public class ButtonController {

    @GetMapping("/{id}")
    public String handleButtonAction(@PathVariable int id) {
        String actionResult = "";
        switch (id) {
            case 1:
                actionResult = "系统重置操作完成";
                // 执行系统重置的操作逻辑
                break;
            case 2:
                actionResult = "碗重置操作完成";
                // 执行碗重置的操作逻辑
                break;
            case 3:
                actionResult = "转盘重置操作完成";
                // 执行转盘重置的操作逻辑
                break;
            case 4:
                actionResult = "粉丝重置操作完成";
                // 执行粉丝重置的操作逻辑
                break;
            case 5:
                actionResult = "自动升碗操作完成";
                // 执行自动升碗的操作逻辑
                break;
            case 6:
                actionResult = "机器人取碗操作完成";
                // 执行机器人取碗的操作逻辑
                break;
            case 7:
                actionResult = "机器人取粉丝操作完成";
                // 执行机器人取粉丝的操作逻辑
                break;
            case 8:
                actionResult = "机器人出汤操作完成";
                // 执行机器人出汤的操作逻辑
                break;
            case 9:
                actionResult = "出汤测试操作完成";
                // 执行出汤测试的操作逻辑
                break;
            case 10:
                actionResult = "碗推杆复位操作完成";
                // 执行碗推杆复位的操作逻辑
                break;
            case 11:
                actionResult = "切刀测试操作完成";
                // 执行切刀测试的操作逻辑
                break;
            case 12:
                actionResult = "调料测试操作完成";
                // 执行调料测试的操作逻辑
                break;
            case 13:
                actionResult = "称重测试操作完成";
                // 执行称重测试的操作逻辑
                break;
            case 14:
                actionResult = "蒸汽测试操作完成";
                // 执行蒸汽测试的操作逻辑
                break;
            case 15:
                actionResult = "出料测试操作完成";
                // 执行出料测试的操作逻辑
                break;
            default:
                actionResult = "未知操作";
        }
        return actionResult;
    }

    @GetMapping("/emergencyStop")
    public String emergencyStop() {
        // 实现急停逻辑，例如停止所有关键操作
        // 替换为实际的急停逻辑

        // 模拟急停动作
        return "急停操作完成";
    }
}
