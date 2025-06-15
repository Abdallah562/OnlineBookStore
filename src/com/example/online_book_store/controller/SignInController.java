package com.example.online_book_store.controller;

import com.example.online_book_store.database.DataBase;
import com.example.online_book_store.database.UsersDataBase;
//import com.example.online_book_store.manager.DatabaseManager;
import com.example.online_book_store.model.Admin;
import com.example.online_book_store.model.User;
import com.example.online_book_store.model.UserFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignInController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    public void handleSignIn(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Verify the user credentials
        User user = verifyUser(username, password);
        if (user != null) {
            goToHomePage(event, user);
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }
    private User verifyUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = UsersDataBase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Return a User object with the retrieved details
                User user = (User) UserFactory.getUser("customer");
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setRole(rs.getString("role"));

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void goToRegister(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/online_book_store/view/Register.fxml"));

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Register");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void goToHomePage(ActionEvent event, User user) {
        try {
            User userHandler = UserFactory.getUser(user.getRole());
            setData((User) userHandler,user.getRole());
            userHandler.loadView(event, user);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    public void setData(User user,String type) {
        User user1 = (User) UserFactory.getUser(type);
        user1.setId(user.getId());
        user1.setEmail(user.getEmail());
        user1.setUsername(user.getUsername());
        user1.setRole(user.getRole());
        user1.setPhone(user.getPhone());
        user1.setPassword(user.getPassword());
    }
}
