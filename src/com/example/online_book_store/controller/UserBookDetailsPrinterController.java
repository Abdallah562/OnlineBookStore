package com.example.online_book_store.controller;

import com.example.online_book_store.model.*;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.example.online_book_store.controller.AdminViewController.showAlert;
import static com.example.online_book_store.controller.UserBrowseViewController.generateOrderId;

public class UserBookDetailsPrinterController {
    Book book;
    Cart userCart;
    @FXML
    VBox bookDetailsPane;
    @FXML
    ImageView bookCover;
    @FXML
    Label titleLabel;
    @FXML
    Label authorLabel;
    @FXML
    Label categoryLabel;
    @FXML
    Label priceLabel;
    @FXML
    Label popularityLabel;
    @FXML
    Label quantityLabel;
    @FXML
    Label editionLabel;
    @FXML
    Label numofPagesLabel;
    @FXML
    Button proceedToCheckoutButton;

    public void initialize(Book book){
        this.book = book;
        userCart = Cart.getInstance();
        // Display the book cover in details
        ByteArrayInputStream imageStream = new ByteArrayInputStream(book.getImage());
        Image bookCoverImage = new Image(imageStream, 200, 300, false, false);
        bookCover.setImage(bookCoverImage);

        titleLabel.setText("Title: "+book.getTitle());
        authorLabel.setText("Author: "+book.getAuthor());
        categoryLabel.setText("Category: "+book.getCategory());
        priceLabel.setText("Price: "+book.getPrice()+"$");
        popularityLabel.setText("Popularity: "+book.getPopularity());
        quantityLabel.setText("Quantity: "+book.getQuantity());
        editionLabel.setText("Edition: "+book.getEdition());
        numofPagesLabel.setText("Num of Pages: "+book.getNumOfPages());

    }
    public void navigateToBrowseBooks(ActionEvent actionEvent) {
        try {
            // Create an instance of UserBrowseView and pass the current user
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/UserBrowseView.fxml"));
            Parent root = loader.load();

            // Pass the user data to UserBrowseView
            UserBrowseViewController controller = loader.getController();
            controller.setUserData();

            // Navigate to Browse Books View
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Browse Books");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addToCart(ActionEvent actionEvent) {
        System.out.println("Adding"+book);
        if (book != null) {
            userCart.addBook(book);
            showAlert(Alert.AlertType.INFORMATION, "Book Added", "The book was added to your cart.");
        } else {
            showAlert(Alert.AlertType.WARNING, "No Book Selected", "Please select a book to add to the cart.");
        }
    }
    public void removefromCart(ActionEvent actionEvent) {
        System.out.println("Removing"+book);
        if (book != null && userCart.getCartItems().contains(book)) {
            userCart.removeBook(book);
            showAlert(Alert.AlertType.INFORMATION, "Book Removed", "The book has been removed from your cart.");
        } else {
            showAlert(Alert.AlertType.WARNING, "No Book Selected", "Error while removing book.");
        }
    }
    @FXML
    private void proceedToCheckout() {
        if (userCart.getCartItems().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cart Empty", "Your cart is empty. Add books to proceed.");
            return;
        }

        double totalPrice = userCart.getCartItems().stream().mapToDouble(Book::getPrice).sum();
        ProxyOrder proxyOrder = new ProxyOrder(generateOrderId(), ((User)UserFactory.getUser("customer")).getId(), userCart.getCartItems(), userCart.getBooksname(),totalPrice);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/UserOrdersView.fxml"));
            BorderPane root = loader.load();

            UserOrdersViewController controller = loader.getController();
            controller.loadUserOrders(FXCollections.observableArrayList(proxyOrder)); // Pass the order to the table

            Scene scene = new Scene(root);
            Stage stage = (Stage) proceedToCheckoutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
