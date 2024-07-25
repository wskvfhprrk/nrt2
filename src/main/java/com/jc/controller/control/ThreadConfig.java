package com.jc.controller.control;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

@Configuration
public class ThreadConfig {

    @Bean
    public ExecutorService executorService() {
        // 创建一个包含6个线程的线程池
        return Executors.newFixedThreadPool(6);
    }
}
