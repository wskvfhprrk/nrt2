package com.jc.utils;

import com.jc.config.DataConfig;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
public class ConfigLoader {
    String filePath = "src/main/resources/configurationFile.txt";

    @PostConstruct
    public DataConfig loadConfig() {
        Properties properties = new Properties();
        DataConfig dataConfig = new DataConfig();

        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);

            dataConfig.setBeef15(Integer.parseInt(properties.getProperty("10Beef")));
            dataConfig.setBeef15(Integer.parseInt(properties.getProperty("15Beef")));
            dataConfig.setBeef20(Integer.parseInt(properties.getProperty("20Beef")));
            dataConfig.setCilantro(Integer.parseInt(properties.getProperty("cilantro")));
            dataConfig.setChoppedGreenOnion(Integer.parseInt(properties.getProperty("choppedGreenOnion")));
            dataConfig.setSoupExtractionTime(Integer.parseInt(properties.getProperty("soupExtractionTime")));
            dataConfig.setVibratorTime(Integer.parseInt(properties.getProperty("vibratorTime")));
            dataConfig.setSoupHeatingTemperature(Integer.parseInt(properties.getProperty("soupHeatingTemperature")));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataConfig;
    }
}
