package com.jc.utils;

import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class PropertiesWriter {

    private Properties properties = new Properties();

    // 加载 properties 文件
    public void loadProperties(String fileName) throws IOException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + fileName);
                return;
            }
            properties.load(input);
        }
    }

    // 更新某个 key 的值，并保存回文件
    public void updateProperty(String key, String value, String fileName) throws IOException {
        properties.setProperty(key, value);
        try (FileOutputStream output = new FileOutputStream(getClass().getClassLoader().getResource(fileName).getFile())) {
            properties.store(output, null);
        }
    }

    // 获取某个 key 的值
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
