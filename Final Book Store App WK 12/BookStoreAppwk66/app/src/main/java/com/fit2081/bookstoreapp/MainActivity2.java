package com.fit2081.bookstoreapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    // Button btn;
    RecyclerView recyclerView;
    RecyclerAdapter adapter1;
    RecyclerView.LayoutManager layoutManager;

    Type type = new TypeToken<ArrayList<Item>>(){}.getType();

    // ArrayList<Item>  data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        recyclerView= findViewById((R.id.my_recycler_view));
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        Log.d("array data", intent.getStringExtra(MainActivity.BOOK_KEY));
        ArrayList<Item> data = new Gson().fromJson(intent.getStringExtra(MainActivity.BOOK_KEY),type);
        // data= new Gson().fromJson(intent.getStringExtra(MainActivity.MOVIE_KEY),type);

        adapter1 = new RecyclerAdapter();
        adapter1.setData(data);
        recyclerView.setAdapter(adapter1);

    }

}