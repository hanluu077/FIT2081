package com.fit2081.bookstoreapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{


    ArrayList<Item> data = new ArrayList<Item>();
    private List<Books> myBooks;



    public void setData(ArrayList<Item> data) {
        this.data = data;
    }

    //new ArrayList<Item>();
    //public RecyclerAdapter(ArrayList<Item> data ){this.data= data;}
    // public void setData(ArrayList<Item> data) {
    //  this.data = data;


    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);
        Log.d("week6App","onCreateViewHolder");
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        holder.BookID.setText(data.get(position).getBookID());
        holder.BookTitle.setText(data.get(position).getBookTitle());
        holder.BookAuthor.setText(data.get(position).getBookAuthor());
        holder.BookISBN.setText(data.get(position).getBookISBN());
        holder.BookDescription.setText(data.get(position).getBookDescription());
        holder.BookPrice.setText(data.get(position).getBookPrice());
        holder.BookRating.setText(data.get(position).getBookRating());


        Log.d("week6App","onBindViewHolder");

    }
    @Override
    public int getItemCount() {
        return data.size();

    }
    public void setBook(List<Books> newData) {
        myBooks=newData;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        //cardView
        TextView BookID;
        TextView BookTitle;
        TextView BookAuthor;
        TextView BookISBN;
        TextView BookDescription;
        TextView BookPrice;
        TextView BookRating;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            BookID = itemView.findViewById(R.id.bookID_id);
            BookTitle = itemView.findViewById(R.id.bookTitle_id);
            BookAuthor = itemView.findViewById(R.id.bookAuthor_id);
            BookISBN = itemView.findViewById(R.id.bookISBN_id);
            BookDescription = itemView.findViewById(R.id.bookDescription_id);
            BookPrice = itemView.findViewById(R.id.bookPrice_id);
            BookRating = itemView.findViewById(R.id.ratingTv);

        }
    }
}