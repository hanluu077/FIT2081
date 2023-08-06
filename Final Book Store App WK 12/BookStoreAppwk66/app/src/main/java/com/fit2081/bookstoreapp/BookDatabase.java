package com.fit2081.bookstoreapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Books.class}, version = 1) // the following class represents your database ('Books'.class) --> get access to this database
public abstract class BookDatabase extends RoomDatabase { // reason for abstract = (1)// have an abstract method in line 18, (2) also no one can have access to this method unless they call it i.e line 26 ("getDatabase")

    public static final String CUSTOMER_DATABASE_NAME = "customer_database";

    public abstract IBookDao bookDAO();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile BookDatabase INSTANCE; // volatile = Using RAM memory because nowadays computers have many CPU memory
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static BookDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BookDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), // calls and creates your class
                                    BookDatabase.class, CUSTOMER_DATABASE_NAME)
                            .build();
                }
            }
        }

        return INSTANCE;
    }


}
