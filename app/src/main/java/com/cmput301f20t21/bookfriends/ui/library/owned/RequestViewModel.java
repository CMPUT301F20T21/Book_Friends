/*
 * RequestViewModel.java
 * Version: 1.0
 * Date: November 4, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.library.owned;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.repositories.api.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.factories.BookRepositoryFactory;
import com.cmput301f20t21.bookfriends.repositories.impl.BookRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.impl.RequestRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.api.RequestRepository;
import com.google.android.gms.maps.model.LatLng;
import com.cmput301f20t21.bookfriends.utils.NotificationSender;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The ViewModel for RequestActivity
 */
public class RequestViewModel extends ViewModel {
    private final MutableLiveData<Book> book = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Request>> requests = new MutableLiveData<>(new ArrayList<>());
    private final ArrayList<Request> requestsData = requests.getValue();

    private final RequestRepository requestRepository;
    private final BookRepository bookRepository;

    public RequestViewModel() {
        this(RequestRepositoryImpl.getInstance(), BookRepositoryFactory.getRepository());
    }

    public RequestViewModel(RequestRepository requestRepository, BookRepository bookRepository) {
        this.requestRepository = requestRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Function to get the book information from FireStore
     * @param bookId we will query the book information based on the bookID
     */
    private void fetchBook(String bookId) {
        bookRepository.getBookById(bookId).addOnSuccessListener(b -> {
            book.setValue(b);
        });
    }

    /**
     * get all the requesters of a book based on its bookId
     * @param bookId the book id to query for requesters
     */
    private void fetchRequests(String bookId) {
        requestRepository.getOpenedRequestByBookId(bookId).addOnSuccessListener(requesterDocumentsSnapShots -> {
           List<DocumentSnapshot> documents = requesterDocumentsSnapShots.getDocuments();
           requestsData.clear(); // we are sure to always refresh the requests list
           requestsData.addAll(IntStream.range(0, documents.size()).mapToObj(i -> {
               DocumentSnapshot document = documents.get(i);
               return requestRepository.getRequestFromDocument(document);
           }).collect(Collectors.toList()));
           requests.setValue(requestsData);
        });
    }


    /**
     * function to notify the content displayed on device when the data is changed
     * @param bookId we get book information by book ID then pass the content to display
     * @return a list of books
     */
    public LiveData<Book> getBook(String bookId) {
        fetchBook(bookId);
        return this.book;
    }

    /**
     * live data of requesters retrieving from FireStore
     * @param bookId the book id to query for requesters
     * @return a list of requests
     */
    public LiveData<ArrayList<Request>> getRequests(String bookId) {
        fetchRequests(bookId);
        return this.requests;
    }

    /**
     * When the requester is denied, remove his/her request.
     * update the status of this requester to DENIED
     * @param position the position of the request to remove
     */
    public void removeRequest(Integer position) {
        Request request = requestsData.get(position);
        requestRepository.deny(request.getId()).addOnSuccessListener(aVoid -> {
            requestsData.remove(request);
            requests.setValue(requestsData);
            if (requestsData.isEmpty()) {
                Book bookData = book.getValue();
                if (bookData != null) {
                    bookRepository.updateBookStatus(bookData, BOOK_STATUS.AVAILABLE)
                            .addOnSuccessListener(updatedBook -> book.setValue(updatedBook));
                }
            }
        });
    }

    /**
     * When the requester is accepted, update the status of this requester to ACCEPTED
     * @param position the position of the request to accept
     */
    public void acceptRequest(Integer position, LatLng meetingLocation) {
        Request request = requestsData.get(position);
        GeoPoint geoPoint = new GeoPoint(meetingLocation.latitude, meetingLocation.longitude);
        requestRepository.accept(request.getId()).addOnSuccessListener(aVoid -> {
            requestsData.remove(request);
            // deny all other requests
            List<String> ids = new ArrayList<>();
            for (int i = 0; i < requestsData.size(); i++) {
                ids.add(requestsData.get(i).getId());
            }

            NotificationSender.getInstance().accept(request, res -> {
                Log.e("bfriends", "accept call response: " + res);
            }, err -> {
                err.printStackTrace();
                Log.e("bfriends", "accept call failed: " + err.getLocalizedMessage());
            });
            // clear the requests array anyways
            requestRepository.batchDeny(ids).addOnSuccessListener(aVoid1 -> {
                requestsData.clear();
                requests.setValue(requestsData);
                Book bookData = book.getValue();
                if (bookData != null) {
                    bookRepository.updateBookStatus(bookData, BOOK_STATUS.ACCEPTED)
                            .addOnSuccessListener(updatedBook -> book.setValue(updatedBook));
                }
            }).addOnFailureListener(err -> {
                err.printStackTrace();
                requestsData.clear();
                requests.setValue(requestsData);
            });

            // write location to firebase
            requestRepository.addMeetingLocation(request.getId(), geoPoint).addOnSuccessListener(aVoid2 ->{
                //TODO: Not sure what to code here as the location is updated in Firebase
            });
        });
    }
}
