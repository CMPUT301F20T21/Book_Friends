package com.cmput301f20t21.bookfriends.ui.library;

import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;

import java.util.ArrayList;

public class BorrowedViewModel extends ViewModel {
    public ArrayList<Book> getBooks() {
        ArrayList<Book> mData = new ArrayList<>();
        mData.add(new Book ("a1"));
        mData.add(new Book ("a2"));
        mData.add(new Book ("a3"));
        mData.add(new Book ("a4"));
        return mData;
    }

}