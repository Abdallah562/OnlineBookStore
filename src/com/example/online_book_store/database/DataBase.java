package com.example.online_book_store.database;

import com.example.online_book_store.model.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public interface DataBase<T> {
    String DATABASE_URL = "jdbc:sqlite:bookstore.db";
    void initializeDatabase();
    public default Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    Map<Integer, T> loadData();
}
