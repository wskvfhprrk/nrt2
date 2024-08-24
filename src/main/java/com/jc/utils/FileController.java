package com.jc.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@RestController
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/read-file")
    public String readFile() throws IOException {
        // 假设文件路径为 src/main/resources/configurationFile.txt
        return fileService.readFileAsString("src/main/resources/configurationFile.txt");
    }

    @PostMapping("/write-file")
    public String writeFile(@RequestParam String content) throws IOException {
        // 假设文件路径为 src/main/resources/configurationFile.txt
        fileService.writeFile("src/main/resources/configurationFile.txt", content);
        return "File written successfully";
    }

    @PostMapping("/append-file")
    public String appendFile(@RequestParam String content) throws IOException {
        // 假设文件路径为 src/main/resources/configurationFile.txt
        fileService.appendToFile("src/main/resources/configurationFile.txt", content);
        return "Content appended successfully";
    }
}
