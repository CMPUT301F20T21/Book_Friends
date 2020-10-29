package com.cmput301f20t21.bookfriends.ui.borrow;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.ui.library.BaseBookListAdapter;

import java.util.ArrayList;

public class AcceptedListAdapter extends BaseBookListAdapter {
    public AcceptedListAdapter(ArrayList<Book> books) {
        super(books);
    }

    @NonNull
    @Override
    public com.cmput301f20t21.bookfriends.ui.borrow.AcceptedListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_list, parent, false);
        return new com.cmput301f20t21.bookfriends.ui.borrow.AcceptedListAdapter.ViewHolder(itemView);
    }


    public static class ViewHolder extends BaseBookListAdapter.ViewHolder {
        final ImageButton moreBtn;

        public ViewHolder(View v) {
            super(v);
            moreBtn = v.findViewById(R.id.item_book_more_btn);
            moreBtn.setOnClickListener(this::showPopup);
        }

        private void showPopup(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), moreBtn);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.library_owned_book_item_menu, popup.getMenu());
            popup.setGravity(Gravity.END);
            popup.show();
        }
    }
}
