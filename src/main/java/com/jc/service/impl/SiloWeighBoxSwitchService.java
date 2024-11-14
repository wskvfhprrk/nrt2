package com.jc.service.impl;


import com.jc.config.IpConfig;
import com.jc.config.Result;
import com.jc.netty.server.NettyServerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 料仓称重盒开关控制器
 */
@Service
public class SiloWeighBoxSwitchService {

    @Autowired
    private NettyServerHandler nettyServerHandler;
    @Autowired
    private IpConfig ipConfig;

    /**
     * 根据编号打开继电器
     */
    public Result openRelayByNumber(int number){
        //00 07 00 00 00 06 01 05 00 00 FF 00
        StringBuffer sb=new StringBuffer("000700000006010500");
        sb.append(String.format("%02d", number-1));
        sb.append("FF00");
        nettyServerHandler.sendMessageToClient(ipConfig.getSiloWeighBoxIp(),sb.toString(),true);
        return Result.success();
    }
    /**
     * 根据编号关闭继电器
     */
    public Result closeRelayByNumber(int number){
        //00 07 00 00 00 06 01 05 00 00 FF 00
        StringBuffer sb=new StringBuffer("000700000006010500");
        sb.append(String.format("%02d", number-1));
        sb.append("0000");
        nettyServerHandler.sendMessageToClient(ipConfig.getSiloWeighBoxIp(),sb.toString(),true);
        return Result.success();
    }

    /**
     * 根据编号称重盒打开
     */
    public Result openWeighBox(int number){
        closeRelayByNumber(number);
        openRelayByNumber(number+4);
        return Result.success();
    }
    /**
     * 根据编号称重盒关闭
     */
    public Result closeWeighBox(int number){
        openRelayByNumber(number);
        openRelayByNumber(number+4);
        return Result.success();
    }
}
