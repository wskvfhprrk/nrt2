package com.jc.controller.control;

import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.entity.Order;
import com.jc.enums.SteamGeneratorState;
import com.jc.service.RobotService;
import com.jc.service.impl.IODeviceService;
import com.jc.service.impl.RelayDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 制作牛肉汤
 */
@RestController
@Slf4j
public class ModeController {

    @Autowired
    private PubConfig pubConfig;
    @Autowired
    private RelayDeviceService relayDeviceService;
    @Autowired
    private RobotService robotService;
    @Autowired
    private IODeviceService ioDeviceService;

    /**
     * 制作牛肉汤
     *
     * @param order
     * @return
     */
    public Result mode(Order order) {
        // TODO: 2024/7/24 检查所有条件是否可以做，如果可以再做，不可以停止
        // TODO: 2024/7/24 如果在重置状态下可以制作牛肉汤
        //TODO: 2024/7/24  根据订单准备调料
        //打开蒸汽
        pubConfig.setSteamGeneratorCurrentState(SteamGeneratorState.STEAMING.getValue());
        relayDeviceService.steamOpen();
        // TODO: 2024/7/24 增加气压计后，如果气压不大于5kg
        //第一道工序——机器人放碗
        Result result = robotService.takeBowl();
        if (result.getCode() == 200) {
            //转台工作
            pubConfig.setTurntableRotationStatus(true);
        } else {
            log.error(result.getMessage());
            return result;
        }
        //第二道工序——放粉丝（不准备）
        //第三道工序——准备放调料
        //第四道工序——准备蒸汽
        //第五道工序——准备调料和汤
        //第六道工序——取汤（不准备）
        return Result.success();
    }
}
