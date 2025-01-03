package com.example.thrifters.model;

public class Product {
    private int productId;
    private String name;
    private String description;
    private String size;
    private String type;
    private double price;
    private int stockLevel;
    private String category;

    // Constructor, getters, and setters
    public Product(int productId, String name, String description, String size, String type, double price, int stockLevel, String category) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.size = size;
        this.type = type;
        this.price = price;
        this.stockLevel = stockLevel;
        this.category = category;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

