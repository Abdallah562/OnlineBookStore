package com.example.online_book_store.controller;

import com.example.online_book_store.model.Admin;

import com.example.online_book_store.model.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.ByteArrayInputStream;
import java.util.Map;

public class AdminBooksPrinterController {
    Admin admin;
    @FXML
    private TilePane browseBooksPane;
    @FXML
    public void initialize(){
        admin = Admin.getInstance();
        Map<Integer, Book> books = admin.inventoryManager.allBooks;

        for (Book book : books.values()) {
            VBox bookItem = createBookCover(book.getTitle(), book.getImage());
            bookItem.setOnMouseClicked(event -> {
                navigateToBookDetails(event, book);
            });
            browseBooksPane.getChildren().add(bookItem);
        }
    }
    private void navigateToBookDetails(MouseEvent actionEvent, Book book) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/AdminBookDetailsPrinter.fxml"));
            Parent root = loader.load();

            // Get the controller instance
            AdminBookDetailsPrinterController controller = loader.getController();
            controller.initialize(book);

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root,400,700));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private VBox createBookCover(String title, byte[] imageData) {
        // Convert byte array to InputStream
        ByteArrayInputStream imageStream = new ByteArrayInputStream(imageData);

        // Create an image for the book cover from InputStream
        Image bookCoverImage = new Image(imageStream, 100, 150, false, false);
        ImageView bookCover = new ImageView(bookCoverImage);

        // Create a label for the book title
        Label bookTitle = new Label(title);

        // Wrap the image and title in a VBox
        VBox bookItem = new VBox(5, bookCover, bookTitle);
        bookItem.setStyle("-fx-alignment: center; -fx-border-color: black; -fx-padding: 5; -fx-margin:50; ");

        return bookItem;
    }
    public void navigateToAdminView(ActionEvent actionEvent) {

        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/AdminView.fxml"));
            Parent root = loader.load();

            AdminViewController controller = loader.getController();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
