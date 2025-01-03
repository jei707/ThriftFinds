package com.example.thrifters.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.thrifters.database.DatabaseConnection;
import com.example.thrifters.model.Order;  

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class OrdersController {

    // FXML Elements
    @FXML private TextField searchBox;
    @FXML private Button adminProfile2;
    @FXML private Button homeUi2;
    @FXML private Button orders2;
    @FXML private Button orders12;
    @FXML private Button orders112;
    @FXML private Hyperlink logout2;
    @FXML private Pane productContainer;
    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Integer> orderID;
    @FXML private TableColumn<Order, Integer> userID;
    @FXML private TableColumn<Order, Date> orderDate;
    @FXML private TableColumn<Order, String> address;
    @FXML private TableColumn<Order, String> action;
    @FXML private Text ordersText;
    @FXML private ImageView searchIcon;

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Helper method to switch scenes
    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            root = FXMLLoader.load(getClass().getResource(fxmlFile));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load scene: " + fxmlFile);
        }
    }

    // Event handler for Home button
    @FXML
    private void onHomeButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/homeAdminInterface.fxml");
    }

    // Event Handler for Orders button
    @FXML
    private void onOrdersButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/ordersAdminInterface.fxml");
    }

    // Event Handler for Users button
    @FXML
    private void onUsersButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/usersAdminInterface.fxml");
    }

    // Event Handler for Reports button
    @FXML
    private void onReportsButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/reportAdminInterface.fxml");
    }

    // Event Handler for Logout hyperlink
    @FXML
    private void onLogoutClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/thriftfindsLoginPage.fxml");
    }

    @FXML
    private void onAdminProfileClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/profileAdminInterface.fxml");
    }
    // Event handler for Search
    @FXML
    private void onSearch(ActionEvent event) {
        String query = searchBox.getText();
        System.out.println("Searching for: " + query);
        // Implement search logic
    }


        // Initialize method (if needed for further setup)
    @FXML
    public void initialize() {
        System.out.println("OrdersController initialized.");
        loadOrdersFromDatabase();  // Load orders from the database
    }

    // Method to load orders from the database
    private void loadOrdersFromDatabase() {
        ObservableList<Order> orderList = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Orders"; // Modify this query if needed
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int userId = rs.getInt("user_id");
                Date orderDate = rs.getDate("order_date");
                String address = rs.getString("address");
                // Fetch other required details here (for example, action column)

                // Add order to the list
                Order order = new Order(orderId, userId, orderDate, address);
                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error occurred while loading orders: " + e.getMessage());
        }

        // Set the items for the TableView
        ordersTable.setItems(orderList);
    }

}
