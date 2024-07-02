package com.jc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ip")
@Data
public class IpConfig {

    private String local;
    private int nettyPort;
    private String io;
    private String relay;
    private String send485Order;
    private String receive485Signal;
    private String ducoIp;
    private int ducoPort;

}
