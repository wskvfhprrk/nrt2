package com.jc.netty.server;

import com.jc.config.ClientConfig;
import com.jc.config.IpConfig;
import com.jc.config.PubConfig;
import com.jc.service.impl.Relay1DeviceGatewayService;
import com.jc.service.impl.Reset;
import com.jc.utils.HexConvert;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Netty 服务器处理器
 * 负责处理客户端连接、断开连接及消息读取等操作
 */
@Component
@ChannelHandler.Sharable
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    // 维护所有客户端通道的集合
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    // 维护客户端IP与通道的映射关系
    private static final Map<String, Channel> clientMap = new ConcurrentHashMap<>();
    @Autowired
    @Lazy
    private FicationProcessing ficationProcessing;
    @Autowired
    private ClientConfig clientConfig;
    @Autowired
    private IpConfig ipConfig;
    @Autowired
    private PubConfig pubConfig;
    @Autowired
    @Lazy
    private Relay1DeviceGatewayService relay1DeviceGatewayService;
    @Autowired
    @Lazy
    private Reset reset;

    /**
     * 客户端连接时调用
     *
     * @param ctx 通道处理器上下文
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        InetSocketAddress clientAddress = (InetSocketAddress) channel.remoteAddress();
        clientMap.put(clientAddress.getAddress().toString().replace("/", ""), channel);
        channels.add(channel);
        log.info("客户端连接成功，IP地址为：{}", clientAddress.getAddress().getHostAddress());
        updateClientConfig(clientAddress, true);
    }

    /**
     * 更新设备在线的状态
     *
     * @param clientAddress
     * @param flag
     */
    private void updateClientConfig(InetSocketAddress clientAddress, Boolean flag) {
        if (ipConfig.getRelay1DeviceGateway().equals(clientAddress.getAddress().getHostAddress())) {
            clientConfig.setRelay1Device(flag);
        } else if (ipConfig.getStepServoDriverGateway().equals(clientAddress.getAddress().getHostAddress())) {
            clientConfig.setSend485Order(flag);
        } else if (ipConfig.getTemperatureWeighingGateway().equals(clientAddress.getAddress().getHostAddress())) {
            clientConfig.setReceive485Singal(flag);
        } else if (ipConfig.getDucuIp().equals(clientAddress.getAddress().getHostAddress())) {
            clientConfig.setDocuOnLine(flag);
        } else if (ipConfig.getSignalAcquisitionDeviceGateway().equals(clientAddress.getAddress().getHostAddress())) {
            clientConfig.setIOdevice(flag);
        } else if (ipConfig.getRelay2DeviceGateway().equals(clientAddress.getAddress().getHostAddress())) {
            clientConfig.setRelay2Device(flag);
        } else if (ipConfig.getRelay1DeviceGateway().equals(clientAddress.getAddress().getHostAddress())) {
            clientConfig.setRelay1Device(flag);
        }
        boolean allDevicesConnected = clientConfig.getSend485Order() && clientConfig.getDocuOnLine() &&
                clientConfig.getIOdevice() && clientConfig.getReceive485Singal() &&
                clientConfig.getRelay1Device() && clientConfig.getRelay2Device();
        if (allDevicesConnected) {
            pubConfig.setAllDevicesConnectedStatus(true);
            reset.start();
        }
    }

    /**
     * 客户端断开连接时调用
     *
     * @param ctx 通道处理器上下文
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        InetSocketAddress clientAddress = (InetSocketAddress) channel.remoteAddress();
        clientMap.remove(clientAddress.getAddress().toString().replace("/", ""));
        channels.remove(channel);
        log.info("客户端断开连接，IP地址为：{}", clientAddress.getAddress().getHostAddress());
        updateClientConfig(clientAddress, false);
    }

    /**
     * 读取消息时调用
     *
     * @param ctx 通道处理器上下文
     * @param msg 消息对象
     * @throws Exception 抛出异常
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        InetSocketAddress clientAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = clientAddress.getAddress().toString().replace("/", "");
        if (msg instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf) msg;
            try {
                // 获取可读字节数
                int readableBytes = byteBuf.readableBytes();
                // 创建一个字节数组来存储可读的字节
                byte[] bytes = new byte[readableBytes];
                byteBuf.readBytes(bytes);
                // 处理字符串
                String s = HexConvert.BinaryToHexString(bytes);
                boolean hexStringWithSpaces = isHexStringWithSpaces(s);
                // 检查是否为16进制字符串
                if (hexStringWithSpaces) {
                    String hexString = HexConvert.BinaryToHexString(bytes);
//                log.info("clientIp：{}发送的HEX字符:{}", clientIp, hexString);
                    ficationProcessing.classificationProcessing(clientIp, true, hexString);
                } else {
                    String str = new String(bytes, StandardCharsets.UTF_8);
//                log.info("clientIp：{}发送的普通字符串：{}", clientIp, str);
                    ficationProcessing.classificationProcessing(clientIp, false, str);
                }
                releaseBuffer(byteBuf);
            } finally {
                byteBuf.release();
            }
        } else {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof java.net.SocketException && cause.getMessage().contains("Connection reset")) {
            InetSocketAddress clientAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            String clientIp = clientAddress.getAddress().toString().replace("/", "");
            log.warn("客户端{}连接已重置：{}", clientIp, cause.getMessage());
            updateClientConfig(clientAddress, true);
        } else {
            log.error("未处理的异常：", cause);
            InetSocketAddress clientAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            updateClientConfig(clientAddress, false);
        }
        // 关闭上下文
        ctx.close();
    }

    /**
     * 判断字符串是否为16进制字符串
     *
     * @param str 待检查的字符串
     * @return 如果字符串为16进制字符串则返回true，否则返回false
     */
    private boolean isHexStringWithSpaces(String str) {
        // 检查字符串是否为空
        if (str == null || str.isEmpty()) {
            return false;
        }
        // 检查字符串中的每个字符是否为16进制字符或空格
        for (char ch : str.toCharArray()) {
            if (!((ch >= '0' && ch <= '9') ||
                    (ch >= 'a' && ch <= 'f') ||
                    (ch >= 'A' && ch <= 'F') ||
                    ch == ' ')) {
                return false;
            }
        }
        return true;
    }

    /**
     * 发送消息到指定客户端
     *
     * @param clientIp 客户端IP地址
     * @param message  消息内容
     * @param hex      是否为16进制消息
     */
    public void sendMessageToClient(String clientIp, String message, Boolean hex) {
//        log.info("向：{} 发送指令：{}",clientIp,message);
        //停一下防止多条造成混乱
//        try {
//            Thread.sleep(Constants.SLEEP_TIME_MS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        for (Map.Entry<String, Channel> entry : clientMap.entrySet()) {
            String address = entry.getKey();
            if (address.equals(clientIp)) {
                Channel channel = entry.getValue();
//                log.info("服务器发送指令：{}", message);
                ByteBuf buff = Unpooled.buffer();
                try {
                    if (hex) {
                        buff.writeBytes(HexConvert.hexStringToBytes(message.replaceAll(" ", "")));
                        channel.writeAndFlush(buff);
                    } else {
                        buff.writeBytes(message.getBytes());
                        channel.writeAndFlush(buff);
                    }
                } finally {
                    buff.release();
                }
                return;
            }
        }
        log.error("无法找到与 IP 地址 {} 相关联的通道！", clientIp);
    }

    /**
     * 释放ByteBuf资源
     *
     * @param buffer ByteBuf对象
     */
    private void releaseBuffer(ByteBuf buffer) {
        if (buffer != null && buffer.refCnt() != 1) {
            buffer.release(); // 释放 ByteBuf 资源
        }
    }
}
