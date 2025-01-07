package com.example.thrifters.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CartItem {
    private final IntegerProperty orderId;
    private final IntegerProperty productId;
    private final StringProperty productName;
    private final DoubleProperty productPrice;
    private final IntegerProperty quantity;
    private final BooleanProperty selected;
    private final BooleanProperty addedToOrderSummary; // New property

    // Updated constructor to initialize addedToOrderSummary
    public CartItem(int orderId, int productId, String productName, int quantity, double price) {
        this.orderId = new SimpleIntegerProperty(orderId);
        this.productId = new SimpleIntegerProperty(productId);
        this.productName = new SimpleStringProperty(productName);
        this.productPrice = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.selected = new SimpleBooleanProperty(false);
        this.addedToOrderSummary = new SimpleBooleanProperty(false); // Initialize to false
    }

    // Getters for properties
    public IntegerProperty orderIdProperty() {
        return orderId;
    }

    public IntegerProperty productIdProperty() {
        return productId;
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public DoubleProperty productPriceProperty() {
        return productPrice;
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public BooleanProperty addedToOrderSummaryProperty() {
        return addedToOrderSummary;
    }

    // Getters for fields
    public int getOrderItemId() {
        return orderId.get();
    }

    public int getProductId() {
        return productId.get();
    }

    public String getProductName() {
        return productName.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public double getPrice() {
        return productPrice.get();
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    // Getter and setter for addedToOrderSummary
    public boolean isAddedToOrderSummary() {
        return addedToOrderSummary.get();
    }

    public void setAddedToOrderSummary(boolean addedToOrderSummary) {
        this.addedToOrderSummary.set(addedToOrderSummary);
    }
}
