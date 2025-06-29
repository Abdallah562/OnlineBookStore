package com.example.online_book_store.model;

import javafx.event.ActionEvent;

public abstract class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String role;
    public User(){}
    private User(int id, String username, String password, String email, String phone,String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }
    private User(int id, String username, String password, String email, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }
    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getRole() {return role;    }
    public void setRole(String role) {this.role = role; }

    // Optional: Override toString() for easier debugging
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", address='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public abstract void loadView(ActionEvent event, User user);
}
