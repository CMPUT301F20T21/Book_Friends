package com.cmput301f20t21.bookfriends.ui.library;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;

import java.util.ArrayList;

public class BorrowedListAdapter extends BaseBookListAdapter {

    public BorrowedListAdapter(ArrayList<Book> books) {
        super(books);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_list, parent, false);
        return new ViewHolder(itemView);
    }

    public static class ViewHolder extends BaseBookListAdapter.ViewHolder {
        final ImageButton moreBtn;
        final TextView owner;

        public ViewHolder(View v) {
            super(v);
            owner = v.findViewById(R.id.item_book_owner);
            moreBtn = v.findViewById(R.id.item_book_more_btn);
            moreBtn.setOnClickListener(this::showPopup);
        }

        private void showPopup(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), moreBtn);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.library_borrowed_book_item_menu, popup.getMenu());
            popup.setGravity(Gravity.END);
            popup.show();
        }

        @Override
        public void onBind(Book book) {
            super.onBind(book);
            this.owner.setText(this.itemView.getResources().getString(R.string.book_list_item_owner, book.getOwner()));
        }
    }
}
