package com.cmput301f20t21.bookfriends.ui.browse;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.AvailableBook;
import com.cmput301f20t21.bookfriends.ui.component.BaseBookListAdapter;

import java.util.List;

public class AvailableBookListAdapter extends BaseBookListAdapter<AvailableBook> {
    public interface OnRequestCallback {
        void run(AvailableBook book);
    }
    private OnRequestCallback requestCallback;
    public AvailableBookListAdapter(List<AvailableBook> books, OnRequestCallback requestCallback) {
        super(books);
        this.requestCallback = requestCallback;
    }
    @NonNull
    @Override
    public AvailableBookListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_list, parent, false);
        return new AvailableBookListAdapter.ViewHolder(itemView, requestCallback);
    }

    public static class ViewHolder extends BaseBookListAdapter.ViewHolder<AvailableBook> {
        final ImageButton moreBtn;
        final TextView owner;
        final TextView status;
        final Button requestBtn;
        private OnRequestCallback requestCallback;

        public ViewHolder(View v, OnRequestCallback requestCallback) {
            super(v);
            this.requestCallback = requestCallback;
            owner = v.findViewById(R.id.item_book_owner);
            moreBtn = v.findViewById(R.id.item_book_more_btn);
            status = v.findViewById(R.id.status);
            requestBtn = v.findViewById(R.id.request_button);
            moreBtn.setVisibility(View.GONE);
        }

        @Override
        public void onBind(AvailableBook book) {
            super.onBind(book);
            this.owner.setText(this.itemView.getResources().getString(R.string.book_list_item_owner, book.getOwner()));
            if (book.getRequested()) {
                status.setText("requested");
                requestBtn.setVisibility(View.INVISIBLE);
            } else {
                status.setText("");
                requestBtn.setVisibility(View.VISIBLE);
                requestBtn.setOnClickListener(v -> {
                    requestCallback.run(book);
                });
            }
        }
    }
}

