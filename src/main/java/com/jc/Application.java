package com.jc;

import com.jc.mqtt.MqttConsumerConfig;
import com.jc.mqtt.MqttProviderConfig;
import com.jc.service.impl.SignalAcquisitionDeviceGatewayService;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication(scanBasePackages = "com.jc")
@EnableAsync
@EnableScheduling
@Slf4j
public class Application {

    @Autowired
    private ApplicationContext ctx;
    @Autowired
    private MqttProviderConfig mqttProviderConfig;
    @Autowired
    private MqttConsumerConfig mqttConsumerConfig;


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void openBrowser() {
        //启动mqtt发送端连接
        mqttProviderConfig.connect();
        //启动mqtt接收端连接
        mqttConsumerConfig.connect();
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            // 启动Netty服务器
            ChannelFuture serverFuture = ctx.getBean("serverBootstrap", ChannelFuture.class);
            if (serverFuture != null) {
                serverFuture.sync();
                log.info("Netty服务器启动成功。");
            } else {
                log.error("Netty服务器启动失败。");
            }


            // 添加关闭钩子
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    serverFuture.channel().close();
                    serverFuture.channel().eventLoop().shutdownGracefully();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

            // 阻塞直到服务器通道关闭
            serverFuture.channel().closeFuture().sync();
        };
    }
}
