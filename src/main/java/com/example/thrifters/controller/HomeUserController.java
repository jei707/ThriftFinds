package com.example.thrifters.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.thrifters.database.DatabaseConnection;
import com.example.thrifters.model.Product;
import com.example.thrifters.model.User;
import com.example.thrifters.service.UserSession;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomeUserController {

    // FXML Elements
    @FXML private Button homeUi;
    @FXML private Button cartUi;
    @FXML private Button ordersUi;
    @FXML private Button aboutShop;
    @FXML private Button contact; // Fixed typo from 'cantact' to 'contact'
    @FXML private Hyperlink logout;
    @FXML private Button menCategory; // Fixed typo from 'menCategoy' to 'menCategory'
    @FXML private Button womenCategory;
    @FXML private Button allProductCategory;
    @FXML private TextField searchBox;
    @FXML private Pane productContainer;

    private Stage stage;
    private Scene scene;
    private Parent root;

    // List to store cart items
    private List<Product> cart = new ArrayList<>();
    private String activeCategory = "All"; 

    @FXML
    public void initialize() {
        loadProductsFromDatabase(activeCategory, "");
        
    }

    private void loadProductsFromDatabase(String category, String searchTerm) {
        productContainer.getChildren().clear();
        System.out.println("Loading products from database...");
    
        String sql = "SELECT * FROM Products WHERE category LIKE ? AND name LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, category.equals("All") ? "%" : category); // If category is "All", fetch all products
            stmt.setString(2, "%" + searchTerm + "%"); // Search by name with the search term
            
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                int productId = rs.getInt("product_id");  // Get the actual product ID
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String imagePath = rs.getString("image");
                int stockLevel = rs.getInt("stock_level");
    
                Image image;
                try {
                    image = new Image(new File(imagePath).toURI().toString());
                } catch (Exception e) {
                    System.err.println("Error loading image: " + imagePath);
                    image = new Image("/com/example/thrifters/ImageView Pictures/placeholder.jpg");
                }
    
                // Pass the productId along with other details
                createProductCard(productId, name, price, image, stockLevel);
            }
            System.out.println("Finished loading products.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error occurred while loading products: " + ex.getMessage());
        }
    }
    
    private void createProductCard(int productId, String name, double price, Image image, int stockLevel) {
        Pane newProductPane = new Pane();
        newProductPane.setPrefSize(170, 214);
        newProductPane.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 0.5;");
    
        // Product Image
        ImageView productImage = new ImageView(image);
        productImage.setFitHeight(154);
        productImage.setFitWidth(150);
        productImage.setLayoutX(10);
        productImage.setLayoutY(10);
    
        // Product Name
        Text productName = new Text(name);
        productName.setLayoutX(10);
        productName.setLayoutY(178);
        productName.setFont(Font.font("Arial", 12));
    
        // Product Price
        Text productPrice = new Text(price + " php");
        productPrice.setLayoutX(10);
        productPrice.setLayoutY(192);
        productPrice.setFont(Font.font("Arial", 11));
    
        // Stock Level
        Text stockText = new Text("Stock: " + stockLevel);
        stockText.setLayoutX(10);
        stockText.setLayoutY(204);
        stockText.setFont(Font.font("Arial", 11));
    
        // Cart Icon
        ImageView cartIcon = new ImageView("/com/example/thrifters/ImageView Pictures/shopping-cart (1).png");
        cartIcon.setFitHeight(20);
        cartIcon.setFitWidth(19);
        cartIcon.setLayoutX(140);
        cartIcon.setLayoutY(188);
        cartIcon.setStyle("-fx-cursor: hand;");
    
        // Message Text for "Added to Cart"
        Text addedToCartMessage = new Text("Added to Cart");
        addedToCartMessage.setLayoutX(50);
        addedToCartMessage.setLayoutY(210);
        addedToCartMessage.setFont(Font.font("Arial", 12));
        addedToCartMessage.setStyle("-fx-fill: green;");
        addedToCartMessage.setVisible(false);
    
        cartIcon.toFront();
    
        // Add event handler to Cart Icon
        cartIcon.setOnMouseClicked(event -> {
            System.out.println("Cart icon clicked for product: " + name);
    
            // Call addToCart with dynamic product ID
            addToCart(productId, name, price, "Category", "Description", "Size", "Type", stockLevel, image);
    
            // Show the "Added to Cart" message
            addedToCartMessage.setVisible(true);
    
            // Hide the message after 2 seconds
            new Thread(() -> {
                try {
                    Thread.sleep(2000); // Display message for 2 seconds
                    Platform.runLater(() -> addedToCartMessage.setVisible(false));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    
        // Add all elements to the pane
        newProductPane.getChildren().addAll(productImage, productName, productPrice, stockText, cartIcon, addedToCartMessage);
        productContainer.getChildren().add(newProductPane);
    
        // Rearrange panes after adding the new product card
        rearrangeProductPanes();
    }
    
    

    private void addToCart(int productId, String name, double price, String category, String description, 
        String size, String type, int stockLevel, Image image) {
    try (Connection conn = DatabaseConnection.getConnection()) {
        // Get or create the current cart ID
        int cartId = createOrGetCartId(conn, getCurrentUserId()); // Fetch the current user's cart ID

        // Insert item into Order_Items table
        String imagePath = image.getUrl();
        String sql = "INSERT INTO Order_Items (order_id, user_id, product_id, quantity, price, image) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);

        int quantity = 1; // Default quantity to 1 for adding to cart
        stmt.setInt(1, cartId);  // Order ID (cart ID)
        stmt.setInt(2, getCurrentUserId());  // Add user ID here
        stmt.setInt(3, productId);  // Product ID
        stmt.setInt(4, quantity);  // Quantity
        stmt.setBigDecimal(5, new BigDecimal(price));  // Price
        stmt.setString(6, imagePath);  // Product image URL

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Added to cart in database: " + name);
        } else {
            System.out.println("Failed to add to cart in database: " + name);
        }

        // Optionally add the product to the local cart list for in-memory tracking
        Product product = new Product(productId, name, description, size, type, price, stockLevel, category, imagePath);
        cart.add(product);
    } catch (SQLException ex) {
        ex.printStackTrace();
        System.out.println("Error adding to cart: " + ex.getMessage());
    }
}


private int createOrGetCartId(Connection conn, int userId) throws SQLException {
    // Fetch the current cart based on user and status
    String selectCartSql = "SELECT order_id FROM Orders WHERE user_id = ? AND status = 'In Cart' LIMIT 1"; // Adjust the query to match your schema
    PreparedStatement selectStmt = conn.prepareStatement(selectCartSql);
    selectStmt.setInt(1, userId);
    ResultSet rs = selectStmt.executeQuery();

    if (rs.next()) {
        // If an active cart exists, return its ID
        return rs.getInt("order_id");
    } else {
        // If no active cart exists, create a new one
        String insertCartSql = "INSERT INTO Orders (user_id, status) VALUES (?, ?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertCartSql, PreparedStatement.RETURN_GENERATED_KEYS);
        insertStmt.setInt(1, userId);
        insertStmt.setString(2, "In Cart");  // Set the cart status as "In Cart"
        insertStmt.executeUpdate();

        // Get the generated cart ID
        ResultSet generatedKeys = insertStmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        } else {
            throw new SQLException("Failed to create a new cart.");
        }
    }
}

private static User getCurrentUser() {
    return UserSession.getCurrentUser();  // Assuming UserSession has a method to get the logged-in user
}


public static int getCurrentUserId() {
    User currentUser = getCurrentUser(); // Assuming you have a method to get the current user
    return currentUser != null ? currentUser.getUserId() : -1; // Or throw an exception if no user is logged in
}







    private void rearrangeProductPanes() {
        int columns = 7; // Decrease the number of columns to fit products better with more gaps
        double xGap = 180; // Larger horizontal gap
        double yGap = 220; // Larger vertical gap

        for (int i = 0; i < productContainer.getChildren().size(); i++) {
            Pane productPane = (Pane) productContainer.getChildren().get(i);
            int currentRow = i / columns;
            int currentColumn = i % columns;

            // Apply larger gaps for x and y positions
            double xPosition = 20 + currentColumn * xGap;
            double yPosition = 20 + currentRow * yGap;

            productPane.setLayoutX(xPosition);
            productPane.setLayoutY(yPosition);
        }
    }

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

    @FXML
    private void onUserProfileButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/profileUserInterface.fxml");
    }

    public void onUsersButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/ordersUserInterface.fxml");
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
        UserSession.clearSession(); // Clear user session
    switchScene(event, "/com/example/thrifters/thriftfindsLoginPage.fxml"); // Redirect to login page
}

    @FXML
    private void onMenCategoryClick(ActionEvent event) {
        activeCategory = ((Button) event.getSource()).getText();
        loadProductsFromDatabase(activeCategory, "");  // Fetch products for "Men"
    }

    @FXML
    private void onWomenCategoryClick(ActionEvent event) {
        activeCategory = ((Button) event.getSource()).getText();
        loadProductsFromDatabase(activeCategory, "");  // Fetch products for "Women"
    }

    @FXML
    private void onChildrenCategoryClick(ActionEvent event) {
        activeCategory = ((Button) event.getSource()).getText();
        loadProductsFromDatabase(activeCategory, "");  // Fetch products for "Children"
    }

    @FXML
    private void onOthersCategoryClick(ActionEvent event) {
        activeCategory = ((Button) event.getSource()).getText();
        loadProductsFromDatabase(activeCategory, "");  // Fetch products for "Others"
    }

    @FXML
    private void onAllProductCategoryClick(ActionEvent event) {
        activeCategory = "All";
        loadProductsFromDatabase(activeCategory, searchBox.getText()); // Fetch all products (no category filter)
    }


    // Other event handlers (e.g., category clicks) can stay as they are or be updated similarly.
}
