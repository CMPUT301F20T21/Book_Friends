package com.cmput301f20t21.bookfriends.ui.borrow.accepted;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.cmput301f20t21.bookfriends.enums.SCAN_ERROR;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;
import com.cmput301f20t21.bookfriends.ui.scanner.ScannerActivity;
import com.cmput301f20t21.bookfriends.ui.component.detailButtons.DetailButtonModel;
import com.cmput301f20t21.bookfriends.ui.component.detailButtons.DetailButtonsFragment;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

            // inflate the buttons again when the request refreshes
            inflateDetailButtons(request);
        });

        vm.getErrorMessage().observe(this, error -> {
            if (error.equals(SCAN_ERROR.INVALID_ISBN)) {
                Toast.makeText(this, getString(R.string.scan_invalid_isbn_error), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.unexpected_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * parse the request geo location and returns a string of the address
     *
     * @param request the request.
     * @return the address string
     */
    private String getMeetingAddress(Request request) {
        if (request == null) {
            return "";
        }

        GeoPoint geoPoint = request.getMeetingLocation();
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            if (addresses == null) {
                return getString(R.string.geocoder_failure);
            }
            Address addr = addresses.get(0);
            // https://stackoverflow.com/a/19927013/7358099
            StringBuilder addressString = new StringBuilder("");
            for (int i = 0; i <= addr.getMaxAddressLineIndex(); i++) {
                addressString.append(addr.getAddressLine(i));
            }
            return addressString.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return getString(R.string.get_meeting_address_failure);
        }
    }

    /**
     * create all the accepted detail-specific buttons and define their onclick behaviours
     * for how button models work in the buttons recycler, refer to components/detailButtons
     *
     * @return the list of button models
     */
    private List<DetailButtonModel> getDetailButtonModels(Request request) {
        ArrayList<DetailButtonModel> buttonModels = new ArrayList<>();
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
    private void inflateDetailButtons(Request request) {
        DetailButtonsFragment buttonsFragment = new DetailButtonsFragment(getDetailButtonModels(request));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_buttons_container, buttonsFragment)
                .commit();
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

}



