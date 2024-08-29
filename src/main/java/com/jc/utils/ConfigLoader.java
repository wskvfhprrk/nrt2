package com.jc.utils;

import com.jc.config.BeefConfig;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
public class ConfigLoader {
    String filePath = "src/main/resources/configurationFile.txt";

    @PostConstruct
    public BeefConfig loadConfig() {
        Properties properties = new Properties();
        BeefConfig beefConfig = new BeefConfig();

        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);

            beefConfig.setBeef15(Integer.parseInt(properties.getProperty("10Beef")));
            beefConfig.setBeef15(Integer.parseInt(properties.getProperty("15Beef")));
            beefConfig.setBeef20(Integer.parseInt(properties.getProperty("20Beef")));
            beefConfig.setCilantro(Integer.parseInt(properties.getProperty("cilantro")));
            beefConfig.setChoppedGreenOnion(Integer.parseInt(properties.getProperty("choppedGreenOnion")));
            beefConfig.setSoupExtractionTime(Integer.parseInt(properties.getProperty("soupExtractionTime")));
            beefConfig.setVibratorTime(Integer.parseInt(properties.getProperty("vibratorTime")));
            beefConfig.setSoupHeatingTemperature(Double.parseDouble(properties.getProperty("soupHeatingTemperature")));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return beefConfig;
    }
}
