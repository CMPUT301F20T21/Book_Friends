package com.cmput301f20t21.bookfriends.ui.library;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.services.BookService;

import java.util.ArrayList;
import java.util.List;

public class BaseBookListAdapter extends RecyclerView.Adapter<BaseBookListAdapter.ViewHolder> {
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
        // TODO flesh out book view card
        final TextView title;
        final TextView author;
        final TextView isbn;
        final ImageView bookImage;
        View holderView;
        Book book;


        public ViewHolder(View v) {
            super(v);
            holderView = v;
            title = v.findViewById(R.id.item_book_title);
            author = v.findViewById(R.id.item_book_author);
            isbn = v.findViewById(R.id.item_book_isbn);
            bookImage = v.findViewById(R.id.booklist_image_view);
        }

        public void onBind(Book book) {
            this.title.setText(book.getTitle());
            this.author.setText(this.itemView.getResources().getString(R.string.book_list_item_author, book.getAuthor()));
            this.isbn.setText(book.getIsbn());
            this.book = book;
            Uri imageUri = book.getImageUri();
            if (imageUri != null) {
                Glide.with(holderView).load(imageUri).into(bookImage);
            } else {
                // there is a bug that one image is loaded into multiple imageViews
                // must set the image to something default for each imageView if no image exist
                Glide.with(holderView).load(R.drawable.no_image).into(bookImage);
            }
        }
    }
}