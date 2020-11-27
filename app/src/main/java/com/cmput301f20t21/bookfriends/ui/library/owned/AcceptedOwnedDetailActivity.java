package com.cmput301f20t21.bookfriends.ui.library.owned;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.cmput301f20t21.bookfriends.enums.SCAN_ERROR;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;
import com.cmput301f20t21.bookfriends.ui.scanner.ScannerActivity;

public class AcceptedOwnedDetailActivity extends BaseDetailActivity {
    public static final int GET_SCANNED_ISBN = 2001;
    private AcceptedOwnedDetailViewModel vm;

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
                loadingOverlay.show();
                vm.handleScannedIsbn(book.getIsbn(), scannedIsbn);
            }
        }
    }
}
