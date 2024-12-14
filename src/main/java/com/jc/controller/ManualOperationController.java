package com.jc.controller;

import com.jc.config.DataConfig;
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
    private Relay1DeviceGatewayService relay1DeviceGatewayService;
//    @Autowired
//    private Relay2DeviceGatewayService relay2DeviceGatewayService;
    @Autowired
    private BowlService bowlService;
    @Autowired
    private TemperatureWeighingGatewayService temperatureWeighingGatewayService;
    @Autowired
    private DataConfig dataConfig;
    @Autowired
    private FansService fansService;
    @Autowired
    private Reset reset;
    @Autowired
    private PubConfig pubConfig;
    @Autowired
    private TemperatureWeighingGatewayService temperatureWeightReadingService;

    @GetMapping("/{id}")
    public Result handleButtonAction(@PathVariable int id, @RequestParam(required = false) Integer number) throws Exception {
        log.info("按钮操作触发，ID=={}", id);
        Result result = null;
        switch (id) {
            // buttonsGroup1
            case 1:
                result = robotService.robotReset();
                if (result.getCode() != 200) return result;
                break;
            case 2:
                result = robotService.robotTakeFans();
                if (result.getCode() != 200) return result;
                break;
            case 3:
                result = robotService.robotTakeBasket();
                if (result.getCode() != 200) return result;
                break;
            case 4:
                result = robotService.robotTakeBowl();
                if (result.getCode() != 200) return result;
                break;
            case 5:
                result = robotService.robotPlaceBowl();
                if (result.getCode() != 200) return result;
                break;
            case 6:
                result = robotService.robotDeliverMeal();
                if (result.getCode() != 200) return result;
                break;
            case 7:
                result = relay1DeviceGatewayService.foodOutletReset();
                if (result.getCode() != 200) return result;
                break;
            case 8:
                result = relay1DeviceGatewayService.foodOutletDeliver();
                if (result.getCode() != 200) return result;
                break;

            // buttonsGroup2
            case 9:
                result = relay1DeviceGatewayService.deliverBowl();
                if (result.getCode() != 200) return result;
                break;
            case 10:
                result = fansService.fanReset();
                if (result.getCode() != 200) return result;
                break;
            case 11:
                result = fansService.moveFanBin(number == null ? 2 : number);
                if (result.getCode() != 200) return result;
                break;
            case 12:
                result = fansService.takeFans();
                if (result.getCode() != 200) return result;
                break;
            case 13:
                result = bowlService.spoonReset();
                if (result.getCode() != 200) return result;
                break;
            case 14:
                result = bowlService.spoonLoad();
                if (result.getCode() != 200) return result;
                break;
            case 15:
                result = bowlService.spoonPour();
                if (result.getCode() != 200) return result;
                break;

            // buttonsGroup3
            case 16:
                result = relay1DeviceGatewayService.lowerSteamCover();
                if (result.getCode() != 200) return result;
                break;
            case 17:
                result = relay1DeviceGatewayService.soupSteamCoverDown();
                if (result.getCode() != 200) return result;
                break;
            case 18:
                result = relay1DeviceGatewayService.soupSteamCoverUp();
                if (result.getCode() != 200) return result;
                break;
            case 19:
                result = relay1DeviceGatewayService.soupAdd(number != null ? number : dataConfig.getSoupExtractionTime());
                if (result.getCode() != 200) return result;
                break;
            case 20:
                result = relay1DeviceGatewayService.soupPipeExhaust(number != null ? number : dataConfig.getSoupExhaustTime());
                if (result.getCode() != 200) return result;
                break;
            case 21:
                result = temperatureWeightReadingService.soupHeatTo(number != null ? number : dataConfig.getSteamAdditionTimeSeconds());
                if (result.getCode() != 200) return result;
                break;
            case 22:
                result = relay1DeviceGatewayService.steamAndSoupAdd();
                if (result.getCode() != 200) return result;
                break;

            // buttonsGroup4（页面中的按钮组4）
            case 23:  // 后箱风扇开
                result = relay1DeviceGatewayService.rearFanOpen();
                if (result.getCode() != 200) return result;
                break;
            case 24:  // 后箱风扇关
                result = relay1DeviceGatewayService.rearFanClose();
                if (result.getCode() != 200) return result;
                break;
            case 25:  // 一号配菜（g）
                result = relay1DeviceGatewayService.meatSlicingMachine(number == null ? 1 : number);
                if (result.getCode() != 200) return result;
                break;
            case 26:  // 二号配菜（g）
                result = relay1DeviceGatewayService.vibrator1Test(number == null ? dataConfig.getVibratorTime() : number);
                if (result.getCode() != 200) return result;
                break;
            case 27:  // 1称重清0
                result = relay1DeviceGatewayService.vibrationSwitchOn();
                if (result.getCode() != 200) return result;
                break;
            case 28:  // 1标重500g
                result = relay1DeviceGatewayService.vibrationSwitchOff();
                if (result.getCode() != 200) return result;
                break;
            case 29:  // 2称重清0
                result = relay1DeviceGatewayService.vibrationSwitchControl(number == null ? 0 : number);
                if (result.getCode() != 200) return result;
                break;

            // buttonsGroup5（页面中的按钮组5）
            case 31:  // 切肉机切肉（份量）
                result = relay1DeviceGatewayService.meatSlicingMachine(number != null ? number : 1);
                if (result.getCode() != 200) return result;
                break;
            case 32:  // 震动器（秒）
                result = relay1DeviceGatewayService.openVibrator();
                if (result.getCode() != 200) return result;
                break;
            case 33:  // 出料开关（秒）
                result = temperatureWeighingGatewayService.clearAll();
                if (result.getCode() != 200) return result;
                break;
            case 34:  // 震动料开关打开
                result = temperatureWeighingGatewayService.calibrateWeight1();
                if (result.getCode() != 200) return result;
                break;
            case 35:  // 震动料开关关闭
                result = temperatureWeighingGatewayService.calibrateWeight2();
                if (result.getCode() != 200) return result;
                break;


            // buttonsGroup6
            case 40:
                relay1DeviceGatewayService.relayClosing(Constants.Y_RIGHT_DOOR);
                break;
            case 41:
                relay1DeviceGatewayService.relayOpening(Constants.Y_RIGHT_DOOR);
                break;
            case 42:
                relay1DeviceGatewayService.relayClosing(Constants.Y_OPEN_LOWER_RIGHT_DOOR);
                break;
            case 43:
                relay1DeviceGatewayService.relayOpening(Constants.Y_OPEN_LOWER_RIGHT_DOOR);
                break;
            case 44:
                relay1DeviceGatewayService.relayClosing(Constants.Y_MIDDLE_LEFT_UPPER_DOOR);
                break;
            case 45:
                relay1DeviceGatewayService.relayOpening(Constants.Y_MIDDLE_LEFT_UPPER_DOOR);
                break;
            case 46:
                relay1DeviceGatewayService.relayClosing(Constants.Y_MIDDLE_LEFT_LOWER_DOOR);
                break;
            case 47:
                relay1DeviceGatewayService.relayOpening(Constants.Y_MIDDLE_LEFT_LOWER_DOOR);
                break;
            case 48:
                relay1DeviceGatewayService.relayClosing(Constants.Y_MIDDLE_RIGHT_UPPER_DOOR);
                break;
            case 49:
                relay1DeviceGatewayService.relayOpening(Constants.Y_MIDDLE_RIGHT_UPPER_DOOR);
                break;
            case 50 :
                relay1DeviceGatewayService.relayClosing(Constants.Y_MIDDLE_RIGHT_LOWER_DOOR);
                break;
            case 51 :
                relay1DeviceGatewayService.relayOpening(Constants.Y_MIDDLE_RIGHT_LOWER_DOOR);
                break;

            default:
                throw new IllegalArgumentException("Invalid button ID: " + id);
        }

        return result.getCode() != 200 ? result : Result.success("操作完成");
    }

    @GetMapping("/emergencyStop")
    public String emergencyStop() {
        log.info("Emergency stop triggered");
        relay1DeviceGatewayService.closeAll();
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
