/*
 * ProgressButton.java
 * Version: 1.0
 * Date: October 20, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.component;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.cmput301f20t21.bookfriends.R;

/**
 * Progress button used to login button in {@link com.cmput301f20t21.bookfriends.ui.login.LoginActivity}
 */
public class ProgressButton {
    private View view;
    private CardView cardView;
    private ConstraintLayout layout;
    private ProgressBar progressBar;
    private TextView textView;
    private String defaultText;
    private String doneText;

    private Animation fadeIn;
    private Animation fadeOut;

    public ProgressButton(Context context, View view, String defaultText, String doneText) {
        fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);

        this.view = view;
        cardView = view.findViewById(R.id.progress_button_card_view);
        layout = view.findViewById(R.id.progress_button_layout);
        progressBar = view.findViewById(R.id.progress_button_progress_bar);
        textView = view.findViewById(R.id.progress_button_text_view);
        textView.setText(defaultText);

        this.defaultText = defaultText;
        this.doneText = doneText;
    }

    public void onClick() {
        // prevent user from spamming the login button after it is clicked
        view.setClickable(false);

        // set and start animation for text
        progressBar.setAnimation(fadeIn);
        textView.setAnimation(fadeOut);
        progressBar.getAnimation().start();
        textView.getAnimation().start();

        // set progress bar and text visibility
        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
    }

    public void onSuccess() {
        // background transition color for button
        ColorDrawable[] color = {new ColorDrawable(cardView.getCardBackgroundColor().getDefaultColor()),
                new ColorDrawable(view.getResources().getColor(R.color.positive))};
        TransitionDrawable trans = new TransitionDrawable(color);

        // set and start animation for text
        progressBar.setAnimation(fadeOut);
        textView.setAnimation(fadeIn);
        progressBar.getAnimation().start();
        textView.getAnimation().start();

        // set and start background color transition
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            layout.setBackground(trans);
        } else {
            layout.setBackgroundDrawable(trans);
        }
        trans.startTransition(100);

        // set progress bar and text visibility
        progressBar.setVisibility(View.GONE);
        textView.setText(doneText);
        textView.setVisibility(View.VISIBLE);
    }

    public void onError() {
        // set and start animation for text
        progressBar.setAnimation(fadeOut);
        textView.setAnimation(fadeIn);
        progressBar.getAnimation().start();
        textView.getAnimation().start();

        // set progress bar and text visibility
        progressBar.setVisibility(View.GONE);
        textView.setText(defaultText);
        textView.setVisibility(View.VISIBLE);

        view.setClickable(true);
    }
}