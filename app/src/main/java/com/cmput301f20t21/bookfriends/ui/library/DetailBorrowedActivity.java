package com.cmput301f20t21.bookfriends.ui.library;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;

public class DetailBorrowedActivity extends BaseDetailActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       super.onOptionsItemSelected(item);
       return true;
    }
}
