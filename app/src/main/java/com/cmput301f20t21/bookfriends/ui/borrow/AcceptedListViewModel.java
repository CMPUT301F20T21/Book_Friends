package com.cmput301f20t21.bookfriends.ui.borrow;


import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;

import java.util.ArrayList;

public class AcceptedListViewModel extends ViewModel {
    public ArrayList<Book> getBooks() {
        ArrayList<Book> AcceptedBookData = new ArrayList<>();
        AcceptedBookData.add(new Book ("The two towers" ));
        AcceptedBookData.add(new Book ("The Lord of the Rings"));
        AcceptedBookData.add(new Book ("Notes for CMPUT301"));
        AcceptedBookData.add(new Book ("The Return of the King"));
        AcceptedBookData.add(new Book ("The Hobbit"));
        AcceptedBookData.add(new Book ("The Return of the King"));
        AcceptedBookData.add(new Book ("The Return of the King"));
        AcceptedBookData.add(new Book ("The Return of theKing"));
        return AcceptedBookData;
    }
}