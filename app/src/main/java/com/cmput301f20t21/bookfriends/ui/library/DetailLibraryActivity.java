package com.cmput301f20t21.bookfriends.ui.library;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_ACTION;
import com.cmput301f20t21.bookfriends.ui.add.AddEditActivity;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;

public class DetailLibraryActivity extends BaseDetailActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void openAddEditActivity(Book book) {
        Intent intent = new Intent(DetailLibraryActivity.this, AddEditActivity.class);
        intent.putExtra(OwnedListFragment.BOOK_ACTION_KEY, BOOK_ACTION.EDIT);
        intent.putExtra(OwnedListFragment.BOOK_DATA_KEY, book);
        startActivityForResult(intent, BOOK_ACTION.EDIT.getCode());
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
