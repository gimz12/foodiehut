package com.example.foodiehut.Admin;

public class OrderAdmin {
    private int orderId;
    private double totalPrice;
    private String status;
    private String orderDate;
    private String deliveryLocation;

    public OrderAdmin(int orderId, double totalPrice, String status, String orderDate, String deliveryLocation) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDate = orderDate;
        this.deliveryLocation = deliveryLocation;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }
}
