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

public class BaseBookListAdapter extends RecyclerView.Adapter<BaseBookListAdapter.ViewHolder> {
    protected ArrayList<Book> books;

    public BaseBookListAdapter(ArrayList<Book> books) {
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
        holder.onBind(books.get(position));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // TODO flesh out book view card
        final TextView title;
        final TextView author;
        final TextView isbn;

        Book book;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.item_book_title);
            author = v.findViewById(R.id.item_book_author);
            isbn = v.findViewById(R.id.item_book_isbn);
        }

        public void onBind(Book book) {
            this.title.setText(book.getTitle());
            this.author.setText(this.itemView.getResources().getString(R.string.book_list_item_author, book.getAuthor()));
            this.isbn.setText(book.getIsbn());
            this.book = book;
        }
    }
}