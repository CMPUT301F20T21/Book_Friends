package com.cmput301f20t21.bookfriends.ui.library.owned;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.ui.component.BaseBookListAdapter;

import java.util.List;

public class OwnedListAdapter extends BaseBookListAdapter {
    private OnDeleteListener deleteListener;
    private OnItemClickListener itemClickListener;
    private OnViewRequestsListener viewRequestsListener;

    public OwnedListAdapter(List<Book> books, OnItemClickListener itemClickListener, OnDeleteListener deleteListener, OnViewRequestsListener viewRequestsListener) {
        super(books);
        this.itemClickListener = itemClickListener;
        this.deleteListener = deleteListener;
        this.viewRequestsListener = viewRequestsListener;
    }

    @NonNull
    @Override
    public OwnedListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_list, parent, false);
        return new OwnedListAdapter.ViewHolder(itemView, itemClickListener, deleteListener, viewRequestsListener);
    }
    public interface OnViewRequestsListener {
        void run(String bookId);
    }

    public interface OnDeleteListener {
        void run(Book book);
    }

    public interface OnItemClickListener {
        void run(int position);
    }

    public static class ViewHolder extends BaseBookListAdapter.ViewHolder {
        final ImageButton moreBtn;
        final OnDeleteListener deleteListener;
        final OnViewRequestsListener viewRequestsListener;

        public ViewHolder(View v, OnItemClickListener itemClickListener, OnDeleteListener deleteListener, OnViewRequestsListener viewRequestsListener) {
            super(v);
            moreBtn = v.findViewById(R.id.item_book_more_btn);
            moreBtn.setOnClickListener(this::showPopup);
            v.setOnClickListener(view -> itemClickListener.run(getAdapterPosition()));
            this.deleteListener = deleteListener;
            this.viewRequestsListener = viewRequestsListener;
        }

        private void showPopup(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), moreBtn);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.library_owned_book_item_menu, popup.getMenu());
            popup.setGravity(Gravity.END);
            popup.show();
            popup.setOnMenuItemClickListener(this::onMenuItemClick);
        }

        private boolean onMenuItemClick(MenuItem menuItem) {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.library_owned_book_menu_delete) {
                deleteListener.run(book);
                return true;
            } else if (itemId == R.id.library_owned_book_menu_view_requests) {
                viewRequestsListener.run(book.getId());
                return true;
            } else {
                return false;
            }
        }
    }
}
