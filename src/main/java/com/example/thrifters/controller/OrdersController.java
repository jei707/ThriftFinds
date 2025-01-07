package com.example.thrifters.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import com.example.thrifters.database.DatabaseConnection;
import com.example.thrifters.model.Order;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class OrdersController {

    // FXML Elements
    @FXML private TextField searchBox;
    @FXML private Button adminProfile2, homeUi2, orders2, orders12, orders112;
    @FXML private Hyperlink logout2;
    @FXML private Pane productContainer;
    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Integer> orderID, userID;
    @FXML private TableColumn<Order, Timestamp> orderDate;
    @FXML private TableColumn<Order, String> address, status;
    @FXML private TableColumn<Order, Double> totalAmount;
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

    // Event handlers for navigation buttons
    @FXML
    private void onHomeButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/homeAdminInterface.fxml");
    }

    @FXML
    private void onOrdersButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/ordersAdminInterface.fxml");
    }

    @FXML
    private void onUsersButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/usersAdminInterface.fxml");
    }

    @FXML
    private void onReportsButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/reportAdminInterface.fxml");
    }

    @FXML
    private void onLogoutClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/thriftfindsLoginPage.fxml");
    }

    @FXML
    private void onAdminProfileClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/profileAdminInterface.fxml");
    }

    // Event handler for search
    @FXML
    private void onSearch(ActionEvent event) {
        String query = searchBox.getText();
        System.out.println("Searching for: " + query);
        // Implement search logic if needed
    }

    // Initialize method to set up the table and database connection
    @FXML
    public void initialize() {
        configureTableColumns();
        ObservableList<Order> orders = fetchOrdersFromDatabase();
        ordersTable.setItems(orders);
        ordersTable.setEditable(true); // Enable editing for the table
    }

    private void configureTableColumns() {
        if (orderID != null) {
            orderID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOrderId()).asObject());
        }
        if (userID != null) {
            userID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUserId()).asObject());
        }
        if (orderDate != null) {
            orderDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOrderDate()));
        }
        if (address != null) {
            address.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        }
        if (totalAmount != null) {
            totalAmount.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalAmount()).asObject());
        }
        if (status != null) {
            status.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
            configureStatusColumn();
        }
    }

    private void configureStatusColumn() {
        List<String> statusOptions = List.of("pending", "completed", "shipped", "cancelled");
        status.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(statusOptions)));
        status.setOnEditCommit(event -> {
            Order order = event.getRowValue();
            String newStatus = event.getNewValue();
            order.setStatus(newStatus);
            updateOrderStatusInDatabase(order.getOrderId(), newStatus);
        });
    }

    private void updateOrderStatusInDatabase(int orderId, String newStatus) {
        String query = "UPDATE orders SET status = ? WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<Order> fetchOrdersFromDatabase() {
        ObservableList<Order> orders = FXCollections.observableArrayList();
        String query = "SELECT orders.order_id, orders.user_id, orders.order_date, users.address, orders.total_amount, orders.status " +
                       "FROM orders INNER JOIN users ON orders.user_id = users.user_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("order_id"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("order_date"),
                    rs.getString("address"),
                    rs.getDouble("total_amount"),
                    rs.getString("status")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
