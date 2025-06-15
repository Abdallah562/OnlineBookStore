package com.example.online_book_store.controller;


import com.example.online_book_store.manager.OrderManager;
import com.example.online_book_store.model.Book;
import com.example.online_book_store.model.RealOrder;
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

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class AdminOrdersController {

    @FXML
    private TableView<RealOrder> ordersTable;
    @FXML
    private TableColumn<RealOrder, Integer> orderIdColumn;
    @FXML
    private TableColumn<RealOrder, Integer> userIdColumn;
    @FXML
    private TableColumn<RealOrder, String> booksColumn; // Display book IDs
    @FXML
    private TableColumn<RealOrder, Double> totalPriceColumn;
    @FXML
    private TableColumn<RealOrder, String> orderDateColumn;
    @FXML
    private TableColumn<RealOrder, String> orderStatusColumn;
    @FXML
    private TableColumn<RealOrder, String> orderCancelledIDsColumn;
    @FXML
    private Button processOrderButton;

    private ObservableList<RealOrder> allOrders = FXCollections.observableArrayList();
    OrderManager orderManager;

    // Initialize the table with data from the database
    public void initialize() {
        // Set up table columns using PropertyValueFactory
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        booksColumn.setCellValueFactory(new PropertyValueFactory<>("booksname")); // Display book IDs
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        orderCancelledIDsColumn.setCellValueFactory(new PropertyValueFactory<>("cancelledIDs"));

        // Load data into the table
        loadOrdersFromDatabase();

        ordersTable.setItems(allOrders);
    }

    // Load all orders from the database
    private void loadOrdersFromDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:bookstore.db")) {
            String query = "SELECT * FROM Orders";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {

                    int orderId = rs.getInt("orderId");
                    int userId = rs.getInt("userId");
                    String booksIDs = rs.getString("Books"); // Book IDs as a comma-separated string
                    String booksname = rs.getString("booksname"); // Book IDs as a comma-separated string
                    double totalPrice = rs.getDouble("totalPrice");
                    String orderDate = rs.getString("orderDate");
                    String orderStatus = rs.getString("orderStatus");
                    String cancelledIDs = rs.getString("CancelledIDs");

                    // Create a new Order object and add it to the list
                    allOrders.add(new RealOrder(orderId, userId, booksIDs,booksname ,totalPrice, orderDate, orderStatus,cancelledIDs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Handle Accept Order
    @FXML
    private void processOrder() {
        orderManager = new OrderManager();
        List<String> cancelledIDs = new Vector<>();
        String bookID;
        double totalPrice=0;
        double price;

        RealOrder selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        System.out.println(selectedOrder);
        if (selectedOrder != null && "Waiting for Admin to Confirm".equals(selectedOrder.getStatus())) {
            List<String> bookIDs = Arrays.stream(selectedOrder.getBookIds().split(", ")).toList();

            for (int i = 0; i < bookIDs.stream().count(); i++) {
                bookID = bookIDs.get(i);
                price = orderManager.checkBookAvailability(Integer.parseInt(bookID),1);
                if(price == 0)
                {
                    cancelledIDs.add(bookID);
                }
                else {
//
                    totalPrice += price;
                }
            }
            String iDs = selectedOrder.getCancelledIDsAsString(cancelledIDs);
            selectedOrder.setCancelledIDs(iDs);
            selectedOrder.setTotalPrice(totalPrice);
            if (cancelledIDs.stream().count() >= bookIDs.stream().count())
            {
                selectedOrder.setStatus("Cancelled");
                showAlert(Alert.AlertType.INFORMATION, "Order Cancelled", "The order has been cancelled.");
            }
            else {
                selectedOrder.setStatus("Accepted");
                showAlert(Alert.AlertType.INFORMATION, "Order Accepted", "The order has been accepted.");
            }
            updateOrderStatus(selectedOrder);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Only pending orders can be accepted.");
        }
    }
    public void CancelOrder(ActionEvent actionEvent) {
        orderManager = new OrderManager();

        RealOrder selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        System.out.println(selectedOrder);
        if (selectedOrder != null && "Waiting for Admin to Confirm".equals(selectedOrder.getStatus())) {

            selectedOrder.setCancelledIDs(selectedOrder.getBookIds());
            selectedOrder.setTotalPrice(0);
            selectedOrder.setStatus("Cancelled");
            showAlert(Alert.AlertType.INFORMATION, "Order Cancelled", "The order has been cancelled.");

            updateOrderStatus(selectedOrder);

        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Only pending orders can be accepted.");
        }
    }

    // Update the order status in the database
    private void updateOrderStatus(RealOrder selectedOrder) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:bookstore.db")) {
            String updateQuery = "UPDATE Orders SET orderStatus = ?, cancelledIDs = ?,totalPrice = ? WHERE orderId = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                pstmt.setString(1, selectedOrder.getStatus());
                pstmt.setString(2,selectedOrder.getCancelledIDs());
                pstmt.setDouble(3,selectedOrder.getTotalPrice());
                pstmt.setInt(4, selectedOrder.getOrderId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Refresh the table after updating the order status
        ordersTable.refresh();
    }

    // Helper method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void generateReport(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/AdminInventoryView.fxml"));
            Parent root = loader.load();

            AdminInventoryController controller = loader.getController();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            System.err.println("Error loading Admin Inventory View: " + e.getMessage());
        }
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