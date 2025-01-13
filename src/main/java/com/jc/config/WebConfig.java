package com.jc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 解决跨域问题
 *
 */
@Configuration
@EnableWebSocket
public class WebConfig implements WebMvcConfigurer, WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWebSocketHandler(), "/ws")
                .setAllowedOrigins("http://127.0.0.1:8080", "http://localhost:8080", 
                      "http://127.0.0.1:8081", "http://localhost:8081")
                .withSockJS();
    }

    class MyWebSocketHandler extends TextWebSocketHandler {
        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            String payload = message.getPayload();

            // Route messages based on their content
            switch(payload) {
                case "getKeySuccess":
                    session.sendMessage(new TextMessage("getKeySuccess"));
                    break;
                case "qrSuccess":
                    session.sendMessage(new TextMessage("qrSuccess"));
                    break;
                case "paySuccess":
                    session.sendMessage(new TextMessage("paySuccess"));
                    break;
                case "failedToRetrievePassword":
                    session.sendMessage(new TextMessage("failedToRetrievePassword"));
                    break;
                default:
                    session.sendMessage(new TextMessage("Echo: " + payload));
            }
        }
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 允许对所有路径进行跨域
                        .allowedOrigins("http://127.0.0.1:8080", "http://localhost:8080", 
                              "http://127.0.0.1:8081", "http://localhost:8081") // 允许的源
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的请求方法
                        .allowedHeaders("*") // 允许的请求头
                        .allowCredentials(true); // 是否允许发送凭证
            }
        };
    }

}
