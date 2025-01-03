package com.example.thrifters.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AdminProfileController {

    // FXML Elements
    @FXML private TextField searchBox;
    @FXML private Button homeButton;
    @FXML private Button ordersButton;
    @FXML private Button usersButton;
    @FXML private Button reportsButton;
    @FXML private Button profileButton;
    @FXML private Hyperlink logoutLink;
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

    // Event handler for Reports button
    @FXML
    private void onReportsButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/reportAdminInterface.fxml");
    }

    // Event handler for User Profile button
    @FXML
    private void onAdminProfileClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/profileAdminInterface.fxml");
    }

    // Event handler for Logout hyperlink
    @FXML
    private void onLogoutClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/thriftfindsLoginPage.fxml");
    }

    // Event handler for Search box
    @FXML
    private void onSearch(ActionEvent event) {
        String query = searchBox.getText();
        System.out.println("Searching for: " + query);
        // Implement search logic here
    }

    // Initialize method for setup tasks
    @FXML
    public void initialize() {
        System.out.println("AdminController initialized.");
        // Additional initialization logic, if needed
    }
}
