package com.cmput301f20t21.bookfriends.ui.component;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.ui.component.detailButtons.DetailButtonModel;
import com.cmput301f20t21.bookfriends.ui.component.detailButtons.DetailButtonsFragment;
import com.cmput301f20t21.bookfriends.ui.profile.ProfileViewUserActivity;
import com.cmput301f20t21.bookfriends.utils.ImagePainter;

import java.util.ArrayList;
import java.util.List;

public class BaseDetailActivity extends AppCompatActivity {
    public static final String BOOK_ACTION_KEY = "com.cmput301f20t21.bookfriends.BOOK_ACTION";
    public static final String BOOK_DATA_KEY = "com.cmput301f20t21.bookfriends.BOOK_DATA";

    protected Book book;
    protected Button button;
    protected LoadingOverlay loadingOverlay;
    private ImageView bookImage;
    private TextView title;
    private TextView isbn;
    private TextView author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_white_18);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bookImage = findViewById(R.id.book_image_view); // will be replaced with actual image
        isbn = findViewById(R.id.detail_ISBN);
        title = findViewById(R.id.detail_title);
        author = findViewById(R.id.detail_author);
        button = findViewById(R.id.detail_action_button);
        loadingOverlay = new LoadingOverlay(this, findViewById(R.id.loading_overlay));

        Intent getIntent = getIntent();
        book = getIntent.getParcelableExtra(BOOK_DATA_KEY);
        setDetails();
    }

    public void updateBook(Book updateBook) {
        book = updateBook;
        setDetails();
    }

    public void setDetails() {
        isbn.setText(book.getIsbn());
        title.setText(book.getTitle());
        author.setText(getString(R.string.author, book.getAuthor()));
        ImagePainter.paintImage(bookImage, book.getImageUrl());
    }

    public void onViewOwnerProfile(View view) {
        Intent intent = new Intent(this, ProfileViewUserActivity.class);
        intent.putExtra(ProfileViewUserActivity.USERNAME_KEY, book.getOwner());
        startActivity(intent);
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

    protected List<DetailButtonModel> getDetailButtonModels() {
        ArrayList<DetailButtonModel> buttonModels = new ArrayList<>();
        buttonModels.add(
                new DetailButtonModel(
                        getString(R.string.view_owner_profile),
                        book.getOwner(),
                        this::onViewOwnerProfile,
                        null
                ));
        return buttonModels;
    }

    /**
     * create and inflate and show the list of buttons
     */
    protected void inflateDetailButtons() {
        DetailButtonsFragment buttonsFragment = new DetailButtonsFragment(getDetailButtonModels());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.detail_buttons_container, buttonsFragment)
                .commit();
    }
}
