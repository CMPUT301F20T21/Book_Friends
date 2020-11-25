/*
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.library.owned;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_ACTION;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;

public class RequestedOwnedDetailActivity extends BaseDetailActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        button.setText(R.string.view_request);
        button.setOnClickListener(this::onViewRequests);
    }

    public void onViewRequests(View view) {
        Intent intent = new Intent(this, RequestActivity.class);
        intent.putExtra(OwnedListFragment.VIEW_REQUEST_KEY, book.getId());
        startActivityForResult(intent, BOOK_ACTION.VIEW_REQUESTS.getCode());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == BOOK_ACTION.VIEW_REQUESTS.getCode()) {
                Book book = data.getParcelableExtra(BaseDetailActivity.BOOK_DATA_KEY);
                if (book.getStatus() != BOOK_STATUS.REQUESTED) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(BaseDetailActivity.BOOK_DATA_KEY, book);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        }
    }

}
