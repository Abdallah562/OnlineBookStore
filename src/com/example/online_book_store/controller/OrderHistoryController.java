package com.example.online_book_store.controller;

import com.example.online_book_store.database.OrdersDatabase;
import com.example.online_book_store.model.OrderInterface;
import com.example.online_book_store.model.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.online_book_store.controller.AdminViewController.showAlert;

public class OrderHistoryController {

    @FXML
    private Button reviewButton;
    @FXML
    private TableView<OrderInterface> ordersTableView;

    @FXML
    private TableColumn<OrderInterface, Integer> orderIdColumn;

    @FXML
    private TableColumn<OrderInterface, String> orderDateColumn;

    @FXML
    private TableColumn<OrderInterface, String> booksnameColumn; // Added Book Names column

    @FXML
    private TableColumn<OrderInterface, Double> priceColumn;

    @FXML
    private TableColumn<OrderInterface, String> statusColumn;

    @FXML
    private TextArea reviewTextArea;

    @FXML
    private Button submitReviewButton;

    private ObservableList<OrderInterface> userOrders;

    private User currentUser;

    public OrderHistoryController() {
        userOrders = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        // Set up columns to match Order properties
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        booksnameColumn.setCellValueFactory(new PropertyValueFactory<>("booksname"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Set table data
        ordersTableView.setItems(userOrders);

        reviewButton.setOnAction(this::handleReviewAction);


    }

    private void handleReviewAction(javafx.event.ActionEvent actionEvent) {
        OrderInterface selectedOrder = ordersTableView.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {
            navigateToReviews(selectedOrder.getOrderId());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Order Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an order to review.");
            alert.showAndWait();
        }
    }

    public void setUser(User currentUser) {
        this.currentUser = currentUser;
        loadOrdersForCurrentUser();
    }
    private void loadOrdersForCurrentUser() {
        userOrders.setAll(OrdersDatabase.fetchOrdersByUserId(currentUser.getId()));
    }
    private void navigateToReviews(int orderID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/OrderReviews.fxml"));
            Parent root = loader.load();

            // Pass the orderID to the OrderReviewsController
            OrderReviewsController controller = loader.getController();
            controller.setIDs(orderID,this.currentUser.getId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Submit Review");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void goToHome(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/Home.fxml"));
            Parent root = loader.load();

            HomeController controller = loader.getController();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleCancelOrder() {
        // Get the selected order from the table
        OrderInterface selectedOrder = ordersTableView.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {
            if (!"Accepted".equalsIgnoreCase(selectedOrder.getStatus())) {
                // Update the status in the database
                boolean updateSuccessful = OrdersDatabase.updateOrderStatus(selectedOrder.getOrderId(), "Cancelled");

                if (updateSuccessful) {
                    // Update the status in the UI
                    selectedOrder.setStatus("Cancelled");
                    ordersTableView.refresh();

                    // Show success alert
                    showAlert(Alert.AlertType.INFORMATION, "Order Cancelled", "Your order has been successfully cancelled.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Cancellation Failed", "Failed to cancel the order. Please try again.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Cannot Cancel", "Confirmed orders cannot be cancelled.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Order Selected", "Please select an order to cancel.");
        }
    }
}
