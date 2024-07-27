package com.jc.controller.control;

import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.entity.Order;
import com.jc.service.impl.TurntableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

/**
 * 任务中心管理器
 */
@Service
@Slf4j
public class TaskCoordinator {

    @Autowired
    private PubConfig pubConfig;
    private Order order1;
    private Order order4;

    @Autowired
    private RobotPlaceEmptyBowl robotPlaceEmptyBowl;
    @Autowired
    private IngredientPreparation ingredientPreparation;
    @Autowired
    private SteamPreparation steamPreparation;
    @Autowired
    private TurntableService turntableService;

    public void executeTasks(Order order) throws InterruptedException, ExecutionException {
        //判断转台是否在1和4两个工位才能够放置空碗
        if (pubConfig.getTurntableNumber() == 1 || pubConfig.getTurntableNumber() == 4) {
            if (pubConfig.getTurntableNumber() == 1) {
                order1 = order;
            }
            if (pubConfig.getTurntableNumber() == 4) {
                order4 = order;
            }
            Result result1 = robotPlaceEmptyBowl.start(order1);
            Result result2 = ingredientPreparation.start(order1);
            Result result3 = steamPreparation.start(order1);
            //只要机器人把碗放到台上复位即可
            if (result1.getCode() == 200) {
                turntableService.moveNumber(3);
            }else {
                log.error(result1.getMessage());
            }
        }
    }
}
