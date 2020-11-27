/*
 * AcceptedOwnedDetailActivity.java
 * Version: 1.0
 * Date: November 20, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.library.owned;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.cmput301f20t21.bookfriends.enums.SCAN_ERROR;
import com.cmput301f20t21.bookfriends.repositories.api.RequestRepository;
import com.cmput301f20t21.bookfriends.repositories.impl.RequestRepositoryImpl;

import java.util.Arrays;

/**
 * ViewModel for {@link AcceptedOwnedDetailActivity}, handles scanning to hand over a book
 */
public class AcceptedOwnedDetailViewModel extends ViewModel {
    private final RequestRepository requestRepository;

    private final MutableLiveData<Request> request = new MutableLiveData<>();
    private final MutableLiveData<SCAN_ERROR> errorMessage = new MutableLiveData<>();

    public AcceptedOwnedDetailViewModel() {
        this(RequestRepositoryImpl.getInstance());
    }

    public AcceptedOwnedDetailViewModel(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    /**
     * get the request on the selected book
     * @param book the selected book
     * @return a {@link Request} entity object
     */
    public LiveData<Request> getRequest(Book book) {
        fetchRequest(book.getId());
        return request;
    }

    /**
     * get the error message
     * @return a {@link MutableLiveData} that contains {@link SCAN_ERROR} error message
     */
    public MutableLiveData<SCAN_ERROR> getErrorMessage() {
        return errorMessage;
    }

    /**
     * fetch the request from the provided book id
     * @param bookId the book id to fetch the request for
     */
    private void fetchRequest(String bookId) {
        requestRepository
                .getRequestsByBookIdAndStatus(bookId, Arrays.asList(REQUEST_STATUS.ACCEPTED, REQUEST_STATUS.HANDING))
                .addOnSuccessListener(requests -> request.setValue(requests.get(0)))
                .addOnFailureListener(e -> errorMessage.setValue(SCAN_ERROR.UNEXPECTED));
    }

    /**
     * check if the ISBN scanned from user matches the selected book's ISBN
     * @param currentIsbn the selected book's ISBN
     * @param scannedIsbn the ISBN that the user scanned
     */
    public void handleScannedIsbn(String currentIsbn, String scannedIsbn) {
        if (currentIsbn.equals(scannedIsbn)) {
            requestRepository.updateRequestStatus(request.getValue(), REQUEST_STATUS.HANDING)
                    .addOnSuccessListener(request::setValue)
                    .addOnFailureListener(e -> errorMessage.setValue(SCAN_ERROR.UNEXPECTED));
        } else {
            errorMessage.setValue(SCAN_ERROR.INVALID_ISBN);
        }
    }
}
