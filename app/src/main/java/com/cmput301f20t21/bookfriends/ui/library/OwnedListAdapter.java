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
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;

import java.util.ArrayList;

public class OwnedListAdapter extends RecyclerView.Adapter<OwnedListAdapter.ViewHolder> {
    private ArrayList<Book> books;

    public OwnedListAdapter(ArrayList<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public OwnedListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_list, parent, false);
        return new OwnedListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnedListAdapter.ViewHolder holder, int position) {
        Book book = books.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(holder.root.getResources().getString(R.string.book_list_item_author, book.getAuthor()));
        holder.owner.setText("");
        holder.isbn.setText(book.getIsbn());
        holder.setBook(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView author;
        TextView owner;
        TextView isbn;
        ImageButton moreBtn;

        Book book;
        View root;

        public ViewHolder(View v) {
            super(v);
            root = v;
            title = v.findViewById(R.id.item_book_title);
            author = v.findViewById(R.id.item_book_author);
            owner = v.findViewById(R.id.item_book_owner);
            isbn = v.findViewById(R.id.item_book_isbn);
            moreBtn = v.findViewById(R.id.item_book_more_btn);

            moreBtn.setOnClickListener(this::showPopup);
        }

        public void setBook(Book book) {
            this.book = book;
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
