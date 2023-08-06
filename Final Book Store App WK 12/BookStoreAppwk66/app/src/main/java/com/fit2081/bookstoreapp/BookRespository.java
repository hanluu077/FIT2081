package com.fit2081.bookstoreapp;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BookRespository {

    private IBookDao myBookDao;
    private LiveData<List<Books>> myAllBookDao;

    BookRespository(Application app) {
        BookDatabase db = BookDatabase.getDatabase(app);
        myBookDao = db.bookDAO(); // access to the database
        myAllBookDao = myBookDao.getAllBooks(); // allows you to get access to all the operations in the interface
    }

    LiveData<List<Books>> getAllBooks() {
        return myAllBookDao;
    } // create a getter for all the books

    public void addBookRepo(Books books) {
        BookDatabase.databaseWriteExecutor.execute(() -> myBookDao.addBooks(books));


    }

    void deleteAll() {
        BookDatabase.databaseWriteExecutor.execute(() -> {
            myBookDao.deleteAllBooks();

        });
    }
}