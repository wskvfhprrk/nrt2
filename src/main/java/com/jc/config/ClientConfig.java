package com.jc.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties("client")
@Data
public class ClientConfig {
    private Boolean docuOnLine;
    private Boolean send485Order;
    private Boolean receive485Singal;
    private Boolean iOdevice;
    private Boolean relay1Device;
    private Boolean relay2Device;
}
