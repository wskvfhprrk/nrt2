package com.jc.entity;

import lombok.Data;

@Data
public class Order {
    private String orderId;
    private String customerName;
    private String selectedRecipe;
    private int selectedPrice;
    private String selectedSpice;
    private boolean addCilantro;
    private boolean addOnion;
    private boolean isProcessed;

}
