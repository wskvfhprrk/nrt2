package com.jc.netty.server;

import com.jc.config.IpConfig;
import com.jc.service.DocuService;
import com.jc.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 分类处理服务
 * 用于根据客户端IP地址将消息分类处理到对应的设备处理器
 */
@Service
@Slf4j
public class FicationProcessing {

    @Autowired
    private IODeviceService ioDeviceService; // IO设备处理器
    @Autowired
    private IpConfig ipConfig;
    @Autowired
    @Lazy
    private RelayDeviceService relayDeviceService; // 继电器设备处理器
    @Autowired
    @Lazy
    private Relay1DeviceService relay1DeviceService; // 继电器设备处理器
    @Autowired
    private Send485OrderService send485OrderService;
    @Autowired
    private Receive485SignalService receive485SignalService;
    @Autowired
    private DocuService docuService;

    /**
     * 分类处理方法
     *
     * @param clientIp 客户端IP地址
     * @param flag     是否为16进制消息
     * @param message  消息内容
     */
    public void classificationProcessing(String clientIp, boolean flag, String message) {
        // 根据客户端IP地址分类处理消息到对应的设备处理器
        if (clientIp.equals(ipConfig.getIo())) {
            // 如果客户端IP地址匹配IO设备IP地址，则交由IO设备处理器处理消息
            ioDeviceService.handle(message, flag);
        } else if (clientIp.equals(ipConfig.getRelay())) {
            relayDeviceService.handle(message, flag);
        } else if (clientIp.equals(ipConfig.getSiloWeighBoxIp())) {
            relay1DeviceService.handle(message, flag);
        } else if (clientIp.equals(ipConfig.getSend485Order())) {
            send485OrderService.handle(message, flag);
        } else if (clientIp.equals(ipConfig.getReceive485Signal())) {
            receive485SignalService.handle(message, flag);
        } else if (clientIp.equals(ipConfig.getDucuIp())) {
            docuService.handle(message, flag);
        } else {
            // 其他情况视为未知设备IP地址，记录错误日志
            log.error("未知的设备IP地址：{}", clientIp);
        }
    }
}
