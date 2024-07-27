package com.jc.service;

import com.jc.config.PubConfig;
import com.jc.constants.Constants;
import com.jc.service.DeviceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DocuService implements DeviceHandler {

    @Autowired
    private PubConfig pubConfig;

    /**
     * 处理消息
     *
     * @param message 消息内容
     * @param isHex   是否为16进制消息
     */
    @Override
    public void handle(String message, boolean isHex) {
        if (isHex) {
            log.info("HEX消息: {}", message);
            //判断是否到了home点
            isHome(message);
        } else {
            log.info("普通消息: {}", message);
        }
    }

    private void isHome(String message) {
        if (message.replaceAll(" ","").equals(Constants.ROBOT_HOME)) {
            pubConfig.setRobotStatus(true);
        }
    }
}
