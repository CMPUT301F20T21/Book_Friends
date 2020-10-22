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

public class OwnedListAdapter extends RecyclerView.Adapter<OwnedListAdapter.ViewHolder>{
    private ArrayList<Book> books;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // TODO flesh out book view card
        TextView title;
        TextView owner;
        TextView isbn;
        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.item_book_title);
            owner = v.findViewById(R.id.item_book_owner);
            isbn = v.findViewById(R.id.item_book_isbn);
        }
    }

    public OwnedListAdapter(ArrayList<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public OwnedListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_list, parent, false);
        return new OwnedListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnedListAdapter.ViewHolder holder, int position) {
        holder.title.setText(books.get(position).getTitle());
        holder.owner.setText(books.get(position).getOwner());
        holder.isbn.setText(books.get(position).getIsbn());
    }


    @Override
    public int getItemCount() {
        return books.size();
    }
}
