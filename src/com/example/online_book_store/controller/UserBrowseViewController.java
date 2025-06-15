package com.example.online_book_store.controller;

import com.example.online_book_store.controller.UserOrdersViewController;
import com.example.online_book_store.manager.InventoryManager;
import com.example.online_book_store.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class UserBrowseViewController {

    private User currentUser;
    private Cart userCart = Cart.getInstance();
    // FXML elements
    @FXML
    private TilePane browseBooksPane;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> categoryFilter;
    @FXML
    private ComboBox<String> sortOptions;
    @FXML
    private Button proceedToCheckoutButton;
    private ObservableList<Book> allBooks;

    public void setUserData() {
        this.currentUser = (User) UserFactory.getUser("customer");
        System.out.println("User Data in Browse Books View: ID=" + currentUser.getId() + ", Username=" + currentUser.getUsername());
    }

    // Initialize table and filters
    public void initialize() {
        Map<Integer,Book> books = InventoryManager.getInstance().allBooks;
        allBooks = FXCollections.observableArrayList(InventoryManager.getInstance().allBooks.values());
        populateBooksPane(allBooks);
        categoryFilter.setItems(getUniqueCategories());

        categoryFilter.setValue("All");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterBooks(newValue, categoryFilter.getValue()));
        categoryFilter.setOnAction(e -> filterBooks(searchField.getText(), categoryFilter.getValue()));
        sortOptions.setOnAction(e -> sortBooks(sortOptions.getValue()));
    }
    private ObservableList<String> getUniqueCategories() {
        Collection<Book> books = InventoryManager.getInstance().allBooks.values();

        Set<String> categorySet = books.stream()
                .map(Book::getCategory) // Get the category of each book
                .filter(Objects::nonNull) // Ignore null categories
                .collect(Collectors.toSet());

        categorySet.add("All");
        return FXCollections.observableArrayList(categorySet);
    }
    private VBox createBookCover(String title, byte[] imageData) {
        ByteArrayInputStream imageStream = new ByteArrayInputStream(imageData);

        Image bookCoverImage = new Image(imageStream, 100, 150, false, false);
        ImageView bookCover = new ImageView(bookCoverImage);

        Label bookTitle = new Label(title);

        VBox bookItem = new VBox(5, bookCover, bookTitle);
        bookItem.setStyle("-fx-alignment: center; -fx-border-color: black; -fx-padding: 5; -fx-margin:50; ");

        return bookItem;
    }
    private void navigateToBookDetails(MouseEvent actionEvent, Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/UserBookDetailsPrinter.fxml"));
            Parent root = loader.load();

            UserBookDetailsPrinterController controller = loader.getController();
            controller.initialize(book);

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void filterBooks(String searchQuery, String category) {
        ObservableList<Book> allBooks = FXCollections.observableArrayList(InventoryManager.getInstance().allBooks.values());

        ObservableList<Book> filteredList = allBooks.filtered(book -> {
            boolean matchesSearchQuery = (searchQuery == null || searchQuery.isEmpty()) ||
                    book.getTitle().toLowerCase().contains(searchQuery.toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(searchQuery.toLowerCase());
            boolean matchesCategory = (category == null || category.equalsIgnoreCase("All")) ||
                    book.getCategory().equalsIgnoreCase(category);

            return matchesSearchQuery && matchesCategory;
        });

        // Update the TilePane
        browseBooksPane.getChildren().clear();
        for (Book book : filteredList) {
            VBox bookCover = createBookCover(book.getTitle(), book.getImage());
            bookCover.setUserData(book); // Store book for sorting and navigation
            bookCover.setOnMouseClicked(event -> navigateToBookDetails(event, book)); // Handle click
            browseBooksPane.getChildren().add(bookCover);
        }
    }
    private void sortBooks(String criterion) {
        if (criterion == null || browseBooksPane.getChildren().isEmpty()) {
            return;
        }

        List<Book> booksToSort = browseBooksPane.getChildren().stream()
                .filter(node -> node instanceof VBox) // Filter only VBox nodes
                .map(node -> (Book) ((VBox) node).getUserData()) // Map VBox to Book
                .filter(Objects::nonNull) // Remove null userData
                .collect(Collectors.toList());

        if (criterion.equals("Popularity")) {
            booksToSort.sort((b1, b2) -> Integer.compare(b2.getPopularity(), b1.getPopularity()));
        } else if (criterion.equals("Price")) {
            booksToSort.sort(Comparator.comparingDouble(Book::getPrice));
        }
        populateBooksPane(FXCollections.observableArrayList(booksToSort));
    }
    private void populateBooksPane(ObservableList<Book> books) {
        browseBooksPane.getChildren().clear();
        for (Book book : books) {
            VBox bookCover = createBookCover(book.getTitle(), book.getImage());
            bookCover.setUserData(book); // Attach the book to the VBox as userData
            bookCover.setOnMouseClicked(event -> navigateToBookDetails(event, book)); // Add event to navigate to details
            browseBooksPane.getChildren().add(bookCover); // Add the VBox to the TilePane
        }
    }
    static int generateOrderId() {
        return (int) (System.currentTimeMillis() % 100000);
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
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void goToHome(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/Home.fxml"));
            Parent root = loader.load();

            HomeController controller = loader.getController();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
