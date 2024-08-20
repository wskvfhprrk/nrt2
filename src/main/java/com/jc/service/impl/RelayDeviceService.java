package com.jc.service.impl;

import com.jc.config.IpConfig;
import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.netty.server.NettyServerHandler;
import com.jc.service.DeviceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    /**
     * 处理消息
     *
     * @param message 消息内容
     * @param isHex   是否为16进制消息
     */
    @Override
    public void handle(String message, boolean isHex) {
        if (isHex) {
            log.info("继电器设备——HEX消息: {}", message);
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
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        openClose(Constants.THE_FOOD_OUTLET_IS_FACING_DOWNWARDS_SWITCH, 15);
        //出完后盖板盖上
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
     * 蒸汽测试,打开5秒后关闭
     *
     * @param number
     */
    public void steam(int number) {
        log.info("蒸汽测试,打开{}}秒后关闭", number);
        //继电器7打开15秒关闭
        this.openClose(Constants.STEAM_SWITCH, number);
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


    // 创建一个固定线程池
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public void startSoupPump(int seconds) {
        executorService.submit(() -> soupPump(seconds));
    }

    /**
     * 打开抽汤泵到液位后多少秒关闭——至少1秒
     *
     * @param seconds 多少秒关闭
     */
    public void soupPump(int seconds) {
        log.info("打开抽汤泵，保持{}秒钟时间", seconds);
        openClose(Constants.SOUP_PUMP_SWITCH, seconds);
        // 打开抽汤泵
//        relayOpening(Constants.SOUP_PUMP_SWITCH);
//
//        // 最少是1秒钟阻隔时间
//        if (seconds <= 0) {
//            seconds = 1;
//        }
//
//        // 将秒转换为毫秒
//        int milliseconds = seconds * 1000;
//
//        // 标志位，表示是否继续等待
//        Boolean flag = true;
//
//        while (flag) {
//            try {
//                // 等待指定时间
//                Thread.sleep(milliseconds);
//
//                // 每次等待之后重新检查调料是否完成添加
//                String status = ioDeviceService.getStatus();
//                String[] split = status.split(",");
//                if (split[Constants.SOUP_LEVEL_SENSOR].equals(SignalLevel.HIGH.getValue())) {
//                    flag = false;
//                }
//            } catch (InterruptedException e) {
//                // 处理异常
//                Thread.currentThread().interrupt();
//                log.error("线程被中断", e);
//                break;
//            }
//        }
//
//        // 关闭抽汤泵
//        relayClosing(Constants.SOUP_PUMP_SWITCH);
    }

//    // 优雅地关闭线程池
//    public void shutdown() {
//        try {
//            executorService.shutdown();
//            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
//                executorService.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            executorService.shutdownNow();
//            Thread.currentThread().interrupt();
//        }
//    }

    /**
     * 蒸汽打开
     */
    public void steamOpen() {
        log.info("蒸汽打开");
        relayOpening(Constants.STEAM_SWITCH);
    }

    /**
     * 蒸汽关闭
     */
    public void steamClose() {
        log.info("蒸汽关闭");
        relayClosing(Constants.STEAM_SWITCH);
    }

    /**
     * 打开震动器
     */
    public Result openVibrator() {
        openClose(Constants.SHAKER_SWITCH, 5);
        return Result.success();
    }

    /**
     * 打开风扇
     * @return
     */
    public Result rearFanOpen() {
        relayOpening(Constants.REAR_BOX_FAN);
        return Result.success();
    }

    /**
     * 关闭风扇
     * @return
     */
    public Result rearFanClose() {
        relayClosing(Constants.REAR_BOX_FAN);
        return Result.success();
    }

    /**
     * 震动器测试（秒）
     * @param number
     * @return
     */
    public Result vibratorTest(Integer number) {
        openClose(Constants.SHAKER_SWITCH, number);
        return Result.success();
    }

    public Result heatSoupToTemperature(Integer number) {
        pubConfig.setSoupTemperatureValue(0.0);
        //发送查询温度指令
        Boolean flag = true;
        while (flag) {
            //发送温度
            nettyServerHandler.sendMessageToClient(ipConfig.getReceive485Signal(), Constants.READ_SOUP_TEMPERATURE_COMMAND, true);
            try {
                Thread.sleep(Constants.SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (pubConfig.getSoupTemperatureValue() >= number) {
                flag = false;
                relayClosing(Constants.SOUP_STEAM_SOLENOID_VALVE);
            } else {
                relayOpening(Constants.SOUP_STEAM_SOLENOID_VALVE);
            }

        }
        return Result.success();
    }

    /**
     * 弹簧货道（编号）
     *
     * @param number
     * @return
     */
    public Result springChannel(Integer number) {
        int i = 0;
        switch (number) {
            case 1:
                i = Constants.SPRING_TRACK_MOTOR1;
                break;
            case 2:
                i = Constants.SPRING_TRACK_MOTOR2;
                break;
            case 3:
                i = Constants.SPRING_TRACK_MOTOR3;
                break;
            case 4:
                i = Constants.SPRING_TRACK_MOTOR4;
                break;
            case 5:
                i = Constants.SPRING_TRACK_MOTOR5;
                break;
            default:

        }
        // 货道通电2秒，让货道自动转一圈
        openClose(i, Constants.GOODS_AISLE_POWER_ON2_SECONDS);
        return Result.success();
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
            case 4:
                relayOpening(Constants.SIDE_DISH_WEIGHING_BOX_SWITCH4_1);
                Thread.sleep(Constants.SLEEP_TIME_MS);
                relayOpening(Constants.SIDE_DISH_WEIGHING_BOX_SWITCH4_2);
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
            case 4:
                relayClosing(Constants.SIDE_DISH_WEIGHING_BOX_SWITCH4_1);
                Thread.sleep(Constants.SLEEP_TIME_MS);
                relayClosing(Constants.SIDE_DISH_WEIGHING_BOX_SWITCH4_2);
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
    public Result vegetableMotorInKg(int i, Integer number) {
        vegetableMotor(i);
        //查看是否够重量
        Boolean flag = true;
        while (flag) {
            //发送称重指令
            nettyServerHandler.sendMessageToClient(ipConfig.getReceive485Signal(), Constants.READ_WEIGHT_VALUE, true);
            if (pubConfig.getCalculateWeight().length > 0 && pubConfig.getCalculateWeight()[i-1] >= number) {
                flag = false;
            }
            try {
                Thread.sleep(Constants.SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        vegetableMotorStop(i);
        return Result.success();
    }

    /**
     * 碗蒸汽杆向上——电推杆
     */
    public Result bowlSteamRodUp() {
        relayClosing(Constants.STEAM_COVER_DESCEND);
        try {
            Thread.sleep(Constants.SLEEP_TIME_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        relayClosing(Constants.STEAM_COVER_RISE);
        return Result.success();
    }

    /**
     * 碗蒸汽杆向下——电推杆
     *
     * @return
     */
    public Result bowlSteamRodDown() {
        relayOpening(Constants.STEAM_COVER_RISE);
        try {
            Thread.sleep(Constants.SLEEP_TIME_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        relayOpening(Constants.STEAM_COVER_DESCEND);
        return Result.success();
    }

    /**
     * @return
     */
    public Result soupSwitchOn() {
        relayOpening(Constants.SOUP_SWITCH);
        return Result.success();
    }

    /**
     * 汤开关关
     *
     * @return
     */
    public Result soupSwitchOff() {
        relayClosing(Constants.SOUP_SWITCH);
        return Result.success();
    }
    /**
     * 配菜电机1转动
     */
}
