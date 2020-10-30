package com.cmput301f20t21.bookfriends.ui.browse;

import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;

import java.util.ArrayList;

public class BrowseViewModel extends ViewModel {
    public ArrayList<Book> getBooks() {
        ArrayList<Book> SearchBookData = new ArrayList<>();
        SearchBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        SearchBookData.add(new Book ("123","456","Some Book Name","lyu","book","lyu", BOOK_STATUS.ACCEPTED));
        return SearchBookData;
    }
}
