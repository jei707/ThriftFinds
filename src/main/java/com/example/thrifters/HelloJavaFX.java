package com.example.thrifters;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class HelloJavaFX extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        // Initial scene when the application starts
        loadScene("thriftfindsLoginPage.fxml");

        // Setting the title and window size
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();
        stage.setTitle("ThriftFinds");
        stage.setWidth(screenWidth);
        stage.setHeight(screenHeight);
        
        // Center the window on the screen
        stage.centerOnScreen();
        stage.show();
    }

    // Method to load a scene from an FXML file
    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setScene(scene);
        } catch (IOException e) {
            // Handle the exception with a user-friendly message
            showErrorMessage("Error loading FXML file: " + fxmlFile);
        }
    }

    // Method to display an error message
    private void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to switch to the 'About Shop' screen
    public void switchToAbtShop() {
        loadScene("abtShopUserInterface.fxml");
    }

    // Method to switch to the 'Cart' screen
    public void switchToCart() {
        loadScene("cartUserInterface.fxml");
    }

    // Method to switch to the 'Contact Us' screen
    public void switchToContactUs() {
        loadScene("contactUsUserInterface.fxml");
    }

    public void switchToLoginPage() {
        loadScene("thriftfindsLoginPage");
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
