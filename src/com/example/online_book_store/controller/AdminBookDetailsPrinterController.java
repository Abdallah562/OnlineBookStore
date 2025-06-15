package com.example.online_book_store.controller;

import com.example.online_book_store.model.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;

public class AdminBookDetailsPrinterController {
    Book book;
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

    public void initialize(Book book){
        this.book = book;
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
    public void navigateToBooksPrinter(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/AdminBooksPrinterView.fxml"));
            Parent root = loader.load();

            AdminBooksPrinterController controller = loader.getController();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void navigateToEditBook(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/EditBook.fxml"));
            Parent root = loader.load();

            AdminViewController controller = loader.getController();
            controller.setBookDetails(book);

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void navigateToDeleteBook(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/DeleteBook.fxml"));
            Parent root = loader.load();

            AdminViewController controller = loader.getController();
            controller.setBookDetails(book);

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
