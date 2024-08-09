package com.jc.service.impl;

import com.jc.config.Result;
import com.jc.config.RobotConfig;
import com.jc.constants.Constants;
import com.jc.utils.CRC16;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 调味机按配方出料
 */
@Service
@Slf4j
public class SeasoningMachineService {

    @Autowired
    private Send485OrderService send485OrderService;
    @Autowired
    private RobotConfig robotConfig;

    /**
     * 调味机按配方出料
     * 1、发送置位指令出料
     * 2、发送配方号
     * 3、发送复位指令
     */
    public void dischargeAccordingToFormula(int formula)  {
        try {
            sendResetInstruction();
            Thread.sleep(100L);
            recipeNumberOrder(formula);
            Thread.sleep(100L);
            sendSetInstruction();
            Thread.sleep(100L);
            sendResetInstruction();
            Thread.sleep(100L);
            //不停查询
//            ejectionIsComplete();
        }catch (InterruptedException e){
            log.error(e.getMessage());
        }
    }

    private void ejectionIsComplete() {
        while (!robotConfig.isEjectionIsComplete()){
            //02 01 00 07 00 01 4C 38
            //485地址编码
            String addressCoding = String.format("%02d", Constants.SEASONING_MACHINE);
            //功能码
            String functionCode = "01";
            //起始地址
            String startAddress = "0007";
            //数据
            String datastr="0001";
            String order = addressCoding + functionCode + startAddress + datastr;
            String modbusrtuString = CRC16.getModbusrtuString(order);
            send485OrderService.sendOrder(order+modbusrtuString);
            log.info("询问调味机是否出料完成！");
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //发送置位指令
    private void sendSetInstruction() {
        //485地址编码
        String addressCoding = String.format("%02d", Constants.SEASONING_MACHINE);
        //功能码
        String functionCode = "05";
        //起始地址
        String startAddress = "0006";
        //数据
        String datastr="FF00";
        String order = addressCoding + functionCode + startAddress + datastr;
        String modbusrtuString = CRC16.getModbusrtuString(order);
        send485OrderService.sendOrder(order+modbusrtuString);
        log.info("置位指令发送完毕！");
    }
    //发送复位指令
    private void sendResetInstruction() {
        //485地址编码
        String addressCoding = String.format("%02d", Constants.SEASONING_MACHINE);
        //功能码
        String functionCode = "05";
        //起始地址
        String startAddress = "0006";
        //数据
        String datastr="0000";
        String order = addressCoding + functionCode + startAddress + datastr;
        String modbusrtuString = CRC16.getModbusrtuString(order);
        send485OrderService.sendOrder(order+modbusrtuString);
        log.info("复位指令发送完毕！");
    }
    //发送配方指令
    private void recipeNumberOrder(int formula) {
        //485地址编码
        String addressCoding = String.format("%02d", Constants.SEASONING_MACHINE);
        //功能码
        String functionCode = "06";
        //起始地址
        String startAddress = "0001";
        //数据
        String datastr=String.format("%04X", formula);
        String order = addressCoding + functionCode + startAddress + datastr;
        String modbusrtuString = CRC16.getModbusrtuString(order);
        send485OrderService.sendOrder(order+modbusrtuString);
        log.info("配方：{}发送完毕",formula);
    }

    /**
     * 称重测试打开
     * @return
     */
    public Result selectionTestOpen() {
        // TODO: 2024/8/8 称重传感器连续发送数据
        return Result.success();
    }

    /**
     * 称重测试关闭
     * @return
     */
    public Result selectionTestClose() {
        return Result.success();
    }
}
