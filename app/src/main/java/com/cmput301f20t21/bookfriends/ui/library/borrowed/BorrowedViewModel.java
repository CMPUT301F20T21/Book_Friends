/*
 * BorrowedViewModel.java
 * Version: 1.0
 * Date: November 4, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.library.borrowed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.cmput301f20t21.bookfriends.enums.SCAN_ERROR;
import com.cmput301f20t21.bookfriends.repositories.api.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.api.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.api.RequestRepository;
import com.cmput301f20t21.bookfriends.repositories.factories.AuthRepositoryFactory;
import com.cmput301f20t21.bookfriends.repositories.factories.BookRepositoryFactory;
import com.cmput301f20t21.bookfriends.repositories.impl.AuthRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.impl.BookRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.impl.RequestRepositoryImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The ViewModel for BorrowedListFragment
 */
public class BorrowedViewModel extends ViewModel {
    private final RequestRepository requestRepository;
    private final BookRepository bookRepository;
    private final AuthRepository authRepository;

    private final MutableLiveData<List<Book>> books = new MutableLiveData<>(new ArrayList<>());
    private final List<Book> bookData = books.getValue();
    private final MutableLiveData<BOOK_ERROR> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Integer> updatedPosition = new MutableLiveData<>(0);

    public BorrowedViewModel() {
        this(RequestRepositoryImpl.getInstance(), BookRepositoryFactory.getRepository(), AuthRepositoryFactory.getRepository());
    }

    public BorrowedViewModel(RequestRepository requestRepository, BookRepository bookRepository, AuthRepository authRepository) {
        this.requestRepository = requestRepository;
        this.bookRepository = bookRepository;
        this.authRepository = authRepository;

        fetchBooks();
    }

    /**
     * get the list of books from MutableLiveData
     * @return a list of books
     */
    public LiveData<List<Book>> getBooks() {
        return books;
    }

    /**
     * get the latest updated position of the book
     * @return the updated position
     */
    public LiveData<Integer> getUpdatedPosition() {
        return updatedPosition;
    }

    /**
     * get the error message
     * @return a {@link MutableLiveData} that contains {@link BOOK_ERROR} error message
     */
    public LiveData<BOOK_ERROR> getErrorMessage() {
        return errorMessage;
    }

    public Book getBookByIndex(Integer index) {
        return bookData.get(index);
    }

    private void fetchBooks() {
        String username = authRepository.getCurrentUser().getUsername();

        requestRepository.getRequestsByUsernameAndStatus(
                username, Arrays.asList(REQUEST_STATUS.BORROWED, REQUEST_STATUS.RETURNING)
        ).addOnSuccessListener(requests -> {
            List<String> borrowedBookIds = requests
                    .stream()
                    .map(Request::getBookId)
                    .collect(Collectors.toList());

            if (borrowedBookIds.isEmpty()) {
                return;
            }
            bookRepository.batchGetBooks(borrowedBookIds).addOnSuccessListener(borrowedBooks -> {
                bookData.clear();
                bookData.addAll(borrowedBooks);
                books.setValue(bookData);
            }).addOnFailureListener(error -> errorMessage.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS));
        }).addOnFailureListener(e -> errorMessage.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS));
    }
}