/*
 * AcceptedOwnedDetailActivity.java
 * Version: 1.0
 * Date: November 20, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.library.owned;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.enums.SCAN_ERROR;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;
import com.cmput301f20t21.bookfriends.ui.scanner.ScannerActivity;

/**
 * Detail Activity for owner books with "ACCEPTED" {@link BOOK_STATUS}
 */
public class AcceptedOwnedDetailActivity extends BaseDetailActivity {
    public static final int GET_SCANNED_ISBN = 2001;
    private AcceptedOwnedDetailViewModel vm;

    /**
     * Called when creating the activity view
     * @param savedInstanceState the saved objects, should contain nothing for this activity
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new ViewModelProvider(this).get(AcceptedOwnedDetailViewModel.class);

        vm.getRequest(book).observe(this, request -> {
            loadingOverlay.hide();
            if (request.getStatus().equals(REQUEST_STATUS.ACCEPTED)) {
                button.setText(R.string.scan_hand_over);
                button.setOnClickListener(this::openScanner);
            } else if (request.getStatus().equals(REQUEST_STATUS.HANDING)) {
                button.setText(getString(R.string.scan_hand_over_success, request.getRequester()));
                button.setClickable(false);
            }
        });

        vm.getErrorMessage().observe(this, error -> {
            loadingOverlay.hide();
            if (error.equals(SCAN_ERROR.INVALID_ISBN)) {
                Toast.makeText(this, getString(R.string.scan_invalid_isbn_error), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.unexpected_error), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * onClickListener for the "Scan to Handover" button
     * called when owner wants to handover a accepted book
     * opens ScannerActivity
     * @param view the "Scan to Handover" button view
     */
    private void openScanner(View view) {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivityForResult(intent, GET_SCANNED_ISBN);
    }

    /**
     * called upon returning from the {@link ScannerActivity}
     * @param requestCode the request code that starts the activity
     * @param resultCode the result code sent from the activity
     * @param data the intent data that contains the isbn scanned
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GET_SCANNED_ISBN) {
                String scannedIsbn = data.getStringExtra(ScannerActivity.ISBN_KEY);
                loadingOverlay.show();
                vm.handleScannedIsbn(book.getIsbn(), scannedIsbn);
            }
        }
    }
}
