package com.cmput301f20t21.bookfriends.ui.library;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;

import java.util.ArrayList;

public class OwnedBookRecyclerAdapter extends RecyclerView.Adapter<OwnedBookRecyclerAdapter.ViewHolder>{
    private ArrayList<Book> books;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // TODO flesh out book view card
        TextView owner;
        public ViewHolder(View v) {
            super(v);
            owner = v.findViewById(R.id.item_book_owner);
        }
    }

    public OwnedBookRecyclerAdapter(ArrayList<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.owner.setText(books.get(position).getOwnerId());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
