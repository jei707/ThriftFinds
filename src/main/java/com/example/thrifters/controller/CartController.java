package com.example.thrifters.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.thrifters.database.DatabaseConnection;
import com.example.thrifters.model.CartItem;
import com.example.thrifters.service.UserSession;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CartController {

    @FXML private Button userProfile2;
    @FXML private Pane productContainer;
    @FXML private TableColumn<CartItem, Integer> orderID;
    @FXML private TableColumn<CartItem, String> productName;
    @FXML private TableColumn<CartItem, Double> productPrice;
    @FXML private TableColumn<CartItem, Integer> quantity;
    @FXML private TableColumn<CartItem, Boolean> action;
    @FXML private Label subtotal;
    @FXML private Label shippingFee;
    @FXML private Text subtotalPrice;
    @FXML private Text shippingFeePrice;
    @FXML private Text totaltotal;
    @FXML private Label date;
    @FXML private Label address;
    @FXML private Button homeUi2;
    @FXML private Button cartUi2;
    @FXML private Button aboutShop2;
    @FXML private Button cantact1;
    @FXML private Hyperlink logout2;
    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, Void> actionbutton;
    @FXML private Button checkout;
    @FXML private TableColumn<CartItem, String> status;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private List<CartItem> cartItems;
    private double totalPrice = 0.0;

    private void switchScene(ActionEvent event, String fxmlFilePath) {
        try {
            root = FXMLLoader.load(getClass().getResource(fxmlFilePath));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    public void initialize() {
        
        

        // Setup actionbutton column
        actionbutton.setCellFactory(param -> new TableCell<>() {
            private final Button buyButton = new Button("Buy");
            private final Button removeButton = new Button("Remove");
            private final HBox actionBox = new HBox(5, buyButton, removeButton);

            {
                // Configure "Remove" button
                removeButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
                removeButton.setOnAction(event -> {
                    CartItem item = getTableView().getItems().get(getIndex());
                    cartTable.getItems().remove(item);  // Remove from the TableView
                
                    // Delete the item from the database
                    String deleteQuery = "DELETE FROM Order_Items WHERE order_item_id = ?";
                
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
                        stmt.setInt(1, item.getOrderItemId());  // Use the order_item_id from the CartItem
                        int rowsAffected = stmt.executeUpdate();
                
                        if (rowsAffected > 0) {
                            System.out.println("Successfully removed item from the database: " + item.getProductName());
                        } else {
                            System.out.println("Failed to remove item from the database: " + item.getProductName());
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                
                    // Optionally, you can call fetchCartItems to refresh the list of items after deletion.
                    fetchCartItems();
                });
                


                // Configure "Buy" button
                buyButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
                buyButton.setOnAction(event -> {
                    CartItem item = getTableView().getItems().get(getIndex());  // Get the current CartItem
                    item.setAddedToOrderSummary(true);
                    addToSubtotal(item);  // Add the price of the clicked item to the subtotal
                    updateOrderSummary();
                    System.out.println("Proceeding to checkout");
                });                
                
            }

            @Override
            protected void updateItem(Void unused, boolean empty) {
                super.updateItem(unused, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionBox);
                }
            }
        });

        // Fetch cart items
        fetchCartItems();
    }

    private void addToSubtotal(CartItem item) {
        totalPrice += item.getPrice() * item.getQuantity(); // Add the price of the item to the running total
        double shippingFeeValue = 38.0; // Shipping fee
        subtotalPrice.setText(String.format("%.2f", totalPrice)); // Update the subtotal display
        shippingFeePrice.setText(String.format("%.2f", shippingFeeValue)); // Update the shipping fee display
        updateTotalTotal(totalPrice, shippingFeeValue); // Update the totaltotal field
    }

    private void updateTotalPrice(double finalTotalPrice) {
        // Update the total price (Subtotal + Shipping Fee)
        // You can display it wherever you want in the UI
        Label totalPriceLabel = new Label();  // Assume you have a label for the total price
        totalPriceLabel.setText(String.format("%.2f", finalTotalPrice));
    }
    
    
    

    private void fetchCartItems() {
        cartItems = new ArrayList<>();
        int userId = UserSession.getCurrentUserId();  // Get the logged-in user ID
        System.out.println("Fetching cart items for user: " + userId);
    
        String query = "SELECT oi.order_item_id, oi.product_id, oi.quantity, oi.price, p.name " +
                       "FROM Order_Items oi " +
                       "JOIN Products p ON oi.product_id = p.product_id " +
                       "JOIN Orders o ON oi.order_id = o.order_id " +
                       "WHERE o.user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            boolean dataFound = false;
            while (rs.next()) {
                dataFound = true;
                int orderItemId = rs.getInt("order_item_id");
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                String productName = rs.getString("name");
    
                CartItem item = new CartItem(orderItemId, productId, productName, quantity, price);
                cartItems.add(item);
                System.out.println("Added item to cart: " + productName);
            }
    
            if (!dataFound) {
                System.out.println("No items found for user ID: " + userId);
            }
    
            // Display the fetched items in the TableView
            displayCartItems();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void displayCartItems() {
        // Ensure cartItems list is not empty before displaying
        if (cartItems.isEmpty()) {
            System.out.println("No cart items to display.");
        } else {
            System.out.println("Displaying cart items.");
        }
    
        cartTable.getItems().clear();
        cartTable.getItems().addAll(cartItems);  // Add items to the table
        cartTable.refresh();  // Refresh the TableView to reflect the new data
    }
    

    private void calculateTotalPrice(CartItem item) {
        // Add only the price of the item that triggered the buy button
        totalPrice = item.getPrice() * item.getQuantity();
    }
    

    private void updateOrderSummary() {
        // Update the subtotal
        subtotalPrice.setText(String.format("%.2f", totalPrice));
        
        // Update the shipping fee
        double shippingFeeValue = 38.0; // Shipping fee
        shippingFeePrice.setText(String.format("%.2f", shippingFeeValue));
        
        // Update the totaltotal field
        updateTotalTotal(totalPrice, shippingFeeValue);
        
        // Date (today's date)
        LocalDate today = LocalDate.now();
        date.setText(today.toString());
        
        // Fetch user's address
        String userAddress = fetchUserAddress();
        address.setText(userAddress);
    }

    private void updateTotalTotal(double subtotal, double shippingFee) {
        // Calculate and update the total price (subtotal + shipping fee)
        double total = subtotal + shippingFee;
        totaltotal.setText(String.format("%.2f", total)); // Update the totaltotal field in the UI
    }
    

    private String fetchUserAddress() {
        String address = "";
        int userId = UserSession.getCurrentUserId();  // Get the logged-in user ID
        String query = "SELECT address FROM Users WHERE user_id = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                address = rs.getString("address");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return address;
    }
    

    @FXML
private void onCheckoutButtonClick(ActionEvent event) {
    // Filter items added to the order summary
    List<CartItem> itemsToCheckout = new ArrayList<>();
    for (CartItem item : cartTable.getItems()) {
        if (item.isAddedToOrderSummary()) {
            itemsToCheckout.add(item);
        }
    }

    // Check if there are items to checkout
    if (itemsToCheckout.isEmpty()) {
        showErrorMessage("No items selected for checkout. Please add items to your cart.");
        return; // Exit the method if there are no items to checkout
    }

    // Calculate the total amount for these items
    double totalAmount = itemsToCheckout.stream()
        .mapToDouble(item -> item.getPrice() * item.getQuantity())
        .sum();

    // Get user address
    String userAddress = address.getText();
    int userId = UserSession.getCurrentUserId(); // Use the current user's ID

    // Save order to the database
    boolean isOrderSaved = saveOrderToDatabase(userId, totalAmount, userAddress);

    if (isOrderSaved) {
        // Remove checked-out items from cartTable and database
        for (CartItem item : itemsToCheckout) {
            cartTable.getItems().remove(item);
            removeItemFromDatabase(item);
        }

        // Reset totals
        totalPrice = 0.0;
        subtotalPrice.setText("0.00");
        shippingFeePrice.setText("38.00");
        totaltotal.setText("38.00");

        showSuccessMessage("Order saved successfully!");
    } else {
        showErrorMessage("Failed to save the order. Please try again.");
    }
}

        private void removeItemFromDatabase(CartItem item) {
            String deleteQuery = "DELETE FROM Order_Items WHERE order_item_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
                stmt.setInt(1, item.getOrderItemId());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    

    

       
    private boolean saveOrderToDatabase(int userId, double totalAmount, String userAddress) {
        String insertOrderQuery = "INSERT INTO Orders (user_id, order_date, total_amount, status) VALUES (?, ?, ?, ?)";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertOrderQuery)) {
    
            stmt.setInt(1, userId);
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDate.now().atStartOfDay())); // Current date
            stmt.setDouble(3, totalAmount);
            stmt.setString(4, "pending"); // Set initial status to 'pending'
    
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showSuccessMessage(String message) {
        // Display the success message (You can use a dialog, label, or other UI element)
        System.out.println(message); // Replace with a UI notification
    }
    
    private void showErrorMessage(String message) {
        // Display the error message (You can use a dialog, label, or other UI element)
        System.err.println(message); // Replace with a UI notification
    }
    
    


    @FXML
    private void onUserProfileClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/profileUserInterface.fxml");
    }

    @FXML
    private void onHomeButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/homeUserInterface.fxml");
    }

    @FXML
    private void onCartButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/cartUserInterface.fxml");
    }

    public void onUsersButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/ordersUserInterface.fxml");
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
}
