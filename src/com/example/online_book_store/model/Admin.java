package com.example.online_book_store.model;

import com.example.online_book_store.controller.AdminViewController;
import com.example.online_book_store.database.BooksDataBase;
import com.example.online_book_store.manager.InventoryManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Map;

public class Admin extends User {
    public InventoryManager inventoryManager;
    private int lastBookID;
    static Admin admin;
    private void getCounter(){
        for (Map.Entry<Integer, Book> entry : inventoryManager.allBooks.entrySet()) {
            lastBookID = entry.getKey();
        }
    }
    public static Admin getInstance(){
        if (admin == null){
            admin = new Admin();
        }
        return admin;
    }
    @Override
    public void loadView(ActionEvent event, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/online_book_store/view/AdminView.fxml"));
            Parent root = loader.load();

            AdminViewController controller = loader.getController();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Admin(){inventoryManager = InventoryManager.getInstance(); getCounter();lastBookID++;}
    public String addBook(Book book){ //To Be Edited   //COR or Factory
        getCounter();
        book.setBookID(++lastBookID);
        String respone = BooksDataBase.addRecord(book);
        if (respone.equalsIgnoreCase("Record Inserted Successfully")) {
            lastBookID++;
            inventoryManager.allBooks.put(book.getBookID(), book);
        }
        return respone;
    }
    public String editBook(Book book) {
        inventoryManager.allBooks.replace(book.getBookID(),book);
        return BooksDataBase.updateRecord(book);
    }
    public String deleteBook(int choice){
        inventoryManager.allBooks.remove(choice);
        return BooksDataBase.deleteRecord(choice);
    }
}
