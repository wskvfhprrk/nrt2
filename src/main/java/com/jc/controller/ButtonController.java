package com.jc.controller;

import com.jc.constants.Constants;
import com.jc.service.RobotService;
import com.jc.service.impl.RelayDeviceService;
import com.jc.service.impl.SeasoningMachineService;
import com.jc.service.impl.StepperMotorService;
import com.jc.service.impl.TurntableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 按纽测试页面
 */
@RestController
@RequestMapping("/buttonAction")
public class ButtonController {

    @Autowired
    private RobotService robotService;
    @Autowired
    private RelayDeviceService relayDeviceService;
    @Autowired
    private TurntableService turntableService;
    @Autowired
    private BowlController bowlService;
    @Autowired
    private StepperMotorService stepperMotorService;
    @Autowired
    private SeasoningMachineService seasoningMachineService;

    @GetMapping("/{id}")
    public String handleButtonAction(@PathVariable int id) {
        String actionResult = "";
        switch (id) {
            case 1:
                actionResult = "机器人重置";
                // 执行系统重置的操作逻辑
                robotService.reset();
                break;
            case 2:
                actionResult = "机器人取碗";
                // 执行碗重置的操作逻辑
                robotService.takeBowl();
                break;
            case 3:
                actionResult = "机器人出汤";
                // 执行转盘重置的操作逻辑
                robotService.putBowl();
                break;
            case 4:
                actionResult = "机器人取粉丝";
                // 执行粉丝重置的操作逻辑
                break;
            case 5:
                actionResult = "出餐口复位完成";
                // 执行自动升碗的操作逻辑
                relayDeviceService.coverClosed();
                break;
            case 6:
                actionResult = "出餐口出餐完成";
                // 执行机器人取碗的操作逻辑
                relayDeviceService.coverOpen();
                break;
            case 7:
                seasoningMachineService.dischargeAccordingToFormula(500);
                actionResult = "调料机出料操作完成";
                // 执行机器人取粉丝的操作逻辑
                break;
            case 8:
                actionResult = "待用操作完成";

                break;
            case 9:
                actionResult = "转台复位操作完成";
                // 执行出汤测试的操作逻辑
                turntableService.turntableReset();
                break;
            case 10:
                actionResult = "碗复位操作完成";
                // 执行碗复位的操作逻辑
                bowlService.bowlReset();
                break;
            case 11:
                actionResult = "蒸汽测试完成";
                    // 蒸汽测试的操作逻辑
                relayDeviceService.steam();
                break;
            case 12:
                actionResult = "'转台移动下一工位操作完成";
                // '转台移动下一工位的操作逻辑
                turntableService.moveToNext();
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
        // 实现急停逻辑，例如停止所有关键操作——即关闭所以继电器
        relayDeviceService.closeAll();
        //所有步进电机停止
        stepperMotorService.stop(Constants.BOWL_STEPPER_MOTOR);
        stepperMotorService.stop(Constants.ROTARY_TABLE_STEPPER_MOTOR);
        stepperMotorService.stop(Constants.FAN_STEPPER_MOTOR);

        return "急停操作完成";
    }
    @GetMapping("/reset")
    public String reset() throws Exception {
        //机器人重置命令
        robotService.reset();
        //转盘重置
        turntableService.turntableReset();
        //碗重置
        bowlService.bowlReset();
        //盖板打开
        relayDeviceService.coverOpen();
        return "机器复位成功！";
    }
}
