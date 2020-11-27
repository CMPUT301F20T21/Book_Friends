package com.cmput301f20t21.bookfriends.ui.component;

import android.app.Activity;
import android.content.Context;
import android.view.View;

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
