/*
 * GoogleBookService.java
 * Version: 1.0
 * Date: November 20, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.services;

import android.util.Log;

import com.cmput301f20t21.bookfriends.callbacks.OnFailCallback;
import com.cmput301f20t21.bookfriends.callbacks.OnFailCallbackWithMessage;
import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallbackWithMessage;
import com.cmput301f20t21.bookfriends.entities.GoogleBookData;
import com.cmput301f20t21.bookfriends.enums.SCAN_ERROR;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Service to grab book information with the provided ISBN
 */
public class GoogleBookService {
    private final Gson gson;
    private final OkHttpClient client;
    private static GoogleBookService instance;

    private GoogleBookService() {
        gson = new Gson();
        client = new OkHttpClient();
    }

    public static GoogleBookService getInstance() {
        if (instance == null) {
            instance = new GoogleBookService();
        }
        return instance;
    }

    public void getBookInfoByIsbn(String isbn, OnSuccessCallbackWithMessage<GoogleBookData> successCallback, OnFailCallbackWithMessage<SCAN_ERROR> failCallback) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
                okhttp3.Request req = new okhttp3.Request.Builder()
                        .url(url)
                        .get()
                        .build();

                try {
                    Response res = client.newCall(req).execute();
                    GoogleBookData data = gson.fromJson(res.body().string(), GoogleBookData.class);
                    if (data.getTotalItems() > 0) {
                        successCallback.run(data);
                    } else {
                        failCallback.run(SCAN_ERROR.INVALID_ISBN);
                    }

                } catch (Exception e) {
                    Log.d("GOOGLE_BOOK:", e.getMessage());
                    failCallback.run(SCAN_ERROR.UNEXPECTED);
                }
            }
        };
        thread.start();
    }
}
