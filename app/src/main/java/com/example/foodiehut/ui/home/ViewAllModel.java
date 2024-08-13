package com.example.foodiehut.ui.home;

import java.io.Serializable;

public class ViewAllModel implements Serializable {

    private String name;
    private String description;
    private String category;
    private double price;
    private byte[] image;
    private boolean availability;

    public ViewAllModel() {
    }

    public ViewAllModel(String name, String description,String category, double price, byte[] image, boolean availability) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.image = image;
        this.availability = availability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
