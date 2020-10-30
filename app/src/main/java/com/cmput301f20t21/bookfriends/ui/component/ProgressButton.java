package com.cmput301f20t21.bookfriends.ui.component;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;

import java.util.ArrayList;

public class ProgressButton {
    private View view;
    private CardView cardView;
    private ConstraintLayout layout;
    private ProgressBar progressBar;
    private TextView textView;
    private String defaultText;
    private String doneText;

    private Animation fadeIn;
    private Animation fadeOut;

    public ProgressButton(Context context, View view, String defaultText, String doneText) {
        fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);

        this.view = view;
        cardView = view.findViewById(R.id.progress_button_card_view);
        layout = view.findViewById(R.id.progress_button_layout);
        progressBar = view.findViewById(R.id.progress_button_progress_bar);
        textView = view.findViewById(R.id.progress_button_text_view);
        textView.setText(defaultText);

        this.defaultText = defaultText;
        this.doneText = doneText;
    }

    public void onClick() {
        // prevent user from spamming the login button after it is clicked
        view.setClickable(false);

        // set and start animation for text
        progressBar.setAnimation(fadeIn);
        textView.setAnimation(fadeOut);
        progressBar.getAnimation().start();
        textView.getAnimation().start();

        // set progress bar and text visibility
        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
    }

    public void onSuccess() {
        // background transition color for button
        ColorDrawable[] color = {new ColorDrawable(cardView.getCardBackgroundColor().getDefaultColor()),
                new ColorDrawable(view.getResources().getColor(R.color.positive))};
        TransitionDrawable trans = new TransitionDrawable(color);

        // set and start animation for text
        progressBar.setAnimation(fadeOut);
        textView.setAnimation(fadeIn);
        progressBar.getAnimation().start();
        textView.getAnimation().start();

        // set and start background color transition
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            layout.setBackground(trans);
        } else {
            layout.setBackgroundDrawable(trans);
        }
        trans.startTransition(100);

        // set progress bar and text visibility
        progressBar.setVisibility(View.GONE);
        textView.setText(doneText);
        textView.setVisibility(View.VISIBLE);
    }

    public void onError() {
        // set and start animation for text
        progressBar.setAnimation(fadeOut);
        textView.setAnimation(fadeIn);
        progressBar.getAnimation().start();
        textView.getAnimation().start();

        // set progress bar and text visibility
        progressBar.setVisibility(View.GONE);
        textView.setText(defaultText);
        textView.setVisibility(View.VISIBLE);

        view.setClickable(true);
    }

    // book functions for later on
    public static class BaseBookListAdapter extends RecyclerView.Adapter<BaseBookListAdapter.ViewHolder> {
        protected ArrayList<Book> books;

        public BaseBookListAdapter(ArrayList<Book> books) {
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
            final TextView title;
            final TextView author;
            final TextView isbn;

            Book book;

            public ViewHolder(View v) {
                super(v);
                title = v.findViewById(R.id.item_book_title);
                author = v.findViewById(R.id.item_book_author);
                isbn = v.findViewById(R.id.item_book_isbn);
            }

            public void onBind(Book book) {
                this.title.setText(book.getTitle());
                this.author.setText(this.itemView.getResources().getString(R.string.book_list_item_author, book.getAuthor()));
                this.isbn.setText(book.getIsbn());
                this.book = book;
            }
        }

    }
}
