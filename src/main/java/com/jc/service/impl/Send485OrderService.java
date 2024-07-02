package com.jc.service.impl;

import com.jc.service.DeviceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 发送485指令
 */
@Service
@Slf4j
public class Send485OrderService implements DeviceHandler {
    /**
     * 处理消息
     *
     * @param message 消息内容
     * @param isHex 是否为16进制消息
     */
    @Override
    public void handle(String message, boolean isHex) {
        if (isHex) {
            log.info("发送485指令——HEX消息: {}", message);
        } else {
            log.info("发送485指令——普通消息: {}", message);
            // 在这里添加处理普通字符串消息的逻辑
        }
    }
}
