package com.jc.netty.client;

import com.jc.config.IpConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
public class NettyClientConfig {

    @Autowired
    private IpConfig ipConfig;

    private EventLoopGroup workerGroup;
    private Channel channel;

    @Bean
    public Bootstrap bootstrap() {
        workerGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        final Bootstrap handler = b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                final ChannelPipeline entries = ch.pipeline().addLast(new NettyClientHandler());
            }
        });

        return b;
    }

    @PreDestroy
    public void destroy() {
        if (channel != null) {
            channel.close();
        }
        workerGroup.shutdownGracefully();
    }

    public void connectAndSendData(String message)  {
        ChannelFuture f = null;
        try {
            f = bootstrap().connect(ipConfig.getDucuIp(), ipConfig.getDucuPort()).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        channel = f.channel();
        NettyClientHandler.sendData(channel, message);
    }
}
