package com.cmput301f20t21.bookfriends.ui.library;

import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;

import java.util.ArrayList;

public class OwnedViewModel extends ViewModel {
    public ArrayList<Book> getBooks() {
        ArrayList<Book> mData = new ArrayList<>();

        return mData;
    }
}