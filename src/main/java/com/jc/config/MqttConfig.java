package com.jc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mqtt")
@Data
public class MqttConfig {
    private String username;
    private String password;
    private String url;
    private String send_id;
    private String listen_id;
}
