package com.example.thrifters.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.thrifters.database.DatabaseConnection;
import com.example.thrifters.model.User;
import com.example.thrifters.service.UserSession;

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

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

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
        User loggedInUser = authenticateUser(username, password);

        if (loggedInUser != null) {
            // Set user data in the session
            UserSession.setCurrentUser(loggedInUser); // Set the current user
            UserSession.setLoggedInUsername(loggedInUser.getUsername()); // Store username in session

            System.out.println("Logged in user: " + loggedInUser.getUsername() + ", Role: " + loggedInUser.getRole());
            showSuccessMessage("Login successful!");

            // Check user role and load appropriate home page
            if ("admin".equalsIgnoreCase(loggedInUser.getRole())) {
                System.out.println("Loading admin home page...");
                loadAdminHomePage();
            } else {
                System.out.println("Loading user home page...");
                loadUserHomePage();
            }
        } else {
            showErrorMessage("Invalid username or password.");
        }
    }

    // Method to check if the username and password are valid, and return a User object
    private User authenticateUser (String username, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Use a parameterized query to prevent SQL injection
            String query = "SELECT user_id, username, role, address, email FROM Users WHERE username = ? AND password = ?"; // Remember to hash password in production
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password); // In production, use hashed password
    
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        // Get the role from the database
                        String role = resultSet.getString("role").trim(); // Trim whitespace
                        System.out.println("User  Role: '" + role + "'"); // Log the role value with quotes
    
                        // Create and return the User object with all required fields
                        return new User(
                            resultSet.getInt("user_id"), // Assuming user_id is the unique identifier in the database
                            resultSet.getString("username"),
                            role,
                            resultSet.getString("address"),
                            resultSet.getString("email")  // Adding email or any missing field
                        );
                    } else {
                        System.out.println("No user found with the provided username and password.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorMessage("Database connection error.");
        }
        return null; // Return null if authentication fails
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
