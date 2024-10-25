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
 * 手动按钮测试页面
 */
@RestController
@RequestMapping("/buttonAction")
@Slf4j
public class ManualOperationController {

    @Autowired
    private RobotService robotService;
    @Autowired
    private RelayDeviceService relayDeviceService;
    @Autowired
    private TurntableService turntableService;
    @Autowired
    private BowlService bowlService;
    @Autowired
    private StepperMotorService stepperMotorService;
    @Autowired
    private SeasoningMachineService seasoningMachineService;
    @Autowired
    private BeefConfig beefConfig;
    @Autowired
    private FansService fansService;
    @Autowired
    private Reset reset;
    @Autowired
    private PubConfig pubConfig;

    @GetMapping("/{id}")
    public String handleButtonAction(@PathVariable int id, @RequestParam(required = false) Integer number) throws Exception {
        log.info("Button action triggered with id=={}", id);
        String actionResult = "";

        // Handling button actions based on ID and new button order
        switch (id) {
            // Group 1: 机器人和取餐操作
            case 1:
                actionResult = "机器人重置";
                robotService.robotReset();
                break;
            case 2:
                actionResult = "机器人取碗";
                robotService.robotTakeBowl();
                break;
            case 3:
                actionResult = "机器人取粉丝";
                robotService.robotTakeFans();
                break;
            case 4:
                actionResult = "机器人出餐";
                robotService.robotDeliverMeal();
                break;
            case 5:
                actionResult = "取餐口复位";
                relayDeviceService.foodOutletReset();
                break;
            case 6:
                actionResult = "取餐口出餐";
                relayDeviceService.foodOutletDeliver();
                break;

            // Group 2: 碗和粉丝仓操作
            case 7:
                actionResult = "出碗";
                relayDeviceService.deliverBowl();
                break;
            case 8:
                actionResult = "粉丝仓复位";
                fansService.noodleBinReset();
                break;
            case 9:
                actionResult = "粉丝仓出粉丝";
                fansService.noodleBinDeliver();
                break;
            case 10:
                actionResult = "装菜勺复位";
                bowlService.spoonReset();
                break;
            case 11:
                actionResult = "装菜勺倒菜";
                bowlService.spoonPour();
                break;

            // Group 3: 蒸汽和温度控制
            case 12:
                actionResult = "蒸汽打开";
                relayDeviceService.steamOpen();
                break;
            case 13:
                actionResult = "蒸汽关闭";
                relayDeviceService.steamClose();
                break;
            case 14:
                actionResult = "加汤蒸汤盖下降";
                relayDeviceService.soupSteamCoverDown();
                break;
            case 15:
                actionResult = "加汤蒸汤盖上升";
                relayDeviceService.soupSteamCoverUp();
                break;
            case 16:
                actionResult = "关闭汤蒸汽阀";
                relayDeviceService.soupSteamValveClose();
                break;
            case 17:
                actionResult = "加汤";
                relayDeviceService.soupAdd(number != null ? number : beefConfig.getSoupExtractionTime());
                break;
            case 18:
                actionResult = "汤管排气";
                relayDeviceService.soupPipeExhaust(number != null ? number : beefConfig.getSoupExhaustTime());
                break;
            case 19:
                actionResult = "汤加热至";
                relayDeviceService.soupHeatTo(number != null ? number : (int) beefConfig.getSoupHeatingTemperature());
                break;
            case 20:
                actionResult = "加蒸汽";
                relayDeviceService.bowlSteamAdd(number != null ? number : beefConfig.getBowlSteamTime());
                break;

            // Group 4: 风扇和震动测试
            case 21:
                actionResult = "后箱风扇开";
                relayDeviceService.rearFanOpen();
                break;
            case 22:
                actionResult = "后箱风扇关";
                relayDeviceService.rearFanClose();
                break;
            case 23:
                actionResult = "震动器1测试";
                relayDeviceService.vibrator1Test(number != null ? number : beefConfig.getVibratorTime());
                break;
            case 24:
                actionResult = "震动器2测试";
                relayDeviceService.vibrator2Test(number != null ? number : beefConfig.getVibratorTime());
                break;
            case 25:
                actionResult = "出料3测试";
                relayDeviceService.DischargeBin3Test(number != null ? number : beefConfig.getVibratorTime());
                break;

            // Group 5: 配菜操作
            case 26:
                actionResult = "一号配菜";
                relayDeviceService.vegetable1Motor(1, number != null ? number : beefConfig.getBeef10());
                break;
            case 27:
                actionResult = "二号配菜";
                relayDeviceService.vegetable1Motor(2, number != null ? number : beefConfig.getCilantro());
                break;
            case 28:
                actionResult = "三号配菜";
                relayDeviceService.vegetable1Motor(3, number != null ? number : beefConfig.getChoppedGreenOnion());
                break;
            case 29:
                actionResult = "四号配菜";
                relayDeviceService.vegetable1Motor(4, number != null ? number : beefConfig.getChoppedGreenOnion());
                break;

            // Group 6: 料仓操作
            case 30:
                actionResult = "一号料仓打开";
                relayDeviceService.firstBinOpen();
                break;
            case 31:
                actionResult = "一号料仓关闭";
                relayDeviceService.firstBinClose();
                break;
            case 32:
                actionResult = "二号料仓打开";
                relayDeviceService.secondBinOpen();
                break;
            case 33:
                actionResult = "二号料仓关闭";
                relayDeviceService.secondBinClose();
                break;
            case 34:
                actionResult = "三号料仓打开";
                relayDeviceService.thirdBinOpen();
                break;
            case 35:
                actionResult = "三号料仓关闭";
                relayDeviceService.thirdBinClose();
                break;

            default:
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
