package com.jc.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@Slf4j
public class PropertyService {

    @Autowired
    private PropertiesWriter propertiesWriter;

    @PostConstruct
    public void updateAppProperty() {
        try {
            // 加载配置文件
            propertiesWriter.loadProperties("application.properties");

            // 更新某个属性值
            propertiesWriter.updateProperty("data.test", "UpdatedAppName", "application.properties");

            // 获取更新后的值
            String updatedAppName = propertiesWriter.getProperty("data.test");
            log.info("更新后的data.test: {}", updatedAppName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
