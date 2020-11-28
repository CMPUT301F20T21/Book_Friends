/*
 * BrowseViewModel.java
 * Version: 1.0
 * Date: October 26, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.browse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.cmput301f20t21.bookfriends.repositories.api.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.api.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.api.RequestRepository;
import com.cmput301f20t21.bookfriends.repositories.factories.AuthRepositoryFactory;
import com.cmput301f20t21.bookfriends.repositories.factories.BookRepositoryFactory;
import com.cmput301f20t21.bookfriends.repositories.impl.RequestRepositoryImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ViewModel for {@link BrowseFragment}
 */
public class BrowseViewModel extends ViewModel {
    // exposed live data
    private final MutableLiveData<BOOK_ERROR> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Book>> books = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> updatedPosition = new MutableLiveData<>(0);

    private final MutableLiveData<String> searchQuery = new MutableLiveData<>(""); // the updated search query we are using
    // the updated/filtered/calculated book results based on changed books and/or searchQuery
    private final MediatorLiveData<List<Book>> searchedBooks = new MediatorLiveData<>();
    // need this reference to keep adapter referring to the same array in memory or else it won't update
    private final List<Book> searchedBookData = new ArrayList<>();
    // raw book data
    private final List<Book> bookData = books.getValue();

    private final RequestRepository requestRepository;
    private final BookRepository bookRepository;
    private final String currentUsername;

    // production
    public BrowseViewModel() {
        this(AuthRepositoryFactory.getRepository(), RequestRepositoryImpl.getInstance(), BookRepositoryFactory.getRepository());
    }

    // dependency injection for unit test
    public BrowseViewModel(AuthRepository authRepository, RequestRepository requestRepository, BookRepository bookRepository) {
        this.requestRepository = requestRepository;
        this.bookRepository = bookRepository;
        // get logged in user's username
        currentUsername = authRepository.getCurrentUser().getUsername();

        setupSearchedBooks(); // init the searched books before we fetch anything
        fetchBooks();
    }

    /**
     * create the searched books live data by making it listens to two source of changes:
     * 1. the original books list: any changes of the books will trigger it to re-calculate results
     * 2. the search keyword/query: changes of query should filter the searched books, too
     */
    private void setupSearchedBooks() {
        searchedBooks.setValue(searchedBookData);
        searchedBooks.addSource(searchQuery, keyword -> refreshSearchedBooks(keyword, bookData)); // keyword is new
        searchedBooks.addSource(books, bookData -> refreshSearchedBooks(searchQuery.getValue(), bookData)); // bookData is new (this is a local one!)
    }

    /**
     * filter the original bookData (or the updated bookData) with the keyword (or the updated one)
     * and set that value to searchedBookData and its live data wrapper
     *
     * @param keyword the keyword user typed to search
     * @param bookData the books to filter
     */
    private void refreshSearchedBooks(String keyword, List<Book> bookData) {
        Function<String, Boolean> contains = ifNotNullAndContains(keyword);
        List<Book> newBookData = bookData
                .stream()
                .filter(book -> contains.apply(book.getTitle()) ||
                        contains.apply(book.getAuthor()) ||
                        contains.apply(book.getOwner()) ||
                        contains.apply(book.getIsbn())
                )
                .collect(Collectors.toList());
        searchedBookData.clear();
        searchedBookData.addAll(newBookData);
        searchedBooks.setValue(searchedBookData);
    }

    /**
     * check if the attribute contains the keyword
     * @param keyword the keyword user typed to search
     * @return a {@link Function} object
     */
    private Function<String, Boolean> ifNotNullAndContains(String keyword) {
        return (attribute) -> {
            if (attribute == null || keyword == null) return true;
            return attribute.toLowerCase().contains(keyword.toLowerCase());
        };
    }

    /**
     * get a list of books, filtered by keyword
     * @return {@link LiveData} that contains a {@link List} of {@link Book}
     */
    public LiveData<List<Book>> getBooks() {
        return searchedBooks;
    }

    /**
     * get the error message
     * @return a {@link MutableLiveData} that contains {@link BOOK_ERROR} error message
     */
    public LiveData<BOOK_ERROR> getErrorMessage() {
        return errorMessage;
    }

    /**
     * get a {@link Book} from the given index
     * @param index the index of the book required
     * @return {@link Book} the book at the given index
     */
    public Book getBookByIndex(Integer index) {
        return bookData.get(index);
    }

    /**
     * US 03.01.01
     * As a borrower, I want to specify a keyword, and search for all books that are not currently accepted or borrowed whose **description** contains the keyword.
     * NOTE: description here interpreted as all the basic information of the book: title, author, owner
     *
     * @param keyword the keyword to search with
     */
    public void filterBookWithKeyword(String keyword) {
        searchQuery.setValue(keyword); // will trigger update routine for searchedBooks
    }

    /**
     * get the updated position of a book
     * @return the updated position
     */
    public MutableLiveData<Integer> getUpdatedPosition() {
        return updatedPosition;
    }

    /**
     * remove the requested books from the list of browse/available books
     * @param requestedBook the requested book
     */
    public void handleRequestedBook(Book requestedBook) {
        if (bookData.indexOf(requestedBook) != -1) {
            bookData.remove(requestedBook);
            books.setValue(bookData);
        }
    }

    private void fetchBooks() {
        // first, get all the request made by this user.
        requestRepository.getRequestsByUsernameAndStatus(currentUsername, Arrays.asList(REQUEST_STATUS.OPENED)).addOnSuccessListener(requests -> {
            // get available book for current user (book in available status and not owned by this user)
            bookRepository.getAvailableBooksForUser(currentUsername).addOnSuccessListener(availableBooks -> {
                bookData.clear();
                bookData.addAll(filterRequestedFromAvailableBooks(availableBooks, requests));
                books.setValue(bookData);
            }).addOnFailureListener(e -> errorMessage.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS));
        }).addOnFailureListener(e -> errorMessage.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS));
    }

    private List<Book> filterRequestedFromAvailableBooks(List<Book> availableBooks, List<Request> requests) {
        List<String> requestedBookIds = requests
                .stream()
                .map(request -> request.getBookId())
                .collect(Collectors.toList());

        return availableBooks
                .stream()
                .filter(availableBook -> requestedBookIds.indexOf(availableBook.getId()) == -1)
                .collect(Collectors.toList());
    }
}
