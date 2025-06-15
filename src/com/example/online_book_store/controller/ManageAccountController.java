package com.example.online_book_store.controller;

import com.example.online_book_store.database.UsersDataBase;
//import com.example.online_book_store.manager.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ManageAccountController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    private int userId;
    public void setUserData(int userId, String username, String password, String email, String phone) {
        this.userId = userId;
        usernameField.setText(username);
        passwordField.setText(password);
        emailField.setText(email);
        phoneField.setText(phone);
    }
    public void saveChanges() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        if (updateUserDetails(userId, username, password, email, phone)) {

            System.out.println("User details updated successfully.");
        } else {
            System.out.println("User ID: " + userId);
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            System.out.println("Address: " + email);
            System.out.println("Phone: " + phone);

            System.out.println("Error updating user details.");
        }
    }
    private boolean updateUserDetails(int userId, String username, String password, String email, String phone) {
        String updateSQL = "UPDATE users SET username = ?, password = ?, email = ?, phone = ? WHERE id = ?";

        try (Connection conn = UsersDataBase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setInt(5, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("User ID: " + userId);
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            System.out.println("Address: " + email);
            System.out.println("Phone: " + phone);


            System.out.println("Error updating user details: " + e.getMessage());
            return false;
        }
    }
}
