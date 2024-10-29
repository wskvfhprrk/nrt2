package com.jc.controller;

import com.jc.config.BeefConfig;
import com.jc.config.PubConfig;
import com.jc.config.Result;
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
    private SiloWeighBoxSwitchService siloWeighBoxSwitchService;
    @Autowired
    private BowlService bowlService;
    @Autowired
    private StepperMotorService stepperMotorService;
    @Autowired
    private WeightService weightService;
    @Autowired
    private BeefConfig beefConfig;
    @Autowired
    private FansService fansService;
    @Autowired
    private Reset reset;
    @Autowired
    private PubConfig pubConfig;

    @GetMapping("/{id}")
    public Result handleButtonAction(@PathVariable int id, @RequestParam(required = false) Integer number) throws Exception {
        log.info("Button action triggered with id=={}", id);
        String actionResult = "";
        Result result = null;

        // Handling button actions based on new button order
        switch (id) {
            // Group 1: 机器人和取餐操作
            case 1:
                actionResult = "机器人重置";
                result = robotService.robotReset();
                break;
            case 2:
                actionResult = "机器人取碗";
                result = robotService.robotTakeBowl();
                break;
            case 3:
                actionResult = "机器人取粉丝";
                result = robotService.robotTakeFans();
                break;
            case 4:
                actionResult = "机器人出餐";
                result = robotService.robotDeliverMeal();
                break;
            case 5:
                actionResult = "取餐口复位";
                result = relayDeviceService.foodOutletReset();
                break;
            case 6:
                actionResult = "取餐口出餐";
                result = relayDeviceService.foodOutletDeliver();
                break;

            // Group 2: 碗和粉丝仓操作
            case 7:
                actionResult = "出碗";
                result = relayDeviceService.deliverBowl();
                break;
            case 8:
                actionResult = "粉丝仓复位";
                result = fansService.noodleBinReset();
                break;
            case 9:
                actionResult = "粉丝仓出粉丝";
                result = fansService.noodleBinDeliver();
                break;
            case 10:
                actionResult = "装菜勺复位";
                result = bowlService.spoonReset();
                break;
            case 11:
                actionResult = "装菜勺倒菜";
                result = bowlService.spoonPour();
                break;
            case 12:
                actionResult = "装菜勺装菜";
                result = bowlService.spoonLoad();
                break;

            // Group 3: 蒸汽和温度控制
            case 13:
                actionResult = "蒸汽打开";
                result = relayDeviceService.steamOpen();
                break;
            case 14:
                actionResult = "蒸汽关闭";
                result = relayDeviceService.steamClose();
                break;
            case 15:
                actionResult = "加汤蒸汤盖下降";
                result = relayDeviceService.soupSteamCoverDown();
                break;
            case 16:
                actionResult = "加汤蒸汤盖上升";
                result = relayDeviceService.soupSteamCoverUp();
                break;
            case 17:
                actionResult = "关汤蒸汽阀";
                result = relayDeviceService.soupSteamValveClose();
                break;
            case 18:
                actionResult = "加汤";
                result = relayDeviceService.soupAdd(number != null ? number : beefConfig.getSoupExtractionTime());
                break;
            case 19:
                actionResult = "汤管排气";
                result = relayDeviceService.soupPipeExhaust(number != null ? number : beefConfig.getSoupExhaustTime());
                break;
            case 20:
                actionResult = "汤加热至";
                result = relayDeviceService.soupHeatTo(number != null ? number : (int) beefConfig.getSoupHeatingTemperature());
                break;
            case 21:
                actionResult = "加蒸汽";
                result = relayDeviceService.bowlSteamAdd(number != null ? number : beefConfig.getBowlSteamTime());
                break;

            // Group 4: 风扇和震动测试
            case 22:
                actionResult = "后箱风扇开";
                result = relayDeviceService.rearFanOpen();
                break;
            case 23:
                actionResult = "后箱风扇关";
                result = relayDeviceService.rearFanClose();
                break;
            case 24:
                actionResult = "震动器1测试";
                result = relayDeviceService.vibrator1Test(number != null ? number : beefConfig.getVibratorTime());
                break;
            case 25:
                actionResult = "震动器2测试";
                result = relayDeviceService.vibrator2Test(number != null ? number : beefConfig.getVibratorTime());
                break;
            case 26:
                actionResult = "出料3测试";
                result = relayDeviceService.DischargeBin3Test(number != null ? number : beefConfig.getVibratorTime());
                break;

            // Group 5: 配菜操作
            case 27:
                actionResult = "一号配菜";
                result = relayDeviceService.vegetable1Motor(1, number != null ? number : beefConfig.getBeef10());
                break;
            case 28:
                actionResult = "二号配菜";
                result = relayDeviceService.vegetable1Motor(2, number != null ? number : beefConfig.getCilantro());
                break;
            case 29:
                actionResult = "三号配菜";
                result = relayDeviceService.vegetable1Motor(3, number != null ? number : beefConfig.getChoppedGreenOnion());
                break;
            case 30:
                actionResult = "四号配菜";
                result = relayDeviceService.vegetable1Motor(4, number != null ? number : beefConfig.getChoppedGreenOnion());
                break;
            case 31:
                actionResult = "称重全部清0";
                result = weightService.clearAll();
                break;
            case 32:
                actionResult = "标重500g";
                result = weightService.calibrateWeight(number != null ? number : 1);
                break;
            case 33:
                actionResult = "打开称重盒";
                result = siloWeighBoxSwitchService.openWeighBox(number != null ? number : 1);
                break;
            case 34:
                actionResult = "关闭称重盒";
                result = siloWeighBoxSwitchService.closeWeighBox(number != null ? number : 1);
                break;

            // Group 6: 料仓操作
            case 35:
                actionResult = "一号料仓打开";
                result = relayDeviceService.firstBinOpen();
                break;
            case 36:
                actionResult = "一号料仓关闭";
                result = relayDeviceService.firstBinClose();
                break;
            case 37:
                actionResult = "二号料仓打开";
                result = relayDeviceService.secondBinOpen();
                break;
            case 38:
                actionResult = "二号料仓关闭";
                result = relayDeviceService.secondBinClose();
                break;
            case 39:
                actionResult = "三号料仓打开";
                result = relayDeviceService.thirdBinOpen();
                break;
            case 40:
                actionResult = "三号料仓关闭";
                result = relayDeviceService.thirdBinClose();
                break;

            default:
                actionResult = "未知操作";
        }

        // Returning result
        if (result != null && result.getCode() != 200) {
            return result;
        }

        return Result.success(actionResult);
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
