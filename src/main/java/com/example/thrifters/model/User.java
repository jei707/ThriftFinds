package com.example.thrifters.model;

public class User {
    private int userId;
    private String username;
    private String role;
    private String address;
    private String email;

    public User(int userId, String username, String role, String address, String email) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.address = address;
        this.email = email;
        
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getRole() {
        return role; // Getter for role
    }

    public void setRole(String role) {
        this.role = role; // Setter for role
    }
}
