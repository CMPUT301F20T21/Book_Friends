package com.cmput301f20t21.bookfriends.ui.library;

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

import java.util.ArrayList;

public class OwnedListAdapter extends BaseBookListAdapter {
    public OwnedListAdapter(ArrayList<Book> books) {
        super(books);
    }

    @NonNull
    @Override
    public OwnedListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_list, parent, false);
        return new OwnedListAdapter.ViewHolder(itemView);
    }

    /**
     * This method will be called by the recycler when data updates
     * due to inheritance, it only recognize the looser view holder class in base adapter
     * but we passed to it a more specific one so the down-casting should always work
     *
     * @param holder   the view holder to bind/update, will be down-casted to child view holder type
     * @param position the position of the item
     */
    @Override
    public void onBindViewHolder(@NonNull BaseBookListAdapter.ViewHolder holder, int position) {
        this.onBindViewHolder((OwnedListAdapter.ViewHolder) holder, position);
    }

    private void onBindViewHolder(@NonNull OwnedListAdapter.ViewHolder holder, int position) {
        holder.onBind(books.get(position));
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

        @Override
        public void onBind(Book book) {
            super.onBind(book);
            this.owner.setText("");
        }
    }
}
