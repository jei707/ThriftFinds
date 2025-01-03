package com.example.thrifters.controller;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.thrifters.database.DatabaseConnection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private CheckBox rememberMeCheckBox;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink forgotPassword;

    @FXML
    private Hyperlink signUp;

    // This method will be triggered when the "Login" button is clicked
    @FXML
    private void handleLogin() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        // Basic validation
        if (username.isEmpty() || password.isEmpty()) {
            showErrorMessage("Both fields must be filled out.");
            return;
        }

        // Authenticate user with the database
        if (isValidUser(username, password)) {
            showSuccessMessage("Login successful!");
            loadUserHomePage();
        } else {
            showErrorMessage("Invalid username or password.");
        }
    }

    // Method to check if the username, password and role are valid
    private boolean isValidUser(String username, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);  // Password should ideally be hashed in production
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        // Check if user is an admin or regular user based on role
                        String role = resultSet.getString("role");
                        if (role != null && role.equals("admin")) {
                            // Admin user, load admin page
                            loadAdminHomePage();
                        } else {
                            // Regular user, load user home page
                            loadUserHomePage();
                        }
                        return true;  // Authentication successful
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorMessage("Database connection error.");
        }
        return false;  // Invalid user
    }
        // Method to load the admin home page (homeAdminInterface.fxml)
        private void loadAdminHomePage() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/thrifters/homeAdminInterface.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showErrorMessage("Failed to load admin home page.");
            }
        }
        
    // Method to load the user home page (homeUserInterface.fxml)
    private void loadUserHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/thrifters/homeUserInterface.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Failed to load user home page.");
        }
    }

    // Method to show an error message
    private void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Login failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to show a success message
    private void showSuccessMessage(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    // This method will be triggered when the "Forgot Password" hyperlink is clicked
    @FXML
    private void handleForgotPassword() {
        showInfoMessage("Password reset functionality is not implemented yet.");
    }

    // This method will be triggered when the "Sign Up" hyperlink is clicked
    @FXML
    private void handleSignUp() {
        loadSignUpPage();
    }


    // Method to show an information message
    private void showInfoMessage(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to load the sign-up page
    private void loadSignUpPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/thrifters/signUpForm.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) signUp.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Failed to load sign-up page.");
        }
    }
}
