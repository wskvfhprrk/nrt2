package com.jc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "ip")
@Data
public class IpConfig {
    /**
     * 本机ip
     */
    private String local;
    /**
     * 本机tcp端口
     */
    private int nettyPort;
    /**
     * 信号采集设备网关
     */
    private String signalAcquisitionDeviceGateway;
    /**
     * 继电器1设备网关
     */
    private String relay1DeviceGateway;
    /**
     * 步进伺服驱动器网关
     */
    private String stepServoDriverGateway;
    /**
     * 继电器2设备网关
     */
    private String relay2DeviceGateway;
    /**
     * 温度称重网关
     */
    private String temperatureWeighingGateway;
    /**
     * 机器人
     */
    private String ducuIp;
    /**
     * 机器人服务端端口
     */
    private int ducuPort;

}
