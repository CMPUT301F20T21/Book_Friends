package com.cmput301f20t21.bookfriends.ui.request;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapDialog extends DialogFragment {
    private GoogleMap myMap;
    private EditText searchText;
    private Button cancelSearchButton;
    private Button confirmSearchButton;

    // constructor
    public MapDialog() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog =  super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.map_dialog);

        MapView mapView = (MapView) dialog.findViewById(R.id.map_view);

        mapView.onCreate(dialog.onSaveInstanceState());
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng edmontonLatLng = new LatLng(53.544388, -113.490929);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(edmontonLatLng, 12f));
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
                        // clear the previous marker
                        myMap.clear();
                        // get the first address
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        // move camera to that address
                        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));

                        MarkerOptions options = new MarkerOptions()
                                .position(latLng)
                                .title(address.getAddressLine(0));
                        myMap.addMarker(options);
                        // TODO: hide the keyboard after searching (optional)
                        // not sure why this is not working properly
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    }
                }
                return false;
            }
        });

        cancelSearchButton = dialog.findViewById(R.id.cancel_search);
        cancelSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        confirmSearchButton = (Button) dialog.findViewById(R.id.confirm_search);
        confirmSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: accept the request only when user specifies the location of meeting
                dialog.dismiss();
            }
        });

        return dialog;
    }

}
