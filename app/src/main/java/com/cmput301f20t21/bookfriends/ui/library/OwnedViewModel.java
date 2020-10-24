package com.cmput301f20t21.bookfriends.ui.library;

import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;

import java.util.ArrayList;

public class OwnedViewModel extends ViewModel {
    public ArrayList<Book> getBooks() {
        ArrayList<Book> mData = new ArrayList<>();
        mData.add(new Book ("The two towers" ));
        mData.add(new Book ("The Lord of the Rings"));
        mData.add(new Book ("Notes for CMPUT301"));
        mData.add(new Book ("The Return of the King"));
        mData.add(new Book ("The Hobbit"));
        mData.add(new Book ("The Return of the King"));
        mData.add(new Book ("The Return of the King"));
        mData.add(new Book ("The Return of theKing"));
        return mData;
    }
}