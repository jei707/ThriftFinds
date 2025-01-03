package com.example.thrifters.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductDatabase {

    // Method to add a new product
    public static boolean addProduct(String name, String description, String category, String imagePath, double price, int quantity, String dateAdded) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO products (name, description, category, image, price, quantity, date_added) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, description);
                stmt.setString(3, category);
                stmt.setString(4, imagePath);
                stmt.setDouble(5, price);
                stmt.setInt(6, quantity);
                stmt.setString(7, dateAdded);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteProduct(int productId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM products WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, productId);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

        // Method to update a product in the database
        public static boolean updateProduct(int productId, String name, String imageUrl, double price) {
            String updateQuery = "UPDATE products SET name = ?, image_url = ?, price = ? WHERE id = ?";
    
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(updateQuery)) {
    
                // Set the parameters
                statement.setString(1, name);
                statement.setString(2, imageUrl);
                statement.setDouble(3, price);
                statement.setInt(4, productId);
    
                // Execute the update
                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
    
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Failed to update the product: " + e.getMessage());
            }
            return false;
        }
    }
    
    

