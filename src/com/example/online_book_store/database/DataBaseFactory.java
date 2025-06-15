package com.example.online_book_store.database;

public class DataBaseFactory<T> {
    public static<T> DataBase<T> getDataBase(DatabaseType type){
        if (type==DatabaseType.BOOKS){
            return (DataBase<T>) BooksDataBase.getInstance();
        } else if (type == DatabaseType.USERS) {
            return (DataBase<T>) UsersDataBase.getInstance();
        } else if (type == DatabaseType.ORDERS) {
            return (DataBase<T>) OrdersDatabase.getInstance();
        } else if (type == DatabaseType.REVIEWS) {
            return (DataBase<T>) ReviewsDatabase.getInstance();
        }
        throw new IllegalArgumentException("Unknown database type: " + type);
    }
}

