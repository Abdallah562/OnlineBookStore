package com.example.online_book_store.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;

public class RealOrder implements OrderInterface {
    private String bookIds;
    private String cancelledIDs;
    private int orderId;
    private int userId;
    private List<Book> books;
    private String booksname;
    private double totalPrice;
    private LocalDateTime orderDate;
    private String status;
    private String bookIdsAsString;
    public RealOrder(int orderId, int userId, List<Book> books, String booksname, double totalPrice, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.books = books;
        this.booksname = booksname;
        this.totalPrice = totalPrice;
        this.orderDate = LocalDateTime.now();
        this.status = status;
    }
    public RealOrder(int orderId, int userId, String books, String booksname, double totalPrice, String orderDate, String status, String cancelledIDs) {
        this.orderId = orderId;
        this.userId = userId;
        this.bookIds = books;
        this.booksname= booksname;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDate=LocalDateTime.parse(orderDate);
        this.cancelledIDs = cancelledIDs;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getBookIds() {
        return bookIds;
    }
    public String getBooksname() {
        return booksname;
    }
    public void setBooksname(String booksname) {
        this.booksname = booksname;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    public void setBookIds(String bookIds) {this.bookIds = bookIds;}
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
    public String getCancelledIDsAsString(List<String> cancelledIDs) {return String.join(", ", cancelledIDs);}
    public String getCancelledIDs() {return cancelledIDs;}
    public void setCancelledIDs(String  cancelledIDs) {this.cancelledIDs = cancelledIDs;}
    public String getBookIdsAsString() {return bookIdsAsString;}
    public void setBookIdsAsString(String bookIdsAsString) {this.bookIdsAsString = bookIdsAsString;}
    @Override
    public void confirmOrder() {System.out.println("Order confirmed: " + this);}
    @Override
    public int getOrderId() {return orderId;}
    @Override
    public void saveToDatabase() {
        String booksList = "";
        for (Book book : books) {
            if (booksList.length() > 0) {
                booksList += ", ";

            }
            booksList += book.getBookID(); // Add book id to the list


        }
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:bookstore.db")) {
            String query = "INSERT INTO Orders (orderID, userID, books, booksname, totalPrice, orderDate, orderStatus) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = ((java.sql.Connection) conn).prepareStatement(query)) {
                stmt.setInt(1, orderId);
                stmt.setInt(2, userId);
                stmt.setString(3, booksList);
                stmt.setString(4, booksname);
                stmt.setDouble(5, totalPrice);
                stmt.setString(6, orderDate.toString());
                stmt.setString(7, status);
                stmt.executeUpdate();
            }
            System.out.println("Order saved to database: " + this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public double getTotalPrice() {return totalPrice;}

    @Override
    public String getStatus() {return status;}

    @Override
    public void setStatus(String status) {this.status = status;}

    @Override
    public String toString() {
        return "RealOrder{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                '}';
    }
}