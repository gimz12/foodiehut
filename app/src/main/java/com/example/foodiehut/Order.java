package com.example.foodiehut;

public class Order {
    private int orderId;
    private int userId;
    private String orderDate;
    private double totalPrice;
    private String status;
    private String deliveryLocation;
    private String promoCode;

    // Constructor, getters, and setters
    public Order(int orderId, int userId, String orderDate, double totalPrice, String status, String deliveryLocation, String promoCode) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.deliveryLocation = deliveryLocation;
        this.promoCode = promoCode;
    }

    // Getters and setters for all fields
    public int getOrderId() { return orderId; }
    public int getUserId() { return userId; }
    public String getOrderDate() { return orderDate; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public String getDeliveryLocation() { return deliveryLocation; }
    public String getPromoCode() { return promoCode; }
}
