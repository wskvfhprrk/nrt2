package com.jc.service.impl;

import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.enums.SignalLevel;
import com.jc.service.DeviceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 碗控制服务实现类，负责碗的升降操作和状态检查
 */
@Service
@Slf4j
public class BowlService implements DeviceHandler {

    private final IODeviceService ioDeviceService;
    private final RelayDeviceService relayDeviceService;
    private final PubConfig pubConfig;

    public BowlService(IODeviceService ioDeviceService, RelayDeviceService relayDeviceService, PubConfig pubConfig) {
        this.ioDeviceService = ioDeviceService;
        this.relayDeviceService = relayDeviceService;
        this.pubConfig = pubConfig;
    }

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
     * 重置碗
     */
    public void bowlReset() {
        // 获取传感器状态
        String ioStatus = ioDeviceService.getStatus();
        // 解析传感器状态字符串
        String[] split = ioStatus.split(",");
        boolean bowlSensor = split[Constants.AUTO_BOWL_LIFT_POSITION_SENSOR].equals(SignalLevel.HIGH.getValue()); // 碗传感器状态
        boolean lowerLimit = split[Constants.BOWL_LOWER_LIMIT_SENSOR].equals(SignalLevel.HIGH.getValue()); // 轨道最低极限点状态
        boolean upperLimit = split[Constants.BOWL_UPPER_LIMIT_SENSOR].equals(SignalLevel.HIGH.getValue()); // 轨道最高极限点状态

        //如果到达最高位，碗传感器又最高
        if (!bowlSensor && upperLimit) {
            log.info("没有碗了！");
            return;
        }
        // 如果2为高电平4为低电平，直接降碗
        if (bowlSensor && !lowerLimit) {
            this.bowlDescent();
            // 等待传感器2变为高电平，最多等待30秒
            while (bowlSensor) {
                try {
                    Thread.sleep(Constants.SLEEP_TIME_MS);
                    ioStatus = ioDeviceService.getStatus();
                    split = ioStatus.split(",");
                    bowlSensor = split[Constants.AUTO_BOWL_LIFT_POSITION_SENSOR].equals(SignalLevel.HIGH.getValue());
                    if (!bowlSensor) {
                        relayDeviceService.stopBowl();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info("碗已经降到位！");
            pubConfig.setIsResetBowl(true);
            return;
        }
        // 如果2、4传感器都为低电平，直接升碗
        if (!bowlSensor && !upperLimit) {
            this.bowlRising();
            // 等待传感器2变为高电平，最多等待30秒
            while (!bowlSensor) {
                try {
                    Thread.sleep(Constants.SLEEP_TIME_MS);
                    ioStatus = ioDeviceService.getStatus();
                    split = ioStatus.split(",");
                    bowlSensor = split[Constants.AUTO_BOWL_LIFT_POSITION_SENSOR].equals(SignalLevel.HIGH.getValue());
                    if (bowlSensor) {
                        relayDeviceService.stopBowl();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //碗重置状态更改
            pubConfig.setIsResetBowl(true);
            log.info("碗已经升到位！");
            return;
        }
        // 如果传感器2为低电平，说明碗还未升到位
        log.error("碗未升到位，请检查传感器！");
    }

    /**
     * 连续出碗检查方法，用于检测碗是否已经升到位，并在需要时进行降碗操作直到碗低出传感器为止。
     */
    public void continuousBowlCheck() {
        // 获取传感器状态
        String ioStatus = ioDeviceService.getStatus();

        // 解析传感器状态字符串
        String[] split = ioStatus.split(",");
        boolean bowlSensor = split[Constants.AUTO_BOWL_LIFT_POSITION_SENSOR].equals(SignalLevel.HIGH.getValue()); // 碗传感器状态
        boolean upperLimit = split[Constants.BOWL_UPPER_LIMIT_SENSOR].equals(SignalLevel.HIGH.getValue()); // 轨道最高极限点状态
        //如果传感器无值到达了上限——没有碗了
        if (!bowlSensor && upperLimit) {
            log.error("没有碗了！");
            return;
        }
        if (!bowlSensor && !upperLimit) {
            this.bowlRising();
            // 等待传感器2变为高电平，最多等待30秒
            int count = 0;
            while (!bowlSensor) {
                try {
                    Thread.sleep(Constants.SLEEP_TIME_MS);
                    count++;
                    ioStatus = ioDeviceService.getStatus();
                    split = ioStatus.split(",");
                    bowlSensor = split[Constants.AUTO_BOWL_LIFT_POSITION_SENSOR].equals(SignalLevel.HIGH.getValue());
                    if (bowlSensor) {
                        relayDeviceService.stopBowl();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info("碗已经升到位！");
        }
    }

    public Result bowlRising() {
        //先开关管方向
        relayDeviceService.bowlRise();
        //再打开开关
        relayDeviceService.openBowl();
        return Result.success();
    }

    public Result bowlDescent() {
        //先开关管方向
        relayDeviceService.bowlDrop();
        //再打开开关
        relayDeviceService.openBowl();
        return Result.success();
    }

}
