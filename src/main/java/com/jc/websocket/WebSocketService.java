//package com.jc.websocket;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class WebSocketService {
//
//    private final WebSocketHandler webSocketHandler;
//
//    @Autowired
//    public WebSocketService(WebSocketHandler webSocketHandler) {
//        this.webSocketHandler = webSocketHandler;
//    }
//
//    // 广播消息给所有 WebSocket 连接
//    public void notifyAllClients(String message) {
//        webSocketHandler.broadcastMessage(message);
//    }
//
//    // 或者给特定用户发送消息
//    // 你可以通过 WebSocketSession 来标识客户端
//}
