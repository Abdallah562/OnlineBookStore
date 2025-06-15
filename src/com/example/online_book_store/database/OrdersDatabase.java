package com.example.online_book_store.database;


import com.example.online_book_store.model.OrderInterface;
import com.example.online_book_store.model.RealOrder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrdersDatabase implements DataBase<RealOrder> {

    public static OrdersDatabase ordersDatabase;
    public static OrdersDatabase getInstance(){
        if (ordersDatabase == null)
            ordersDatabase = new OrdersDatabase();
        return ordersDatabase;
    }
    private OrdersDatabase(){initializeDatabase();}
    @Override
    public void initializeDatabase(){
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS Orders (" +
                    " orderID INTEGER NOT NULL," +
                    " userID INTEGER NOT NULL," +
                    " books TEXT NOT NULL," +
                    " booksname TEXT NOT NULL," + // Storing books as a comma-separated string or serialized data
                    " totalPrice REAL NOT NULL," +     // Total price for the order
                    " orderDate TEXT NOT NULL," +      // Date the order was placed, stored as text (ISO format)
                    " orderStatus TEXT NOT NULL," +
                    " cancelledIDs TEXT, " +    // Status of the order (e.g., 'Pending', 'Confirmed')
                    " PRIMARY KEY (orderID));";        // order


            stmt.execute(sql);
            System.out.println("Orders Database initialized successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public Map<Integer, RealOrder> loadData() {
        return null;
    }
    public static List<OrderInterface> fetchOrdersByUserId(int userId) {
        List<OrderInterface> orders = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT * FROM Orders WHERE userID = " + userId;
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int orderId = rs.getInt("orderID");
                String books = rs.getString("books"); // Books stored as a string
                String booksname = rs.getString("booksname"); // Books stored as a string
                double totalPrice = rs.getDouble("totalPrice");
                String orderDate = rs.getString("orderDate");
                String status = rs.getString("orderStatus");
                String cancelledIDs = rs.getString("cancelledIDs");

                // Create an Order object and add it to the list
                OrderInterface order = new RealOrder(orderId, userId, books,booksname, totalPrice, orderDate, status, cancelledIDs);
                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }
    public static boolean updateOrderStatus(int orderId, String newStatus) {
        String sql = "UPDATE Orders SET orderStatus = ? WHERE orderID = ?";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, orderId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if at least one row was updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}