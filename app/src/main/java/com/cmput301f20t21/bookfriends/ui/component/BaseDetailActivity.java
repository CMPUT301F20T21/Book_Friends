package com.cmput301f20t21.bookfriends.ui.component;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.utils.GlideApp;

public class BaseDetailActivity extends AppCompatActivity {
    public static final String BOOK_ACTION_KEY = "com.cmput301f20t21.bookfriends.BOOK_ACTION";
    public static final String BOOK_EDIT_KEY = "com.cmput301f20t21.bookfriends.BOOK_EDIT";
    public static final String VIEW_REQUEST_KEY = "com.cmput301f20t21.bookfriends.VIEW_REQUEST";
    public static final String BOOK_DATA_KEY = "com.cmput301f20t21.bookfriends.BOOK_DATA";

    protected Book detailBook;
    private ImageView bookImage;
    private TextView detailTitle;
    private TextView detailISBN;
    private TextView detailAuthor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_white_18);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bookImage = findViewById(R.id.book_image_view); // will be replaced with actual image
        detailISBN = findViewById(R.id.detail_ISBN);
        detailTitle = findViewById(R.id.detail_title);
        detailAuthor = findViewById(R.id.detail_author);

        Intent getIntent = getIntent();
        detailBook = getIntent.getParcelableExtra(BOOK_DATA_KEY);
        setDetails();
    }

    public void updateBook(Book updateBook) {
        detailBook = updateBook;
        setDetails();
    }

    public void setDetails() {
        detailISBN.setText(detailBook.getIsbn());
        detailTitle.setText(detailBook.getTitle());
        detailAuthor.setText(getString(R.string.author, detailBook.getAuthor()));
        if (detailBook.getImageUrl() != null) {
            GlideApp.with(this)
                    .load(detailBook.getImageUrl())
                    .placeholder(R.drawable.no_image)
                    .into(bookImage);
        } else {
            GlideApp.with(this)
                    .load(R.drawable.no_image)
                    .into(bookImage);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
