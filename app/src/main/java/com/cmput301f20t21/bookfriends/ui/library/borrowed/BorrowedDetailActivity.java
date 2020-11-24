package com.cmput301f20t21.bookfriends.ui.library.borrowed;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.cmput301f20t21.bookfriends.enums.SCAN_ERROR;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;
import com.cmput301f20t21.bookfriends.ui.scanner.ScannerActivity;

public class BorrowedDetailActivity extends BaseDetailActivity {
    public static final int GET_SCANNED_ISBN = 2001;

    private BorrowedDetailViewModel vm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm = new ViewModelProvider(this).get(BorrowedDetailViewModel.class);

        vm.getRequest(book).observe(this, request -> {
            if (request.getStatus().equals(REQUEST_STATUS.BORROWED)) {
                button.setText(R.string.scan_to_return);
                button.setOnClickListener(this::openScanner);
            } else if (request.getStatus().equals(REQUEST_STATUS.RETURNING)) {
                button.setText(getString(R.string.scan_hand_over_success, book.getOwner()));
                button.setClickable(false);

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

    @Override
    protected void onStart() {
        super.onStart();
        vm.registerSnapshotListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        vm.unregisterSnapshotListener();
    }

    private void openScanner(View view) {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivityForResult(intent, GET_SCANNED_ISBN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GET_SCANNED_ISBN) {
                String scannedIsbn = data.getStringExtra(ScannerActivity.ISBN_KEY);
                vm.handleScannedIsbn(book, scannedIsbn, this::onScannedSuccess);
            }
        }
    }

    private void onScannedSuccess(Book updatedBook) {
        updateBook(updatedBook);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       super.onOptionsItemSelected(item);
       return true;
    }

}
