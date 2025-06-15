package com.example.online_book_store.database;

//import com.example.online_book_store.manager.DatabaseManager;
import com.example.online_book_store.model.User;

import java.sql.*;
import java.util.Map;

public class UsersDataBase implements DataBase<User> {
    static UsersDataBase usersDataBase;
    private UsersDataBase(){
        initializeDatabase();
    }

    public static DataBase<User> getInstance() {
        if (usersDataBase == null){
            synchronized (UsersDataBase.class) {
                if (usersDataBase == null) {
                    usersDataBase = new UsersDataBase();
                }
            }
        }
        return usersDataBase;
    }
    @Override
    public void initializeDatabase() {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL,
                    password TEXT NOT NULL,
                    email TEXT NOT NULL,
                    phone TEXT NOT NULL,
                    role TEXT NOT NULL
                );
                """;


        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(createTableSQL);

            System.out.println("Users Database initialized!");

        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    @Override
    public Map<Integer, User> loadData() {
        return null;
    }

    public static void registerUser(String username, String password,String email,String phone, String role) {
        String insertSQL = "INSERT INTO users (username, password, email, phone, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, role);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
    }
}
