package com.jc.netty.client;

import com.jc.config.PubConfig;
import com.jc.constants.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private PubConfig pubConfig;

    public static void sendData(Channel channel, String data) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(data.getBytes());
        channel.writeAndFlush(buf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 读取服务端响应
        ByteBuf buf = (ByteBuf) msg;
        try {
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            String response = new String(data);
            log.info("机器人服务器返回信息:{} ", response);
            if (response.equals(Constants.ROBOT_ALREADY_ENABLE_COMMAND)) {
                //无法被spirng管理，只有new
                pubConfig = new PubConfig();
                pubConfig.setRobotAlreadyEnableCommand(true);
            }
//        if (response.equals(Constants.ROBOT_EXECUTE_SUCCESS_COMMAND)) {
//            pubConfig.setRobotExecutionNaming(true);
//        }
//        if (response.equals(Constants.ROBOT_EXECUTE_FAILURE_COMMAND)) {
//            pubConfig.setRobotExecutionNaming(false);
            ctx.close(); // 关闭连接}
        } finally {
            // 确保释放 msg——否则内存泄漏
            buf.release();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 异常处理
        cause.printStackTrace();
        ctx.close();
    }
}
