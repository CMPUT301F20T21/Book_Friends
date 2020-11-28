/*
 * RequestedListAdapter.java
 * Version: 1.0
 * Date: November 3, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.borrow.requested;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.ui.component.BaseBookListAdapter;

import java.util.List;

/**
 * Adapter for {@link androidx.recyclerview.widget.RecyclerView} in {@link RequestedListFragment}
 */
public class RequestedListAdapter extends BaseBookListAdapter {
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void run(int position);
    }

    public RequestedListAdapter(List<Book> books, OnItemClickListener itemClickListener) {
        super(books);
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RequestedListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_list, parent, false);
        return new RequestedListAdapter.ViewHolder(itemView, itemClickListener);
    }

    public static class ViewHolder extends BaseBookListAdapter.ViewHolder {
        final ImageButton moreBtn;
        final TextView owner;

        public ViewHolder(View v, OnItemClickListener itemClickListener) {
            super(v);
            owner = v.findViewById(R.id.item_book_owner);
            moreBtn = v.findViewById(R.id.item_book_more_btn);
            moreBtn.setVisibility(View.GONE);
            v.setOnClickListener(view -> itemClickListener.run(getAdapterPosition()));
        }

        @Override
        public void onBind(Book book) {
            super.onBind(book);
            this.owner.setText(this.itemView.getResources().getString(R.string.book_list_item_owner, book.getOwner()));
        }
    }
}
