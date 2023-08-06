package com.fit2081.bookstoreapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

public class Item {

    private String bookID, bookTitle, bookAuthor, bookISBN, bookDescription, bookPrice, bookRating;
    @ColumnInfo(name = "ID")
    private int id;


    public Item(String bookID, String bookTitle, String bookAuthor, String bookISBN, String bookDescription, String bookPrice, String bookRating) {

        this.bookID = bookID;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookISBN = bookISBN;
        this.bookDescription = bookDescription;
        this.bookPrice = bookPrice;
        this.bookRating = bookRating;

    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(@NonNull int id) {
//        this.id = id;
//    }
    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookRating() {
        return bookRating;
    }

    public void setBookRating(String bookRating) {
        this.bookRating = bookRating;
    }
}