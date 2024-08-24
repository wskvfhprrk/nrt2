package com.jc.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * 文件读取
 */
@Service
@Slf4j
public class FileService {

    String filePath = "src/main/resources/configurationFile.txt"; // 文件路径

    // 读取文件内容为单个字符串
    public String readFileAsString(String filePath) throws IOException {
        //按行处理逻辑
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                //注释
                if (line.substring(0, 1).equals("#")) {
                    continue;
                }
                String[] split = line.trim().split("=");
                log.info(split[0]); // 参数
                log.info(split[1]); // 值
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    // 读取文件内容为每行字符串的列表
    public List<String> readFileAsLines(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    // 写入内容到文件，覆盖原内容
    public void writeFile(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes());
    }

    // 追加内容到文件
    public void appendToFile(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes(), StandardOpenOption.APPEND);
    }
}
