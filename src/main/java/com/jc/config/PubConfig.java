package com.jc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 共公参数
 */
@Component
@ConfigurationProperties(prefix = "pub")
@Data
public class PubConfig {

    //robotStatus
    private Boolean robotStatus = false;
    //turntableNumber
    private int turntableNumber = 0;
    //orderNumber
    private int orderNumber = 0;
    private Boolean turntableReset;
}
