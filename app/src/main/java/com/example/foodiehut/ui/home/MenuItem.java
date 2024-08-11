package com.example.foodiehut.ui.home;

public class MenuItem {
    private int itemId;
    private String name;
    private String description;
    private double price;
    private byte[] image;
    private boolean availability;

    public MenuItem(int itemId, String name, String description, double price, byte[] image, boolean availability) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.availability = availability;
    }

    // Getters and setters
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public boolean isAvailability() { return availability; }
    public void setAvailability(boolean availability) { this.availability = availability; }
}

