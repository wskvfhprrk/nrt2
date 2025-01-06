package com.jc.vo;

import com.jc.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderVo {
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 选择的食谱
     */
    private String selectedRecipe;
    /**
     * 选择的价格
     */
    private String selectedPrice;
    /**
     * 选择的调料
     */
    private String selectedSpice;
    /**
     * 是否加香菜
     */
    private boolean addCilantro;
    /**
     * 是否加洋葱
     */
    private boolean addOnion;
    /**
     * 支付方式
     */
    private String paymentMethod;
}
