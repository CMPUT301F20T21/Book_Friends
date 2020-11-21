package com.cmput301f20t21.bookfriends.utils;

import com.cmput301f20t21.bookfriends.callbacks.OnFailCallbackWithMessage;
import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallbackWithMessage;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.Request;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;

// https://stackoverflow.com/a/30604191/7358099
public class NotificationSender {
    private static final String TAG = "bfriends";
    private static final String ENDPOINT = "http://155.138.226.148/notify";
    private static NotificationSender instance = null;

    private final OkHttpClient client;

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

    public void request(Book book, String borrowerUsername, OnSuccessCallbackWithMessage<String> successCallback, OnFailCallbackWithMessage<IOException> failCallback) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                String url = ENDPOINT + "/request/" + borrowerUsername + "/" + book.getId();
                okhttp3.Request req = new okhttp3.Request.Builder()
                        .addHeader("connection", "close")
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

}
