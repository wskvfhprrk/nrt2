package com.jc.service.impl;

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

    /**
     * 调味机按配方出料
     * 1、发送置位指令出料
     * 2、发送配方号
     * 3、发送复位指令
     */
    public void dischargeAccordingToFormula(int formula) throws InterruptedException {
        recipeNumberOrder(formula);
        Thread.sleep(100L);
        sendSetInstruction();
        Thread.sleep(100L);
        sendResetInstruction();
        //不停查询
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
        String datastr=String.format("%04d", formula);
        String order = addressCoding + functionCode + startAddress + datastr;
        String modbusrtuString = CRC16.getModbusrtuString(order);
        send485OrderService.sendOrder(order+modbusrtuString);
    }
}
