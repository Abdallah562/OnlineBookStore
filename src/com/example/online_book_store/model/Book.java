package com.example.online_book_store.model;

import java.util.Objects;

public class Book {
    private int bookID;
    private String title;
    private String author;
    private String category;
    private float price;
    private int popularity;
    private String edition;
    private int numofPages;
    private int quantity;
    private byte[] image;
    public Book(){}
    public Book(String title, String author, String category, float price, int popularity, String edition, int numOfPages,int quantity,byte[] image) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.price = price;
        this.popularity = popularity;
        this.edition = edition;
        this.numofPages = numOfPages;
        this.quantity = quantity;
        this.image = image;
    }
    public int getBookID() {
        return bookID;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getCategory() {
        return category;
    }
    public float getPrice() {
        return price;
    }
    public int getPopularity() {
        return popularity;
    }
    public int getNumOfPages() {return numofPages;}
    public int getQuantity() {return quantity;}
    public byte[] getImage() {return image;}


    public void setBookID(int bookID) {this.bookID = bookID;}
    public void setTitle(String title) {this.title = title;}
    public void setAuthor(String author) {this.author = author;}
    public void setCategory(String category) {this.category = category;}
    public void setPrice(float price) {this.price = price;}
    public void setPopularity(int popularity) {this.popularity = popularity;}
    public String getEdition() {return edition;}
    public void setEdition(String edition) {this.edition = edition;}
    public void setImage(byte[] imageData) {this.image = imageData;}
    public void setNumOfPages(int numOfPages) {this.numofPages = numOfPages;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    @Override
    public String toString() {
        return "\nBookID: "+bookID+
                "\nTitle: "+title+
                "\nAuthor: "+author+
                "\nCategory: "+category+
                "\nPrice: "+price+
                "\nPopularity: "+popularity+
                "\nQuantity: "+quantity+
                "\nEdition: "+edition+
                "\nNumber of Pages: "+numofPages+"\n";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return bookID == book.bookID &&
                Float.compare(book.price, price) == 0 &&
                popularity == book.popularity &&
                numofPages == book.numofPages &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author) &&
                Objects.equals(category, book.category) &&
                Objects.equals(edition, book.edition) &&
                quantity == book.quantity;
    }
    @Override
    public int hashCode() {
        return Objects.hash(bookID, title, author, category, price, popularity, edition, numofPages,quantity);
    }
}
