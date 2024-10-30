package com.jc.service.impl;

import com.jc.config.BeefConfig;
import com.jc.config.IpConfig;
import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.enums.SignalLevel;
import com.jc.netty.server.NettyServerHandler;
import com.jc.service.DeviceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 继电器设备处理类
 * 实现了DeviceHandler接口，提供了继电器的打开、关闭及定时关闭功能
 */
@Service
@Slf4j
public class RelayDeviceService implements DeviceHandler {
    @Autowired
    private NettyServerHandler nettyServerHandler;
    @Autowired
    private PubConfig pubConfig;
    @Autowired
    private IpConfig ipConfig;
    @Autowired
    private BeefConfig beefConfig;
    @Autowired
    private IODeviceService ioDeviceService;

    /**
     * 处理消息
     *
     * @param message 消息内容
     * @param isHex   是否为16进制消息
     */
    @Override
    public void handle(String message, boolean isHex) {
        if (isHex) {
//            log.info("继电器设备——HEX消息: {}", message);
        } else {
            log.info("继电器设备——普通消息: {}", message);
            // 在这里添加处理普通字符串消息的逻辑
        }
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
        nettyServerHandler.sendMessageToClient(ipConfig.getRelay(), sb.toString(), true);
    }

    /**
     * 根据编号关闭继电器
     *
     * @param no 继电器编号，范围为1-32
     */
    public Result relayClosing(int no) {
        if (no <= 0 || no > 32) {
            log.error("编号{}继电器不存在！", no);
            return Result.error(500, "编号继电器不存在"); // 添加return，防止继续执行
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
        nettyServerHandler.sendMessageToClient(ipConfig.getRelay(), sb.toString(), true);
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
            return Result.error(500, "编号继电器不存在"); // 添加return，防止继续执行
        }
        if (second <= 0 || second > 177777) {
            log.error("时间值不能限定", second);
            return Result.error(500, "时间值不能限定"); // 添加return，防止继续执行
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
        nettyServerHandler.sendMessageToClient(ipConfig.getRelay(), sb.toString(), true);
        return Result.success();
    }

    /**
     * 关闭所有继电器
     */
    public Result closeAll() {
        log.info("关闭所有继电器");
        // 发送关闭所有继电器的指令
        nettyServerHandler.sendMessageToClient(ipConfig.getRelay(), "48 3A 01 57 00 00 00 00 00 00 00 00 DA 45 44", true);
        steamOpen();
        return Result.success();
    }

    /**
     * 打开所有继电器
     */
    public Result openAll() {
        log.info("打开所有继电器");
        // 发送打开所有继电器的指令
        nettyServerHandler.sendMessageToClient(ipConfig.getRelay(), "48 3A 01 57 55 55 55 55 55 55 55 55 82 45 44", true);
        return Result.success();
    }

    /**
     * 出餐口向下
     *
     * @return
     */
    public Result theFoodOutletIsFacingDownwards() {
        log.info("出餐口向下");
        relayClosing(Constants.THE_FOOD_OUTLET_IS_FACING_UPWARDS_SWITCH);
        openClose(Constants.THE_FOOD_OUTLET_IS_FACING_DOWNWARDS_SWITCH, 15);
        this.coverClosed();
        return Result.success();
    }

    /**
     * 出餐口向上
     *
     * @return
     */
    public Result theFoodOutletIsFacingUpwards() {
        log.info("出餐口向上");
        //先打开盖板
        this.coverOpen();
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        relayClosing(Constants.THE_FOOD_OUTLET_IS_FACING_DOWNWARDS_SWITCH);
        openClose(Constants.THE_FOOD_OUTLET_IS_FACING_UPWARDS_SWITCH, 15);
        return Result.success();
    }

    /**
     * 出料开仓出料
     *
     * @return
     */
    public String coverOpen() {
        log.info("出料开仓出料");
        relayClosing(Constants.DISCHARGING_IS_PROHIBITED_AFTER_CLOSING_THE_WAREHOUSE_SWITCH);
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        openClose(Constants.DISCHARGING_FROM_WAREHOUSE_SWITCH, 15);
        return "ok";
    }

    /**
     * 出料关仓禁止出料
     *
     * @return
     */
    public String coverClosed() {
        log.info("出料关仓禁止出料");
        relayClosing(Constants.DISCHARGING_FROM_WAREHOUSE_SWITCH);
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        openClose(Constants.DISCHARGING_IS_PROHIBITED_AFTER_CLOSING_THE_WAREHOUSE_SWITCH, 15);
        return "ok";
    }

    /**
     * 停止碗开关
     */
    public void stopBowl() {
        log.info("停止碗开关");
        relayClosing(Constants.BOWL_L_SWITCH);
    }

    /**
     * 打碗开关
     */
    public void openBowl() {
        log.info("打碗开关");
        relayOpening(Constants.BOWL_L_SWITCH);
    }

    /**
     * 碗上升
     */
    public void bowlRise() {
        log.info("碗上升");
        relayOpening(Constants.BOWL_N_SWITCH);
    }

    /**
     * 碗下降
     */
    public void bowlDrop() {
        log.info("碗下降");
        relayClosing(Constants.BOWL_N_SWITCH);
    }

    /**
     * 打开抽汤泵到液位后多少秒关闭——至少1秒
     *
     * @param seconds 多少秒关闭
     */
    public Result soupAdd(int seconds) {
        //盖子下降
        Result result = this.soupSteamCoverDown();
        if (result.getCode() == 500) {
            return result;
        }
        //抽汤前先打开汤开关，防止水流
        openClose(Constants.Y_BOWL_STEAM_SOLENOID_VALVE, seconds);
        log.info("打开抽汤{}秒钟", seconds);

        openClose(Constants.Y_SOUP_PUMP_SWITCH, seconds);
        //加蒸汽完成后
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //盖子上升
        this.soupSteamCoverUp();
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
        openClose(Constants.Y_SHAKER_SWITCH_1, 5);
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
     * 出料仓3测试
     *
     * @param number
     * @return
     */
    public Result DischargeBin3Test(Integer number) {
        openClose(Constants.Y_DISCHARGE_BIN_3, number);
        return Result.success();
    }

    /**
     * 汤加热多少（度）
     *
     * @param number
     * @return
     */
    public Result soupHeatTo(Integer number) {
        //关闭汤开关
        soupSteamValveClose();
        //初始化温度
        pubConfig.setSoupTemperatureValue(0.0);
        //发送查询温度指令
        Boolean flag = true;
        while (flag) {
            //发送温度
            readTemperature();
            Double soupTemperatureValue = pubConfig.getSoupTemperatureValue();
//            if (soupTemperatureValue >= beefConfig.getSoupHeatingTemperature()) {
//                pubConfig.setIsAddingBowlCompleted(true);
//            }
            if (soupTemperatureValue >= number) {
                flag = false;
                relayClosing(Constants.Y_SOUP_STEAM_SOLENOID_VALVE);
            } else {
                relayOpening(Constants.Y_SOUP_STEAM_SOLENOID_VALVE);
            }

        }
        return Result.success();
    }

    /**
     * 读取汤的温度值
     *
     * @return
     */
    public Result readTemperature() {
        nettyServerHandler.sendMessageToClient(ipConfig.getReceive485Signal(), Constants.READ_SOUP_TEMPERATURE_COMMAND, true);
        try {
            Thread.sleep(Constants.SLEEP_TIME_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.success(pubConfig.getSoupTemperatureValue());
    }

    /**
     * 配菜称重盒打开（编号）
     *
     * @param number
     * @return
     */
    public Result openWeighingBox(Integer number) throws InterruptedException {
        int i = 0;
        switch (number) {
            case 1:
                relayOpening(Constants.SIDE_DISH_WEIGHING_BOX_SWITCH1_1);
                Thread.sleep(Constants.SLEEP_TIME_MS);
                relayOpening(Constants.SIDE_DISH_WEIGHING_BOX_SWITCH1_2);
                break;
            case 2:
                relayOpening(Constants.SIDE_DISH_WEIGHING_BOX_SWITCH2_1);
                Thread.sleep(Constants.SLEEP_TIME_MS);
                relayOpening(Constants.SIDE_DISH_WEIGHING_BOX_SWITCH2_2);
                break;
            case 3:
                relayOpening(Constants.SIDE_DISH_WEIGHING_BOX_SWITCH3_1);
                Thread.sleep(Constants.SLEEP_TIME_MS);
                relayOpening(Constants.SIDE_DISH_WEIGHING_BOX_SWITCH3_2);
                break;
            default:
        }
        return Result.success();
    }

    /**
     * 配菜称重盒关闭（编号）
     *
     * @param number
     * @return
     */
    public Result closeWeighingBox(Integer number) throws InterruptedException {
        int i = 0;
        switch (number) {
            case 1:
                relayClosing(Constants.SIDE_DISH_WEIGHING_BOX_SWITCH1_1);
                Thread.sleep(Constants.SLEEP_TIME_MS);
                relayClosing(Constants.SIDE_DISH_WEIGHING_BOX_SWITCH1_2);
                break;
            case 2:
                relayClosing(Constants.SIDE_DISH_WEIGHING_BOX_SWITCH2_1);
                Thread.sleep(Constants.SLEEP_TIME_MS);
                relayClosing(Constants.SIDE_DISH_WEIGHING_BOX_SWITCH2_2);
                break;
            case 3:
                relayClosing(Constants.SIDE_DISH_WEIGHING_BOX_SWITCH3_1);
                Thread.sleep(Constants.SLEEP_TIME_MS);
                relayClosing(Constants.SIDE_DISH_WEIGHING_BOX_SWITCH3_2);
                break;
            default:
        }
        return Result.success();
    }

    /**
     * 配菜称重盒关闭打开（编号）
     *
     * @param number
     * @return
     */
    public Result vegetableMotor(Integer number) {
        int i = 0;
        switch (number) {
            case 1:
                i = Constants.INGREDIENT_MOTOR1;
                break;
            case 2:
                i = Constants.INGREDIENT_MOTOR2;
                break;
            case 3:
                i = Constants.INGREDIENT_MOTOR3;
                break;
            case 4:
                i = Constants.INGREDIENT_MOTOR4;
                break;
            default:

        }
        relayOpening(i);
        return Result.success();
    }

    /**
     * 配菜称重盒关闭关闭（编号）
     *
     * @param number
     */
    public Result vegetableMotorStop(Integer number) {
        int i = 0;
        switch (number) {
            case 1:
                i = Constants.INGREDIENT_MOTOR1;
                break;
            case 2:
                i = Constants.INGREDIENT_MOTOR2;
                break;
            case 3:
                i = Constants.INGREDIENT_MOTOR3;
                break;
            case 4:
                i = Constants.INGREDIENT_MOTOR4;
                break;
            default:

        }
        relayClosing(i);
        return Result.success();
    }

    /**
     * 配菜电机（KG）
     *
     * @param i
     * @param number
     * @return
     */
    public Result vegetable1Motor(int i, Integer number) throws InterruptedException {
        //清零
        //02 06 00 26 00 01 A9 F2
//        nettyServerHandler.sendMessageToClient(ipConfig.getReceive485Signal(), Constants.ZEROING_CALIBRATION, true);
//        Thread.sleep(Constants.SLEEP_TIME_MS);
        vegetableMotor(i - 1);
        //查看是否够重量
        Boolean flag = true;
        while (flag) {
            //发送称重指令
            nettyServerHandler.sendMessageToClient(ipConfig.getReceive485Signal(), Constants.READ_WEIGHT_VALUE, true);
            if (pubConfig.getCalculateWeight().length > 0 && pubConfig.getCalculateWeight()[i - 1] >= number) {
                flag = false;
            }
        }
        vegetableMotorStop(i);
        return Result.success();
    }


    /**
     * 碗蒸汽加热（秒）
     *
     * @return
     */
    public Result bowlSteamAdd(int number) {
        //盖子方向向下
        this.soupSteamCoverDown();
        //加蒸汽
        openClose(Constants.Y_BOWL_STEAM_SOLENOID_VALVE, number);
        openClose(Constants.Y_BATCHING_STEAM_SOLENOID_VALVE, number);
        //加蒸汽完成后
        try {
            Thread.sleep(number * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //盖子方向向上
        this.soupSteamCoverUp();
        return Result.success();
    }


    /**
     * 汤开关关
     *
     * @return
     */
    public Result soupSteamValveClose() {
        relayClosing(Constants.Y_SOUP_SWITCH);
        return Result.success();
    }

    /**
     * 打开蒸汽发生器
     */
    public Result openSteamGenerator() {
        relayOpening(Constants.Y_STEAM_SWITCH);
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
        //抽汤泵打开+2秒
        openClose(Constants.Y_SOUP_PUMP_SWITCH, second + 2);

        return Result.success();
    }

    public Result deliverBowl() {
        if (ioDeviceService.getStatus(Constants.X_BOWL_PRESENT_SIGNAL) == SignalLevel.LOW.ordinal()) {
            log.error("没有碗了！");
            return Result.error(500, "没有碗了！");
        }
        relayOpening(Constants.Y_CHU_WAN);
        return Result.success();
    }

    public void chuWanStop() {
        relayClosing(Constants.Y_CHU_WAN);
    }


    public Result foodOutletReset() {
        return Result.success();
    }

    public Result foodOutletDeliver() {
        return Result.success();
    }

    /**
     * 一号料仓打开
     *
     * @return
     */
    public Result firstBinOpen() {
        //控制方向
        relayOpening(Constants.Y_FIRST_BIN_DIRECTION_CONTROL);
        //控制电源
        relayOpening(Constants.Y_HOPPER1_SWITCH_CONTROL);

        return Result.success();
    }

    public Result firstBinClose() {
        //控制方向
        relayClosing(Constants.Y_FIRST_BIN_DIRECTION_CONTROL);
        //控制电源
        openClose(Constants.Y_HOPPER1_SWITCH_CONTROL, 2);
        return Result.success();
    }

    public Result secondBinOpen() {
        //控制方向
        relayOpening(Constants.Y_SECOND_BIN_DIRECTION_CONTROL);
        //控制电源
        relayOpening(Constants.Y_HOPPER2_SWITCH_CONTROL);
        return Result.success();
    }

    public Result secondBinClose() {
        //控制方向
        relayClosing(Constants.Y_SECOND_BIN_DIRECTION_CONTROL);
        //控制电源
        openClose(Constants.Y_HOPPER2_SWITCH_CONTROL, 2);
        return Result.success();
    }

    public Result thirdBinOpen() {
        //控制方向
        relayOpening(Constants.Y_THIRD_BIN_DIRECTION_CONTROL);
        //控制电源
        relayOpening(Constants.Y_HOPPER3_SWITCH_CONTROL);
        return Result.success();
    }

    public Result thirdBinClose() {
        //控制方向
        relayClosing(Constants.Y_THIRD_BIN_DIRECTION_CONTROL);
        //控制电源
        openClose(Constants.Y_HOPPER3_SWITCH_CONTROL, 2);
        return Result.success();
    }

    /**
     * 加汤蒸汤盖下降
     *
     * @return
     */
    public Result soupSteamCoverDown() {
        if (ioDeviceService.getStatus(Constants.X_SOUP_RIGHT_LIMIT) == SignalLevel.HIGH.ordinal()) {
            return Result.error(500, "菜勺还在倒菜位置上！");
        }
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

        openClose(Constants.Y_TELESCOPIC_ROD_DIRECTION_CONTROL, 9);
        //盖子开关通电
        openClose(Constants.Y_TELESCOPIC_ROD_SWITCH_CONTROL, 9);
        //无需要停止
        return Result.success();
    }
}
