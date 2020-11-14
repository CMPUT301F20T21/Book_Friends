package com.cmput301f20t21.bookfriends.ui.request;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f20t21.bookfriends.utils.GlideApp;
import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.ui.library.OwnedListFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestActivity extends AppCompatActivity {
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean locationPermissionGranted = false;
    private GoogleMap myMap;

    private EditText searchText;

    private RecyclerView recyclerView;
    private RequestAdapter requestAdapter;
    //    private final ArrayList<Request> requestDataList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView descriptionTextView;
    private TextView bookStatus;
    private ImageView bookImage;

    private RequestViewModel vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_requests_activity);

        // bind the text view
        titleTextView = findViewById(R.id.title_text_view);
        authorTextView = findViewById(R.id.author_text_view);
        descriptionTextView = findViewById(R.id.description_text_view);
        bookStatus = findViewById(R.id.status_text_view);
        bookImage = findViewById(R.id.book_image_view);

        vm = new ViewModelProvider(this).get(RequestViewModel.class);

        // getting book ID from previous activity
        String bookId = getIntent().getStringExtra(OwnedListFragment.VIEW_REQUEST_KEY);
        // since the data is mutable live, set the observer so the content will change accordingly
        vm.getBook(bookId).observe(this, book -> {
            titleTextView.setText(book.getTitle());
            authorTextView.setText(book.getAuthor());
            descriptionTextView.setText(book.getDescription());
            bookStatus.setText(book.getStatus().toString());
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(book.getCoverImageName());
            GlideApp.with(this)
                    .load(storageReference)
                    .placeholder(R.drawable.no_image)
                    .into(bookImage);
        });


        // set up the back button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_white_18);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.request_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // the requests live data. fetched once, but we keep the reference for multiple usage
        LiveData<ArrayList<Request>> requestsLiveData = vm.getRequests(bookId);
        requestAdapter = new RequestAdapter(requestsLiveData.getValue());
        requestAdapter.setOnItemClickListener(new RequestAdapter.OnItemClickListener() {
            @Override
            public void onRejectClick(int position) {
                removeItem(position);
            }

            @Override
            public void onAcceptClick(int position) {
                openDialog(position);
            }
        });

        recyclerView.setAdapter(requestAdapter);
        // see https://github.com/CMPUT301F20T21/Book_Friends/pull/90/files#r516310260
        requestsLiveData.observe(this, requests -> requestAdapter.notifyDataSetChanged());
        // we don't update a single item for simplicity, instead, just update requests array and let recycler figure out what changed
    }

    /**
     * setup the back button on the title
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * function to remove an item when we click on Reject button
     *
     * @param position that needs removing
     */
    public void removeItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            vm.removeRequest(position);
            // don't notify changes, let's let the requests array observer do everything for us
        }
    }

    /**
     * When user click on the accept button
     * popup a dialog to prompt user about their action:
     * accept one item and remove all other items
     *
     * @param position that we accept the item
     */
    public void openDialog(int position) {
        LayoutInflater inflater = getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inflater.inflate(R.layout.confirm_dialog, null))
                .setTitle(getString(R.string.accept_this_request))
                .setPositiveButton(R.string.edit_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(RequestActivity.this, "Accepted Request", Toast.LENGTH_SHORT).show();
                        openMapDialog();
                        vm.acceptRequest(position);
                    }
                })
                .setNegativeButton(R.string.edit_cancel, null)
                .show();
    }

    private void openMapDialog() {
//        checkLocationPermission();
        Dialog dialog = new Dialog(RequestActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.map_dialog);


        MapView mapView = (MapView) dialog.findViewById(R.id.map_view);
        MapsInitializer.initialize(RequestActivity.this);

        mapView.onCreate(dialog.onSaveInstanceState());
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                myMap = googleMap;
            }
        });

        searchText = (EditText) dialog.findViewById(R.id.input_search);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == keyEvent.ACTION_DOWN
                        || keyEvent.getAction() == keyEvent.KEYCODE_ENTER) {
                        // execute searching
                    String searchString = searchText.getText().toString();
                    Geocoder geocoder = new Geocoder(dialog.getContext());
                    List<Address> addressList = new ArrayList<>();
                    try {
                        addressList = geocoder.getFromLocationName(searchString, 1);
                    } catch (IOException e) {

                    }

                    if (addressList.size() > 0) {
                        // get the first address
                        Address address = addressList.get(0);
//                        Log.d("Map", "geoLocate: found a location: " + address.toString());
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        // move camera to that address
                        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));


                        MarkerOptions options = new MarkerOptions()
                                .position(latLng)
                                .title(address.getAddressLine(0));
                        myMap.addMarker(options);
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    }
                }
                return false;
            }
        });

        Button cancelSearchButton = (Button) dialog.findViewById(R.id.cancel_search);
        cancelSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button confirmSearchButton = (Button) dialog.findViewById(R.id.confirm_search);
        confirmSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void checkLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    // Initialize the map
                }
            }
        }
    }
}
