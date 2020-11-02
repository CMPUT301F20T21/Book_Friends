/*
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.datatransfer;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// https://stackoverflow.com/a/18463758
    public class BookDataTransferHelper {
    public static final String BOOK_OBJECT_KEY = "com.cmput301f20t21.bookfriends.BOOK_OBJECT";

    public static void transfer(Book book, Context context) {
        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor preferenceEditor = sharedPreference.edit();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriSerializer())
                .create();
        String json = gson.toJson(book);
        preferenceEditor.putString(BOOK_OBJECT_KEY, json);
        preferenceEditor.apply();
    }

    public static Book receive(Context context) {
        SharedPreferences sharedPreference =
                PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriDeserializer())
                .create();
        String json = sharedPreference.getString(BOOK_OBJECT_KEY, "");
        return gson.fromJson(json, Book.class);
    }

}
