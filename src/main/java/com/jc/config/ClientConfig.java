package com.jc.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("client")
@Data
public class ClientConfig {
    private Boolean docuOnLine;
    private Boolean send485Order;
    private Boolean receive485Singal;
    private Boolean iOdevice;
    private Boolean relayDevice;
}
