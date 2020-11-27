/*
 * MeetLocationDialog.java
 * Version: 1.0
 * Date: November 10, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.borrow.accepted;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cmput301f20t21.bookfriends.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

/**
 * Dialog to display the meet location set by the owner
 */
public class MeetLocationDialog extends DialogFragment implements OnMapReadyCallback {
    private GoogleMap map;
    private MapView mapView;
    private GeoPoint geoPoint;

    private Button cancelButton;

    public MeetLocationDialog(GeoPoint point) {
        this.geoPoint = point;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.map_dialog);
        dialog.findViewById(R.id.map_search_layout).setVisibility(View.GONE);
        dialog.findViewById(R.id.map_ic_gps).setVisibility(View.GONE);
        dialog.findViewById(R.id.map_search_confirm).setVisibility(View.GONE);

        cancelButton = dialog.findViewById(R.id.map_search_cancel);
        cancelButton.setText(R.string.meet_location_dialog_cancel);
        cancelButton.setOnClickListener(v -> dismiss());

        mapView = dialog.findViewById(R.id.map_view);
        initMap(savedInstanceState);
        return dialog;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (geoPoint != null) {
            LatLng latLng = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
            moveCamera(latLng, 12f);
            MarkerOptions options = new MarkerOptions()
                    .position(latLng);
            map.addMarker(options);
        }
    }

    private void initMap(@Nullable Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    /**
     * function to move camera to a desired location
     *
     * @param latLng the lat and long of location that we want to move camera to
     * @param zoom   how big the map should be displayed at that location
     */
    private void moveCamera(LatLng latLng, float zoom) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }
}
