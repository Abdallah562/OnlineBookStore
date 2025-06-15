package com.example.online_book_store.controller;

import com.example.online_book_store.database.ReviewsDatabase;
import com.example.online_book_store.model.Review;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class OrderReviewsController {

    @FXML
    private TableView<Review> reviewsTable;

    @FXML
    private TableColumn<Review, String> commentColumn;
    @FXML
    private TableColumn<Review, String> dateColumn;
    @FXML
    private TextArea commentField;
    @FXML
    private Button submitButton;
    @FXML
    private Button closeButton;

    private int orderID;
    private int userID;
    private final ObservableList<Review> reviewsList = FXCollections.observableArrayList();

    public void initialize() {
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("reviewDate"));

        loadPastReviews();

        commentField.textProperty().addListener((observable, oldValue, newValue) -> {
            submitButton.setDisable(newValue.trim().isEmpty());
        });

        submitButton.setOnAction(event -> submitReview());
        closeButton.setOnAction(event -> closeWindow());
    }
    public void setIDs(int orderID, int userID) {
        this.orderID = orderID;
        this.userID=userID;
        loadPastReviews();
    }
    private void loadPastReviews() {
        reviewsList.clear();
        reviewsList.addAll(ReviewsDatabase.getReviewsByOrderID(orderID)); // Fetch reviews from the database
        reviewsTable.setItems(reviewsList);
    }
    private void submitReview() {
        try {
            String comment = commentField.getText().trim();
            if (comment.isEmpty() || comment.length() < 5 || comment.length() > 500) {
                throw new IllegalArgumentException("Comment must be between 5 and 500 characters.");
            }
            String reviewDate = java.time.LocalDate.now().toString();
            Review review = new Review(orderID, userID, comment, reviewDate);
            ReviewsDatabase.addReview(review);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Review Submitted");
            alert.setHeaderText(null);
            alert.setContentText("Thank you for your feedback!");
            alert.showAndWait();

            loadPastReviews();

            commentField.clear();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
