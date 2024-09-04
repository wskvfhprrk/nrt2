package com.jc.controller;

import com.jc.config.BeefConfig;
import com.jc.config.PubConfig;
import com.jc.constants.Constants;
import com.jc.service.RobotService;
import com.jc.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 按钮测试页面
 */
@RestController
@RequestMapping("/buttonAction")
@Slf4j
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
    private BeefConfig beefConfig;
    @Autowired
    private Reset reset;
    @Autowired
    private PubConfig pubConfig;

    @GetMapping("/{id}")
    public String handleButtonAction(@PathVariable int id, @RequestParam(required = false) Integer number) throws Exception {
        log.info("Button action triggered with id=={}", id);
        String actionResult = "";

        // Handling button actions based on ID
        switch (id) {
            // 机器人和取餐操作
            case 1:  // 机器人重置
                actionResult = "机器人重置";
                robotService.reset();
                break;
            case 2:  // 机器人取碗
                actionResult = "机器人取碗";
                robotService.takeBowl();
                break;
            case 3:  // 机器人出汤
                actionResult = "机器人出汤";
                robotService.putBowl();
                break;
            case 4:  // 取餐口复位
                actionResult = "取餐口复位";
                relayDeviceService.theFoodOutletIsFacingUpwards();
                break;
            case 5:  // 取餐口出餐
                actionResult = "取餐口出餐";
                relayDeviceService.theFoodOutletIsFacingDownwards();
                break;

            // 转台和碗操作
            case 6:  // 转台复位
                actionResult = "转台复位";
                turntableService.turntableReset();
                break;
            case 7:  // 转台工位（数字）
                if (number != null) {
                    turntableService.alignToPosition(number);
                    actionResult = "转台工位（数字）";
                } else {
                    actionResult = "缺少必要参数";
                }
                break;
            case 8:  // 碗复位
                actionResult = "碗复位";
                bowlService.bowlReset();
                break;
            case 9:  // 碗向上
                actionResult = "碗向上";
                bowlService.bowlRising();
                break;
            case 10: // 碗向下
                actionResult = "碗向下";
                bowlService.bowlDown();
                break;

            // 蒸汽和温度控制
            case 11: // 蒸汽打开
                actionResult = "蒸汽打开";
                relayDeviceService.steamOpen();
                break;
            case 12: // 蒸汽关闭
                actionResult = "蒸汽关闭";
                relayDeviceService.steamClose();
                break;
            case 13: // 关闭汤蒸汽阀
                actionResult = "关闭汤蒸汽阀";
                relayDeviceService.soupSwitchOff();
                break;
            case 14: // 抽汤（秒）
                if (number == null) {
                    number = beefConfig.getSoupExtractionTime();
                }
                relayDeviceService.soupPump(number);
                actionResult = "抽汤";
                break;
            case 15: // 抽汤排气（秒）
                if (number == null) {
                    number = beefConfig.getSoupExhaustTime();
                }
                relayDeviceService.soupExhaust(number);
                actionResult = "抽汤排气";
                break;
            case 16: // 汤加热温度（度）
                if (number == null) {
                    double temperature = beefConfig.getSoupHeatingTemperature();
                    number = (int) temperature;
                }
                relayDeviceService.soupHeating(Double.valueOf(number));
                actionResult = "汤加热温度";
                break;
            case 17: // 碗加蒸汽（秒）
                if (number == null) {
                    number = beefConfig.getBowlSteamTime();
                }
                relayDeviceService.bowlSteam(number);
                actionResult = "碗加蒸汽";
                break;

            // 风扇和震动测试
            case 18: // 后箱风扇开
                actionResult = "后箱风扇开";
                relayDeviceService.rearFanOpen();
                break;
            case 19: // 后箱风扇关
                actionResult = "后箱风扇关";
                relayDeviceService.rearFanClose();
                break;
            case 20: // 震动器测试（秒）
                if (number == null) {
                    number = beefConfig.getVibratorTime();
                }
                relayDeviceService.vibratorTest(number);
                actionResult = "震动器测试";
                break;

            // 称重与配料操作
            case 21: // 一号配菜电机（g）
                if (number == null) {
                    number = beefConfig.getBeef10();
                }
                relayDeviceService.vegetableMotorInKg(1, number);
                actionResult = "一号配菜电机";
                break;
            case 22: // 二号配菜电机（g）
                if (number == null) {
                    number = beefConfig.getCilantro();
                }
                relayDeviceService.vegetableMotorInKg(2, number);
                actionResult = "二号配菜电机（g）";
                break;
            case 23: // 三号配菜电机（g）
                if (number == null) {
                    number = beefConfig.getChoppedGreenOnion();
                }
                relayDeviceService.vegetableMotorInKg(3, number);
                actionResult = "三号配菜电机（g）";
                break;

            // 配料分发操作
            case 24: // 调料机（配方）
                if (number != null) {
                    seasoningMachineService.dischargeAccordingToFormula(number);
                    actionResult = "调料机测试（配方）完成";
                } else {
                    actionResult = "缺少必要参数";
                }
                break;
            case 25: // 弹簧货道（编号）
                if (number != null) {
                    relayDeviceService.springChannel(number);
                    actionResult = "弹簧货道（编号）";
                } else {
                    actionResult = "缺少必要参数";
                }
                break;
            case 26: // 配菜称重盒打开（编号）
                if (number != null) {
                    relayDeviceService.openWeighingBox(number);
                    actionResult = "配菜称重盒打开（编号）";
                } else {
                    actionResult = "缺少必要参数";
                }
                break;
            case 27: // 配菜称重盒关闭（编号）
                if (number != null) {
                    relayDeviceService.closeWeighingBox(number);
                    actionResult = "配菜称重盒关闭（编号）";
                } else {
                    actionResult = "缺少必要参数";
                }
                break;

            default: // 未知操作
                actionResult = "未知操作";
        }
        return actionResult;
    }


    @GetMapping("/emergencyStop")
    public String emergencyStop() {
        log.info("Emergency stop triggered");
        relayDeviceService.closeAll();
        stepperMotorService.stop(Constants.ROTARY_TABLE_STEPPER_MOTOR);
        pubConfig.setIsExecuteTask(false);
        return "急停操作完成";
    }

    @GetMapping("/reset")
    public String reset() throws Exception {
        log.info("Resetting machine");
        reset.start();
        return "机器复位成功！";
    }
}
