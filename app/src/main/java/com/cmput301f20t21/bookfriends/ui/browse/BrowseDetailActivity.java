package com.cmput301f20t21.bookfriends.ui.browse;

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
    private Button actionButton;
    private BrowseDetailViewModel vm;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new ViewModelProvider(this).get(BrowseDetailViewModel.class);
        actionButton = findViewById(R.id.detail_action_button);

        actionButton.setText(R.string.request);
        actionButton.setOnClickListener(this::sendRequest);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        return true;
    }

    private void sendRequest(View view) {
        vm.sendRequest(detailBook, this::onSendRequestSuccess, this::onSendRequestFailure);
    }

    private void onSendRequestSuccess() {

    }

    private void onSendRequestFailure() {

    }

}
