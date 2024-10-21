package com.jc.controller;

import com.jc.config.PubConfig;
import com.jc.service.impl.RedisQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


/**
 * 订单队列监听器类，定期检查队列中的未处理订单并处理它们
 */
@Service
@Slf4j
public class OrderQueueListener {

    @Autowired
    private RedisQueueService redisQueueService; // 业务逻辑服务
    @Autowired
    private PubConfig pubConfig;
    @Autowired
    private TaskCoordinator taskCoordinator;


    // 每秒钟检查一次队列中的订单
//    @Scheduled(fixedRate = 2000) // 1秒
    public void checkAndProcessOrders() {
        //如果定时任务没有打开就不要进行
        if (!pubConfig.getIsExecuteTask()){
//            log.info("自检未完成，不能开始订单");
            return;}
        //取出订单时机——有订单并且在编号为1或4工位时 并且机器人复位情况下才可以
        if (redisQueueService.getQueueSize() > 0 ) {
            try {
                taskCoordinator.executeTasks();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
