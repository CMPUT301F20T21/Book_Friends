/*
 * OwnedViewModel.java
 * Version: 1.0
 * Date: November 4, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */


package com.cmput301f20t21.bookfriends.ui.library.owned;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.repositories.api.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.api.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.factories.AuthRepositoryFactory;
import com.cmput301f20t21.bookfriends.repositories.factories.BookRepositoryFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The ViewModel for OwnedListFragment
 */
public class OwnedViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final BookRepository bookRepository;

    private final MutableLiveData<List<Book>> books = new MutableLiveData<>(new ArrayList<>());
    private final List<Book> bookData = Objects.requireNonNull(books.getValue());
    private final MediatorLiveData<List<Book>> filteredBooks = new MediatorLiveData<>();
    private final List<Book> filteredBookData = new ArrayList<>();
    private final MutableLiveData<Integer> updatedPosition = new MutableLiveData<>(0);
    private final MutableLiveData<BOOK_ERROR> errorMessageObserver = new MutableLiveData<>();

    public OwnedViewModel() {
        this(AuthRepositoryFactory.getRepository(), BookRepositoryFactory.getRepository());
    }

    public OwnedViewModel(AuthRepository authRepository, BookRepository bookRepository) {
        this.authRepository = authRepository;
        this.bookRepository = bookRepository;

        setSource();
        fetchBooks();
    }

    /**
     * get a list of books
     * @return a list of book
     */
    public LiveData<List<Book>> getBooks() {
        return filteredBooks;
    }

    /**
     * get the updated position
     * @return the updated position
     */
    public LiveData<Integer> getUpdatedPosition() {
        return updatedPosition;
    }

    /**
     * get the error message observer
     * @return the error message observer
     */
    public LiveData<BOOK_ERROR> getErrorMessageObserver() {
        return errorMessageObserver;
    }

    /**
     * get a book object by list index
     * @param index the index of the book to get
     * @return the book at the specified index
     */
    public Book getBookByIndex(Integer index) {
        return filteredBookData.get(index);
    }


    /**
     * add a book to the list
     * @param book the book to add
     */
    public void addBook(Book book) {
        bookData.add(book);
        books.setValue(bookData);
    }

    /**
     * update a book that is in the list
     * @param oldBook the old book to be updated
     * @param updatedBook the new book to update
     */
    public void updateBook(Book oldBook, Book updatedBook) {
        int indexToUpdate = bookData.indexOf(oldBook);
        if (indexToUpdate != -1) {
            bookData.set(indexToUpdate, updatedBook);
            books.setValue(bookData);
        } else {
            errorMessageObserver.setValue(BOOK_ERROR.FAIL_TO_EDIT_BOOK);
        }
    }

    /**
     * delete a book locally and from firestore
     * @param book the book to delete
     */
    public void deleteBook(Book book) {
        bookRepository.delete(book.getId())
                .addOnSuccessListener(
                        res -> {
                            if (book.getImageUrl() != null) {
                                bookRepository.deleteImage(book.getImageUrl());
                            }
                            bookData.remove(book);
                            books.setValue(bookData);
                        })
                .addOnFailureListener(e -> errorMessageObserver.setValue(BOOK_ERROR.FAIL_TO_DELETE_BOOK));
    }

    public void filterBooks (
            boolean includeAvailable, boolean includeRequested,
            boolean includeAccepted, boolean includeBorrowed
    ) {
        filteredBookData.clear();
        for (Book book : bookData) {
            BOOK_STATUS status = book.getStatus();
            if (
                    status == BOOK_STATUS.AVAILABLE && includeAvailable ||
                    status == BOOK_STATUS.REQUESTED && includeRequested ||
                    status == BOOK_STATUS.ACCEPTED && includeAccepted ||
                    status == BOOK_STATUS.BORROWED && includeBorrowed
            ) {
                filteredBookData.add(book);
            }
        }
        filteredBooks.setValue(filteredBookData);
    }

    /**
     * setup the source so that filteredBooks are listening to books data change
     */
    private void setSource() {
        filteredBooks.setValue(filteredBookData);
        filteredBooks.addSource(books, bookData -> {
            filteredBookData.clear();
            filteredBookData.addAll(bookData);
            filteredBooks.setValue(filteredBookData);
        });
    }

    /**
     * fetch a list of book that belongs to current user
     */
    protected void fetchBooks() {
        String currentUsername = authRepository.getCurrentUser().getUsername();
        bookRepository.getBooksOfOwnerId(currentUsername)
                .addOnSuccessListener(
                        result -> {
                            bookData.clear();
                            bookData.addAll(result);
                            books.setValue(bookData);
                        })
                .addOnFailureListener(e -> errorMessageObserver.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS));
    }

}