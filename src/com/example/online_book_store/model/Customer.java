package com.example.online_book_store.model;

import com.example.online_book_store.controller.HomeController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.IOException;

public class Customer extends User {

    private Customer(){}

    static Customer customer;
    public static Customer getInstance(){
        if (customer == null)
            customer = new Customer();
        return customer;
    }
    @Override
    public void loadView(ActionEvent event, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/Home.fxml"));
            Parent root = loader.load();

            HomeController controller = loader.getController();
            controller.setUserData("customer");

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
