/*
 * DetailButtonModel.java
 * Version: 1.0
 * Date: November 15, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.component.detailButtons;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A model used for each of the detail buttons
 */
public class DetailButtonModel {
    private String title;
    private String body; // optional
    private OnDetailButtonClick onClick;
    private AfterBind afterBind;

    /**
     * The model representation of the button. we store content and listeners and callbacks here
     * not views.
     *
     * @param title     the title texts for this button
     * @param body      the body/secondary texts for this button
     * @param onClick   the onclick callback
     * @param afterBind optional hook callback after onBind almost finishes. for extra styling use.
     */
    public DetailButtonModel(@NonNull String title, @Nullable String body, @NonNull OnDetailButtonClick onClick, @Nullable AfterBind afterBind) {
        this.title = title;
        this.body = body;
        this.onClick = onClick;
        this.afterBind = afterBind;
    }

    /**
     * used only by the view holder
     *
     * @return the on click callback
     */
    public OnDetailButtonClick getOnClick() {
        return onClick;
    }

    /**
     * used only by the view holder
     *
     * @return the body text string
     */
    public String getBody() {
        return body;
    }

    /**
     * used only by the view holder
     *
     * @return the title text string
     */
    public String getTitle() {
        return title;
    }

    /**
     * used only by the view holder
     *
     * @return the after bind hook/callback
     */
    public AfterBind getAfterBind() {
        return afterBind;
    }

    public interface OnDetailButtonClick {
        void run(View view);
    }

    public interface AfterBind {
        void run(View view);
    }
}
