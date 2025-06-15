package com.example.online_book_store.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class ManageCategoriesController {

    @FXML
    private ListView<String> categoriesListView;

    @FXML
    private TextField selectedCategoryField, editCategoryField;

    @FXML
    private Button updateButton, deleteButton;

    private Connection connection;
    private ObservableList<String> categories;

    @FXML
    public void initialize() {
        connectToDatabase();
        loadCategories();

        // Set up listener for category selection
        categoriesListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedCategoryField.setText(newVal);
                editCategoryField.setText(newVal);
                setButtonsDisabled(false);  // Enable the buttons when a category is selected
            } else {
                clearFields();
            }
        });
    }
    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:bookstore.db");
        } catch (SQLException e) {
            showAlert("Database Error", "Could not connect to the database: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    private void loadCategories() {
        categories = FXCollections.observableArrayList();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT category FROM Books")) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
            categoriesListView.setItems(categories);
        } catch (SQLException e) {
            showAlert("Error", "Failed to load categories: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void handleUpdateButtonAction() {
        String selectedCategory = selectedCategoryField.getText();
        String newCategory = editCategoryField.getText();

        if (selectedCategory == null || selectedCategory.isEmpty()) {
            showAlert("Error", "Please select a category first.", Alert.AlertType.WARNING);
            return;
        }

        if (newCategory == null || newCategory.isEmpty()) {
            showAlert("Error", "Please provide a new category name.", Alert.AlertType.WARNING);
            return;
        }

        if (selectedCategory.equals(newCategory)) {
            showAlert("Info", "No changes were made. The category name is the same.", Alert.AlertType.INFORMATION);
            return;
        }

        executeUpdate("UPDATE Books SET category = ? WHERE category = ?", newCategory, selectedCategory, "Category updated successfully.");
    }
    @FXML
    private void handleDeleteButtonAction() {
        String selectedCategory = selectedCategoryField.getText();

        if (selectedCategory == null || selectedCategory.isEmpty()) {
            showAlert("Error", "Please select a category to delete.", Alert.AlertType.WARNING);
            return;
        }

        // Confirm before deleting
        Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDelete.setTitle("Confirm Deletion");
        confirmDelete.setHeaderText("Are you sure you want to delete this category?");
        confirmDelete.setContentText("This action will delete all books in the selected category.");

        confirmDelete.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                executeUpdate("DELETE FROM Books WHERE category = ?", selectedCategory, "Category deleted successfully.");
            }
        });
    }
    private void executeUpdate(String query, String param1, String param2, String successMessage) {
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, param1);
            pstmt.setString(2, param2);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                loadCategories();  // Reload categories after the update
                clearFields();
                showAlert("Success", successMessage, Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Operation failed. No matching category found.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Error", "Operation failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    private void executeUpdate(String query, String param1, String successMessage) {
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, param1);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                loadCategories();  // Reload categories after the delete
                clearFields();
                showAlert("Success", successMessage, Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Operation failed. No matching category found.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Error", "Operation failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    private void clearFields() {
        selectedCategoryField.clear();
        editCategoryField.clear();
        setButtonsDisabled(true);  // Disable the buttons when no category is selected
    }
    private void setButtonsDisabled(boolean disabled) {
        updateButton.setDisable(disabled);
        deleteButton.setDisable(disabled);
    }
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void navigateToAdminView(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/AdminView.fxml"));
            Parent root = loader.load();

            AdminViewController controller = loader.getController();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

