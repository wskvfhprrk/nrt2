package com.jc.entity;

import com.jc.enums.OrderStatus;
import lombok.Data;

import java.io.Serializable;

/**
 * Order 实体类，表示一个订单
 */
@Data
public class Order implements Serializable {

    private String orderId;           // 订单ID
    private String customerName;      // 客户姓名
    private String selectedRecipe;    // 选择的食谱
    private int selectedPrice;        // 选择的价格
    private String selectedSpice;     // 选择的调料
    private boolean addCilantro;      // 是否加香菜
    private boolean addOnion;         // 是否加洋葱
    private OrderStatus status;       // 订单的处理状态

    // lombok的@Data注解会自动生成Getters, Setters, toString, equals, 和 hashCode方法
}

