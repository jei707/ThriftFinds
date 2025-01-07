package com.example.thrifters.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.thrifters.database.DatabaseConnection;
import com.example.thrifters.model.Order;
import com.example.thrifters.service.UserSession;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UserOrdersController {

    @FXML
    private Button profile;
    @FXML
    private TextField searchBox;
    @FXML
    private Button homeUi1;
    @FXML
    private Button cartUI1;
    @FXML
    private Button ordersUi1;
    @FXML
    private Button abtShop1;
    @FXML
    private Button contactUs1;
    @FXML
    private Hyperlink logout22;
    @FXML private TableColumn<Order, Integer> orderIdColumn;
    @FXML private TableColumn<Order, String> orderDateColumn;
    @FXML private TableColumn<Order, Double> totalAmountColumn;
    @FXML private TableColumn<Order, String> statusColumn;
    @FXML private TableColumn<Order, String> addressColumn;
    
    
    @FXML private TableView<Order> ordersTableView;  // ListView to display orders

    // Handle profile button click
    @FXML
    private void onUserProfileClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/profileUserInterface.fxml");
    }

    // Handle search box input
    @FXML
    private void onSearchBoxInput() {
        String searchText = searchBox.getText();
        System.out.println("Search query: " + searchText);
        // Implement search logic here
    }

    // Handle Home button click
    @FXML
    private void onHomeButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/homeUserInterface.fxml");
    }

    // Handle Cart button click
    @FXML
    private void onOrdersButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/cartUserInterface.fxml");
    }

    // Handle Orders button click
    @FXML
    private void onUsersButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/ordersUserInterface.fxml");
    }

    // Handle About Shop button click
    @FXML
    private void onReportsButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/abtShopUserInterface.fxml");
    }

    // Handle Contact Us button click
    @FXML
    private void onContactUsButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/contactUsUserInterface.fxml");
    }

    // Handle logout click
    @FXML
    private void onLogoutClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/thriftfindsLoginPage.fxml");
    }

    // Helper method to switch scenes
    private void switchScene(ActionEvent event, String fxmlFilePath) {
        try {
            // Load the new scene
            AnchorPane root = FXMLLoader.load(getClass().getResource(fxmlFilePath));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();  // Get the current stage
            stage.setScene(scene); // Set the new scene
            stage.show(); // Show the new scene
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchOrders() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws SQLException {
                Connection connection = DatabaseConnection.getConnection();
                ObservableList<Order> ordersData = FXCollections.observableArrayList();
    
                if (connection != null) {
                    String sql = "SELECT o.order_id, o.order_date, o.total_amount, o.status, u.address " +
                                 "FROM Orders o JOIN Users u ON o.user_id = u.user_id " +
                                 "WHERE o.user_id = ?";  // Use the current logged-in user's ID
    
                    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                        stmt.setInt(1, UserSession.getCurrentUserId());  // Get the logged-in user's ID
                        ResultSet rs = stmt.executeQuery();
    
                        // Loop once to process the ResultSet and populate the ObservableList
                        while (rs.next()) {
                            System.out.println("Order ID: " + rs.getInt("order_id"));
                            System.out.println("Order Date: " + rs.getTimestamp("order_date"));
                            System.out.println("Total Amount: " + rs.getDouble("total_amount"));
                            System.out.println("Status: " + rs.getString("status"));
                            System.out.println("Address: " + rs.getString("address"));
    
                            // Create Order object and add it to the ObservableList
                            Order order = new Order(
                                rs.getInt("order_id"),
                                UserSession.getCurrentUserId(),  // Assuming user_id is part of the query result
                                rs.getTimestamp("order_date"),
                                rs.getString("address"),
                                rs.getDouble("total_amount"),
                                rs.getString("status")
                            );
                            ordersData.add(order);  // Add to ObservableList
                        }
    
                        System.out.println("Orders Data Size: " + ordersData.size());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
    
                // Update the TableView with the fetched data in the JavaFX application thread
                Platform.runLater(() -> ordersTableView.setItems(ordersData));
    
                return null;
            }
        };
    
        // Run the task in a separate thread to avoid blocking the UI thread
        new Thread(task).start();
    }
    

    // Call this method when the orders UI is loaded
    @FXML
    public void initialize() {
    // Initialize TableColumns
    orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
    orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
    totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
    addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
    statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    

    // Fetch orders in the background
    fetchOrders();
}
}
