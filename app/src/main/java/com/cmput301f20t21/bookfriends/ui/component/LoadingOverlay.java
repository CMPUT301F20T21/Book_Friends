/*
 * LoadingOverlay.java
 * Version: 1.0
 * Date: November 25, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.component;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * a loading screen that displays after an async button click
 */
public class LoadingOverlay {
    private Activity activity;
    private View view;

    public LoadingOverlay(Context context, View view) {
        this.activity = (Activity) context;
        this.view = view;
    }

    public void show() {
        activity.runOnUiThread(() -> {
            view.setVisibility(View.VISIBLE);
        });
    }

    public void hide() {
        activity.runOnUiThread(() -> {
            view.setVisibility(View.GONE);
        });
    }
}
