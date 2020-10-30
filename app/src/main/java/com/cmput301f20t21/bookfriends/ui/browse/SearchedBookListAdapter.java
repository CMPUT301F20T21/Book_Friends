package com.cmput301f20t21.bookfriends.ui.browse;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;

import com.cmput301f20t21.bookfriends.ui.library.BaseBookListAdapter;

import java.util.ArrayList;

public class SearchedBookListAdapter extends BaseBookListAdapter {

        public SearchedBookListAdapter(ArrayList<Book> books) {
            super(books);
        }

        @NonNull
        @Override
        public SearchedBookListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_list, parent, false);
            return new SearchedBookListAdapter.ViewHolder(itemView);
        }

        public static class ViewHolder extends BaseBookListAdapter.ViewHolder {
            final ImageButton moreBtn;
            final TextView owner;


            public ViewHolder(View v) {
                super(v);
                owner = v.findViewById(R.id.item_book_owner);
                moreBtn = v.findViewById(R.id.item_book_more_btn);
                moreBtn.setVisibility(View.GONE);
            }

            @Override
            public void onBind(Book book) {
                super.onBind(book);
                this.owner.setText(this.itemView.getResources().getString(R.string.book_list_item_owner, book.getOwner()));


            }
        }
    }

