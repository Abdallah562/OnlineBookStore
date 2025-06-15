package com.example.online_book_store.model;

import java.awt.event.ActionEvent;

public abstract class UserFactory {
    public static User getUser(String type){
        if (type.equalsIgnoreCase("admin"))
            return Admin.getInstance();
        else if (type.equalsIgnoreCase("customer"))
            return Customer.getInstance();
        else return null;
    }
}
