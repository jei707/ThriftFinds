package com.example.thrifters.model;

import java.sql.Date;

public class Order {
    private int orderId;
    private int userId;
    private Date orderDate;
    private String address;

    public Order(int orderId, int userId, Date orderDate, String address) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.address = address;
    }

    // Getters and setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
