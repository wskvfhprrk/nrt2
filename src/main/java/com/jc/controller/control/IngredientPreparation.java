package com.jc.controller.control;

import com.jc.config.BeefConfig;
import com.jc.config.Result;
import com.jc.entity.Order;
import com.jc.service.impl.RelayDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;


/**
 * 配料准备任务
 */
@Service
@Slf4j
public class IngredientPreparation {

    @Autowired
    private BeefConfig beefConfig;
    @Autowired
    private RelayDeviceService relayDeviceService;

    //准备牛肉
    public Callable<Result> start1(Order order) {
        log.info("准备牛肉");
        try {
            //牛肉
            Integer number = 0;
            switch (order.getSelectedPrice()) {
                case 10:
                    number = beefConfig.getBeef10();
                    break;
                case 15:
                    number = beefConfig.getBeef15();
                    break;
                case 20:
                    number = beefConfig.getBeef20();
                    break;
                default:
            }
            relayDeviceService.vegetableMotorInKg(1, number);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        // TODO: 2024/8/26 所有配料重量对才发送任务成功，否则阻塞线程
        return (Callable<Result>) Result.success();
    }

    //准备香菜
    public Callable<Result> start2(Order order) {
        log.info("准备香菜");
        try {
            if (order.isAddCilantro()) {
                try {
                    //香菜
                    relayDeviceService.vegetableMotorInKg(2, beefConfig.getCilantro());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        // TODO: 2024/8/26 所有配料重量对才发送任务成功，否则阻塞线程
        return (Callable<Result>) Result.success();
    }

    //准备葱花
    public Callable<Result> start3(Order order) {
        log.info("准备葱花");
        try {
            if (order.isAddOnion()) {
                try {
                    //葱
                    relayDeviceService.vegetableMotorInKg(3, beefConfig.getChoppedGreenOnion());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        // TODO: 2024/8/26 所有配料重量对才发送任务成功，否则阻塞线程
        return (Callable<Result>) Result.success();
    }
}