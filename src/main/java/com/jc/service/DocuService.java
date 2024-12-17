package com.jc.service;

import com.jc.config.PubConfig;
import com.jc.constants.Constants;
import com.jc.enums.SignalLevel;
import com.jc.service.impl.SignalAcquisitionDeviceGatewayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DocuService implements DeviceHandler {

    @Autowired
    private PubConfig pubConfig;
    @Autowired
    private SignalAcquisitionDeviceGatewayService signalAcquisitionDeviceGatewayService;

    /**
     * 处理消息
     *
     * @param message 消息内容
     * @param isHex   是否为16进制消息
     */
    @Override
    public void handle(String message, boolean isHex) {
        if (isHex) {
            log.info("机器人HEX消息: {}", message);
            //判断是否到了home点
            isHome(message);
        } else {
            log.info("普通消息: {}", message);
        }
    }

    private void isHome(String message) {
        if (message.replaceAll(" ", "").equals(Constants.ROBOT_HOME)) {
            pubConfig.setIsRobotStatus(true);
        }
        //配菜完成
        if (message.replaceAll(" ", "").equals(Constants.PEI_CAI)) {
            pubConfig.setSideDishesCompleted(true);
        }
        //出餐完成
        if (message.replaceAll(" ", "").equals(Constants.SERVING_COMPLETED)) {
            pubConfig.setIsServingCompleted(true);
        }
        //机器人获取碗指令
        if (message.replaceAll(" ", "").equals(Constants.GET_BOWL)) {
            pubConfig.setGetBowl(true);
        }
        // 拿篮子
        if (message.replaceAll(" ", "").equals(Constants.NA_LAN)) {
            pubConfig.setNaLan(true);
        }
        // 放牛肉
        if (message.replaceAll(" ", "").equals(Constants.PUT_BEEF)) {
            pubConfig.setPutBeef(true);
        }
        // 放粉丝
        if (message.replaceAll(" ", "").equals(Constants.FENS)) {
            pubConfig.setFens(true);
        }
        // 取碗
        if (message.replaceAll(" ", "").equals(Constants.CHECK_BOWL)) {
            pubConfig.setCheckBowl(true);
        }
        // 放碗
        if (message.replaceAll(" ", "").equals(Constants.PUT_BOWL)) {
            pubConfig.setPutBowl(true);
        }
        // 出餐
        if (message.replaceAll(" ", "").equals(Constants.PICK_UP_SOUP_BOWL)) {
            pubConfig.setPickUpSoupBowl(true);
        }
    }
}
