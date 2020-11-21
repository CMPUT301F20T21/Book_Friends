package com.cmput301f20t21.bookfriends.ui.scanner;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentContainerView;

import com.cmput301f20t21.bookfriends.R;
import com.google.android.gms.vision.barcode.Barcode;

public class ScannerAddActivity extends ScannerBaseActivity {
    FragmentContainerView fragmentContainerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO we might use this fragment to display more book info
        fragmentContainerView = findViewById(R.id.scanner_extra_fragment_container);
    }

    @Override
    protected void onBarcodeReceive(Barcode barcode) {
        super.onBarcodeReceive(barcode);
    }
}
