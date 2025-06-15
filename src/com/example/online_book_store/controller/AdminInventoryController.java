package com.example.online_book_store.controller;

import com.example.online_book_store.manager.InventoryManager;
import com.example.online_book_store.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class AdminInventoryController {
    @FXML
    private TableView<Book> inventoryTable;
    @FXML
    private TableColumn<Book, Integer> bookIdColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> categoryColumn;
    @FXML
    private TableColumn<Book, Float> priceColumn;
    @FXML
    private TableColumn<Book, Integer> popularityColumn;
    @FXML
    private TableColumn<Book, String> editionColumn;
    @FXML
    private TableColumn<Book, Integer> numOfPagesColumn;
    @FXML
    private TableColumn<Book, Integer> stockColumn;
    @FXML
    private Label totalRevenueLabel;
    @FXML
    private Label totalStockLabel;
    @FXML
    private Label topBooksLabel;
    @FXML
    private Label outOfStockBooksLabel;

    // This method will be triggered when the "Generate Report" button is clicked
    @FXML
    public void generateReport() {
        InventoryManager inventoryManager = InventoryManager.getInstance();
        Map<Integer, Book> allBooks = inventoryManager.allBooks;

        if (allBooks == null || allBooks.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        // Populate the TableView with book data
        ObservableList<Book> bookList = FXCollections.observableArrayList(allBooks.values());
        inventoryTable.setItems(bookList);

        // Set up the columns using PropertyValueFactory
        initialize();

        // Calculate total revenue and total stock
        float totalRevenue = 0;
        int totalStock = 0;
        for (Book book : allBooks.values()) {
            totalRevenue += book.getPrice() * book.getQuantity();
            totalStock += book.getQuantity();
        }

        // Update statistical labels
        totalRevenueLabel.setText(String.format("Total Revenue: $%.2f", totalRevenue));
        totalStockLabel.setText("Total Stock: " + totalStock + " books");

        // Show top-priced books
        List<Book> topPricedBooks = allBooks.values().stream()
                .sorted(Comparator.comparingDouble(Book::getPrice).reversed())
                .limit(3)
                .toList();

        StringBuilder topBooksText = new StringBuilder("");
        for (Book book : topPricedBooks) {
            topBooksText.append("- ").append(book.getTitle()).append(" ($").append(book.getPrice()).append(")\n");
        }
        topBooksLabel.setText(topBooksText.toString());

        // List books that are out of stock
        List<Book> outOfStockBooks = allBooks.values().stream()
                .filter(book -> book.getQuantity() == 0)
                .toList();

        if (outOfStockBooksLabel != null) {
            if (!outOfStockBooks.isEmpty()) {
                StringBuilder outOfStockText = new StringBuilder("Out of Stock Books:\n");
                for (Book book : outOfStockBooks) {
                    outOfStockText.append("- ").append(book.getTitle()).append(" (ID: ").append(book.getBookID()).append(")\n");
                }
                outOfStockBooksLabel.setText(outOfStockText.toString());
            } else {
                outOfStockBooksLabel.setText("No books are currently out of stock.");
            }
        } else {
            System.out.println("outOfStockBooksLabel is null. Please check FXML injection.");
        }
    }

    // Initialize method to set up table columns using PropertyValueFactory
    private void initialize() {
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        popularityColumn.setCellValueFactory(new PropertyValueFactory<>("popularity"));
        editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
        numOfPagesColumn.setCellValueFactory(new PropertyValueFactory<>("numOfPages"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    public void navigateToAdminView(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/AdminView.fxml"));
            Parent root = loader.load();

            AdminViewController controller = loader.getController();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void navigateToOrdersManagerView(ActionEvent actionEvent) {
        try {
            Object root = FXMLLoader.load(getClass().getResource("/com/example/online_book_store/view/AdminOrdersView.fxml"));
            Scene scene = new Scene((Parent) root);
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
