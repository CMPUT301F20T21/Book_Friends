package com.cmput301f20t21.bookfriends.ui.library.owned;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.cmput301f20t21.bookfriends.enums.SCAN_ERROR;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;
import com.cmput301f20t21.bookfriends.ui.component.MeetLocationDialog;
import com.cmput301f20t21.bookfriends.ui.component.detailButtons.DetailButtonModel;
import com.cmput301f20t21.bookfriends.ui.component.detailButtons.DetailButtonsFragment;
import com.cmput301f20t21.bookfriends.ui.scanner.ScannerActivity;

import java.util.ArrayList;
import java.util.List;

public class AcceptedOwnedDetailActivity extends BaseDetailActivity {
    public static final int GET_SCANNED_ISBN = 2001;
    private AcceptedOwnedDetailViewModel vm;
    private Request request;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new ViewModelProvider(this).get(AcceptedOwnedDetailViewModel.class);

        vm.getRequest(book).observe(this, request -> {
            this.request = request;
            loadingOverlay.hide();
            if (request.getStatus().equals(REQUEST_STATUS.ACCEPTED)) {
                button.setText(R.string.scan_hand_over);
                button.setOnClickListener(this::openScanner);
            } else if (request.getStatus().equals(REQUEST_STATUS.HANDING)) {
                button.setText(getString(R.string.scan_hand_over_success, request.getRequester()));
                button.setClickable(false);
            }
            inflateDetailButtons();
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
     * create all the accepted detail-specific buttons and define their onclick behaviours
     * for how button models work in the buttons recycler, refer to components/detailButtons
     *
     * @return the list of button models
     */
    @Override
    protected List<DetailButtonModel> getDetailButtonModels() {
        List<DetailButtonModel> buttonModels = new ArrayList<>();
        if (request.getMeetingLocation() != null) {
            buttonModels.add(
                    new DetailButtonModel(
                            getString(R.string.detail_button_meetup_title),
                            getMeetingAddress(request),
                            (view) -> {
                                // onclick
                                MeetLocationDialog meetLocationDialog = new MeetLocationDialog(request.getMeetingLocation());
                                meetLocationDialog.show(getSupportFragmentManager(), "locationMap");
                            },
                            null
                    ));
        }
        return buttonModels;
    }

    /**
     * create and inflate and show the list of buttons
     * <p>
     * we need request because buttons might change content based on request data.
     * and the request comes from vm which means it changes on vm fetch completes
     */
    @Override
    protected void inflateDetailButtons() {
        DetailButtonsFragment buttonsFragment = new DetailButtonsFragment(getDetailButtonModels());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_buttons_container, buttonsFragment)
                .commit();
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
