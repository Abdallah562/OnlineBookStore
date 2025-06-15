package com.example.online_book_store.model;

public interface OrderInterface {
    void confirmOrder();
    void saveToDatabase();
    double getTotalPrice();
    String getStatus();
    void setStatus(String status);
    int getOrderId();
    String getBooksname();
}
