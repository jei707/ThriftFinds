package com.example.thrifters.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import com.example.thrifters.database.DatabaseConnection;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminController {

    @FXML private TextField searchBox;
    @FXML private Button adminProfile;
    @FXML private Button menCategory;
    @FXML private Button womenCategory;
    @FXML private Button allProductCategory;
    @FXML private Button childrenCategory;
    @FXML private Button othersCategory;
    @FXML private Pane productContainer;
    @FXML private Button addNewProduct;
    @FXML private Button homeUi;
    @FXML private Button orders;
    @FXML private Button orders1;
    @FXML private Hyperlink logout1;
    @FXML private Button orders11;
    

    private Stage stage;
    private Scene scene;
    private Parent root;

    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            root = FXMLLoader.load(getClass().getResource(fxmlFile));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load scene: " + fxmlFile);
        }
    }

    @FXML
    public void initialize() {
        loadProductsFromDatabase("All", "");
}

    private String activeCategory = "All"; 

    @FXML
    private void onAddNewProductClick(ActionEvent event) {
        Stage addStage = new Stage();
        addStage.setTitle("Add New Product");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextField nameField = new TextField();
        nameField.setPromptText("Enter product name");

        TextField priceField = new TextField();
        priceField.setPromptText("Enter product price");

        TextField categoryField = new TextField();
        categoryField.setPromptText("Enter product category (optional)");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Enter product description (optional)");

        TextField sizeField = new TextField();
        sizeField.setPromptText("Enter product size (optional)");

        TextField typeField = new TextField();
        typeField.setPromptText("Enter product type (optional)");

        TextField stockLevelField = new TextField();
        stockLevelField.setPromptText("Enter stock level (optional)");

        Button imageChooserButton = new Button("Choose Image");
        ImageView previewImage = new ImageView();
        previewImage.setFitHeight(100);
        previewImage.setFitWidth(100);

        imageChooserButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Product Image");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(addStage);
            if (selectedFile != null) {
                previewImage.setImage(new Image(selectedFile.toURI().toString()));
            }
        });

        Button saveButton = new Button("Save Product");
        saveButton.setOnAction(e -> {
            if (validateFields(nameField, priceField)) {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                Image productImage = previewImage.getImage();
                String category = categoryField.getText().isEmpty() ? null : categoryField.getText();
                String description = descriptionField.getText().isEmpty() ? null : descriptionField.getText();
                String size = sizeField.getText().isEmpty() ? null : sizeField.getText();
                String type = typeField.getText().isEmpty() ? null : typeField.getText();
                String stockLevel = stockLevelField.getText().isEmpty() ? "0" : stockLevelField.getText();

                // Handle image saving
                String imagePath = "/com/example/thrifters/ImageView Pictures/placeholder.jpg"; // Default path
                if (productImage != null) {
                    try {
                        // Ensure the images directory exists
                        File imageDir = new File("images/");
                        if (!imageDir.exists()) {
                            imageDir.mkdirs();
                        }

                        File outputFile = new File(imageDir, name.replaceAll("\\s+", "_") + ".png"); // Save with a sanitized file name
                        ImageIO.write(SwingFXUtils.fromFXImage(productImage, null), "png", outputFile);
                        imagePath = outputFile.getAbsolutePath(); // Update path to saved file
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        showErrorPopup("Image Save Error", "Failed to save the product image.");
                    }
                }

                try (Connection conn = DatabaseConnection.getConnection()) {
                    String sql = "INSERT INTO Products (name, price, image, stock_level, category, description, size, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, name);
                    stmt.setDouble(2, price);
                    stmt.setString(3, imagePath);
                    stmt.setInt(4, Integer.parseInt(stockLevel));
                    stmt.setString(5, category);
                    stmt.setString(6, description);
                    stmt.setString(7, size);
                    stmt.setString(8, type);
                    stmt.executeUpdate();
                    
                
                    System.out.println("Product saved successfully.");
                    addStage.close();
                    
                    // Add the new product card directly to the UI
                    createProductCard(name, price, productImage, Integer.parseInt(stockLevel));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.err.println("SQL State: " + ex.getSQLState());
                    System.err.println("Error Code: " + ex.getErrorCode());
                    System.err.println("Message: " + ex.getMessage());
                    showErrorPopup("Database Error", "Failed to save product to the database. Check logs for details.");
                }
            } else {
                showErrorPopup("Invalid Input", "Please enter a valid name and numeric price.");
            }
        });
    

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> addStage.close());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(new Label("Add New Product"),
                                    new Label("Name:"), nameField,
                                    new Label("Price:"), priceField,
                                    new Label("Category:"), categoryField,
                                    new Label("Description:"), descriptionField,
                                    new Label("Size:"), sizeField,
                                    new Label("Type:"), typeField,
                                    new Label("Stock Level:"), stockLevelField,
                                    new Label("Image:"), imageChooserButton, previewImage,
                                    buttonBox);

        Scene scene = new Scene(layout, 300, 700);
        addStage.setScene(scene);
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.showAndWait();
    }

    private void createProductCard(String name, double price, Image image, int stockLevel) {
        Pane newProductPane = new Pane();
        newProductPane.setPrefSize(170, 200);
        newProductPane.setStyle("-fx-background-color: white;");
    
        // Product Image
        ImageView productImage = new ImageView(image);
        productImage.setFitHeight(132);
        productImage.setFitWidth(143);
        productImage.setLayoutX(10);
        productImage.setLayoutY(7);
    
        // Product Name
        Text productName = new Text(name);
        productName.setLayoutX(10);
        productName.setLayoutY(156);
        productName.setFont(Font.font("Arial", 11));
    
        // Product Price
        Text productPrice = new Text(price + " php");
        productPrice.setLayoutX(10);
        productPrice.setLayoutY(168);
        productPrice.setFont(Font.font("Arial", 11));
    
        // Stock Level
        Text stockText = new Text("Stock: " + stockLevel);
        stockText.setLayoutX(10);
        stockText.setLayoutY(180);
        stockText.setFont(Font.font("Arial", 11));
    
        // Delete Button
        ImageView deleteIcon = new ImageView("/com/example/thrifters/ImageView Pictures/delete.png");
        deleteIcon.setFitHeight(20);
        deleteIcon.setFitWidth(19);
        deleteIcon.setLayoutX(128);
        deleteIcon.setLayoutY(161);
        deleteIcon.setStyle("-fx-cursor: hand;");
    
        // Edit Button
        ImageView editIcon = new ImageView("/com/example/thrifters/ImageView Pictures/edit.png");
        editIcon.setFitHeight(16);
        editIcon.setFitWidth(16);
        editIcon.setLayoutX(108);
        editIcon.setLayoutY(162);
        editIcon.setStyle("-fx-cursor: hand;");
    
        // Delete Action
        deleteIcon.setOnMouseClicked(e -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "DELETE FROM Products WHERE name = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, name);
                stmt.executeUpdate();
    
                System.out.println("Product deleted: " + name);
                productContainer.getChildren().remove(newProductPane);
                rearrangeProductPanes();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showErrorPopup("Database Error", "Failed to delete product. Check logs for details.");
            }
        });
    
        // Edit Action
        editIcon.setOnMouseClicked(e -> onEditProduct(newProductPane, productImage, productName, productPrice));
    
        // Add Hover Effects for Buttons
        deleteIcon.setOnMouseEntered(e -> deleteIcon.setOpacity(0.7));
        deleteIcon.setOnMouseExited(e -> deleteIcon.setOpacity(1));
        editIcon.setOnMouseEntered(e -> editIcon.setOpacity(0.7));
        editIcon.setOnMouseExited(e -> editIcon.setOpacity(1));
    
        // Add all elements to the pane
        newProductPane.getChildren().addAll(productImage, productName, productPrice, stockText, deleteIcon, editIcon);
        productContainer.getChildren().add(newProductPane);
    
        // Rearrange panes after adding the new product card
        rearrangeProductPanes();
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

    private void onEditProduct(Pane productPane, ImageView productImage, Text productName, Text productPrice) {
        Stage editStage = new Stage();
        editStage.setTitle("Edit Product Details");
    
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
    
        // Fetch product details from the database
        String originalName = productName.getText();
        String category = "";
        String description = "";
        String size = "";
        String type = "";
        String stockLevel = "";
    
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Products WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, originalName);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                category = rs.getString("category");
                description = rs.getString("description");
                size = rs.getString("size");
                type = rs.getString("type");
                stockLevel = String.valueOf(rs.getInt("stock_level"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showErrorPopup("Database Error", "Failed to fetch product details for editing.");
            return;
        }
    
        TextField editNameField = new TextField(productName.getText());
        editNameField.setPromptText("Enter product name");
    
        TextField editPriceField = new TextField(productPrice.getText().replace(" php", ""));
        editPriceField.setPromptText("Enter product price");
    
        TextField editCategoryField = new TextField(category);
        editCategoryField.setPromptText("Enter product category");
    
        TextField editDescriptionField = new TextField(description);
        editDescriptionField.setPromptText("Enter product description");
    
        TextField editSizeField = new TextField(size);
        editSizeField.setPromptText("Enter product size");
    
        TextField editTypeField = new TextField(type);
        editTypeField.setPromptText("Enter product type");
    
        TextField editStockLevelField = new TextField(stockLevel);
        editStockLevelField.setPromptText("Enter stock level");
    
        Button imageChooserButton = new Button("Choose Image");
        ImageView previewImage = new ImageView(productImage.getImage());
        previewImage.setFitHeight(100);
        previewImage.setFitWidth(100);
    
        imageChooserButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Product Image");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(editStage);
            if (selectedFile != null) {
                previewImage.setImage(new Image(selectedFile.toURI().toString()));
            }
        });
    
        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> {
            if (validateFields(editNameField, editPriceField)) {
                String name = editNameField.getText();
                double price = Double.parseDouble(editPriceField.getText());
                String updatedCategory = editCategoryField.getText();
                String updatedDescription = editDescriptionField.getText();
                String updatedSize = editSizeField.getText();
                String updatedType = editTypeField.getText();
                String updatedStockLevel = editStockLevelField.getText().isEmpty() ? "0" : editStockLevelField.getText();
    
                // Save updated product details to the database
                try (Connection conn = DatabaseConnection.getConnection()) {
                    String sql = "UPDATE Products SET name = ?, price = ?, category = ?, description = ?, size = ?, type = ?, stock_level = ? WHERE name = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, name);
                    stmt.setDouble(2, price);
                    stmt.setString(3, updatedCategory);
                    stmt.setString(4, updatedDescription);
                    stmt.setString(5, updatedSize);
                    stmt.setString(6, updatedType);
                    stmt.setInt(7, Integer.parseInt(updatedStockLevel));
                    stmt.setString(8, originalName);
                    stmt.executeUpdate();
    
                    // Reload products to reflect updates
                    loadProductsFromDatabase("All", "");
                    editStage.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showErrorPopup("Database Error", "Failed to save product changes.");
                }
            } else {
                showErrorPopup("Invalid Input", "Please enter a valid name and numeric price.");
            }
        });
    
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> editStage.close());
    
        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
    
        layout.getChildren().addAll(
            new Label("Edit Product Details"),
            new Label("Name:"), editNameField,
            new Label("Price:"), editPriceField,
            new Label("Category:"), editCategoryField,
            new Label("Description:"), editDescriptionField,
            new Label("Size:"), editSizeField,
            new Label("Type:"), editTypeField,
            new Label("Stock Level:"), editStockLevelField,
            new Label("Image:"), imageChooserButton, previewImage,
            buttonBox
        );
    
        Scene scene = new Scene(layout, 300, 700);
        editStage.setScene(scene);
        editStage.initModality(Modality.APPLICATION_MODAL);
        editStage.showAndWait();
    }
    
    


    private boolean validateFields(TextField nameField, TextField priceField) {
        if (nameField.getText().isEmpty()) {
            System.out.println("Error: Product name is empty.");
            return false;
        }

        String priceText = priceField.getText().trim();
        try {
            Double.parseDouble(priceText);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showErrorPopup(String title, String message) {
        Stage errorStage = new Stage();
        errorStage.setTitle(title);
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(new Label(message), new Button("OK"));

        Scene scene = new Scene(layout, 250, 150);
        errorStage.setScene(scene);
        errorStage.initModality(Modality.APPLICATION_MODAL);
        errorStage.showAndWait();
    }

    private void loadProductsFromDatabase(String category, String searchQuery) {
        productContainer.getChildren().clear();
        System.out.println("Loading products from database...");
    
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql;
            PreparedStatement stmt;

            if (category.equals("All")) {
                if (searchQuery.isEmpty()) {
                    // No category filter, no search filter
                    sql = "SELECT * FROM Products";
                    stmt = conn.prepareStatement(sql);
                } else {
                    // No category filter, but filter by search query
                    sql = "SELECT * FROM Products WHERE name LIKE ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, "%" + searchQuery + "%");
                }
            } else {
                if (searchQuery.isEmpty()) {
                    // Filter by category only
                    sql = "SELECT * FROM Products WHERE category = ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, category);
                } else {
                    // Filter by both category and search query
                    sql = "SELECT * FROM Products WHERE category = ? AND name LIKE ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, category);
                    stmt.setString(2, "%" + searchQuery + "%");
                }
            }
    
    
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
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
    
                createProductCard(name, price, image, stockLevel);
            }
            System.out.println("Finished loading products.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error occurred while loading products: " + ex.getMessage());
        }
    }
    

    @FXML
    private void onSearchBoxInput(ActionEvent event) {
        String searchQuery = searchBox.getText().trim();
        loadProductsFromDatabase(activeCategory, searchQuery); // Default to "All" for searching
    }
    
        

    @FXML
    private void onHomeButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/homeAdminInterface.fxml");
    }

    @FXML
    private void onOrdersButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/ordersAdminInterface.fxml");
    }

    @FXML
    private void onUsersButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/usersAdminInterface.fxml");
    }

    @FXML
    private void onReportsButtonClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/reportAdminInterface.fxml");
    }

    @FXML
    private void onLogoutClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/thriftfindsLoginPage.fxml");
    }

    @FXML
    private void onAdminProfileClick(ActionEvent event) {
        switchScene(event, "/com/example/thrifters/profileAdminInterface.fxml");
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

    


}
