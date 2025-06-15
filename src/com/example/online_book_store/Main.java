package com.example.online_book_store;

import com.example.online_book_store.database.*;
//import com.example.online_book_store.manager.DatabaseManager;
import com.example.online_book_store.model.Book;
import com.example.online_book_store.model.RealOrder;
import com.example.online_book_store.model.Review;
import com.example.online_book_store.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.security.spec.ECField;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            DataBase<Book> booksDataBase = DataBaseFactory.getDataBase(DatabaseType.BOOKS);
            DataBase<User> usersDataBase = DataBaseFactory.getDataBase(DatabaseType.USERS);
            DataBase<RealOrder> orderDataBase = DataBaseFactory.getDataBase(DatabaseType.ORDERS);
            DataBase<Review> reviewDataBase = DataBaseFactory.getDataBase(DatabaseType.REVIEWS);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/SignIn.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setTitle("Online Book Store");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
