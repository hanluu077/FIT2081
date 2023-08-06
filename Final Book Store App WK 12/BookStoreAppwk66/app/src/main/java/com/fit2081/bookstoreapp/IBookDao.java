package com.fit2081.bookstoreapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IBookDao {
    @Query("select * from books")
    LiveData<List<Books>> getAllBooks();

//    @Query("select * from Books where customerName=:name")
//    List<Books> getCustomer(String name);
//
    @Insert
    void addBooks(Books books);

    @Query("delete FROM books ")


    void deleteAllBooks();

}

