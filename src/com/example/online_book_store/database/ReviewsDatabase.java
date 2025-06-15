package com.example.online_book_store.database;

import com.example.online_book_store.model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReviewsDatabase implements DataBase<Review> {
    public static ReviewsDatabase reviewsDatabase;
    public static ReviewsDatabase getInstance() {
        if (reviewsDatabase == null)
            reviewsDatabase = new ReviewsDatabase();
        return reviewsDatabase;
    }
    private ReviewsDatabase(){initializeDatabase();}
    @Override
    public void initializeDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS Reviews (
                reviewID INTEGER PRIMARY KEY AUTOINCREMENT,
                orderID INTEGER NOT NULL,
                userID INTEGER NOT NULL,
                comment TEXT NOT NULL,
                reviewDate TEXT NOT NULL,
                FOREIGN KEY (orderID) REFERENCES Orders(orderID)
            );
        """;

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Reviews Database initialized successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public Map<Integer, Review> loadData() {
        return null;
    }
    public static void addReview(Review review) {
        String sql = "INSERT INTO Reviews (orderID, userID, comment, reviewDate) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, review.getOrderID());
            pstmt.setInt(2, review.getUserID());
            pstmt.setString(3, review.getComment());
            pstmt.setString(4, review.getReviewDate());

            pstmt.executeUpdate();
            System.out.println("Review added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<Review> getReviewsByOrderID(int orderID) {
        String sql = "SELECT * FROM Reviews WHERE orderID = ?";
        List<Review> reviews = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int reviewID = rs.getInt("reviewID");
                int userID = rs.getInt("userID");
                String comment = rs.getString("comment");
                String reviewDate = rs.getString("reviewDate");

                reviews.add(new Review(reviewID, orderID, userID, comment, reviewDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }


}
