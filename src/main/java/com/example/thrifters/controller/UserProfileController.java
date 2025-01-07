package com.example.thrifters.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.thrifters.database.DatabaseConnection;
import com.example.thrifters.service.UserSession;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class UserProfileController {

    @FXML
    private Button homeUi6;

    @FXML
    private Button cartUi6;

    @FXML
    private Button ordersUi6;

    @FXML
    private Button aboutShop6;

    @FXML
    private Button cantact6;

    @FXML
    private Hyperlink logout6;

    @FXML
    private TextField searchBox;

    @FXML
    private Button userProfile;

    @FXML
    private TextField username;

    @FXML
    private TextField emailAddress;

    @FXML
    private TextField address;

    @FXML
    private ImageView profiePicture;

    @FXML
    private Button cantact1; // Edit Profile button

    @FXML
    private Button cantact11; // Save Profile button

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

    private void loadUserProfile() {
    String loggedInUser = UserSession.getLoggedInUsername();  // Retrieve the logged-in username
    
    // SQL query to fetch user profile data based on the username (logged-in user)
    String query = "SELECT * FROM users WHERE username = ?"; // Adjust table name and columns if necessary
    
    try (Connection conn = DatabaseConnection.getConnection(); // Use the existing DatabaseConnection class
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setString(1, loggedInUser); // Set the logged-in user as the parameter
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            // Retrieve user details from the ResultSet and display them on the UI
            String username = rs.getString("username");
            String email = rs.getString("email");
            String address = rs.getString("address");
            
            // Set the retrieved values into your UI components (replace with actual component names)
            this.username.setText(username);
            this.emailAddress.setText(email);
            this.address.setText(address);
            
            // If there's a profile picture, handle it here (example: Image or Blob)
            // Example: Image profilePic = new Image(rs.getString("profile_picture"));
            // this.profilePicture.setImage(profilePic);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle exception properly (e.g., show error message to user)
    }
}


    @FXML
    private void onHomeClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/homeUserInterface.fxml");
    }

    @FXML
    private void onCartClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/cartUserInterface.fxml");
    }

    public void onUsersButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/ordersUserInterface.fxml");
    }


    @FXML
    private void onAboutShopClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/abtShopUserInterface.fxml");
    }

    @FXML
    private void onContactClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/contactUsUserInterface.fxml");
    }

    @FXML
    private void onLogoutClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/thriftfindsLoginPage.fxml");
    }

    @FXML
    private void onUserProfileClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/userProfile.fxml");
    }

    @FXML
    private void onEditProfileClick(ActionEvent event) {
    // Enable text fields for editing
    username.setDisable(false);
    emailAddress.setDisable(false);
    address.setDisable(false);

    // Optionally, focus on the first text field
    username.requestFocus();
}

    @FXML
    private void onSaveProfileClick(ActionEvent event) {
        System.out.println("Saving profile...");
    
        // Get the updated values from the text fields
        String updatedUsername = username.getText();
        String updatedEmail = emailAddress.getText();
        String updatedAddress = address.getText();
    
        System.out.printf("Updated Profile: Username=%s, Email=%s, Address=%s%n",
                          updatedUsername, updatedEmail, updatedAddress);
    
        // Retrieve the logged-in username (used for updating the correct user)
        String loggedInUser = UserSession.getLoggedInUsername();  
    
        // SQL query to update the user profile
        String updateQuery = "UPDATE users SET username = ?, email = ?, address = ? WHERE username = ?";
    
        try (Connection conn = DatabaseConnection.getConnection(); // Use existing connection class
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
    
            // Set the parameters for the prepared statement
            stmt.setString(1, updatedUsername);  // New username (if changed)
            stmt.setString(2, updatedEmail);     // New email (if changed)
            stmt.setString(3, updatedAddress);   // New address
            stmt.setString(4, loggedInUser);     // Find the correct user to update (logged-in user)
    
            // Execute the update query
            int rowsAffected = stmt.executeUpdate();
    
            if (rowsAffected > 0) {
                System.out.println("Profile updated successfully.");
                // You can show a success message or reload the user profile page
                showSuccessMessage("Profile updated successfully!");
            } else {
                System.out.println("Profile update failed.");
                showErrorMessage("Failed to update profile.");
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorMessage("Database error while updating profile.");
        }
    
        // Disable the text fields after saving
        username.setDisable(true);
        emailAddress.setDisable(true);
        address.setDisable(true);
    }
    
    // Method to show an error message
private void showErrorMessage(String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
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


@FXML
private void initialize() {
    // Load user profile data
    loadUserProfile();

    // Disable text fields by default
    username.setDisable(true);
    emailAddress.setDisable(true);
    address.setDisable(true);
}
}
