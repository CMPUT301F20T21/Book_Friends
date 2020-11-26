package com.cmput301f20t21.bookfriends.ui.browse;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;

public class BrowseDetailActivity extends BaseDetailActivity {
    public static final String REQUESTED_BOOK_INTENT_KEY = "com.cmput301f20t21.bookfriends.REQUESTED_BOOK";
    private BrowseDetailViewModel vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new ViewModelProvider(this).get(BrowseDetailViewModel.class);
        button.setText(R.string.click_to_request);
        button.setOnClickListener(this::sendRequest);
        super.inflateDetailButtons();
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        return true;
    }

    private void sendRequest(View view) {
        vm.sendRequest(book, this::onSendRequestSuccess, this::onSendRequestFailure);
    }

    private void onSendRequestSuccess() {
        Intent intent = new Intent();
        intent.putExtra(REQUESTED_BOOK_INTENT_KEY, book);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void onSendRequestFailure() {

    }
}
