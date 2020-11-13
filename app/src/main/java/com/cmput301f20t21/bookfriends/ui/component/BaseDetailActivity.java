package com.cmput301f20t21.bookfriends.ui.component;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.ui.borrow.RequestedListFragment;
import com.cmput301f20t21.bookfriends.utils.GlideApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BaseDetailActivity extends AppCompatActivity {
    protected Book detailBook;
    private ImageView bookImage;
    private TextView detailTitle;
    private TextView detailISBN;
    private TextView detailAuthor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_browse);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_white_18);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bookImage = findViewById(R.id.book_image_view); // will be replaced with actual image
        detailISBN = findViewById(R.id.detail_ISBN);
        detailTitle = findViewById(R.id.detail_title);
        detailAuthor = findViewById(R.id.detail_author);

        Intent getIntent = getIntent();
        detailBook = getIntent.getParcelableExtra(RequestedListFragment.VIEW_REQUEST_KEY);
        detailISBN.setText(detailBook.getIsbn());
        detailTitle.setText(detailBook.getTitle());
        detailAuthor.setText("Author: "+ detailBook.getAuthor());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(detailBook.getCoverImageName());
        GlideApp.with(this)
                .load(storageReference)
                .placeholder(R.drawable.no_image)
                .into(bookImage);
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
