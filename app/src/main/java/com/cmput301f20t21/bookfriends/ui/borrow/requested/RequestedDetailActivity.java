package com.cmput301f20t21.bookfriends.ui.borrow.requested;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;

public class RequestedDetailActivity extends BaseDetailActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        button.setText(R.string.wait_for_acceptance);
        button.setBackgroundColor(getColor(R.color.gray));
        button.setEnabled(false);
        super.inflateDetailButtons();
    }
}


