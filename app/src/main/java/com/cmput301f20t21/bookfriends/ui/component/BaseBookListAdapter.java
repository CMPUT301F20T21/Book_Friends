/*
 * BaseBookListAdapter.java
 * Version: 1.0
 * Date: October 16, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.utils.ImagePainter;

import java.util.List;

/**
 * A abstract adapter used for all book lists
 */
public abstract class BaseBookListAdapter extends RecyclerView.Adapter<BaseBookListAdapter.ViewHolder> {
    protected List<Book> books;

    public BaseBookListAdapter(List<Book> books) {
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
        protected final TextView title;
        protected final TextView author;
        protected final TextView isbn;
        protected final TextView status;
        protected final ImageView bookImage;
        protected View holderView;
        protected Book book;


        public ViewHolder(View v) {
            super(v);
            holderView = v;
            title = v.findViewById(R.id.item_book_title);
            author = v.findViewById(R.id.item_book_author);
            isbn = v.findViewById(R.id.item_book_isbn);
            status = v.findViewById(R.id.status);
            bookImage = v.findViewById(R.id.booklist_image_view);
        }

        public void onBind(Book book) {
            this.title.setText(book.getTitle());
            this.author.setText(this.itemView.getResources().getString(R.string.book_list_item_author, book.getAuthor()));
            this.isbn.setText(book.getIsbn());
            this.book = book;
            this.status.setText(book.getStatus().toString().toLowerCase());
            ImagePainter.paintImage(bookImage, book.getImageUrl());
        }
    }
}