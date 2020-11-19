package com.cmput301f20t21.bookfriends.ui.borrow.accepted;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.cmput301f20t21.bookfriends.enums.SCAN_ERROR;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;
import com.cmput301f20t21.bookfriends.ui.library.owned.AcceptedOwnedDetailViewModel;
import com.cmput301f20t21.bookfriends.ui.scanner.ScannerBaseActivity;

public class AcceptedDetailActivity extends BaseDetailActivity {

    public static final int GET_SCANNED_ISBN = 2001;
    private Button actionButton;
    private AcceptedDetailViewModel vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new ViewModelProvider(this).get(AcceptedDetailViewModel.class);
        actionButton = findViewById(R.id.detail_action_button);

        vm.getRequest(book).observe(this, request -> {
            if (request.getStatus().equals(REQUEST_STATUS.ACCEPTED)) {
                actionButton.setText(getString(R.string.scan_wait_for_hand_over, book.getOwner()));
                actionButton.setClickable(false);
            } else if (request.getStatus().equals(REQUEST_STATUS.HANDING)) {
                actionButton.setText(R.string.scan_receive);
                actionButton.setOnClickListener(this::openScanner);
            } else if (request.getStatus().equals(REQUEST_STATUS.BORROWED)) {
                actionButton.setText(getString(R.string.scan_receive_success, book.getTitle()));
                actionButton.setClickable(false);
            }
        });

        vm.getErrorMessage().observe(this, error -> {
            if (error.equals(SCAN_ERROR.INVALID_ISBN)) {
                Toast.makeText(this, getString(R.string.scan_invalid_isbn_error), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.unexpected_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openScanner(View view) {
        Intent intent = new Intent(this, ScannerBaseActivity.class);
        startActivityForResult(intent, GET_SCANNED_ISBN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GET_SCANNED_ISBN) {
                String scannedIsbn = data.getStringExtra(ScannerBaseActivity.ISBN_KEY);
                vm.handleScannedIsbn(book, scannedIsbn, this::onScannedSuccess);
            }
        }
    }

    private void onScannedSuccess(Book updatedBook) {
        updateBook(updatedBook);
    }

}



