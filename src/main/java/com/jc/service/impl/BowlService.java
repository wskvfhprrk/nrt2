package com.jc.service.impl;

import com.jc.config.BeefConfig;
import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.enums.SignalLevel;
import com.jc.service.DeviceHandler;
import com.jc.utils.DecimalToHexConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 碗控制服务实现类，负责碗的升降操作和状态检查
 */
@Service
@Slf4j
public class BowlService implements DeviceHandler {

    @Autowired
    private Send485OrderService send485OrderService;
    @Autowired
    private IODeviceService ioDeviceService;
    @Autowired
    private PubConfig pubConfig;
    @Autowired
    private RelayDeviceService relayDeviceService;
    @Autowired
    private BeefConfig beefConfig;

    /**
     * 处理消息
     *
     * @param message 消息内容
     * @param isHex   是否为16进制消息
     */
    @Override
    public void handle(String message, boolean isHex) {
        if (isHex) {
            log.info("碗控制服务——HEX: {}", message);
        } else {
            log.info("碗控制服务——普通消息: {}", message);
            // 在这里添加处理普通字符串消息的逻辑
        }
    }

    /**
     * 装菜勺复位
     *
     * @return
     */
    public synchronized Result spoonReset() {
        if (ioDeviceService.getStatus(Constants.X_SOUP_RIGHT_LIMIT) == SignalLevel.HIGH.ordinal() &&
                ioDeviceService.getStatus(Constants.X_SOUP_INGREDIENT_SENSOR) == SignalLevel.HIGH.ordinal()
        ) {
            return Result.success();
        }
        //如果没有走完就不返回
        if (ioDeviceService.getStatus(Constants.X_SOUP_RIGHT_LIMIT) == SignalLevel.LOW.ordinal()) {
            //移到倒菜位置
            Result result = servoMotorToSteamPosition();
            if (result.getCode() != 200) {
                return result;
            }
        }
        if(ioDeviceService.getStatus(Constants.X_SOUP_INGREDIENT_SENSOR) == SignalLevel.HIGH.ordinal()){
            return Result.success();
        }
        //先发送脉冲数，再发送指令
        String hex = "030600070480";
        send485OrderService.sendOrder(hex);
        //速度
        hex = "030600050050";
        send485OrderService.sendOrder(hex);
        //先发送脉冲数，再发送指令
        hex = "030600000001";
        send485OrderService.sendOrder(hex);
        //如果转动后还没有在位置上就让其慢转到原点
        if (ioDeviceService.getStatus(Constants.X_SOUP_INGREDIENT_SENSOR) == SignalLevel.LOW.ordinal()) {
            //先发送脉冲数，再发送指令
            hex = "030600070000";
            send485OrderService.sendOrder(hex);
            hex = "03060005010";
            send485OrderService.sendOrder(hex);
            //先发送脉冲数，再发送指令
            hex = "030600000001";
            send485OrderService.sendOrder(hex);
        }
        Long beging = System.currentTimeMillis();
        Boolean flag = false;
        while (ioDeviceService.getStatus(Constants.X_SOUP_INGREDIENT_SENSOR) == SignalLevel.LOW.ordinal()) {
            try {
                Thread.sleep(Constants.SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() - beging >= 60000) {
                flag = true;
                break;
            }
        }
        if (flag) {
            log.error("菜勺超过10分钟还没有复位");
            return Result.error("菜勺超过10分钟还没有复位");
        }
        return Result.success();
    }

    /**
     * 伺服电机移到蒸汽位置
     */
    private synchronized Result servoMotorToSteamPosition() {
        if (ioDeviceService.getStatus(Constants.X_SOUP_RIGHT_LIMIT) == SignalLevel.HIGH.ordinal()) {
            return Result.success();
        } else if (ioDeviceService.getStatus(Constants.X_SOUP_ORIGIN) == SignalLevel.HIGH.ordinal()) {
            //先发送脉冲数，再发送指令
            String hex = "02060007" + DecimalToHexConverter.decimalToHex(
                    beefConfig.getLadleWalkingDistanceValue() - beefConfig.getLadleDishDumpingDistancePulseValue());
            send485OrderService.sendOrder(hex);
            //速度
            hex = "020600055000";
            send485OrderService.sendOrder(hex);
            //先发送脉冲数，再发送指令
            hex = "020600010001";
            send485OrderService.sendOrder(hex);
        } else if (ioDeviceService.getStatus(Constants.X_SOUP_LEFT_LIMIT) == SignalLevel.HIGH.ordinal()) {
            //先发送脉冲数，再发送指令
            String hex = "02060007" + DecimalToHexConverter.decimalToHex(beefConfig.getLadleWalkingDistanceValue());
            send485OrderService.sendOrder(hex);
            //速度
            hex = "020600055000";
            send485OrderService.sendOrder(hex);
            //先发送脉冲数，再发送指令
            hex = "020600010001";
            send485OrderService.sendOrder(hex);
        }
        Long begin = System.currentTimeMillis();
        boolean flag = false;
        while (ioDeviceService.getStatus(Constants.X_SOUP_RIGHT_LIMIT) == SignalLevel.LOW.ordinal()) {
            try {
                Thread.sleep(Constants.SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() - begin > 180000) {
                flag = true;
                break;
            }
        }
        if (flag) {
            return Result.error("移动加蒸汽位超过3分钟");
        }
        return Result.success();
    }

    /**
     * 伺服电机移到倒菜位置
     */
    private synchronized Result servoMotorMoveToDumpPosition() {
        if (ioDeviceService.getStatus(Constants.X_SOUP_ORIGIN) == SignalLevel.HIGH.ordinal()) {
            return Result.success();
        } else if (ioDeviceService.getStatus(Constants.X_SOUP_RIGHT_LIMIT) == SignalLevel.HIGH.ordinal()) {
            //先发送脉冲数，再发送指令
            String hex = "02060007" + DecimalToHexConverter.decimalToHex(
                    beefConfig.getLadleWalkingDistanceValue() - beefConfig.getLadleDishDumpingDistancePulseValue());
            send485OrderService.sendOrder(hex);
            //速度
            hex = "020600055000";
            send485OrderService.sendOrder(hex);
            //先发送脉冲数，再发送指令
            hex = "020600000001";
            send485OrderService.sendOrder(hex);
        } else if (ioDeviceService.getStatus(Constants.X_SOUP_LEFT_LIMIT) == SignalLevel.HIGH.ordinal()) {
            //先发送脉冲数，再发送指令
            String hex = "02060007" + DecimalToHexConverter.decimalToHex(beefConfig.getLadleDishDumpingDistancePulseValue());
            send485OrderService.sendOrder(hex);
            //速度
            hex = "020600055000";
            send485OrderService.sendOrder(hex);
            //先发送脉冲数，再发送指令
            hex = "020600010001";
            send485OrderService.sendOrder(hex);
        }
        Long begin = System.currentTimeMillis();
        boolean flag = false;
        while (ioDeviceService.getStatus(Constants.X_SOUP_ORIGIN) == SignalLevel.LOW.ordinal()) {
            try {
                Thread.sleep(Constants.SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() - begin > 180000) {
                flag = true;
                break;
            }
        }
        if (flag) {
            return Result.error("移动装菜位超过3分钟");
        }
        return Result.success();
    }

    /**
     * 装菜勺倒菜
     *
     * @return
     */
    public synchronized Result spoonPour() {
        log.info("装菜勺倒菜");
        pubConfig.setServingDishesCompleted(false);
        //如果没有到达位置倒菜勺——汤右限位
        if (ioDeviceService.getStatus(Constants.X_SOUP_ORIGIN) == SignalLevel.LOW.ordinal()) {
            Result result = servoMotorMoveToDumpPosition();
            if (result.getCode() != 200) {
                return result;
            }
        }
        //需要2次向上，不然的话达不到最顶端
        Result result = relayDeviceService.soupSteamCoverUp();
        if (result.getCode() != 200) {
            return result;
        }
        //先发送脉冲数，再发送指令
        String hex = "03060007" + DecimalToHexConverter.decimalToHex(beefConfig.getLadleDishDumpingRotationValue());
        send485OrderService.sendOrder(hex);
        //倒菜时速度
        hex = "030600053000";
        send485OrderService.sendOrder(hex);
        //先发送脉冲数，再发送指令
        hex = "030600010001";
        send485OrderService.sendOrder(hex);
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //复位
        this.spoonReset();
        Long begin = System.currentTimeMillis();
        Boolean flag = false;
        while (ioDeviceService.getStatus(Constants.X_SOUP_INGREDIENT_SENSOR) == SignalLevel.LOW.ordinal()) {
            try {
                Thread.sleep(Constants.SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() - begin > 600000) {
                flag = true;
                break;
            }
        }
        if (flag) {
            log.error("10分钟倒菜未成功能");
            return Result.error("10分钟倒菜未成功能");
        }
        pubConfig.setServingDishesCompleted(true);
        return Result.success();
    }

    /**
     * 装菜勺装菜
     *
     * @return
     */
    public synchronized Result spoonLoad() {
        //如果在装菜位直接返回
        if (ioDeviceService.getStatus(Constants.X_SOUP_LEFT_LIMIT) == SignalLevel.HIGH.ordinal()) {
            log.info("在装菜位");
            return Result.success();
        }
        //如果碗没有复位不行
        if (ioDeviceService.getStatus(Constants.X_SOUP_INGREDIENT_SENSOR) == SignalLevel.LOW.ordinal()) {
            Result result = spoonReset();
            if (result.getCode() != 200) {
                log.error(result.getMessage());
                return result;
            }
        }
        //先发送脉冲数，再发送指令
        log.info("发送装菜指令");
        //速度
        String hex = "020600055000";
        send485OrderService.sendOrder(hex);
        //根据不同位置进行判断
        if (ioDeviceService.getStatus(Constants.X_SOUP_RIGHT_LIMIT) == SignalLevel.HIGH.ordinal()) {
            hex = "02060007" + DecimalToHexConverter.decimalToHex(beefConfig.getLadleWalkingDistanceValue());
        } else if (ioDeviceService.getStatus(Constants.X_SOUP_ORIGIN) == SignalLevel.HIGH.ordinal()) {
            hex = "02060007" + DecimalToHexConverter.decimalToHex(beefConfig.getLadleDishDumpingDistancePulseValue());
        }
        send485OrderService.sendOrder(hex);
        //先发送脉冲数，再发送指令
        hex = "020600000001";
        send485OrderService.sendOrder(hex);
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (ioDeviceService.getStatus(Constants.X_SOUP_RIGHT_LIMIT) == SignalLevel.HIGH.ordinal()) {
            try {
                Thread.sleep(Constants.SLEEP_TIME_MS * 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if ((ioDeviceService.getStatus(Constants.X_SOUP_RIGHT_LIMIT) == SignalLevel.HIGH.ordinal())) {
                spoonLoad();
            }
        }
        return Result.success();
    }
}
