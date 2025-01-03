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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HomeUserController {

    // FXML Elements
    @FXML private Button homeUi;
    @FXML private Button cartUi;
    @FXML private Button aboutShop;
    @FXML private Button cantact;
    @FXML private Hyperlink logout;
    @FXML private Button menCategoy;
    @FXML private Button womenCategory;
    @FXML private Button allProductCategory;
    @FXML private TextField searchBox;
    @FXML private ImageView addToCart;
    @FXML private Pane productContainer;

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

    // Event Handler for Home button
    @FXML
    private void onHomeButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/homeUserInterface.fxml");
    }

    // Event Handler for Cart button
    @FXML
    private void onCartButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/cartUserInterface.fxml");
    }

    // Event Handler for About Shop button
    @FXML
    private void onAboutShopButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/abtShopUserInterface.fxml");
    }

    // Event Handler for Contact Us button
    @FXML
    private void onContactButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/contactUsUserInterface.fxml");
    }

    // Event Handler for Logout hyperlink
    @FXML
    private void onLogoutClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/thriftfindsLoginPage.fxml");
    }

    // Other event handlers (e.g., category clicks) can stay as they are or be updated similarly.

}
