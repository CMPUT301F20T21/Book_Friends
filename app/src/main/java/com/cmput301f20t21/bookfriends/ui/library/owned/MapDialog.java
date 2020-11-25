package com.cmput301f20t21.bookfriends.ui.library.owned;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.cmput301f20t21.bookfriends.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationTokenSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapDialog extends DialogFragment implements OnMapReadyCallback {
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private GoogleMap myMap;
    private EditText searchText;
    private Button cancelSearchButton;
    private Button confirmSearchButton;
    private ImageView gpsButton;
    private RequestViewModel vm;
    private int position;
    private Context context;
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LatLng meetingLocation;

    // constructor
    public MapDialog(Context context, RequestViewModel vm, int position) {
        this.context = context;
        this.vm = vm;
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.map_dialog);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(dialog.getContext());
        getLocationPermission(dialog);

        return dialog;
    }

    /**
     * function to get location permission from user
     *
     * @param dialog
     */
    private void getLocationPermission(Dialog dialog) {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(dialog.getContext().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(dialog.getContext().getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                initMap(dialog);
            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLocationPermission(getDialog());
    }

    private void initMap(Dialog dialog) {
        mapView = (MapView) dialog.findViewById(R.id.map_view);
        mapView.onCreate(dialog.onSaveInstanceState());
        mapView.onResume();
        mapView.getMapAsync(this);

        gpsButton = (ImageView) dialog.findViewById(R.id.ic_gps);
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
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
                        meetingLocation = new LatLng(address.getLatitude(), address.getLongitude());
                        // move camera to that address
                        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(meetingLocation, 15f));

                        MarkerOptions options = new MarkerOptions()
                                .position(meetingLocation)
                                .title(address.getAddressLine(0));
                        myMap.addMarker(options);
                        // hide the keyboard after searching
                        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);
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
                if (meetingLocation != null) {
                    // accept the request only when user specifies the location of meeting
                    Toast.makeText(context, getString(R.string.accepted_request), Toast.LENGTH_SHORT).show();
                    vm.acceptRequest(position, meetingLocation);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, getString(R.string.pick_location), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    initMap(getDialog());
                }
            }
        }
    }

    /**
     * function to move the camera to user's location
     */
    private void getDeviceLocation() {
        try {
            fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, null)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15f);
                        } else {
                            Toast.makeText(context, getString(R.string.fail_to_get_location), Toast.LENGTH_SHORT).show();
                            LatLng edmontonLatLng = new LatLng(53.544388, -113.490929);
                            moveCamera(edmontonLatLng, 12f);
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * function to move camera to a desired location
     *
     * @param latLng the lat and long of location that we want to move camera to
     * @param zoom   how big the map should be displayed at that location
     */
    private void moveCamera(LatLng latLng, float zoom) {
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        getDeviceLocation();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        myMap.setMyLocationEnabled(true);
        myMap.getUiSettings().setMyLocationButtonEnabled(false);

        myMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                myMap.clear();
                Geocoder geocoder = new Geocoder(getDialog().getContext());
                List<Address> addressList = new ArrayList<>();
                try {
                    addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = addressList.get(0);
                MarkerOptions options = new MarkerOptions().position(latLng);
                meetingLocation = latLng;
                searchText.setText(address.getAddressLine(0));
                myMap.addMarker(options);
            }
        });
    }
}
