/*
 * BrowseDetailViewModel.java
 * Version: 1.0
 * Date: October 26, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.browse;

import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.callbacks.OnFailCallback;
import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallback;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.repositories.api.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.factories.AuthRepositoryFactory;
import com.cmput301f20t21.bookfriends.repositories.factories.BookRepositoryFactory;
import com.cmput301f20t21.bookfriends.repositories.impl.AuthRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.impl.BookRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.impl.RequestRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.api.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.api.RequestRepository;
import com.cmput301f20t21.bookfriends.repositories.impl.AuthRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.impl.RequestRepositoryImpl;
import com.cmput301f20t21.bookfriends.utils.NotificationSender;

/**
 * ViewModel for {@link BrowseDetailActivity}
 */
public class BrowseDetailViewModel extends ViewModel {
    private final BookRepository bookRepository;
    private final RequestRepository requestRepository;
    private final String currentUsername;

    public BrowseDetailViewModel() {
        this(AuthRepositoryFactory.getRepository(), RequestRepositoryImpl.getInstance(), BookRepositoryFactory.getRepository());
    }

    public BrowseDetailViewModel(
            AuthRepository authRepository,
            RequestRepository requestRepository,
            BookRepository bookRepository
    ) {
        this.bookRepository = bookRepository;
        this.requestRepository = requestRepository;
        currentUsername = authRepository.getCurrentUser().getUsername();
    }

    /**
     * Create a {@link Request} object to firestore to indicate user have successfully requested the book
     * @param book the book to request
     * @param successCallback async callback called when request is successful
     * @param failCallback async callback called when the request fails
     */
    public void sendRequest(Book book, OnSuccessCallback successCallback, OnFailCallback failCallback) {
        requestRepository
                .sendRequest(currentUsername, book.getId())
                .addOnSuccessListener(requestId -> {
                    if (book.getStatus() == BOOK_STATUS.AVAILABLE) {
                        bookRepository.updateBookStatus(book, BOOK_STATUS.REQUESTED)
                                .addOnSuccessListener(aVoid -> {
                                    NotificationSender.getInstance().request(book, currentUsername,
                                            res -> successCallback.run(),
                                            err -> {
                                                err.printStackTrace();
                                                successCallback.run(); // success anyways as we don't want notifications break user flow
                                            });
                                })
                                .addOnFailureListener(e -> failCallback.run());
                    } else {
                        NotificationSender.getInstance().request(book, currentUsername,
                                res -> successCallback.run(),
                                err -> {
                                    err.printStackTrace();
                                    successCallback.run(); // success anyways as we don't want notifications break user flow
                                });
                    }
                })
                .addOnFailureListener(e -> failCallback.run());
    }
}
