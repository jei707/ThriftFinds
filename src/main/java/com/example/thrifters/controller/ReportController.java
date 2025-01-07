package com.example.thrifters.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.thrifters.database.DatabaseConnection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ReportController {

    // FXML Elements
    @FXML private TextField searchBox;
    @FXML private Button adminProfile3;
    @FXML private Button homeUi4;
    @FXML private Button orders4;
    @FXML private Button orders14; // Users button
    @FXML private Button orders114; // Report button
    @FXML private Hyperlink logout4;
    @FXML private ImageView searchIcon;
    @FXML private BarChart<String, Number> reportBarChart;

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

    // Event handler for Orders button
    @FXML
    private void onOrdersButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/ordersAdminInterface.fxml");
    }

    // Event handler for Users button
    @FXML
    private void onUsersButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/usersAdminInterface.fxml");
    }

    // Event handler for Report button
    @FXML
    private void onReportsButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/reportAdminInterface.fxml");
    }

    // Event handler for Logout hyperlink
    @FXML
    private void onLogoutClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/thriftfindsLoginPage.fxml");
    }

    // Event handler for User Profile button
    @FXML
    private void onAdminProfileClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/profileAdminInterface.fxml");
    }

    // Event handler for Search box
    @FXML
    private void onSearch(ActionEvent event) {
        String query = searchBox.getText();
        System.out.println("Searching for: " + query);
        // Implement search logic
    }

    // Initialize method for setup tasks
    @FXML
    public void initialize() {
        System.out.println("ReportController initialized.");
        // Setup the BarChart by fetching data from the database
        setupBarChart();
    }

    // Custom method to populate the BarChart
    private void setupBarChart() {
        // Create a new series for the BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sales Report");

        // Query the database to fetch sales data
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connected to database.");

                // SQL query to fetch total sales per month
                String query = "SELECT DATE_FORMAT(order_date, '%Y-%m') AS month, SUM(total_amount) AS total_sales " +
                               "FROM Orders " +
                               "WHERE status = 'completed' " + // Optional: Filter for completed orders
                               "GROUP BY month " +
                               "ORDER BY month ASC";

                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    // Loop through the result set and populate the chart
                    while (resultSet.next()) {
                        String month = resultSet.getString("month");
                        double totalSales = resultSet.getDouble("total_sales");

                        // Log the values to verify the data
                        System.out.println("Month: " + month + ", Total Sales: " + totalSales);

                        // Add data to the series (month as X, total_sales as Y)
                        series.getData().add(new XYChart.Data<>(month, totalSales));
                    }
                }
            } else {
                System.err.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add the series to the BarChart
        if (series.getData().isEmpty()) {
            System.out.println("No data to display in the chart.");
        } else {
            reportBarChart.getData().add(series);
        }
    }
}
