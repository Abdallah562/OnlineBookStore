package com.example.online_book_store.controller;

import com.example.online_book_store.model.Book;
import com.example.online_book_store.model.Cart;


import com.example.online_book_store.model.OrderInterface;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.util.List;

import static com.example.online_book_store.database.OrdersDatabase.updateOrderStatus;

public class UserOrdersViewController {


    @FXML
    private TableView<OrderInterface> ordersTable;
    @FXML
    private TableColumn<OrderInterface, Integer> orderIdColumn;
    @FXML
    private TableColumn<OrderInterface, String> orderStatusColumn;
    @FXML
    private TableColumn<OrderInterface, Double> totalPriceColumn;
    @FXML
    private TableColumn<OrderInterface, String> orderBooksColumn;
    @FXML
    private Button cancelOrderButton;
    @FXML
    private Button proceedToPaymentButton;

    private ObservableList<OrderInterface> userOrders;


    public void initialize() {
        userOrders = FXCollections.observableArrayList();
        ordersTable.setItems(userOrders);

        orderIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getOrderId()).asObject());
        orderStatusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        orderBooksColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBooksname()));
        totalPriceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getTotalPrice()).asObject());

        cancelOrderButton.setDisable(true);
        proceedToPaymentButton.setDisable(true);

        ordersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cancelOrderButton.setDisable(false);
                proceedToPaymentButton.setDisable(false);
            } else {
                cancelOrderButton.setDisable(true);
                proceedToPaymentButton.setDisable(true);
            }
        });
    }

    public void loadUserOrders(ObservableList<OrderInterface> orders) {
        userOrders.setAll(orders);  // Update the orders list
        ordersTable.refresh();      // Refresh the table view to reflect the changes
    }

    @FXML
    private void handleCancelOrder() {
        OrderInterface selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null && !"Accepted".equals(selectedOrder.getStatus())) {

            selectedOrder.setStatus("Cancelled");
            updateOrderStatus(selectedOrder.getOrderId(), "Cancelled");
            Cart.getInstance().clearCart();

            userOrders.remove(selectedOrder);
            ordersTable.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Order Cancelled", "Your order has been cancelled.");

        } else {
            showAlert(Alert.AlertType.ERROR, "Cannot Cancel Order", "Only pending orders can be cancelled.");
        }
    }

    @FXML
    private void handleProceedToPayment() {
        OrderInterface selectedOrder = ordersTable.getSelectionModel().getSelectedItem();

        if (selectedOrder != null && "Pending".equals(selectedOrder.getStatus())) {
            selectedOrder.setStatus("Waiting for Admin to Confirm");
            selectedOrder.saveToDatabase();
            ordersTable.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Order Confirmed", "Your order has been saved to the database.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Cannot Proceed to Payment", "Only pending orders can be confirmed.");
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goToMainMenuView(ActionEvent actionEvent) {
        try {
            Object root = FXMLLoader.load(getClass().getResource("/com/example/online_book_store/view/UserBrowseView.fxml"));
            Scene scene = new Scene((Parent) root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}