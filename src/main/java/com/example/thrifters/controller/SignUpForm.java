package com.example.thrifters.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.thrifters.database.DatabaseConnection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpForm {

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button signUpButton;

    // This method will be triggered when the "Create Account" button is clicked
    @FXML
    private void handleSignUp() {
        String email = emailTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        // Basic validation example (you can expand this as needed)
        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showErrorMessage("All fields must be filled out.");
            return;
        }

        // Insert the user data into the database
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Users (username, email, password, role) VALUES (?, ?, ?, 'customer')";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, email);
                statement.setString(3, password); // Password should ideally be hashed in production

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    showSuccessMessage("Account created successfully!");
                    loadLoginPage();  // Redirect to login page
                } else {
                    showErrorMessage("Failed to create account. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorMessage("Error connecting to the database.");
        }
    }

    // Method to show an error message
    private void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Account creation failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to show a success message
    private void showSuccessMessage(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Account created");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to load the login page
    private void loadLoginPage() {
        try {
            // Load the FXML file for the login page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/thrifters/thriftfindsLoginPage.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) signUpButton.getScene().getWindow();

            // Set the scene to the login page
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Show the login page
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Failed to load login page.");
        }
    }
}
