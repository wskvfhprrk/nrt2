package com.jc.controller;

import com.jc.service.impl.RelayDeviceService;
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
    private RelayDeviceService relayDeviceService;

    /**
     * 关闭所有继电器
     */
    @GetMapping("closeAll")
    public void closeAll() {
        relayDeviceService.closeAll();
    }

    /**
     * 打开指定编号的继电器
     *
     * @param i 继电器编号
     */
    @GetMapping("open")
    public void open(@RequestParam int i) {
        relayDeviceService.relayOpening(i);
    }

    /**
     * 关闭指定编号的继电器
     *
     * @param i 继电器编号
     */
    @GetMapping("close")
    public void close(@RequestParam int i) {
        relayDeviceService.relayClosing(i);
    }

    /**
     * 打开所有继电器
     */
    @GetMapping("openAll")
    public void openAll() {
        relayDeviceService.openAll();
    }

    /**
     * 打开指定编号的继电器一段时间后自动关闭
     *
     * @param no     继电器编号
     * @param second 自动关闭的时间（秒）
     */
    @GetMapping("openClose")
    public void openClose(@RequestParam int no, @RequestParam int second) {
        relayDeviceService.openClose(no, second);
    }

    /**
     * 出餐口向下
     */
    @GetMapping("theFoodOutletIsFacingDownwards")
    public void theFoodOutletIsFacingDownwards() {
        relayDeviceService.theFoodOutletIsFacingDownwards();
    }

    /**
     * 出餐口向上
     */
    @GetMapping("theFoodOutletIsFacingUpwards")
    public void theFoodOutletIsFacingUpwards() {
        relayDeviceService.theFoodOutletIsFacingUpwards();
    }

    /**
     * 出餐口打开仓口出餐——完成后机器人才会抓取碗放到出餐面板上
     */
    @GetMapping("dischargingFromWarehouse")
    public void dischargingFromWarehouse() {
        relayDeviceService.dischargingFromWarehouse();
    }

    /**
     * 关仓餐口——在取餐口向下时关闭出餐口完全下到位才关仓门
     */
    @GetMapping("dischargingIsProhibitedAfterClosingTheWarehouse")
    public void dischargingIsProhibitedAfterClosingTheWarehouse() {
        relayDeviceService.dischargingIsProhibitedAfterClosingTheWarehouse();
    }
    /**
     * 打开排气风扇
     */
    @GetMapping("openFan")
    public void openFan(){
        relayDeviceService.openFan();
    }
    /**
     * 关闭排气风扇
     */
    @GetMapping("closeFan")
    public void closeFan(){
        relayDeviceService.closeFan();
    }
}
