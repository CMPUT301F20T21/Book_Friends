package com.cmput301f20t21.bookfriends.ui.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
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
        }
        return super.onOptionsItemSelected(item);
    }
}
