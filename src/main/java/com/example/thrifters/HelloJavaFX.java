package com.example.thrifters;

import java.io.IOException;

import com.example.thrifters.controller.LoginController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class HelloJavaFX extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        loadCustomFont();

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

            // Check if the loaded scene's controller has a method to set the stage
            if (fxmlLoader.getController() instanceof LoginController) {
                LoginController loginController = (LoginController) fxmlLoader.getController();
                loginController.setStage(primaryStage);  // Passing the stage to the controller
            }
        } catch (IOException e) {
            // Handle the exception with a user-friendly message
            showErrorMessage("Error loading FXML file: " + fxmlFile);
        }
    }

    private void loadCustomFont() {
        // Load custom font
        try {
            Font.loadFont(getClass().getResource("/fonts/FashionSignature.ttf").toExternalForm(), 36);
        } catch (Exception e) {
            System.err.println("Error loading font: " + e.getMessage());
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
        loadScene("thriftfindsLoginPage.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
