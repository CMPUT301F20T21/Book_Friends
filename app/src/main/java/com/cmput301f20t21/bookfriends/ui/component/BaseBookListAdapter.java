package com.cmput301f20t21.bookfriends.ui.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.utils.GlideApp;

import java.util.List;

public abstract class BaseBookListAdapter<T extends Book> extends RecyclerView.Adapter<BaseBookListAdapter.ViewHolder<T>> {
    protected List<T> books;

    public BaseBookListAdapter(List<T> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public ViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_list, parent, false);
        return new ViewHolder<T>(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<T> holder, int position) {
        holder.onBind(books.get(position));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class ViewHolder<T extends Book> extends RecyclerView.ViewHolder {
        protected final TextView title;
        protected final TextView author;
        protected final TextView isbn;
        protected final ImageView bookImage;
        protected View holderView;
        protected T book;


        public ViewHolder(View v) {
            super(v);
            holderView = v;
            title = v.findViewById(R.id.item_book_title);
            author = v.findViewById(R.id.item_book_author);
            isbn = v.findViewById(R.id.item_book_isbn);
            bookImage = v.findViewById(R.id.booklist_image_view);
        }

        public void onBind(T book) {
            this.title.setText(book.getTitle());
            this.author.setText(this.itemView.getResources().getString(R.string.book_list_item_author, book.getAuthor()));
            this.isbn.setText(book.getIsbn());
            this.book = book;
            paintCover();
        }

        protected void paintCover() {
            if (this.book.getImageUrl() == null) {
                GlideApp.with(holderView)
                        .load(R.drawable.no_image)
                        .into(bookImage);
                return;
            };
            GlideApp.with(holderView)
                    .load(this.book.getImageUrl())
                    .placeholder(R.drawable.no_image)
                    .into(bookImage);
        }
    }
}