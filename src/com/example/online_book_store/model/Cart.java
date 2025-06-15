package com.example.online_book_store.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Cart {
    private ObservableList<Book> cartItems;
    static Cart cart;

    public static Cart getInstance(){
        if (cart == null)
            cart = new Cart();
        return cart;
    }
    private Cart() {
        this.cartItems = FXCollections.observableArrayList();
    }
    public ObservableList<Book> getCartItems() {
        return cartItems;
    }
    public void addBook(Book book) {
        cartItems.add(book); // Consider checking for duplicates before adding a book to avoid multiple entries of the same item.
    }
    public void editBook(Book oldBook, Book newBook) {
        int index = cartItems.indexOf(oldBook);
        if (index != -1) {
            cartItems.set(index, newBook);
        } // Add error handling or a return value if oldBook is not found, to improve reliability.
    }
    public void removeBook(Book book) {
        cartItems.remove(book); // Ensure proper handling if the book is not in the cart to avoid unintended behavior.
    }
    public void clearCart() {
        cartItems.clear();
    }
    public double getTotalPrice() {
        double total = 0;
        for (Book book : cartItems) {
            total += book.getPrice(); // Ensure that book.getPrice() cannot return null to avoid potential NullPointerExceptions.
        }
        return total; // Consider using a stream-based calculation for cleaner code: cartItems.stream().mapToDouble(Book::getPrice).sum();
    }
    public String getBooksname() {
        String booksname = "";

        for (Book book : cartItems) {
            if (booksname.length() > 0) {

                booksname += ", ";
            }
            booksname += book.getTitle(); // Add book id to the list
        }
        return booksname; // Consider using a stream-based calculation for cleaner code: cartItems.stream().mapToDouble(Book::getPrice).sum();
    }
}
