package com.jc.controller;

import com.jc.config.PubConfig;
import com.jc.constants.Constants;
import com.jc.service.RobotService;
import com.jc.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 按钮测试页面
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
    @Autowired
    private Send485OrderService send485OrderService;
    @Autowired
    private PubConfig pubConfig;

    @GetMapping("/{id}")
    public String handleButtonAction(@PathVariable int id, @RequestParam(required = false) Integer number) {
        String actionResult = "";
        switch (id) {
            case 1:
                actionResult = "机器人重置";
                robotService.reset();
                break;
            case 2:
                actionResult = "机器人取碗";
                robotService.takeBowl();
                break;
            case 3:
                actionResult = "机器人出汤";
                robotService.putBowl();
                break;
            case 4:
                actionResult = "取餐口复位";
                relayDeviceService.theFoodOutletIsFacingUpwards();
                break;
            case 5:
                actionResult = "取餐口出餐";
                relayDeviceService.theFoodOutletIsFacingDownwards();
                break;
            case 6:
                if (number != null) {
                    seasoningMachineService.dischargeAccordingToFormula(number);
                    actionResult = "调料机测试（配方）完成";
                } else {
                    actionResult = "缺少必要参数";
                }
                break;
            case 7:
                actionResult = "转台复位";
                turntableService.turntableReset();
                break;
            case 8:
                if (number != null) {
                    actionResult = "转台工位（数字）";
                    turntableService.alignToPosition(number);
                } else {
                    actionResult = "缺少必要参数";
                }
                break;
            case 9:
                actionResult = "碗复位";
                bowlService.bowlReset();
                break;
            case 10:
                actionResult = "碗向上";
                bowlService.bowlRising();
                break;
            case 11:
                actionResult = "碗向下";
                bowlService.bowlDown();
                break;
            case 12:
                if (number != null) {
                    actionResult = "抽汤泵（秒）";
                    relayDeviceService.soupPump(number);
                } else {
                    actionResult = "缺少必要参数";
                }
                break;
            case 13:
                actionResult = "后箱风扇开";
                relayDeviceService.rearFanOpen();
                break;
            case 14:
                actionResult = "后箱风扇关";
                relayDeviceService.rearFanClose();
                break;
            case 15:
                if (number != null) {
                    actionResult = "震动器测试（秒）";
                    relayDeviceService.vibratorTest(number);
                } else {
                    actionResult = "缺少必要参数";
                }
                break;
            case 16:
                actionResult = "蒸汽打开";
                relayDeviceService.steamOpen();
                break;
            case 17:
                actionResult = "蒸汽关闭";
                relayDeviceService.steamClose();
                break;
            case 18:
                if (number != null) {
                    actionResult = "汤加热至（度）";
                    relayDeviceService.heatSoupToTemperature(number);
                } else {
                    actionResult = "缺少必要参数";
                }
                break;
            case 19:
                if (number != null) {
                    actionResult = "弹簧货道（编号）";
                    relayDeviceService.springChannel(number);
                } else {
                    actionResult = "缺少必要参数";
                }
                break;
            case 20:
                actionResult = "称重测试打开";
                seasoningMachineService.selectionTestOpen();
                break;
            case 21:
                actionResult = "称重测试关闭";
                seasoningMachineService.selectionTestClose();
                break;
            case 22:
                if (number != null) {
                    actionResult = "配菜称重盒打开（编号）";
                    relayDeviceService.openWeighingBox(number);
                } else {
                    actionResult = "缺少必要参数";
                }
                break;
            case 23:
                if (number != null) {
                    actionResult = "配菜称重盒关闭（编号）";
                    relayDeviceService.closeWeighingBox(number);
                } else {
                    actionResult = "缺少必要参数";
                }
                break;
            case 24:
                if (number != null) {
                    actionResult = "1号配菜电机（g）";
                    relayDeviceService.vegetableMotorInKg(1, number);
                } else {
                    actionResult = "缺少必要参数";
                }
                break;
            case 25:
                if (number != null) {
                    actionResult = "2号配菜电机（g）";
                    relayDeviceService.vegetableMotorInKg(2, number);
                } else {
                    actionResult = "缺少必要参数";
                }
                break;
            case 26:
                if (number != null) {
                    actionResult = "3号配菜电机（g）";
                    relayDeviceService.vegetableMotorInKg(3, number);
                } else {
                    actionResult = "缺少必要参数";
                }
                break;
            default:
                actionResult = "未知操作";
        }
        return actionResult;
    }

    @GetMapping("/emergencyStop")
    public String emergencyStop() {
        relayDeviceService.closeAll();
        stepperMotorService.stop(Constants.ROTARY_TABLE_STEPPER_MOTOR);
        return "急停操作完成";
    }

    @GetMapping("/reset")
    public String reset() throws Exception {
        robotService.reset();
        turntableService.turntableReset();
        bowlService.bowlReset();
        return "机器复位成功！";
    }
}
