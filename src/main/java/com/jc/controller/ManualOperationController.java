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
    @Autowired
    private Relay2DeviceGatewayService relay2DeviceGatewayService;

    @GetMapping("/{id}")
    public Result handleButtonAction(@PathVariable int id, @RequestParam(required = false) Integer number) throws Exception {
        log.info("按钮操作触发，ID=={}", id);
        Result result = null;
        switch (id) {
            // buttonsGroup1
            case 1:  // 机器人重置
                result = robotService.robotReset();
                break;
            case 2:  // 机器人取粉丝
                result = robotService.robotTakeFans();
                break;
            case 3:  // 机器人取菜蓝
                result = robotService.robotTakeBasket();
                break;
            case 4:  // 机器人取碗
                result = robotService.robotTakeBowl();
                break;
            case 5:  // 机器人放碗
                result = robotService.robotPlaceBowl();
                break;
            case 6:  // 机器人出餐
                result = robotService.robotDeliverMeal();
                break;
            case 7:  // 取餐口复位——闭合
                result = relay1DeviceGatewayService.closePickUpCounter();
                break;
            case 8:  // 取餐口出餐
                result = relay1DeviceGatewayService.openPickUpCounter();
                break;

            // buttonsGroup2
            case 9:  // 出碗
                result = relay1DeviceGatewayService.deliverBowl();
                break;
            case 10:  // 粉丝仓复位
                result = fansService.fanReset();
                break;
            case 11:  // 移动粉丝仓（编号）
                result = fansService.moveFanBin(number == null ? 2 : number);
                break;
            case 12:  // 粉丝仓出粉丝
                result = fansService.takeFans();
                break;
            case 13:  // 装菜勺复位
                result = bowlService.spoonReset();
                break;
            case 14:  // 装菜勺倒菜
                result = bowlService.spoonPour();
                break;
            case 15:  // 装菜勺装菜
                result = bowlService.spoonLoad();
                break;

            // buttonsGroup3
            case 16:  // 加蒸汽盖下降
                result = relay1DeviceGatewayService.lowerSteamCover();
                break;
            case 17:  // 加汤盖下降
                result = relay1DeviceGatewayService.soupSteamCoverDown();
                break;
            case 18:  // 加汤蒸汤盖上升
                result = relay1DeviceGatewayService.soupSteamCoverUp();
                break;
            case 19:  // 加汤（秒）
                result = relay1DeviceGatewayService.soupAdd(number != null ? number : dataConfig.getSoupExtractionTime());
                break;
            case 20:  // 汤管排气（秒）
                result = relay1DeviceGatewayService.soupPipeExhaust(number != null ? number : dataConfig.getSoupExhaustTime());
                break;
            case 21:  // 汤加热至（度）
                result = temperatureWeightReadingService.soupHeatTo(number != null ? number : dataConfig.getSteamAdditionTimeSeconds());
                break;
            case 22:  // 加蒸汽（秒）
                result = relay1DeviceGatewayService.SteamAdd(number != null ? number : dataConfig.getSteamAdditionTimeSeconds());
                break;

            // buttonsGroup4
            case 23:  // 后箱风扇开
                result = relay1DeviceGatewayService.rearFanOpen();
                break;
            case 24:  // 后箱风扇关
                result = relay1DeviceGatewayService.rearFanClose();
                break;
            case 25:  // 配菜（g）
                result = temperatureWeighingGatewayService.vegetable1Motor(number == null ? 0 : number);
                break;
            case 27:  // 称重清0
                result = temperatureWeighingGatewayService.clearAll();
                break;
            case 28:  // 1标重500g
                result = temperatureWeighingGatewayService.calibrateWeight1();
                break;
            case 29:  // 2称重清0
//                result = relay1DeviceGatewayService.vibrationSwitchControl(number == null ? 0 : number);
                break;

            // buttonsGroup5
            case 31:  // 切肉机切肉（份量）
                result = relay1DeviceGatewayService.meatSlicingMachine(number != null ? number : 1);
                break;
            case 32:  // 震动器打开
                result = relay1DeviceGatewayService.vibrationSwitchOn();
                break;
            case 36:  // 震动器关闭
                result = relay1DeviceGatewayService.vibrationSwitchOff();
                break;
            case 33:  // 出料开关（秒）
                result = temperatureWeighingGatewayService.clearAll();
                break;
            case 37:  // 称重盒打开
                result = relay2DeviceGatewayService.openWeighBox(Constants.WEIGH_BOX_NUMBER);
                break;
            case 38:  // 称重盒关闭
                result = relay2DeviceGatewayService.closeWeighBox(Constants.WEIGH_BOX_NUMBER);
                break;

            // buttonsGroup6
            case 40:  // 左右上打开
                result = relay1DeviceGatewayService.openClose(Constants.Y_LEFT_DOOR, 1);
                break;
            case 42:  // 左左上打开
                result = relay1DeviceGatewayService.openClose(Constants.Y_OPEN_LOWER_LEFT_DOOR, 1);
                break;
            case 44:  // 中间左上打开
                result = relay1DeviceGatewayService.openClose(Constants.Y_MIDDLE_LEFT_UPPER_DOOR, 1);
                break;
            case 46:  // 中间左下打开
                result = relay1DeviceGatewayService.openClose(Constants.Y_MIDDLE_LEFT_LOWER_DOOR, 1);
                break;
            case 48:  // 中间右上打开
                result = relay1DeviceGatewayService.openClose(Constants.Y_MIDDLE_RIGHT_UPPER_DOOR, 1);
                break;
            case 50:  // 中间右下打开
                result = relay1DeviceGatewayService.openClose(Constants.Y_MIDDLE_RIGHT_LOWER_DOOR, 1);
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
