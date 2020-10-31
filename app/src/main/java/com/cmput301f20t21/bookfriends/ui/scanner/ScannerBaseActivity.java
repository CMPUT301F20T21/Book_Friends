package com.cmput301f20t21.bookfriends.ui.scanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    final static int REQUEST_CAMERA_PERMISSION = 100;
    final static String T = "bf_scanner";
    private SurfaceView surfaceView;
    private TextView isbnText;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private Barcode detectedBarcode;

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
        initWithPermission();
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
     * @param barcode the Barcode object that contains all the information we need
     *                the code is accessible via barcode.rawValue
     */
    protected void onBarcodeReceive(Barcode barcode) {
        detectedBarcode = barcode;
        isbnText.setText(barcode.rawValue);
    }

    private void init() {
        surfaceView = findViewById(R.id.scanner_preview_surface);
        isbnText = findViewById(R.id.scanner_isbn_result_text);

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
                        onBarcodeReceive(b);
                    }
                }
            }
        });
        Log.d(T, "barcode detector init status: " + barcodeDetector.isOperational());

        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(300, 300)
                .setRequestedFps(2.0f)
                .setAutoFocusEnabled(true)
                .build();

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
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init();
            } else {
                // permission denied by user
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }
}