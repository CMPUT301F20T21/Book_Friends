package com.cmput301f20t21.bookfriends.ui.borrow;


import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;

import java.util.ArrayList;

public class AcceptedListViewModel extends ViewModel {
    public ArrayList<Book> getBooks() {
        ArrayList<Book> AcceptedBookData = new ArrayList<>();
        AcceptedBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        AcceptedBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        AcceptedBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        AcceptedBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        AcceptedBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        AcceptedBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        AcceptedBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        AcceptedBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        return AcceptedBookData;
    }
}