package com.example.thrifters.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class ContactController {
    @FXML private TextField searchBox;
    @FXML private Button userProfile;
    @FXML private ImageView facebook;
    @FXML private ImageView instagram;
    @FXML private ImageView x;
    @FXML private TextField username;
    @FXML private TextField emailAddress;
    @FXML private TextField message;
    @FXML private Button submitMessage;
    @FXML private Button homeUi3;
    @FXML private Button cartUi3;
    @FXML private Button aboutShop3;
    @FXML private Button cantact3;
    @FXML private ImageView logout3;

    private Stage stage;
    private Scene scene;
    private Parent root;

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

    // Initialize method to handle setup logic
    @FXML
    public void initialize() {
        // Add any initialization code here
    }

    // Event Handlers
    @FXML
    private void onSearchBoxChange() {
        String searchText = searchBox.getText();
        System.out.println("Search Text: " + searchText);
        // Logic for search functionality
    }

    @FXML
    private void onUserProfileClick() {
        System.out.println("User Profile clicked");
        // Logic to open user profile page
    }

    @FXML
    private void onHomeButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/homeUserInterface.fxml");
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
