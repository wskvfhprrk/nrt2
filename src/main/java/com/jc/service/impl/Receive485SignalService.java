package com.jc.service.impl;

import com.jc.config.PubConfig;
import com.jc.constants.Constants;
import com.jc.service.DeviceHandler;
import com.jc.utils.CRC16;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 收到485信号
 */
@Service
@Slf4j
public class Receive485SignalService implements DeviceHandler {

    @Autowired
    private PubConfig pubConfig;

    /**
     * 处理消息
     *
     * @param message 消息内容
     * @param isHex   是否为16进制消息
     */
    @Override
    public void handle(String message, boolean isHex) {
        if (isHex) {
//            log.info("收到485信号——HEX: {}", message);
            try {
                //解析指令
                ParseCommand(message);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else {
            log.info("收到485信号——普通消息: {}", message);
            // 在这里添加处理普通字符串消息的逻辑
        }
    }

    /**
     * 解析指令
     *
     * @param message
     */
    private void ParseCommand(String message) {
        //先modbus验证，如果验证不过就不管
        try {
            if (message == null) return;
            boolean b = CRC16.validateCRC(message);
            if (!b) {
                return;
            }
            String s = message.substring(0, 2);
            Integer integer = Integer.valueOf(s);
            //计算汤的温度值
            if (integer.equals(Constants.SOUP_TEMPERATURE_SENSOR)) {
                CalculateSoupTemperatureValue(message);
            }
            //计算重量
            if (integer.equals(Constants.WEIGHT_SENSOR_ONE_TO_FOUR)) {
                calculateWeight(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
    }

    /**
     * 计算重量
     *
     * @param message
     * @return
     */
    private int[] calculateWeight(String message) {
        if (Constants.ZEROING_CALIBRATION.replaceAll(" ", "").equals(message.replaceAll(" ", ""))) {
            log.info("所有秤已置零！");
            return new int[4];
        }
        message = message.replaceAll(" ", "");
        int[] sensorValues = new int[4];
        for (int i = 0; i < 4; i++) {
            int startIndex = 6 + i * 8;
            String substring = message.substring(startIndex, startIndex + 8);
            int number = 0;
            try {
                number = Integer.parseInt(substring, 16);
            } catch (Exception e) {
                /**
                 * 错误提示：
                 * java.lang.NumberFormatException: For input string: "FFFFFFC8"
                 * 	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65) ~[na:1.8.0_422]
                 * 	at java.lang.Integer.parseInt(Integer.java:583) ~[na:1.8.0_422]
                 * 	at com.jc.service.impl.Receive485SignalService.calculateWeight(Receive485SignalService.java:81) ~[classes/:na]
                 * 	at com.jc.service.impl.Receive485SignalService.ParseCommand(Receive485SignalService.java:60) ~[classes/:na]
                 * 	at com.jc.service.impl.Receive485SignalService.handle(Receive485SignalService.java:34) ~[classes/:na]
                 * 	at com.jc.netty.server.FicationProcessing.classificationProcessing(FicationProcessing.java:55) ~[classes/:na]
                 * 	at com.jc.netty.server.FicationProcessing$$FastClassBySpringCGLIB$$2ef6415f.invoke(<generated>) ~[classes/:na]
                 * 	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218) ~[spring-core-5.3.24.jar:5.3.24]
                 * 	at org.springframework.aop.framework.CglibAopProxy.invokeMethod(CglibAopProxy.java:386) ~[spring-aop-5.3.24.jar:5.3.24]
                 * 	at org.springframework.aop.framework.CglibAopProxy.access$000(CglibAopProxy.java:85) ~[spring-aop-5.3.24.jar:5.3.24]
                 * 	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:704) ~[spring-aop-5.3.24.jar:5.3.24]
                 * 	at com.jc.netty.server.FicationProcessing$$EnhancerBySpringCGLIB$$5f7acd2e.classificationProcessing(<generated>) ~[classes/:na]
                 * 	at com.jc.netty.server.NettyServerHandler.channelRead(NettyServerHandler.java:130) ~[classes/:na]
                 * 	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) [netty-transport-4.1.68.Final.jar:4.1.68.Final]
                 * 	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) [netty-transport-4.1.68.Final.jar:4.1.68.Final]
                 * 	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) [netty-transport-4.1.68.Final.jar:4.1.68.Final]
                 * 	at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410) [netty-transport-4.1.68.Final.jar:4.1.68.Final]
                 * 	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) [netty-transport-4.1.68.Final.jar:4.1.68.Final]
                 * 	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) [netty-transport-4.1.68.Final.jar:4.1.68.Final]
                 * 	at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919) [netty-transport-4.1.68.Final.jar:4.1.68.Final]
                 * 	at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:166) [netty-transport-4.1.68.Final.jar:4.1.68.Final]
                 * 	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:719) [netty-transport-4.1.68.Final.jar:4.1.68.Final]
                 * 	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:655) [netty-transport-4.1.68.Final.jar:4.1.68.Final]
                 * 	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:581) [netty-transport-4.1.68.Final.jar:4.1.68.Final]
                 * 	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:493) [netty-transport-4.1.68.Final.jar:4.1.68.Final]
                 * 	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:986) [netty-common-4.1.68.Final.jar:4.1.68.Final]
                 * 	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74) [netty-common-4.1.68.Final.jar:4.1.68.Final]
                 * 	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30) [netty-common-4.1.68.Final.jar:4.1.68.Final]
                 * 	at java.lang.Thread.run(Thread.java:750) [na:1.8.0_422]
                 */
                number = 0;
            }
            sensorValues[i] = number;
            log.info("称重传感器 {} 的值为：{} g", i+1, number);
        }
        pubConfig.setCalculateWeight(sensorValues);
        return sensorValues;
    }

    /**
     * 计算汤传感器数据
     *
     * @param message
     */
    private void CalculateSoupTemperatureValue(String message) {
        //01 03 02 01 20 B8 0C
        //截取第六位到第10位数据位
        message = message.replaceAll(" ", "");
        String substring = message.substring(6, 10);
        int i = Integer.parseInt(substring, 16);
        double soupTemperatureValue = i / 10.0;
        pubConfig.setSoupTemperatureValue(soupTemperatureValue);
        log.info("测量汤的温度为：{} 度", soupTemperatureValue);
    }
}
