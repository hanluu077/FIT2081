package com.fit2081.bookstoreapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


public class BookViewModel extends AndroidViewModel {
    private static BookRespository mRepository;
    private LiveData<List<Books>> myBookList;

    public BookViewModel(@NonNull Application application) {
        super(application);
        mRepository = new BookRespository(application); // create a "new" instance because it's not part of the life cycle
        myBookList = mRepository.getAllBooks();
    }

    public LiveData<List<Books>> getAllCustomers() {
        return myBookList;
    }

    public static void insert(Books aBook) {
        mRepository.addBookRepo(aBook);
    }
    public void deleteAll(){
        mRepository.deleteAll();
    }

}
