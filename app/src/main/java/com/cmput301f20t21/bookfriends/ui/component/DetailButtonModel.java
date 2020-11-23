package com.cmput301f20t21.bookfriends.ui.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.callbacks.OnDetailButtonClick;

public class DetailButtonModel {
    private String title;
    private String body; // optional
    private OnDetailButtonClick onClick;

    public DetailButtonModel(@NonNull String title, @Nullable String body, @NonNull OnDetailButtonClick onClick) {
        this.title = title;
        this.body = body;
        this.onClick = onClick;
    }

    public OnDetailButtonClick getOnClick() {
        return onClick;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }
}
