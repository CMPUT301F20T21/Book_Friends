/*
 * ImagePainter.java
 * Version: 1.0
 * Date: October 20, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.utils;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cmput301f20t21.bookfriends.R;

/**
 * A static class/method used to paint the image using Glide
 */
public class ImagePainter {
    public static <T> void paintImage(ImageView imageView, T drawable) {
        // if drawable is null, null instanceof Object will always return false
        if ((drawable instanceof String) || (drawable instanceof Uri)) {
            Glide.with(imageView)
                    .load(drawable)
                    .placeholder(R.drawable.no_image)
                    .into(imageView);
        } else {
            Glide.with(imageView)
                    .load(R.drawable.no_image)
                    .into(imageView);
        }
    }
}
