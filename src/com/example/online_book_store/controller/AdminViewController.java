package com.example.online_book_store.controller;
import com.example.online_book_store.Main;
import com.example.online_book_store.database.OrdersDatabase;
import com.example.online_book_store.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class AdminViewController {
    Book book;
    Admin admin ;
    String response;
    @FXML
    private TextField bookIDField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField popularityField;
    @FXML
    private TextField editionField;
    @FXML
    private TextField numOfPagesField;
    @FXML
    private TextField quantityField;
    @FXML
    private ImageView imageView;
    @FXML
    private ComboBox<String> books;

    public AdminViewController(){
        admin = Admin.getInstance();
    }
    void setBookDetails(Book book){
        System.out.println(book);
        bookIDField.setText(String.valueOf(book.getBookID()));
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        categoryField.setText(book.getCategory());
        priceField.setText(String.valueOf(book.getPrice()));
        popularityField.setText(String.valueOf(book.getPopularity()));
        editionField.setText(book.getEdition());
        numOfPagesField.setText(String.valueOf(book.getNumOfPages()));
        quantityField.setText(String.valueOf(book.getQuantity()));
        imageView.setImage(new Image(new ByteArrayInputStream(book.getImage())));
    }
    @FXML
    void editBook(ActionEvent event){
        System.out.println("Edit BOOK");
        book = getBookDetails();
        response = admin.editBook(book);
        showAlert(Alert.AlertType.INFORMATION,"Edit Book Details",response);
    }
    public void DeleteBook(ActionEvent actionEvent) {
        System.out.println("Delete BOOk");

        book = getBookDetails();
        response = admin.deleteBook(book.getBookID());
        showAlert(Alert.AlertType.INFORMATION,"Delete Book Details",response);
    }
    @FXML
    public void initialize(){
        if (admin == null)
            admin = Admin.getInstance();
        if(books == null)
            books = new ComboBox<>();


        for (Book book : admin.inventoryManager.allBooks.values()) {
            books.getItems().add(book.getBookID()+" - "+book.getTitle());
        }
        books.setOnAction(e -> {
            String[] selected = books.getValue().split(" - ");
            setBookDetails(admin.inventoryManager.allBooks.get(Integer.parseInt(selected[0])));
        });
    }
    private Book getBookDetails() {
        try{
            book = new Book(titleField.getText(),
                    authorField.getText(),
                    categoryField.getText(),
                    Float.parseFloat(priceField.getText()),
                    Integer.parseInt(popularityField.getText()),
                    editionField.getText(),
                    Integer.parseInt(numOfPagesField.getText()),
                    Integer.parseInt(quantityField.getText()),
                    imageToByteArray(imageView.getImage())
            );
            book.setBookID(Integer.parseInt(bookIDField.getText()));
        } catch (Exception e){
            e.printStackTrace();
        }
        return book;
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
    public void navigateToMainView(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        Main main = new Main();
        main.start(stage);
    }
    public void printBooks(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/AdminBooksPrinterView.fxml"));
            Parent root = loader.load();

            AdminBooksPrinterController controller = loader.getController();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root,800,600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void navigateToAddBookView(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/AddBook.fxml"));
            Parent root = loader.load();

            AdminViewController controller = loader.getController();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addBook(ActionEvent actionEvent) {
        try{
            if (imageView.getImage()==null){
                showAlert(Alert.AlertType.ERROR, "Book Status","Please insert image");
                return;
            }
            book = new Book(titleField.getText(),
                    authorField.getText(),
                    categoryField.getText(),
                    Float.parseFloat(priceField.getText().trim()),
                    Integer.parseInt(popularityField.getText().trim()),
                    editionField.getText(),
                    Integer.parseInt(numOfPagesField.getText().trim()),
                    Integer.parseInt(quantityField.getText().trim()),
                    imageToByteArray(imageView.getImage())
            );
            response = admin.addBook(book);
            showAlert(Alert.AlertType.INFORMATION, "Book Status", response);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void navigateToEditBookView(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/EditBook.fxml"));
            Parent root = loader.load();

            AdminViewController controller = loader.getController();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void navigateToDeleteBookView(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/DeleteBook.fxml"));
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
    public static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void navigateToInventoryView(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/AdminInventoryView.fxml"));
            Parent root = loader.load();

            AdminInventoryController controller = loader.getController();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            // Replace printStackTrace with a more robust logging mechanism
            System.err.println("Error loading Admin Inventory View: " + e.getMessage());
        }
    }
    @FXML
    public void chooseImage() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.webp", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                Image image = new Image(fileInputStream);

                imageView.setImage(image);

                System.out.println("Image selected: " + selectedFile.getName());

                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private byte[] imageToByteArray(Image image) throws IOException {
        BufferedImage bufferedImage = javafx.embed.swing.SwingFXUtils.fromFXImage(image, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos); // or "jpg", depending on your format
        baos.flush();
        return baos.toByteArray();
    }
    public void navigateToManageCategoriesView(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/ManageCategoriesView.fxml"));
            Parent root = loader.load();

            ManageCategoriesController controller = loader.getController();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            System.err.println("Error loading Manage Categories View: " + e.getMessage());
        }

    }
}
