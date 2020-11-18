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

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.ui.library.OwnedListFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.cmput301f20t21.bookfriends.utils.ImagePainter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestActivity extends AppCompatActivity {
    private GoogleMap myMap;

    private EditText searchText;

    private RecyclerView recyclerView;
    private RequestAdapter requestAdapter;
    //    private final ArrayList<Request> requestDataList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView bookStatus;
    private ImageView bookImage;

    private RequestViewModel vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_requests);

        // bind the text view
        titleTextView = findViewById(R.id.detail_title);
        authorTextView = findViewById(R.id.detail_author);
        bookStatus = findViewById(R.id.status_text_view);
        bookImage = findViewById(R.id.book_image_view);

        vm = new ViewModelProvider(this).get(RequestViewModel.class);

        // getting book ID from previous activity
        String bookId = getIntent().getStringExtra(OwnedListFragment.VIEW_REQUEST_KEY);
        // since the data is mutable live, set the observer so the content will change accordingly
        vm.getBook(bookId).observe(this, book -> {
            titleTextView.setText(book.getTitle());
            authorTextView.setText(book.getAuthor());
            bookStatus.setText(book.getStatus().toString());
            ImagePainter.paintImage(bookImage, book.getImageUrl());
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
        builder.setView(inflater.inflate(R.layout.dialog_confirm, null))
                .setTitle(getString(R.string.accept_this_request))
                .setPositiveButton(R.string.edit_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(RequestActivity.this, "Accepted Request", Toast.LENGTH_SHORT).show();
                        MapDialog mapDialog = new MapDialog();
                        mapDialog.show(getSupportFragmentManager(), "map");
//                        openMapDialog();
                        vm.acceptRequest(position);
                    }
                })
                .setNegativeButton(R.string.edit_cancel, null)
                .show();
    }

}
