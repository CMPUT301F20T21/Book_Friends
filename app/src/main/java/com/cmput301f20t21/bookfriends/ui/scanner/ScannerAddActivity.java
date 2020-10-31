package com.cmput301f20t21.bookfriends.ui.scanner;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.ui.component.ProgressButton;
import com.cmput301f20t21.bookfriends.ui.profile.ProfileSearchFragment;
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
        /* TODO should we show something more?
         * 1. there could be async request to grab book info
         * 2. then we could be inflating a book card
         * 3. we could have a progress loading icon...
         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scanner_add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.scanner_menu_button) {
            // TODO pass data to parent book edit activity
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
