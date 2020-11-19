package com.cmput301f20t21.bookfriends.utils;

import android.content.Context;
import android.util.Log;

import com.cmput301f20t21.bookfriends.callbacks.OnFailCallback;
import com.cmput301f20t21.bookfriends.callbacks.OnFailCallbackWithMessage;
import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallback;
import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallbackWithMessage;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.entities.User;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;

// https://stackoverflow.com/a/30604191/7358099
public class NotificationSender {
    private static final String TAG = "bfriends";
    private static final String ENDPOINT = "http://155.138.226.148/notify";
    private static NotificationSender instance = null;

    private OkHttpClient client;

    private NotificationSender() {
        client = new OkHttpClient();
    }

    public static NotificationSender getInstance() {
        if (instance == null) {
            instance = new NotificationSender();
        }
        return instance;
    }

    public void accept(Request request, OnSuccessCallbackWithMessage<String> successCallback, OnFailCallbackWithMessage<IOException> failCallback) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                String url = ENDPOINT + "/accept/" + request.getId();
                okhttp3.Request req = new okhttp3.Request.Builder()
                        .url(url)
                        .get()
                        .build();

                try {
                    Response res = client.newCall(req).execute();
                    successCallback.run(res.body().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    failCallback.run(e);
                }
            }
        };
        thread.start();
    }

    public void request(Book book, User borrower, OnSuccessCallbackWithMessage<String> successCallback, OnFailCallbackWithMessage<IOException> failCallback) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                String url = ENDPOINT + "/request/" + borrower.getUsername() + "/" + book.getId();
                okhttp3.Request req = new okhttp3.Request.Builder()
                        .addHeader("connection", "close")
                        .url(url)
                        .get()
                        .build();
                try {
                    Log.e(TAG, "sending request: " + url);
                    Response res = client.newCall(req).execute();
                    Log.e(TAG, "response: " + res.body().toString());
                    successCallback.run(res.body().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    failCallback.run(e);
                }
            }
        };
        thread.start();
    }

}
