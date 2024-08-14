package com.example.foodiehut;

public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int itemId;
    private int quantity;
    private String customization;
    private double price; // New field for item price

    public OrderItem(int orderItemId, int orderId, int itemId, int quantity, String customization, double price) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.customization = customization;
        this.price = price; // Initialize price
    }

    // Getters and Setters
    public int getOrderItemId() {
        return orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCustomization() {
        return customization;
    }

    public double getPrice() {
        return price;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCustomization(String customization) {
        this.customization = customization;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
