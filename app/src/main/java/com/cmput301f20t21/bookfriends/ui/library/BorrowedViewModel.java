package com.cmput301f20t21.bookfriends.ui.library;

import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;

import java.util.ArrayList;

public class BorrowedViewModel extends ViewModel {
    public ArrayList<Book> getBooks() {
        ArrayList<Book> mData = new ArrayList<>();
        mData.add(new Book ("Some Book Name"));
        mData.add(new Book ("How to Squish a Cat"));
        mData.add(new Book ("Calculus II"));
        mData.add(new Book ("Calculus III"));
        mData.add(new Book ("Calculus IV"));
        mData.add(new Book ("Calculator V"));
        mData.add(new Book ("Terminator V"));
        return mData;
    }

}