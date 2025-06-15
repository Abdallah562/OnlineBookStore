package com.example.online_book_store.controller;

import com.example.online_book_store.Main;
import com.example.online_book_store.model.User;
import com.example.online_book_store.model.UserFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    private User currentUser;
    public void setUserData(String type) {
        this.currentUser = (User) UserFactory.getUser(type);
        System.out.println(currentUser);
        System.out.println("User Data in Home Page: ID=" + currentUser.getId() + ", Username=" + currentUser.getUsername());
    }
    @FXML
    private void goToManageAccount(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/ManageAccount.fxml"));
            Parent root = loader.load();
            setUserData("customer");

            // Pass the user data to the ManageAccountController
            ManageAccountController controller = loader.getController();
            controller.setUserData(
                    currentUser.getId(),
                    currentUser.getUsername(),
                    currentUser.getPassword(),
                    currentUser.getEmail(),
                    currentUser.getPhone()
            );

            // Navigate to Manage Account
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manage Account");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToBrowseBooks(ActionEvent event) {
        try {
            // Create an instance of UserBrowseView and pass the current user
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/UserBrowseView.fxml"));
            Parent root = loader.load();

            // Pass the user data to UserBrowseView
            UserBrowseViewController controller = loader.getController();
            controller.setUserData();

            // Navigate to Browse Books View
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Browse Books");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToOrderHistory(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/OrderHistory.fxml"));
            Parent root = loader.load();
            OrderHistoryController controller = loader.getController();
            setUserData("customer");
            controller.setUser(currentUser);
            // Get the current stage from the event source
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void goToMainMenu(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        Main main = new Main();
        main.start(stage);
    }
}