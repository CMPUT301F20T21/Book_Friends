package com.cmput301f20t21.bookfriends.ui.borrow;

import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;

import java.util.ArrayList;

public class RequestedViewModel extends ViewModel {
    public ArrayList<Book> getBooks() {
        ArrayList<Book> RequestedBookData = new ArrayList<>();
        RequestedBookData.add(new Book ("Some Book Name"));
        RequestedBookData.add(new Book ("How to Squish a Cat"));
        RequestedBookData.add(new Book ("Calculus II"));
        RequestedBookData.add(new Book ("Calculus III"));
        RequestedBookData.add(new Book ("Calculus IV"));
        RequestedBookData.add(new Book ("Calculator V"));
        RequestedBookData.add(new Book ("Terminator V"));
        return RequestedBookData;
    }



}