package com.cmput301f20t21.bookfriends.ui.library;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;

import java.util.ArrayList;

public class LibraryViewModel extends ViewModel {
    public enum TabType {
        MY_BOOKS,
        BORROWED_BOOKS
    }

    public static TabType getTabTypeFromPosition (int position) {
        switch (position) {
            case 0: return TabType.MY_BOOKS;
            case 1: return TabType.BORROWED_BOOKS;
            default: return null;
        }
    }

    public ArrayList<Book> getOwnedBooks() {
        ArrayList<Book> mData = new ArrayList<>();
        mData.add(new Book ("lucas", "123456"));
        mData.add(new Book ("lucas2", "123456"));
        mData.add(new Book ("lucas3", "123456"));
        mData.add(new Book ("lucas4", "123456"));
        return mData;
    }

    public ArrayList<Book> getBorrowedBooks() {
        ArrayList<Book> mData = new ArrayList<>();
        mData.add(new Book ("a1", "123456"));
        mData.add(new Book ("a2", "123456"));
        mData.add(new Book ("a3", "123456"));
        mData.add(new Book ("a4", "123456"));
        return mData;
    }

}