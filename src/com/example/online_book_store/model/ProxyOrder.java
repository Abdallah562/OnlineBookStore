package com.example.online_book_store.model;

import java.util.List;

public class ProxyOrder implements OrderInterface {
    private RealOrder realOrder;
    private int orderId;
    private int userId;
    private List<Book> books;
    private String booksname;
    private double totalPrice;
    private String status;
    public ProxyOrder(int orderId, int userId, List<Book> books, String booksname, double totalPrice) {
        this.orderId = orderId;
        this.userId = userId;
        this.books = books;
        this.booksname = booksname;
        this.totalPrice = totalPrice;
        this.status = "Pending";
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
    public String getBooksname() {
        return booksname;
    }
    public void setBooksname(String booksname) {
        this.booksname = booksname;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    @Override
    public void confirmOrder() {
        if (realOrder == null) {
            realOrder = new RealOrder(orderId, userId, books, booksname, totalPrice, status);
        }
        realOrder.confirmOrder();
    }
    @Override
    public int getOrderId() {
        return orderId;
    }
    @Override
    public void saveToDatabase() {
        if (realOrder == null) {
            realOrder = new RealOrder(orderId, userId, books, booksname, totalPrice, status);
        }
        realOrder.saveToDatabase();
    }
    @Override
    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String getStatus() {
        return status;
    }
    @Override
    public void setStatus(String status) {
        this.status = status;
        if (realOrder != null) {
            realOrder.setStatus(status);
        }
    }


}
