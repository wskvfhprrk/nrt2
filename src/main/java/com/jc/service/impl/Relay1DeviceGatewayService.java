package com.jc.service.impl;

import com.jc.config.*;
import com.jc.constants.Constants;
import com.jc.enums.SignalLevel;
import com.jc.netty.server.NettyServerHandler;
import com.jc.service.DeviceHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 继电器1设备处理类
 * 实现了DeviceHandler接口，提供了继电器的打开、关闭及定时关闭功能
 */
@Service
@Slf4j
public class Relay1DeviceGatewayService implements DeviceHandler {
    @Autowired
    private NettyServerHandler nettyServerHandler;
    @Autowired
    private PubConfig pubConfig;
    @Autowired
    private IpConfig ipConfig;
    @Autowired
    private DataConfig dataConfig;
    @Autowired
    private SignalAcquisitionDeviceGatewayService signalAcquisitionDeviceGatewayService;
    //发送中状态——true正在发送中——不允许再次发送指令，等待其返回值后才可发送，false已经有返回值
    private Boolean sendHexStatus = false;

    @Autowired
    private PortionOptionsConfig portionOptionsConfig;

    /**
     * 处理消息
     *
     * @param message 消息内容
     * @param isHex   是否为16进制消息
     */
    @Override
    public void handle(String message, boolean isHex) {
        sendHexStatus = false;
//        log.info("继电器设备接收到HEX消息: {}", message);
    }

    /**
     * 发送modbus指令
     *
     * @param s
     */
    private void sendOrder(String s) {
        while (sendHexStatus) {
            try {
                Thread.sleep(Constants.COMMAND_INTERVAL_POLLING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        log.info("继电器设备发送HEX消息: {}", s);
        sendHexStatus = true;
        nettyServerHandler.sendMessageToClient(ipConfig.getRelay1DeviceGateway(), s, true);
    }

    /**
     * 根据编号打开继电器
     *
     * @param no 继电器编号，范围为1-32
     */
    public void relayOpening(int no) {
        if (no <= 0 || no > 32) {
            log.error("编号{}继电器不存在！", no);
            return; // 添加return，防止继续执行
        }
        // 将编号转换为16进制字符串
        String hexString = Integer.toHexString(no).toUpperCase();
        // 如果字符串长度不足两位，则在前面补0
        if (hexString.length() < 2) {
            hexString = "0" + hexString;
        }
        // 构造指令字符串
        StringBuffer sb = new StringBuffer("48 3A 01 70 ");
        sb.append(hexString);
        sb.append(" 01 00 00 45 44");
        // 发送指令
        sendOrder(sb.toString());
    }

    /**
     * 根据编号关闭继电器
     *
     * @param no 继电器编号，范围为1-32
     */
    public Result relayClosing(int no) {
        if (no <= 0 || no > 32) {
            log.error("编号{}继电器不存在！", no);
            return Result.error("编号继电器不存在"); // 添加return，防止继续执行
        }
        // 将编号转换为16进制字符串
        String hexString = Integer.toHexString(no).toUpperCase();
        // 如果字符串长度不足两位，则在前面补0
        if (hexString.length() < 2) {
            hexString = "0" + hexString;
        }
        // 构造指令字符串
        StringBuffer sb = new StringBuffer("48 3A 01 70 ");
        sb.append(hexString);
        sb.append(" 00 00 00 45 44");
        // 发送指令
        sendOrder(sb.toString());
        return Result.success();
    }


    /**
     * 继电器打开一段时间后自动关
     *
     * @param no     继电器编号，范围为1-32
     * @param second 延迟关闭的时间，单位为秒，范围为1-177777
     */
    public Result openClose(int no, int second) {
        if (no <= 0 || no > 32) {
            log.error("编号{}继电器不存在！", no);
            return Result.error("编号继电器不存在"); // 添加return，防止继续执行
        }
        if (second <= 0 || second > 177777) {
            log.error("时间值不能限定", second);
            return Result.error("时间值不能限定"); // 添加return，防止继续执行
        }
        // 将编号转换为16进制字符串
        String hexString = Integer.toHexString(no).toUpperCase();
        // 如果字符串长度不足两位，则在前面补0
        if (hexString.length() < 2) {
            hexString = "0" + hexString;
        }
        // 构造指令字符串
        StringBuffer sb = new StringBuffer("48 3A 01 70 ");
        sb.append(hexString);
        sb.append(" 01 ");
        // 将时间转换为16进制字符串
        String hexTime = String.format("%04X", second).toUpperCase();
        sb.append(hexTime);
        sb.append(" 45 44");
        // 发送指令
        sendOrder(sb.toString());
        return Result.success();
    }

    /**
     * 关闭所有继电器
     */
    public Result closeAll() {
        log.info("关闭所有继电器");
        // 发送关闭所有继电器的指令
        sendOrder("48 3A 01 57 00 00 00 00 00 00 00 00 DA 45 44");
        steamOpen();
        return Result.success();
    }

    /**
     * 打开抽汤泵到液位后多少秒关闭——至少1秒
     *
     * @param seconds 多少秒关闭
     */
    public Result soupAdd(int seconds) {
        //盖子下降——两次命令
        Result result = this.soupSteamCoverDown();
        if (result.getCode() != 200) {
            log.error(result.getMessage());
            return result;
        }
        //抽汤前先打开汤开关，防止水流
        openClose(Constants.Y_BOWL_STEAM_SOLENOID_VALVE, seconds);
        log.info("打开抽汤{}秒钟", seconds);
        openClose(Constants.Y_SOUP_PUMP_SWITCH, seconds);
        try {
            Thread.sleep((seconds + 1) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //盖子上升
        result = this.soupSteamCoverUp();
        if (result.getCode() != 200) {
            return result;
        }
        return Result.success();
    }

    /**
     * 打开抽汤泵抽汤
     */
    public Result addSoup() {
        //盖子下降——两次命令
        Result result = this.soupSteamCoverDown();
        if (result.getCode() != 200) {
            log.error(result.getMessage());
            return result;
        }
        //抽汤前先打开汤开关，防止水流
        relayOpening(Constants.Y_BOWL_STEAM_SOLENOID_VALVE);
        log.info("打开抽汤泵");
        relayOpening(Constants.Y_SOUP_PUMP_SWITCH);
        return Result.success();
    }

    /**
     * 关闭抽汤泵抽汤
     */
    public Result closeSoup() {
        //抽汤前先打开汤开关，防止水流
        relayClosing(Constants.Y_BOWL_STEAM_SOLENOID_VALVE);
        log.info("关闭抽汤泵");
        relayClosing(Constants.Y_SOUP_PUMP_SWITCH);
        //盖子上升
        Result result = this.soupSteamCoverUp();
        if (result.getCode() != 200) {
            return result;
        }
        return Result.success();
    }

    /**
     * 根据脉冲数量出汤
     */
    @SneakyThrows
    public Result dispenseSoupByPulseCount(int num) {
        log.info("要出水脉冲值：{}", num);
        pubConfig.setFlowmeterPulseCount(0);
        addSoup();
        while (pubConfig.getFlowmeterPulseCount() < num) {
            Thread.sleep(Constants.COMMAND_INTERVAL_POLLING_TIME);
        }
        closeSoup();
        return Result.success();
    }

    /**
     * 蒸汽打开
     *
     * @return
     */
    public Result steamOpen() {
        log.info("蒸汽打开");
        relayOpening(Constants.Y_STEAM_SWITCH);
        return Result.success();
    }

    /**
     * 蒸汽关闭
     *
     * @return
     */
    public Result steamClose() {
        log.info("蒸汽关闭");
        relayClosing(Constants.Y_STEAM_SWITCH);
        return Result.success();
    }

    /**
     * 打开震动器
     */
    public Result openVibrator() {
        relayOpening(Constants.Y_SHAKER_SWITCH_1);
        return Result.success();
    }

    /**
     * 关闭震动器
     */
    public Result closeVibrator() {
        relayClosing(Constants.Y_SHAKER_SWITCH_1);
        return Result.success();
    }

    /**
     * 打开风扇
     *
     * @return
     */
    public Result rearFanOpen() {
        relayOpening(Constants.REAR_BOX_FAN);
        return Result.success();
    }

    /**
     * 风扇打开多少秒后关闭
     *
     * @param number
     * @return
     */
    public Result rearFanOpenClose(int number) {
        openClose(Constants.REAR_BOX_FAN, number);
        return Result.success();
    }

    /**
     * 关闭风扇
     *
     * @return
     */
    public Result rearFanClose() {
        relayClosing(Constants.REAR_BOX_FAN);
        return Result.success();
    }

    /**
     * 震动器测试1（秒）
     *
     * @param number
     * @return
     */
    public Result vibrator1Test(Integer number) {
        openClose(Constants.Y_SHAKER_SWITCH_1, number);
        return Result.success();
    }

    /**
     * 震动器测试2(秒）
     *
     * @param number
     * @return
     */
    public Result<Object> vibrator2Test(Integer number) {
        openClose(Constants.Y_SHAKER_SWITCH_2, number);
        return Result.success();
    }


    /**
     * 碗蒸汽加热（秒）
     *
     * @return
     */
    public Result SteamAdd(int number) {
        pubConfig.setFinishAddingSteam(false);
        //盖子方向向下
        Result result = lowerSteamCover();
        if (result.getCode() != 200) {
            return result;
        }
        //加蒸汽
        openClose(Constants.Y_BATCHING_STEAM_SOLENOID_VALVE, number);
        //加蒸汽完成后
        try {
            Thread.sleep((number + 2) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //盖子方向向上
        result = this.soupSteamCoverUp();
        if (result.getCode() != 200) {
            return result;
        }
        pubConfig.setFinishAddingSteam(true);
        return Result.success();
    }


    /**
     * 抽汤排气
     *
     * @param second 时间
     * @return
     */
    public Result soupPipeExhaust(Integer second) {
        //汤管开关关闭
        openClose(Constants.Y_SOUP_SWITCH, second);
        //抽汤泵打开
        openClose(Constants.Y_SOUP_PUMP_SWITCH, second);
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.success();
    }

    public Result deliverBowl() {
        if (signalAcquisitionDeviceGatewayService.getStatus(Constants.X_BOWL_PRESENT_SIGNAL) == SignalLevel.LOW.ordinal()
                && signalAcquisitionDeviceGatewayService.getStatus(Constants.X_PLACE_BOWL_SIGNAL) == SignalLevel.LOW.ordinal()) {
            log.error("没有碗了！");
            return Result.error("没有碗了！");
        }
        relayOpening(Constants.Y_CHU_WAN);
        return Result.success();
    }

    public void chuWanStop() {
        relayClosing(Constants.Y_CHU_WAN);
    }


    /**
     * 加蒸汽盖下降
     *
     * @return
     */
    public Result lowerSteamCover() {
        pubConfig.setAddSteam(true);
        relayClosing(Constants.Y_TELESCOPIC_ROD_DIRECTION_CONTROL);
        //盖子开关通电
        relayOpening(Constants.Y_TELESCOPIC_ROD_SWITCH_CONTROL);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //停止靠io传感器信号变化停止
        return Result.success();
    }

    /**
     * 加汤蒸盖下降
     *
     * @return
     */
    public Result soupSteamCoverDown() {
        if (signalAcquisitionDeviceGatewayService.getStatus(Constants.X_SOUP_RIGHT_LIMIT) == SignalLevel.HIGH.ordinal()
                || signalAcquisitionDeviceGatewayService.getStatus(Constants.X_SOUP_ORIGIN) == SignalLevel.HIGH.ordinal()
        ) {
            return Result.error("菜勺没在装菜位置上，加汤蒸盖无法下降！");
        }
        pubConfig.setAddSteam(false);
        relayClosing(Constants.Y_TELESCOPIC_ROD_DIRECTION_CONTROL);
        //盖子开关通电
        relayOpening(Constants.Y_TELESCOPIC_ROD_SWITCH_CONTROL);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //停止靠io传感器信号变化停止
        return Result.success();
    }

    /**
     * 加汤蒸汤盖上升
     *
     * @return
     */
    public Result soupSteamCoverUp() {

        relayOpening(Constants.Y_TELESCOPIC_ROD_DIRECTION_CONTROL);
        //盖子开关通电
        relayOpening(Constants.Y_TELESCOPIC_ROD_SWITCH_CONTROL);
        //无需要停止
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.success();
    }

    /**
     * 加蒸汽和汤
     *
     * @return
     */
    public Result steamAndSoupAdd() {
        //盖子方向向下
        this.soupSteamCoverDown();
        //加蒸汽
        openClose(Constants.Y_BATCHING_STEAM_SOLENOID_VALVE, dataConfig.getSteamAdditionTimeSeconds());
        //加蒸汽完成后
        try {
            Thread.sleep(dataConfig.getSteamAdditionTimeSeconds() * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //抽汤前先打开汤开关，防止水流
        openClose(Constants.Y_BOWL_STEAM_SOLENOID_VALVE, dataConfig.getSoupExtractionTime());
        openClose(Constants.Y_SOUP_PUMP_SWITCH, dataConfig.getSoupExtractionTime());
        //加蒸汽完成后
        try {
            Thread.sleep(dataConfig.getSoupExtractionTime() * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //盖子上升
        return this.soupSteamCoverUp();
    }

    /**
     * 切肉机切肉（份量）
     *
     * @param price 价格——10、15、20、30元
     * @return
     */
    public Result meatSlicingMachine(int price) {
        pubConfig.setDishesAreReady(false);
        //设置切肉初始值为0
        pubConfig.setMeatSlicingQuantity(0);
        //打开切肉机
        int number = portionOptionsConfig.findQuantityByPrice(price);
        relayOpening(Constants.Y_MEAT_SLICER_CONTROL);
        //切刀量大于等于数据时关闭切肉机
        while (pubConfig.getMeatSlicingQuantity() < number) {
            try {
                Thread.sleep(Constants.COMMAND_INTERVAL_POLLING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //关闭切肉机
        relayClosing(Constants.Y_MEAT_SLICER_CONTROL);
        pubConfig.setDishesAreReady(true);
        return Result.success();
    }

    /**
     * 震动料开关打开
     *
     * @return
     */
    public Result vibrationSwitchOn() {
        relayClosing(Constants.Y_VIBRATION_SWITCH_DIRECTION_CONTROL);
        relayOpening(Constants.Y_VIBRATION_SWITCH_CONTROL);
        return Result.success();
    }

    /**
     * 震动料开关关闭
     *
     * @return
     */
    public Result vibrationSwitchOff() {
        relayOpening(Constants.Y_VIBRATION_SWITCH_DIRECTION_CONTROL);
        relayOpening(Constants.Y_VIBRATION_SWITCH_CONTROL);
        return Result.success();
    }


    /**
     * 打开出餐口
     *
     * @return
     */
    public Result openPickUpCounter() {
        log.info("打开出餐口");
        openClose(Constants.Y_PICK_UP_COUNTER_DIRECTION_CONTROL, 30);
        openClose(Constants.Y_PICK_UP_COUNTER, 30);
        return Result.success();
    }

    /**
     * 关闭出餐口
     *
     * @return
     */
    public Result closePickUpCounter() {
        log.info("关闭出餐口");
        relayClosing(Constants.Y_VIBRATION_SWITCH_DIRECTION_CONTROL);
        openClose(Constants.Y_PICK_UP_COUNTER, 30);
        return Result.success();
    }
}
