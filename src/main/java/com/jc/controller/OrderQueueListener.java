package com.jc.controller;

import com.jc.config.PubConfig;
import com.jc.mqtt.ConsumerController;
import com.jc.mqtt.MqttConsumerConfig;
import com.jc.mqtt.MqttProviderConfig;
import com.jc.service.impl.RedisQueueService;
import com.jc.service.impl.SignalAcquisitionDeviceGatewayService;
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
    private TaskCoordinator taskCoordinator;
    @Autowired
    private PubConfig pubConfig;
    @Autowired
    private SignalAcquisitionDeviceGatewayService signalAcquisitionDeviceGatewayService;


    // 每秒钟检查一次队列中的订单
    @Scheduled(cron = "0/1 * * * * ?") // 1秒
    public void checkAndProcessOrders() {
        signalAcquisitionDeviceGatewayService.sendOrderStatus = false;
        //如果定时任务没有打开就不要进行
        if (!pubConfig.getIsExecuteTask()) {
            return;
        }
        //取出订单时机——有订单并且在编号为1或4工位时 并且机器人复位情况下才可以
        if (redisQueueService.getQueueSize() > 0) {
            try {
                taskCoordinator.executeOrder();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
