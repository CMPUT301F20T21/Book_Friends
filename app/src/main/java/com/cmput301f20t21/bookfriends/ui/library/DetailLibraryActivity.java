package com.cmput301f20t21.bookfriends.ui.library;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_ACTION;
import com.cmput301f20t21.bookfriends.ui.add.AddEditActivity;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;

public class DetailLibraryActivity extends BaseDetailActivity {

    private Book oldBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void openAddEditActivity(Book book) {
        Intent intent = new Intent(DetailLibraryActivity.this, AddEditActivity.class);
        intent.putExtra(BaseDetailActivity.BOOK_ACTION_KEY, BOOK_ACTION.EDIT);
        intent.putExtra(BaseDetailActivity.BOOK_DATA_KEY, book);
        startActivityForResult(intent, BOOK_ACTION.EDIT.getCode());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (oldBook != null) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(AddEditActivity.OLD_BOOK_INTENT_KEY, oldBook);
                    resultIntent.putExtra(AddEditActivity.UPDATED_BOOK_INTENT_KEY, detailBook);
                    setResult(RESULT_OK, resultIntent);
                }
                finish();
                return true;
            case R.id.edit_button:
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