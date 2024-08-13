package com.example.foodiehut;

import java.io.Serializable;

public class MyCart implements Serializable {

    String productname;
    double productprice;
    String productDate;
    String productTime;
    int totalQuantity;
    double totoalPrice;

    public MyCart() {
    }

    public MyCart(String productname, double productprice, String productDate, String productTime, int totalQuantity, double totoalPrice) {
        this.productname = productname;
        this.productprice = productprice;
        this.productDate = productDate;
        this.productTime = productTime;
        this.totalQuantity = totalQuantity;
        this.totoalPrice = totoalPrice;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public double getProductprice() {
        return productprice;
    }

    public void setProductprice(double productprice) {
        this.productprice = productprice;
    }

    public String getProductDate() {
        return productDate;
    }

    public void setProductDate(String productDate) {
        this.productDate = productDate;
    }

    public String getProductTime() {
        return productTime;
    }

    public void setProductTime(String productTime) {
        this.productTime = productTime;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTotoalPrice() {
        return totoalPrice;
    }

    public void setTotoalPrice(double totoalPrice) {
        this.totoalPrice = totoalPrice;
    }
}
