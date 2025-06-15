package com.example.online_book_store.model;

public class Review {
    private int reviewID;
    private int orderID;
    private int userID;
    private String comment;
    private String reviewDate;

    // Constructor with all fields
    public Review(int reviewID, int orderID, int userID,  String comment, String reviewDate) {
        this.reviewID = reviewID;
        this.orderID = orderID;
        this.userID = userID;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }
    // Constructor with default reviewID
    public Review(int orderID, int userID, String comment, String reviewDate) {
        this.orderID = orderID;
        this.userID = userID;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }
    // Getters and setters for all fields
    public int getReviewID() {
        return reviewID;
    }
    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }
    public int getOrderID() {
        return orderID;
    }
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getReviewDate() {
        return reviewDate;
    }
    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }
}
