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
        // Handling button actions based on ID
        switch (id) {
            // 机器人和取餐操作
            case 1:  // 机器人重置
                actionResult = "机器人重置";
                robotService.robotReset();  // 机器人重置
                break;
            case 2:  // 机器人取碗
                actionResult = "机器人取碗";
                robotService.robotTakeBowl();  // 机器人取碗
                break;
            case 3:  // 机器人取粉丝
                actionResult = "机器人取粉丝";
        robotService.robotTakeFans();  // 机器人取粉丝
                break;
            case 4:  // 机器人出餐
                actionResult = "机器人出餐";
                robotService.robotDeliverMeal();  // 机器人出餐
                break;
            case 5:  // 取餐口复位
                actionResult = "取餐口复位";
                relayDeviceService.foodOutletReset();  // 取餐口复位
                break;
            case 6:  // 取餐口出餐
                actionResult = "取餐口出餐";
                relayDeviceService.foodOutletDeliver();  // 取餐口出餐
                break;

            // 碗操作
            case 7:  // 出碗
                actionResult = "出碗";
                relayDeviceService.deliverBowl();  // 出碗
                break;
            case 8:  // 粉丝仓复位
                actionResult = "粉丝仓复位";
//                bowlService.noodleBinReset();  // 粉丝仓复位
                break;
            case 9:  // 粉丝仓出粉丝
                actionResult = "粉丝仓出粉丝";
//                bowlService.noodleBinDeliver();  // 粉丝仓出粉丝
                break;

            // 蒸汽和温度控制
            case 11: // 蒸汽打开
                actionResult = "蒸汽打开";
                relayDeviceService.steamOpen();  // 蒸汽打开
                break;
            case 12: // 蒸汽关闭
                actionResult = "蒸汽关闭";
                relayDeviceService.steamClose();  // 蒸汽关闭
                break;
            case 13: // 关闭汤蒸汽阀
                actionResult = "关闭汤蒸汽阀";
                relayDeviceService.soupSteamValveClose();  // 关闭汤蒸汽阀
                break;
            case 14: // 加汤（秒）
                if (number == null) {
                    number = beefConfig.getSoupExtractionTime();
                }
                relayDeviceService.soupAdd(number);  // 加汤（秒）
                actionResult = "加汤";
                break;
            case 15: // 汤管排气（秒）
                if (number == null) {
                    number = beefConfig.getSoupExhaustTime();
                }
                relayDeviceService.soupPipeExhaust(number);  // 汤管排气（秒）
                actionResult = "汤管排气";
                break;
            case 16: // 汤加热至（度）
                if (number == null) {
                    double temperature = beefConfig.getSoupHeatingTemperature();
                    number = (int) temperature;
                }
                relayDeviceService.soupHeatTo(number);  // 汤加热至（度）
                actionResult = "汤加热至";
                break;
            case 17: // 碗加蒸汽（秒）
                if (number == null) {
                    number = beefConfig.getBowlSteamTime();
                }
                relayDeviceService.bowlSteamAdd(number);  // 碗加蒸汽（秒）
                actionResult = "碗加蒸汽";
                break;

            // 风扇和震动测试
            case 18: // 后箱风扇开
                actionResult = "后箱风扇开";
                relayDeviceService.rearFanOpen();  // 后箱风扇开
                break;
            case 19: // 后箱风扇关
                actionResult = "后箱风扇关";
                relayDeviceService.rearFanClose();  // 后箱风扇关
                break;
            case 20: // 震动器1测试（秒）
                if (number == null) {
                    number = beefConfig.getVibratorTime();
                }
                relayDeviceService.vibrator1Test(number);  // 震动器1测试
                actionResult = "震动器1测试";
                break;
            case 21: // 震动器2测试（秒）
                if (number == null) {
                    number = beefConfig.getVibratorTime();
                }
                relayDeviceService.vibrator2Test(number);  // 震动器2测试
                actionResult = "震动器2测试";
                break;
            case 22: // 出料3测试（秒）
                if (number == null) {
                    number = beefConfig.getVibratorTime();
                }
                relayDeviceService.DischargeBin3Test(number);  // 出料3测试
                actionResult = "出料3测试";
                break;

            // 称重与配料操作
            case 23: // 一号配菜（g）
                if (number == null) {
                    number = beefConfig.getBeef10();
                }
                relayDeviceService.vegetable1Motor(1,number);  // 一号配菜（g）
                actionResult = "一号配菜";
                break;
            case 24: // 二号配菜（g）
                if (number == null) {
                    number = beefConfig.getCilantro();
                }
                relayDeviceService.vegetable1Motor(2,number);  // 二号配菜（g）
                actionResult = "二号配菜";
                break;
            case 25: // 三号配菜（g）
                if (number == null) {
                    number = beefConfig.getChoppedGreenOnion();
                }
                relayDeviceService.vegetable1Motor(3,number);  // 三号配菜（g）
                actionResult = "三号配菜";
                break;

            // 配料分发操作
            case 26: // 一号料仓打开
                actionResult = "一号料仓打开";
                relayDeviceService.firstBinOpen();  // 一号料仓打开
                break;
            case 27: // 一号料仓关闭
                actionResult = "一号料仓关闭";
                relayDeviceService.firstBinClose();  // 一号料仓关闭
                break;
            case 28: // 二号料仓打开
                actionResult = "二号料仓打开";
                relayDeviceService.secondBinOpen();  // 二号料仓打开
                break;
            case 29: // 二号料仓关闭
                actionResult = "二号料仓关闭";
                relayDeviceService.secondBinClose();  // 二号料仓关闭
                break;
            case 30: // 三号料仓打开
                actionResult = "三号料仓打开";
                relayDeviceService.thirdBinOpen();  // 三号料仓打开
                break;
            case 31: // 三号料仓关闭
                actionResult = "三号料仓关闭";
                relayDeviceService.thirdBinClose();  // 三号料仓关闭
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
