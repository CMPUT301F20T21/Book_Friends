package com.cmput301f20t21.bookfriends.ui.browse;

import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;

import java.util.ArrayList;

public class BrowseViewModel extends ViewModel {
    private ArrayList<Book> SearchBookData = new ArrayList<>();
    public ArrayList<Book> getBooks() {
        SearchBookData.add(new Book ("123","1","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","2","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","3","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","4","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","5","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","6","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","7","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","8","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","9","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","10","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","11","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","12","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","13","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","14","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        return SearchBookData;
    }
}
