/*
 * BorrowedDetailViewModel.java
 * Version: 1.0
 * Date: October 20, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.library.borrowed;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallbackWithMessage;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.cmput301f20t21.bookfriends.enums.SCAN_ERROR;
import com.cmput301f20t21.bookfriends.repositories.api.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.api.RequestRepository;
import com.cmput301f20t21.bookfriends.repositories.factories.BookRepositoryFactory;
import com.cmput301f20t21.bookfriends.repositories.impl.BookRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.impl.RequestRepositoryImpl;
import com.cmput301f20t21.bookfriends.ui.library.add.AddEditActivity;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Arrays;

/**
 * The ViewModel for {@link BorrowedDetailActivity}
 */
public class BorrowedDetailViewModel extends ViewModel {
    private final RequestRepository requestRepository;
    private final BookRepository bookRepository;

    private final MutableLiveData<Request> request = new MutableLiveData<>();
    private final MutableLiveData<SCAN_ERROR> errorMessage = new MutableLiveData<>();

    private ListenerRegistration listenerRegistration;

    public BorrowedDetailViewModel() {
        this(RequestRepositoryImpl.getInstance(), BookRepositoryFactory.getRepository());
    }

    public BorrowedDetailViewModel(RequestRepository requestRepository, BookRepository bookRepository) {
        this.requestRepository = requestRepository;
        this.bookRepository = bookRepository;
    }

    public LiveData<Request> getRequest(Book book) {
        fetchRequest(book.getId());
        return request;
    }

    public MutableLiveData<SCAN_ERROR> getErrorMessage() {
        return errorMessage;
    }

    /**
     * fetch the request from the provided book id
     * @param bookId the book id to fetch the request for
     */
    private void fetchRequest(String bookId) {
        requestRepository
                    .getRequestsByBookIdAndStatus(bookId, Arrays.asList(REQUEST_STATUS.BORROWED, REQUEST_STATUS.RETURNING))
                .addOnSuccessListener(requests -> {
                    request.setValue(requests.get(0));
                    registerSnapshotListener();
                })
                .addOnFailureListener(e -> errorMessage.setValue(SCAN_ERROR.UNEXPECTED));
    }

    /**
     * check if the ISBN scanned from user matches the selected book's ISBN
     * @param currentBook the selected book
     * @param scannedIsbn the ISBN that the user scanned
     */
    public void handleScannedIsbn(Book currentBook, String scannedIsbn, OnSuccessCallbackWithMessage<Book> successCallback) {
        if (currentBook.getIsbn().equals(scannedIsbn)) {
            requestRepository.updateRequestStatus(request.getValue(), REQUEST_STATUS.RETURNING)
                    .addOnSuccessListener(request::setValue)
                    .addOnFailureListener(e -> errorMessage.setValue(SCAN_ERROR.UNEXPECTED));
        } else {
            errorMessage.setValue(SCAN_ERROR.INVALID_ISBN);
        }
    }

    // https://stackoverflow.com/questions/48699032/how-to-set-addsnapshotlistener-and-remove-in-populateviewholder-in-recyclerview
    public void registerSnapshotListener() {
        if (listenerRegistration == null) {
            Request currentRequest = request.getValue();
            if (currentRequest != null) {
                listenerRegistration = requestRepository.getRefById(currentRequest.getId()).addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        Log.d("REQUEST_SNAPSHOT_ERROR", error.getMessage());
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Request requestFromSnapshot = snapshot.toObject(Request.class);
                        if (!currentRequest.equals(requestFromSnapshot)) {
                            request.setValue(requestFromSnapshot);
                        }
                    }
                });
            }
        }
    }

    public void unregisterSnapshotListener() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }


}
