/*
 * AvailableBookListAdapter.java
 * Version: 1.0
 * Date: October 26, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.browse;

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
 * Adapter for {@link BrowseFragment}
 */
public class AvailableBookListAdapter extends BaseBookListAdapter {
    private OnItemClickListener itemClickListener;
    public interface OnItemClickListener {
        void run(int position);
    }
    public AvailableBookListAdapter(List<Book> books, OnItemClickListener itemClickListener) {
        super(books);
        this.itemClickListener = itemClickListener;
    }
    @NonNull
    @Override
    public AvailableBookListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_list, parent, false);
        return new AvailableBookListAdapter.ViewHolder(itemView, itemClickListener);
    }

    public static class ViewHolder extends BaseBookListAdapter.ViewHolder {
        final ImageButton moreBtn;
        final TextView owner;
        final TextView status;

        public ViewHolder(View v, OnItemClickListener itemClickListener) {
            super(v);
            owner = v.findViewById(R.id.item_book_owner);
            moreBtn = v.findViewById(R.id.item_book_more_btn);
            status = v.findViewById(R.id.status);
            moreBtn.setVisibility(View.GONE);
            v.setOnClickListener(view -> itemClickListener.run(getAdapterPosition()));
        }

        @Override
        public void onBind(Book book) {
            super.onBind(book);
            this.owner.setText(this.itemView.getResources().getString(R.string.book_list_item_owner, book.getOwner()));
            // don't display the status in Browse page
            this.status.setText(null);
        }
    }
}

