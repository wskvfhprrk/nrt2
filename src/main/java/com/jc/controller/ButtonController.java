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

    @GetMapping("/{id}")
    public String handleButtonAction(@PathVariable int id, @RequestParam(required = false) Integer number) throws Exception {
        log.info("id=={}",id);
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
                if (number == null) {
                    number = beefConfig.getSoupExtractionTime();
                }
                actionResult = "抽汤泵";
                relayDeviceService.soupPump(number);

                break;
            case 13:
                actionResult = "汤开关开";
                relayDeviceService.soupSwitchOn();
                break;
            case 14:
                actionResult = "汤开关关";
                relayDeviceService.soupSwitchOff();
                break;
            case 15:
                actionResult = "后箱风扇开";
                relayDeviceService.rearFanOpen();
                break;
            case 16:
                actionResult = "后箱风扇关";
                relayDeviceService.rearFanClose();
                break;
            case 17:
                if (number == null) {
                    number = beefConfig.getVibratorTime();
                }
                actionResult = "震动器测试";
                relayDeviceService.vibratorTest(number);
                break;
            case 18:
                actionResult = "蒸汽打开";
                relayDeviceService.steamOpen();
                break;
            case 19:
                actionResult = "蒸汽关闭";
                relayDeviceService.steamClose();
                break;
            case 20:
                actionResult = "碗加蒸汽蒸汽（秒）";
                if (number == null) {
                    number = beefConfig.getBowlSteamTime();
                }
                relayDeviceService.bowlSteam(number);
                break;
            case 21:
                if (number == null) {
                    number = Integer.parseInt(beefConfig.getSoupHeatingTemperature().toString());
                }
                actionResult = "汤加热温度";
                relayDeviceService.soupHeating(Double.valueOf(number));
                break;
            case 22:
                if (number != null) {
                    actionResult = "弹簧货道（编号）";
                    relayDeviceService.springChannel(number);
                } else {
                    actionResult = "缺少必要参数";
                }
                break;
            case 23:
                actionResult = "配菜称重盒打开（编号）";
                relayDeviceService.openWeighingBox(number);
                break;
            case 24:
                actionResult = "配菜称重盒关闭（编号）";
                relayDeviceService.closeWeighingBox(number);
                break;
            case 25:
                if (number == null) {
                    number = beefConfig.getBeef10();
                }
                actionResult = "1号配菜电机";
                relayDeviceService.vegetableMotorInKg(1, number);
                break;
            case 26:
                if (number == null) {
                    number = beefConfig.getCilantro();
                }
                actionResult = "2号配菜电机（g）";
                relayDeviceService.vegetableMotorInKg(2, number);
                break;
            case 27:
                if (number == null) {
                    number=beefConfig.getChoppedGreenOnion();
                }
                actionResult = "3号配菜电机（g）";
                relayDeviceService.vegetableMotorInKg(3, number);
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
        reset.start();
        return "机器复位成功！";
    }
}
