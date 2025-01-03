package com.example.thrifters.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
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
    @FXML private BarChart<?, ?> reportBarChart;

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
        // Additional setup logic, such as populating the bar chart
        setupBarChart();
    }

    // Custom method to populate the BarChart
    private void setupBarChart() {
        // Example logic for populating the BarChart
        System.out.println("Setting up BarChart...");
        // Add data to the chart (implementation dependent on your data source)
    }
}
