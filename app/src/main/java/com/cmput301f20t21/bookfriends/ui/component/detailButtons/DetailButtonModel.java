package com.cmput301f20t21.bookfriends.ui.component.detailButtons;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    public interface OnDetailButtonClick {
        void run(View view);
    }
}
