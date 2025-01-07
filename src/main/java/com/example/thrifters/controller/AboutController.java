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
import javafx.stage.Stage;

public class AboutController {

    // FXML Elements
    @FXML private TextField searchBox;
    @FXML private Button userProfile;
    @FXML private Button homeUi1;
    @FXML private Button cartUi1;
    @FXML private Button aboutShop1;
    @FXML private Button cantact1;
    @FXML private Hyperlink logout1;

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
        }
    }

    // Event Handlers
    @FXML
    private void onSearch(ActionEvent event) {
        String query = searchBox.getText();
        System.out.println("Searching for: " + query);
        // Implement search functionality if needed
    }

    public void onUsersButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/ordersUserInterface.fxml");
    }

    @FXML
    private void onUserProfileClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/profileUserInterface.fxml");
    }

    @FXML
    private void onHomeButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/homeAdminInterface.fxml");
    }

    @FXML
    private void onCartButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/cartUserInterface.fxml");
    }

    @FXML
    private void onAboutShopButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/abtShopUserInterface.fxml");
    }

    @FXML
    private void onContactButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/contactUsUserInterface.fxml");
    }

    @FXML
    private void onLogoutClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/thriftfindsLoginPage.fxml");
    }
}
