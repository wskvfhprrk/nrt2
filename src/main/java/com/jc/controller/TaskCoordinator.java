package com.jc.controller;

import com.alibaba.fastjson.JSON;
import com.jc.config.DataConfig;
import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.entity.Order;
import com.jc.enums.OrderStatus;
import com.jc.mqtt.MqttProviderConfig;
import com.jc.service.RobotService;
import com.jc.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 任务中心管理器
 */
@Service
@Slf4j
public class TaskCoordinator {

    @Autowired
    private PubConfig pubConfig;
    @Autowired
    private RobotService robotService;
    @Autowired
    private FansService fansService;
    @Autowired
    private DataConfig dataConfig;
    @Autowired
    private Relay1DeviceGatewayService relay1DeviceGatewayService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisQueueService redisQueueService;
    @Autowired
    private BowlService bowlService;
    @Autowired
    private TemperatureWeighingGatewayService temperatureWeightReadingService;
    @Autowired
    private SignalAcquisitionDeviceGatewayService signalAcquisitionDeviceGatewayService;

    // 创建一个固定大小的线程池，线程池大小为 2
    ExecutorService executorService = Executors.newFixedThreadPool(5);
    @Value("${machineCode}")
    private String machineCode;
    @Autowired
    private MqttProviderConfig mqttProviderConfig;

    public Result executeOrder() throws Exception {
        signalAcquisitionDeviceGatewayService.sendOrderStatus = false;
        pubConfig.setCheckBowl(false);
        pubConfig.setPutBowl(false);
        pubConfig.setPutBeef(false);
        pubConfig.setPickUpSoupBowl(false);
        pubConfig.setNaLan(false);
        pubConfig.setGetBowl(false);
        log.info("开始处理订单");
        //先把上一次未处理完的订单作为异常订单处理退款
        Long size = redisTemplate.opsForList().size(Constants.ORDER_REDIS_PRIMARY_KEY_IN_PROGRESS);
        if (size > 0) {
            handleFaultyOrder();
        }
        Long start = System.currentTimeMillis();
        Order order = redisQueueService.dequeue();
        String orderJson = JSON.toJSONString(order);
        //加入正在做的订单redis队列中
        order.setStatus(OrderStatus.PROCESSING);
        redisTemplate.opsForList().rightPush(Constants.ORDER_REDIS_PRIMARY_KEY_IN_PROGRESS, orderJson);
        //菜勺移到装菜位置
        Result result = bowlService.spoonLoad();
        if (result.getCode() != 200) {
            return result;
        }
        //同时处理
        // 取粉丝
        log.info("开始粉丝出粉丝");
        result = fansService.takeFans();
        if (result.getCode() != 200) {
            return result;
        }
        // TODO: 2024/12/16 检测菜篮是否在位置上，不在报警订单不继续做下去
        //配菜加蒸汽
        executorService.submit(() -> {
            addSteamToSideDishes();
        });
        executorService.submit(() -> {
            log.info("开始开始汤加热");
            temperatureWeightReadingService.soupHeatTo(dataConfig.getSoupHeatingTemperature());
        });
        //第一种配菜
        pubConfig.setDishesAreReady(false);
        executorService.submit(() -> {
            log.info("开始切肉");
            //根据订单类型选择
            int selectedPrice = order.getSelectedPrice();
            switch (selectedPrice) {
                case 10:
                    relay1DeviceGatewayService.meatSlicingMachine(1);
                    break;
                case 15:
                    relay1DeviceGatewayService.meatSlicingMachine(2);
                    break;
                case 20:
                    relay1DeviceGatewayService.meatSlicingMachine(3);
                    break;
                case 25:
                    relay1DeviceGatewayService.meatSlicingMachine(4);
                    break;
            }
        });
        //如果称重模块使用
        if (dataConfig.getIsUseWeighing()) {
            executorService.submit(() -> {
                log.info("开始称重配菜");
                temperatureWeightReadingService.vegetable1Motor(dataConfig.getDefaultWeighingValue());
            });
        }
        //必须机器人和粉丝准备到位才可以
        while (!pubConfig.getIsRobotStatus() || !pubConfig.getAreTheFansReady()) {
            Thread.sleep(500L);
        }
        log.info("机器人拿粉丝");
        while (pubConfig.getCurrentFanBinNumber() > 5 || pubConfig.getCurrentFanBinNumber() < 0) {
            Thread.sleep(500L);
        }
        while (pubConfig.getCurrentFanBinNumber() < 1 || pubConfig.getCurrentFanBinNumber() > 5) {
            log.error("粉丝仓没有出粉丝");
            Thread.sleep(500L);
        }
        result = robotService.robotTakeFans();
        if (result.getCode() != 200) {
            log.error(result.getMessage());
            return result;
        }
        //倒菜蓝
        while (!pubConfig.getIsRobotStatus() || !pubConfig.getDishesAreReady()) {
            Thread.sleep(500L);
        }
        log.info("机器人取配菜");
        result = robotService.robotTakeBasket();
        if (result.getCode() != 200) {
            return result;
        }
        //开始机器人取碗
        while (!pubConfig.getPutBeef() || !pubConfig.getIsRobotStatus()) {
            Thread.sleep(Constants.COMMAND_INTERVAL_POLLING_TIME);
        }
        Thread.sleep(2000L);
        log.info("开始机器人取碗");
        result = robotService.robotTakeBowl();
        if (result.getCode() != 200) {
            return result;
        }
        while (!pubConfig.getCheckBowl() || !pubConfig.getIsRobotStatus()) {
            Thread.sleep(Constants.COMMAND_INTERVAL_POLLING_TIME);
        }
        Thread.sleep(2000L);
        log.info("开始放碗……");
        result = robotService.robotPlaceBowl();
        if (result.getCode() != 200) {
            return result;
        }
        //等加完蒸汽和机器人停止状态
        while (!pubConfig.getIsRobotStatus() || !pubConfig.getFinishAddingSteam()) {
            Thread.sleep(200L);
        }
        log.info("倒菜");
        result = bowlService.spoonPour();
        if (result.getCode() != 200) {
            return result;
        }

        log.info("移到装菜");
        result = bowlService.spoonLoad();
        if (result.getCode() != 200) {
            return result;
        }

        //转90度，预防打架
        bowlService.turn90Degrees();

        log.info("开始加汤");
//        result = relay1DeviceGatewayService.soupAdd(dataConfig.getSoupExtractionTime());
        result = relay1DeviceGatewayService.dispenseSoupByPulseCount(dataConfig.getDispenseSoupByPulseCount());
        if (result.getCode() != 200) {
            return result;
        }
        log.info("开始出餐");
        robotService.robotDeliverMeal();
        //从正在做的队列中取出放到已经完成的
        order.setStatus(OrderStatus.PROCESSING);
        //从队列中取出
        redisTemplate.opsForList().leftPop(Constants.ORDER_REDIS_PRIMARY_KEY_IN_PROGRESS);
        //放进做完队列中
        order.setStatus(OrderStatus.COMPLETED);
        orderJson = JSON.toJSONString(order);
        redisTemplate.opsForList().rightPush(Constants.COMPLETED_ORDER_REDIS_PRIMARY_KEY, orderJson);
        //出汤
        log.info("开始出汤");
        log.info("开始出餐口打开出餐");
        executorService.submit(() -> {
            relay1DeviceGatewayService.openPickUpCounter();
            try {
                Thread.sleep(60000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            relay1DeviceGatewayService.closePickUpCounter();
        });
        //从已经完成队列中移除
        executorService.submit(() -> {
            try {
                Thread.sleep(60000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            redisTemplate.delete(Constants.COMPLETED_ORDER_REDIS_PRIMARY_KEY);
        });
        long l = (System.currentTimeMillis() - start) / 1000;
        log.info("出单时间：{}秒", l);
        //处理完订单告诉服务器
        notifyServerAfterProcessingOrder(orderJson);
        return Result.success();
    }

    /**
     * 配菜加蒸汽
     *
     * @return
     */
    private Result addSteamToSideDishes() {
        //初始化一下
        pubConfig.setSideDishesCompleted(false);
        pubConfig.setVegetable1Motor(false);
        pubConfig.setVegetable2Motor(false);
        Long begin = System.currentTimeMillis();
        Boolean flag = false;
        //当配菜弄完时开始加蒸汽
//        while (!pubConfig.getSideDishesCompleted() || !pubConfig.getVegetable1Motor() || !pubConfig.getVegetable2Motor()) {
        while (!pubConfig.getSideDishesCompleted()) {
            try {
                Thread.sleep(200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() - begin > 180000) {
                flag = true;
                break;
            }
            if (flag) {
                log.error("3分钟机器人未完成配菜");
                return Result.error("3分钟机器人未完成配菜");
            }
        }
        log.info("到复位位置");
        Result result = bowlService.spoonReset();
        if (result.getCode() != 200) {
            return result;
        }
        log.info("加蒸汽");
        relay1DeviceGatewayService.rearFanOpenClose(dataConfig.getOpenFanTime());
        result = relay1DeviceGatewayService.SteamAdd(dataConfig.getSteamAdditionTimeSeconds());
        if (result.getCode() != 200) {
            return result;
        }
        pubConfig.setSideDishesCompleted(false);
        return Result.success();
    }

    /**
     * 处理异常订单
     */
    private void handleFaultyOrder() {
        // TODO: 2024/11/15 处理异常订单——正在做的和未做订单都要退款
        //删除缓存中的正在做的数据
        Object o = redisTemplate.opsForList().leftPop(Constants.ORDER_REDIS_PRIMARY_KEY_IN_PROGRESS);
        if (o == null) {
            return;
        }
        //记录本地并上报服务器数据
        Order order = JSON.parseObject(o.toString(), Order.class);
        order.setStatus(OrderStatus.FAILED);
        log.info("失败订单：{}", order);
        //发送mqtt消息
        String topic = "message/order/" + machineCode;
        try {
            mqttProviderConfig.publishSign(0, false, topic, JSON.toJSONString(order));
        } catch (Exception e) {
            e.printStackTrace();
        }
        handleFaultyOrder();
    }

    /**
     * 处理完订单告诉服务器
     *
     * @param orderJson
     */
    private void notifyServerAfterProcessingOrder(String orderJson) {
        Order order = JSON.parseObject(orderJson, Order.class);
        order.setStatus(OrderStatus.COMPLETED);
        log.info("成功订单：{}", order);
        //发送mqtt消息
        String topic = "message/order/" + machineCode;
        try {
            mqttProviderConfig.publishSign(0, false, topic, JSON.toJSONString(order));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
