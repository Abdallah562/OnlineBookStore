package com.example.online_book_store.manager;

import com.example.online_book_store.model.Admin;
import com.example.online_book_store.model.Book;

import java.util.Map;

public class OrderManager {
    Admin admin;
    public OrderManager(){
        admin = Admin.getInstance();

    }
    public float checkBookAvailability(int bookID,int quantity){
        Map<Integer, Book> books = admin.inventoryManager.allBooks;

        Book book = books.get(bookID);
        int availableQuantity = book.getQuantity();
        if (availableQuantity >= quantity){
            book.setQuantity(availableQuantity - quantity);
            books.replace(bookID,book);

            admin.editBook(book);
            System.out.println("Order Placed Successfully");
            return book.getPrice();
        }
        System.out.println("The Required quantity isn't available");
        return 0;
    }

}
