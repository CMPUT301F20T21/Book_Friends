package com.cmput301f20t21.bookfriends.ui.borrow;

import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;

import java.util.ArrayList;

public class RequestedViewModel extends ViewModel {
    private  ArrayList<Book> RequestedBookData = new ArrayList<>();
    public ArrayList<Book> getBooks() {

        RequestedBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        RequestedBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        RequestedBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        RequestedBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        RequestedBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        RequestedBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        RequestedBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        return RequestedBookData;
    }

    public Book getBookByIndex(Integer index) {
        return RequestedBookData.get(index);
    }

}