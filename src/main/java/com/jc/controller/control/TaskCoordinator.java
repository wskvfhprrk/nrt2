package com.jc.controller.control;

import com.jc.config.BeefConfig;
import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.constants.Constants;
import com.jc.entity.Order;
import com.jc.service.impl.RelayDeviceService;
import com.jc.service.impl.TurntableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
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
    @Autowired
    private BeefConfig beefConfig;
    @Autowired
    private RelayDeviceService relayDeviceService;

    public void executeTasks(Order order) throws Exception {
        log.info("开始处理订单");
        //判断转台是否在1和4两个工位才能够放置空碗
        if (pubConfig.getTurntableNumber() % 6 == 1 || pubConfig.getTurntableNumber() % 6 == 4) {
            //初始化加碗完成
            pubConfig.setAddingBowlCompleted(false);
            pubConfig.setServingCompleted(false);
            //机器人取碗
            log.info("机器人取碗");
            Result result1 = robotPlaceEmptyBowl.takeBowl();
            //只要机器人把碗放到台上复位即可
            if (result1.getCode() == 200) {
                //汤加热
                log.info("汤加热");
                new Thread(() -> {
                    log.info("汤加热至中");
                    steamPreparation.start();
                }).start();
                //todo 称重
                //送到第三个转台
                log.info("送到第三个转台");
                turntableService.alignToPosition(3);
                //下一个粉丝
                //打开所有称重盒
                //打开震动哭器下料
                relayDeviceService.vibratorTest(beefConfig.getVibratorTime());
                //阻塞震动器震动时间
                Thread.sleep((beefConfig.getVibratorTime() + 1) * 1000);
                //转到碗加蒸汽位置
                turntableService.alignToPosition(4);
                //加蒸汽
                relayDeviceService.bowlSteam(beefConfig.getBowlSteamTime());
                Thread.sleep(1000L);
                //加完蒸转到第5个工位放汤
                turntableService.alignToPosition(5);
                //出汤
                relayDeviceService.soupPump(beefConfig.getSoupExtractionTime());
                //停留加汤时间
                Thread.sleep((beefConfig.getSoupExtractionTime() + 5) * 1000);
                //转到第6工位出汤
                turntableService.alignToPosition(0);
                robotPlaceEmptyBowl.putBowl();
                while (!pubConfig.getServingCompleted()) {
                    Thread.sleep(Constants.SLEEP_TIME_MS);
                    continue;
                }
                //归位
                turntableService.alignToPosition(1);
                //出汤
                relayDeviceService.theFoodOutletIsFacingDownwards();
                Thread.sleep(beefConfig.getTheFoodOutletIsFacingDownwardsTime() * 1000);
                relayDeviceService.theFoodOutletIsFacingUpwards();
            } else {
                log.error(result1.getMessage());
            }
        }
        if (pubConfig.getTurntableNumber() == 3) {
            // TODO: 2024/7/27 打开称重传感器料仓
            // TODO: 2024/7/27 粉丝货道掉下来
            // TODO: 2024/7/27 震动器打开后自动关闭
            Thread.sleep(5000L);//模拟下配料
            //旋转到第四个工位
            turntableService.alignToPosition(4);
        }
        if (pubConfig.getTurntableNumber() == 4) {
            // TODO: 2024/7/27 盖板盖到碗上
            // TODO: 2024/7/27 打开蒸汽
            Thread.sleep(5000L);//模拟下配料
            //旋转到第起始工位
            turntableService.alignToPosition(5);
        }
        if (pubConfig.getTurntableNumber() == 5) {
            //放汤

        }
    }
}
