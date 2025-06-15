package com.example.online_book_store.manager;

import com.example.online_book_store.database.BooksDataBase;
import com.example.online_book_store.model.Book;
import com.example.online_book_store.database.DataBase;
import com.example.online_book_store.database.DataBaseFactory;
import com.example.online_book_store.database.DatabaseType;

import java.util.Map;


public class InventoryManager {
    public Map<Integer, Book> allBooks ;
                    //BookID, Book
    static InventoryManager inventoryManager;
    private InventoryManager(){
        DataBase<Book> booksDataBase = DataBaseFactory.getDataBase(DatabaseType.BOOKS);
        allBooks = booksDataBase.loadData();
    }
    public static InventoryManager getInstance(){
        if (inventoryManager == null){
            inventoryManager = new InventoryManager();
        }
        return inventoryManager;
    }
}
