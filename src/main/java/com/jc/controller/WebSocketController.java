package com.jc.controller;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebSocketController {

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static void addChannel(Channel channel) {
        channels.add(channel);
    }

    public static void removeChannel(Channel channel) {
        channels.remove(channel);
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String message) {
        channels.writeAndFlush(new TextWebSocketFrame(message));
        return "Message sent to all connected clients";
    }
}
