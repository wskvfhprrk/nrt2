package com.jc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "data.portion-options")
public class PortionOptionsConfig {
    private PortionOption small;
    private PortionOption mid;
    private PortionOption large;
    private PortionOption addMeat; // 修改字段名称

    // Getters and setters
    public PortionOption getSmall() {
        return small;
    }

    public void setSmall(PortionOption small) {
        this.small = small;
    }

    public PortionOption getMid() {
        return mid;
    }

    public void setMid(PortionOption mid) {
        this.mid = mid;
    }

    public PortionOption getLarge() {
        return large;
    }

    public void setLarge(PortionOption large) {
        this.large = large;
    }

    public PortionOption getAddMeat() { // 修改方法名称
        return addMeat;
    }

    public void setAddMeat(PortionOption addMeat) { // 修改方法名称
        this.addMeat = addMeat;
    }

    // Method to find price by type
    public int findPriceByType(String type) {
        switch (type.toLowerCase()) {
            case "small":
                return small != null ? small.getPrice() : 0;
            case "mid":
                return mid != null ? mid.getPrice() : 0;
            case "large":
                return large != null ? large.getPrice() : 0;
            case "addmeat": // 修改类型名称
                return addMeat != null ? addMeat.getPrice() : 0;
            default:
                throw new IllegalArgumentException("Invalid portion type: " + type);
        }
    }

    // Method to find quantity by price
    public int findQuantityByPrice(int price) {
        if (small != null && small.getPrice() == price) {
            return small.getQuantity();
        } else if (mid != null && mid.getPrice() == price) {
            return mid.getQuantity();
        } else if (large != null && large.getPrice() == price) {
            return large.getQuantity();
        } else if (addMeat != null && addMeat.getPrice() == price) { // 修改字段名称
            return addMeat.getQuantity();
        } else {
            throw new IllegalArgumentException("No portion option found with price: " + price);
        }
    }

    public static class PortionOption {
        private int price;
        private int quantity;

        // Getters and setters
        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}