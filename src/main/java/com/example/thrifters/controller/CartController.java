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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CartController {

    // FXML Elements
    @FXML private TextField searchBox;
    @FXML private Button userProfile;
    @FXML private Pane productContainer;
    @FXML private TableColumn<?, ?> orderID;
    @FXML private TableColumn<?, ?> productName;
    @FXML private TableColumn<?, ?> productPrice;
    @FXML private TableColumn<?, ?> quantity;
    @FXML private TableColumn<?, ?> action;
    @FXML private Label subtotal;
    @FXML private Label shippingFee;
    @FXML private Text subtotalPrice;
    @FXML private Text shippingFeePrice;
    @FXML private Label date;
    @FXML private Label address;
    @FXML private Button homeUi2;
    @FXML private Button cartUi2;
    @FXML private Button aboutShop2;
    @FXML private Button cantact1;
    @FXML private Hyperlink logout2;

    private Stage stage;
    private Scene scene;
    private Parent root;

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

    // Helper method to switch scenes
    private void switchScene(ActionEvent event, String fxmlFilePath) {
        try {
            root = FXMLLoader.load(getClass().getResource(fxmlFilePath));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load FXML file: " + fxmlFilePath);
            e.printStackTrace();
        }
    }
}
