package com.jc.controller.control;

import com.jc.config.PubConfig;
import com.jc.config.Result;
import com.jc.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 任务中心管理器
 */
@Service
public class TaskCoordinator {

    @Autowired
    private ExecutorService executorService;
    @Autowired
    private PubConfig pubConfig;

    public void executeTasks(Order order) throws InterruptedException, ExecutionException {
        //判断转台是否在1和4两个工位才能够放置空碗
        if(pubConfig.getTurntableNumber()==1||pubConfig.getTurntableNumber()==4){

        }

        //
        Order order1=new Order("1","asb1");
        Order order2=new Order("2","asb2");
        Order order3=new Order("3","asb3");
        Order order4=new Order("4","asb4");
        Order order5=new Order("5","asb5");
        Order order6=new Order("6","asb6");
        // 创建任务列表
        Callable<Result> task1 = new RobotPlaceEmptyBowlTask(order1);
        Callable<Result> task2 = new IngredientPreparationTask(order2);
        Callable<Result> task3 = new SteamPreparationTask(order3);
        Callable<Result> task4 = new FanTask(order4);
        Callable<Result> task5 = new PlaceIngredientsTask(order5);
        Callable<Result> task6 = new AddSteamTask(order6);

        // 提交任务到线程池并获取 Future 对象
        Future<Result> future1 = executorService.submit(task1);
        Future<Result> future2 = executorService.submit(task2);
        Future<Result> future3 = executorService.submit(task3);
        Future<Result> future4 = executorService.submit(task4);
        Future<Result> future5 = executorService.submit(task5);
        Future<Result> future6 = executorService.submit(task6);

        // 等待并获取任务结果
        Result result1 = future1.get();
        Result result2 = future2.get();
        Result result3 = future3.get();
        Result result4 = future4.get();
        Result result5 = future5.get();
        Result result6 = future6.get();

        // 打印结果
        System.out.println("result1……"+result1.getMessage());
        System.out.println("result2……"+result2.getMessage());
        System.out.println("result3……"+result3.getMessage());
        System.out.println("result4……"+result4.getMessage());
        System.out.println("result5……"+result5.getMessage());
        System.out.println("result6……"+result6.getMessage());
    }
}
