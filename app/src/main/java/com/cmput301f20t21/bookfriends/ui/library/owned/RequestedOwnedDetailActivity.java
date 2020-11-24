/*
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.library.owned;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;

public class RequestedOwnedDetailActivity extends BaseDetailActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        button.setVisibility(View.GONE);
        ownerDetail.setVisibility(View.GONE);
    }
}
