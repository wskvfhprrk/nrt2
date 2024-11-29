//package com.jc.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//
///**
// * 自定义 TaskScheduler 配置线程池
// */
//@Configuration
//public class SchedulerConfig {
//
//    @Bean
//    public ThreadPoolTaskScheduler taskScheduler() {
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setPoolSize(5); // 设置线程池大小，根据任务数量调整
//        scheduler.setThreadNamePrefix("ScheduledTask-");
//        scheduler.initialize();
//        return scheduler;
//    }
//}
