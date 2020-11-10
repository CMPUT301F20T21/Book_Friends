package com.cmput301f20t21.bookfriends.ui.library;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_ACTION;
import com.cmput301f20t21.bookfriends.repositories.BookRepository;
import com.cmput301f20t21.bookfriends.ui.add.AddEditActivity;
import com.cmput301f20t21.bookfriends.utils.GlideApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class detailLibraryActivity extends AppCompatActivity {
    public static final String BOOK_ACTION_KEY = "com.cmput301f20t21.bookfriends.BOOK_ACTION";
    public static final String BOOK_EDIT_KEY = "com.cmput301f20t21.bookfriends.BOOK_EDIT";
    private Book detailBook;
    private ImageView bookImage;
    private TextView detailTitle;
    private TextView detailISBN;
    private TextView detailAuthor;
    private final BookRepository bookRepository = BookRepository.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_borrow);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_white_18);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bookImage = findViewById(R.id.book_image_view); // will be replaced with actual image
        detailISBN = findViewById(R.id.detail_ISBN);
        detailTitle = findViewById(R.id.detail_title);
        detailAuthor = findViewById(R.id.detail_author);

        Intent getIntent = getIntent();
        detailBook = getIntent.getParcelableExtra(OwnedListFragment.VIEW_REQUEST_KEY);
        detailISBN.setText(detailBook.getIsbn());
        detailTitle.setText(detailBook.getTitle());
        detailAuthor.setText("Author: "+detailBook.getAuthor());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(detailBook.getCoverImageName());
        GlideApp.with(this)
                .load(storageReference)
                .placeholder(R.drawable.no_image)
                .into(bookImage);
    }


    private void openAddEditActivity(@Nullable Book book) {
        Intent intent = new Intent(detailLibraryActivity.this, AddEditActivity.class);
        if (book == null) {
            intent.putExtra(BOOK_ACTION_KEY, BOOK_ACTION.ADD);
            startActivityForResult(intent, BOOK_ACTION.ADD.getCode());
        } else {
            intent.putExtra(BOOK_ACTION_KEY, BOOK_ACTION.EDIT);
            intent.putExtra(BOOK_EDIT_KEY, book);
            startActivityForResult(intent, BOOK_ACTION.EDIT.getCode());
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.Add_Edit_button:
                openAddEditActivity(detailBook);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_detail_menu, menu);
        return true;
    }
}
