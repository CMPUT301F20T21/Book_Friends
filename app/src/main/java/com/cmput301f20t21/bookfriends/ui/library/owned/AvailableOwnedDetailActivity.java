/*
 * AvailableOwnedDetailActivity.java
 * Version: 1.0
 * Date: November 20, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.library.owned;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_ACTION;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;
import com.cmput301f20t21.bookfriends.ui.library.add.AddEditActivity;

/**
 * Detail Activity for owner books with "AVAILABLE" {@link BOOK_STATUS}
 */
public class AvailableOwnedDetailActivity extends BaseDetailActivity {

    private Book oldBook;

    /**
     * Called when creating the activity view
     * @param savedInstanceState the saved objects, should contain nothing for this activity
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        button.setVisibility(View.GONE);
    }

    /**
     * onClickListener for the "Edit" button, opens {@link AddEditActivity}
     * @param book the book to edit
     */
    private void openAddEditActivity(Book book) {
        Intent intent = new Intent(AvailableOwnedDetailActivity.this, AddEditActivity.class);
        intent.putExtra(BaseDetailActivity.BOOK_ACTION_KEY, BOOK_ACTION.EDIT);
        intent.putExtra(BaseDetailActivity.BOOK_DATA_KEY, book);
        startActivityForResult(intent, BOOK_ACTION.EDIT.getCode());
    }

    /**
     * setup the back and edit buttons on the title
     *
     * @param item the item that is clicked
     * @return false to allow normal menu processing to proceed, true to consume it here
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (oldBook != null) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(AddEditActivity.OLD_BOOK_INTENT_KEY, oldBook);
                    resultIntent.putExtra(AddEditActivity.UPDATED_BOOK_INTENT_KEY, book);
                    setResult(RESULT_OK, resultIntent);
                }
                finish();
                return true;
            case R.id.edit_button:
                openAddEditActivity(book);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * inflates the buttons on the support action bar
     * @param menu the menu to inflate
     * @return true to display the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.top_detail_menu, menu);
            return true;
    }

    /**
     * called upon returning from the {@link AddEditActivity}
     * @param requestCode the request code that starts the activity
     * @param resultCode the result code sent from the activity
     * @param data the intent data that contains the book edited
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == BOOK_ACTION.EDIT.getCode()) {
                oldBook = data.getParcelableExtra(AddEditActivity.OLD_BOOK_INTENT_KEY);
                Book updatedBook = data.getParcelableExtra(AddEditActivity.UPDATED_BOOK_INTENT_KEY);
                updateBook(updatedBook);
                Toast.makeText(this, getString(R.string.edit_book_successful), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
