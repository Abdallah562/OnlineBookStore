package com.example.online_book_store.database;

import com.example.online_book_store.model.Book;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class BooksDataBase implements DataBase<Book> {
    static BooksDataBase booksDataBase;
    private BooksDataBase(){initializeDatabase();}
    public static DataBase<Book> getInstance() {
        if (booksDataBase == null){
            booksDataBase = new BooksDataBase();
        }
        return booksDataBase;
    }
    @Override
    public void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Books (" +
                " bookID INTEGER PRIMARY KEY," +
                " title TEXT," +
                " author TEXT," +
                " category TEXT," +
                " price REAL," +
                " popularity INTEGER," +
                " quantity INTEGER, " +
                " edition TEXT, " +
                " numofPages INTEGER, " +
                " image BLOB)";


        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(createTableSQL);

            System.out.println("Books Database initialized!");

        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }
    public Map<Integer,Book> loadData(){
        Map<Integer,Book> books = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {
            Book book = null;

            ResultSet rs = stmt.executeQuery("SELECT * FROM Books");
            while (rs.next()) {
                book = new Book();
                book.setBookID(rs.getInt("bookID"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setCategory(rs.getString("category"));
                book.setPrice(rs.getFloat("price"));
                book.setPopularity(rs.getInt("popularity"));
                book.setQuantity(rs.getInt("quantity"));
                book.setEdition(rs.getString("edition"));
                book.setNumOfPages(rs.getInt("numofPages"));
                book.setImage(rs.getBytes("image"));

                books.put(book.getBookID(),book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }
    public static String updateRecord(Book book) {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL)){
            String checkQuery = "SELECT 1 FROM books WHERE bookID = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, book.getBookID());
                ResultSet rs = checkStmt.executeQuery();
                if (isRecordExists(conn,book)){
                    return "Book is Exist";
                }
                if (rs.next()) {
                    // If bookID exists, update the record
                    String updateQuery = "UPDATE books SET bookID = ?, title = ?, author = ?, category = ?, price = ?, popularity = ?, " +
                            "quantity = ?, edition = ?, numofPages = ?, image = ? WHERE bookID = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, book.getBookID());
                        updateStmt.setString(2, book.getTitle());  // Assuming Book has a getTitle() method
                        updateStmt.setString(3, book.getAuthor()); // Assuming Book has a getAuthor() method
                        updateStmt.setString(4, book.getCategory()); // Assuming Book has a getCategory() method
                        updateStmt.setFloat(5, book.getPrice());    // Assuming Book has a getPrice() method
                        updateStmt.setInt(6, book.getPopularity());
                        updateStmt.setInt(7, book.getQuantity());
                        updateStmt.setString(8, book.getEdition());
                        updateStmt.setInt(9, book.getNumOfPages());
                        updateStmt.setBytes(10,book.getImage());
                        updateStmt.setInt(11, book.getBookID());

                        updateStmt.executeUpdate();
                        System.out.println("Book updated successfully.");
                        return "Book updated successfully.";
                    }
                }
            }
        }
         catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String deleteRecord(int bookID) {
        String deleteQuery = "DELETE FROM books WHERE bookID = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

            pstmt.setInt(1, bookID);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                return "Record with bookID " + bookID + " deleted successfully.";
            } else {
                return "No record found with bookID " + bookID + ".";
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String addRecord(Book book){
        System.out.println("Adding Record");

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {

//            // Create a table
//            stmt.execute("CREATE TABLE IF NOT EXISTS Books (" +
//                    " bookID INTEGER PRIMARY KEY," +
//                    " title TEXT," +
//                    " author TEXT," +
//                    " category TEXT," +
//                    " price REAL," +
//                    " popularity INTEGER," +
//                    " quantity INTEGER, " +
//                    " edition TEXT, " +
//                    " numofPages INTEGER)");

            if(isRecordExists(conn,book)){
                System.out.println("The Record is Exist are you Mean edit Record");
                return "The Record is Exist are you Mean edit Record";
            }
            // Insert data
            String insertSQL = "INSERT INTO Books (bookID,title,author,category,price,popularity,quantity,edition,numofPages,image) VALUES (?,?,?,?,?,?,?,?,?,?)";

            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                System.out.println(book);

                pstmt.setInt(1, book.getBookID());
                pstmt.setString(2, book.getTitle());  // Assuming Book has a getTitle() method
                pstmt.setString(3, book.getAuthor()); // Assuming Book has a getAuthor() method
                pstmt.setString(4, book.getCategory()); // Assuming Book has a getCategory() method
                pstmt.setFloat(5, book.getPrice());    // Assuming Book has a getPrice() method
                pstmt.setInt(6, book.getPopularity());
                pstmt.setInt(7,book.getQuantity());
                pstmt.setString(8,book.getEdition());
                pstmt.setInt(9,book.getNumOfPages());
                pstmt.setBytes(10,book.getImage());

                pstmt.executeUpdate();  // Insert data
                return "Record Inserted Successfully";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Couldn't insert Record";
        }
    }
    private static boolean isRecordExists(Connection conn, Book book) throws SQLException {
        String query = "SELECT 1 FROM Books WHERE title = ? AND" +
        " author = ? AND category = ? AND price = ? AND popularity = ? AND" +
        " quantity = ? AND edition = ? AND numofPages = ? AND image = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getCategory());
            pstmt.setFloat(4, book.getPrice());
            pstmt.setInt(5, book.getPopularity());
            pstmt.setInt(6,book.getQuantity());
            pstmt.setString(7,book.getEdition());
            pstmt.setInt(8,book.getNumOfPages());
            pstmt.setBytes(9,book.getImage());

            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if a record is found
        }
    }
}
