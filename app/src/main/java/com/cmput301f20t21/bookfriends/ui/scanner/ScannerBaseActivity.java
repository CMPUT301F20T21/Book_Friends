package com.cmput301f20t21.bookfriends.ui.scanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.cmput301f20t21.bookfriends.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/**
 * the base Activity to be extended by any that needs the scanning functionality
 */
public class ScannerBaseActivity extends AppCompatActivity {
    public static final String ISBN_KEY = "com.cmput301f20t21.bookfriends.ISBN_KEY";

    final static int REQUEST_CAMERA_PERMISSION = 100;
    final static String T = "bf_scanner";
    protected Barcode detectedBarcode = new Barcode();
    private SurfaceView surfaceView;
    private TextView isbnText;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    /**
     * on create, the activity will setup views and ask for permission,
     * and also start detecting for barcode right away.
     *
     * @param savedInstanceState don't need
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        setChildViews();

        surfaceView.setVisibility(View.INVISIBLE);

        initWithPermission();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_white_18);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setChildViews() {
        surfaceView = findViewById(R.id.scanner_preview_surface);
        isbnText = findViewById(R.id.scanner_isbn_result_text);
    }

    /**
     * initialize views, camera and detector after ensuring the camera permission is granted
     */
    private void initWithPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            String[] permission = {Manifest.permission.CAMERA};
            requestPermissions(permission, REQUEST_CAMERA_PERMISSION);
        } else { // permission granted
            init();
        }
    }

    /**
     * when the base scanner successfully detects a barcode, this function is called
     * note that it might be called **many times** upon success detection.
     *
     * @param barcode the Barcode object that contains all the information we need
     *                the code is accessible via barcode.rawValue
     */
    private void onBarcodeUpdate(Barcode barcode) {
        // runs at a high frequency
        if (!barcode.rawValue.equals(detectedBarcode.rawValue)) {
            detectedBarcode = barcode;
            onBarcodeReceive(barcode);
        }
    }

    /**
     * when the barcode change, call this function. exposed to children
     * called way less frequent than onBarcodeUpdate
     *
     * @param barcode
     */
    protected void onBarcodeReceive(Barcode barcode) {
        isbnText.setText(barcode.rawValue);
    }

    /**
     * initialize cameraSource, detector and start detecting
     */
    private void init() {
        isbnText.setText(R.string.scanner_scanning);

        barcodeDetector = new BarcodeDetector.Builder(getApplicationContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0) {
                    for (int i = 0; i < barcodes.size(); i++) {
                        Barcode b = barcodes.valueAt(i);
                        onBarcodeUpdate(b);
                    }
                }
            }
        });

        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(300, 300)
                .setRequestedFps(2.0f)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.setVisibility(View.VISIBLE);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @SuppressLint("MissingPermission")
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        ;
    }

    /**
     * on pause, we want to free up cameraSource object from memory
     */
    @Override
    protected void onPause() {
        if (cameraSource != null) cameraSource.release();
        if (barcodeDetector != null) barcodeDetector.release();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initWithPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                init();
                try {
                    // have to re-initiate the cameraSource because surfaceView already started and we lost the chance to hook
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // permission denied by user
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scanner_add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.scanner_menu_button) {
            // pass the isbn value to whatever parent activity
            setResult(Activity.RESULT_OK, new Intent().putExtra(ISBN_KEY, detectedBarcode.rawValue));
            finish();
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
