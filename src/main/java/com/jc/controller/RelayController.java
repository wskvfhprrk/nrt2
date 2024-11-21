package com.jc.controller;

import com.jc.service.impl.Relay1DeviceGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 继电器控制器
 */
@RestController
public class RelayController {

    @Autowired
    private Relay1DeviceGatewayService relay1DeviceGatewayService;

    /**
     * 关闭所有继电器
     */
    @GetMapping("closeAll")
    public void closeAll() {
        relay1DeviceGatewayService.closeAll();
    }

    /**
     * 打开指定编号的继电器
     *
     * @param i 继电器编号
     */
    @GetMapping("open")
    public void open(@RequestParam int i) {
        relay1DeviceGatewayService.relayOpening(i);
    }

    /**
     * 关闭指定编号的继电器
     *
     * @param i 继电器编号
     */
    @GetMapping("close")
    public void close(@RequestParam int i) {
        relay1DeviceGatewayService.relayClosing(i);
    }

//    /**
//     * 打开所有继电器
//     */
//    @GetMapping("openAll")
//    public void openAll() {
//        relay1DeviceGatewayService.openAll();
//    }

    /**
     * 打开指定编号的继电器一段时间后自动关闭
     *
     * @param no     继电器编号
     * @param second 自动关闭的时间（秒）
     */
    @GetMapping("openClose")
    public void openClose(@RequestParam int no, @RequestParam int second) {
        relay1DeviceGatewayService.openClose(no, second);
    }

    /**
     * 关仓餐口——在取餐口向下时关闭出餐口完全下到位才关仓门
     */
    @GetMapping("dischargingIsProhibitedAfterClosingTheWarehouse")
    public void dischargingIsProhibitedAfterClosingTheWarehouse() {
//        relayDeviceService.dischargingIsProhibitedAfterClosingTheWarehouse();
    }


    @GetMapping("openVibrator")
    public void openVibrator() {
        relay1DeviceGatewayService.openVibrator();
    }

}
