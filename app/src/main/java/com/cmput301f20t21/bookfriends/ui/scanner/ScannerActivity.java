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

public class ScannerActivity extends AppCompatActivity {
    final static int REQUEST_CAMERA_PERMISSION = 100;
    final static String T = "bf_scanner";
    private SurfaceView surfaceView;
    private TextView isbnText;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private Barcode detectedBarcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        initWithPermission();
    }

    private void initWithPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            String[] permission = {Manifest.permission.CAMERA};
            requestPermissions(permission, REQUEST_CAMERA_PERMISSION);
        } else { // permission granted
            init();
        }
    }

    private void onBarcodeReceive(Barcode bar) {
        detectedBarcode = bar;
        isbnText.setText(bar.rawValue);
    }

    private void init() {
        surfaceView = findViewById(R.id.scanner_preview_surface);
        isbnText = findViewById(R.id.scanner_isbn_result_text);

        isbnText.setText("Scanning...");

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
                .setRequestedPreviewSize(400,300)
                .setRequestedFps(5.0f)
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
