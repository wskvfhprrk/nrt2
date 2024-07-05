package com.jc.netty.server;

import com.jc.controller.WebSocketController;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        // Echo the frame back to the client
        log.info("收到WebSocket客户端的信息：{}", frame.text());
        ctx.channel().writeAndFlush(new TextWebSocketFrame("你好, " + frame.text()));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        log.info("WebSocket客户端连接：{}", ctx.channel().id().asLongText());
        WebSocketController.addChannel(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        log.info("WebSocket客户端断开连接：{}", ctx.channel().id().asLongText());
        WebSocketController.removeChannel(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
