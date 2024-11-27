package com.jc.controller;

import com.jc.config.DataConfig;
import com.jc.config.PubConfig;
import com.jc.config.Result;
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
    @Autowired
    private Relay2DeviceGatewayService relay2DeviceGatewayService;
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
        log.info("Button action triggered with id=={}", id);
        Result result = null;

        switch (id) {
            // buttonsGroup1
            case 1:
                result = robotService.robotReset();
                break;
            case 2:
                result = robotService.robotTakeFans();
                break;
            case 3:
                result = robotService.robotTakeBasket();
                break;
            case 4:
                result = robotService.robotTakeBowl();
                break;
            case 5:
                result = robotService.robotPlaceBowl();
                break;
            case 6:
                result = robotService.robotDeliverMeal();
                break;
            case 7:
                result = relay1DeviceGatewayService.foodOutletReset();
                break;
            case 8:
                result = relay1DeviceGatewayService.foodOutletDeliver();
                break;

            // buttonsGroup2
            case 9:
                result = relay1DeviceGatewayService.deliverBowl();
                break;
            case 10:
                result = fansService.fanReset();
                break;
            case 11:
                result = fansService.moveFanBin(number == null ? 2 : number);
                break;
            case 12:
                result = fansService.takeFans();
                break;
            case 13:
                result = bowlService.spoonReset();
                break;
            case 14:
                result = bowlService.spoonLoad();
                break;
            case 15:
                result = bowlService.spoonPour();
                break;

            // buttonsGroup3
            case 16:
                result = relay1DeviceGatewayService.steamOpen();
                break;
            case 17:
                result = relay1DeviceGatewayService.steamClose();
                break;
            case 18:
                result = relay1DeviceGatewayService.lowerSteamCover();
                break;
            case 19:
                result = relay1DeviceGatewayService.soupSteamCoverDown();
                break;
            case 20:
                result = relay1DeviceGatewayService.soupSteamCoverUp();
                break;
            case 21:
                result = relay1DeviceGatewayService.soupAdd(number != null ? number : dataConfig.getSoupExtractionTime());
                break;
            case 22:
                result = relay1DeviceGatewayService.soupPipeExhaust(number != null ? number : dataConfig.getSoupExhaustTime());
                break;
            case 23:
                result = temperatureWeightReadingService.soupHeatTo(number != null ? number : dataConfig.getSteamAdditionTimeSeconds());
                break;
            case 24:
                result = relay1DeviceGatewayService.steamAndSoupAdd();
                break;

            // buttonsGroup4
            case 26:
                result = relay1DeviceGatewayService.rearFanOpen();
                break;
            case 27:
                result = relay1DeviceGatewayService.rearFanClose();
                break;
            case 28:
                result = relay1DeviceGatewayService.meatSlicingMachine(number == null ? 1 : number);
                break;
            case 29:
                result = relay1DeviceGatewayService.vibrator1Test(number == null ? dataConfig.getVibratorTime() : number);
                break;
            case 30:
                result = relay1DeviceGatewayService.vibrationSwitchOn();
                break;
            case 31:
                result = relay1DeviceGatewayService.vibrationSwitchOff();
                break;
            case 32:
                result = relay1DeviceGatewayService.vibrationSwitchControl(number == null ? 0 : number);
                break;

            // buttonsGroup5
            case 33:
                result = temperatureWeighingGatewayService.vegetable1Motor( number != null ? number : dataConfig.getIngredient1Value());
                break;
            case 34:
                result = temperatureWeighingGatewayService.vegetable2Motor( number != null ? number : dataConfig.getIngredient2Value());
                break;
            case 37:
                result = temperatureWeighingGatewayService.clearAll();
                break;
            case 38:
                result = temperatureWeighingGatewayService.calibrateWeight1();
                break;
            case 43:
                result = temperatureWeighingGatewayService.calibrateWeight2();
                break;

            // buttonsGroup6
            case 39:
                result = relay2DeviceGatewayService.openWeighBox(1);
                break;
            case 40:
                result = relay2DeviceGatewayService.closeWeighBox(1);
                break;
            case 41:
                result = relay2DeviceGatewayService.openWeighBox(4);
                break;
            case 42:
                result = relay2DeviceGatewayService.closeWeighBox(4);
                break;


            default:
                throw new IllegalArgumentException("Invalid button ID: " + id);
        }

        return result != null && result.getCode() == 200 ? result : Result.success("操作完成");
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
