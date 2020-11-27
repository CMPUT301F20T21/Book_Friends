/*
 * RequestedDetailActivity.java
 * Version: 1.0
 * Date: November 3, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.borrow.requested;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;

/**
 * Detail activity for borrower to view details of a requested book
 */
public class RequestedDetailActivity extends BaseDetailActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        button.setText(R.string.wait_for_acceptance);
        button.setBackgroundColor(getColor(R.color.gray));
        button.setEnabled(false);
        super.inflateDetailButtons();
    }
}


