/*
 * RequestRepositoryImpl.java
 * Version: 1.0
 * Date: October 16, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.repositories.impl;

import android.util.Log;

import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.cmput301f20t21.bookfriends.exceptions.UnexpectedException;
import com.cmput301f20t21.bookfriends.repositories.api.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.api.RequestRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * implementation of {@link RequestRepository}, contains methods that directly interact
 * with Firebase Firestore
 */
public class RequestRepositoryImpl implements RequestRepository {
    private final CollectionReference requestCollection;

    private static final RequestRepository instance = new RequestRepositoryImpl();

    private RequestRepositoryImpl() {
        requestCollection = FirebaseFirestore.getInstance().collection("requests");
    }

    public static RequestRepository getInstance() {
        return instance;
    }

    public DocumentReference getRefById(String requestId) {
        return requestCollection.document(requestId);
    }

    /**
     * get request by bookId, only for books that are opened
     * @param bookId
     * @return Task QuerySnapshot for request
     */
    public Task<QuerySnapshot> getOpenedRequestByBookId(String bookId) {
        return requestCollection
                .whereEqualTo("bookId", bookId)
                .whereEqualTo("status", REQUEST_STATUS.OPENED.toString())
                .get();
    }

    /**
     * get a list of requests by bookId and request statuses
     * @param bookId the book's id
     * @param statusList a list of {@link REQUEST_STATUS}
     * @return {@link Task} returning a {@link List} of {@link Request}
     */
    public Task<List<Request>> getRequestsByBookIdAndStatus(String bookId, List<REQUEST_STATUS> statusList) {
        return requestCollection
                .whereEqualTo("bookId", bookId)
                .whereIn("status", statusList)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        return task
                                .getResult()
                                .getDocuments()
                                .stream()
                                .map(doc -> doc.toObject(Request.class))
                                .collect(Collectors.toList());
                    }
                    throw new UnexpectedException();
                });
    }

    /**
     * get a list of requests by username and request statuses
     * @param username the requester
     * @param statusList a list of {@link REQUEST_STATUS}
     * @return {@link Task} returning a {@link List} of {@link Request}
     */
    public Task<List<Request>> getRequestsByUsernameAndStatus(String username, List<REQUEST_STATUS> statusList) {
        return requestCollection.whereEqualTo("requester", username)
                .whereIn("status", statusList).get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        List<Request> requests = new ArrayList<Request>();
                        try {
                            requests = task.getResult().getDocuments()
                                    .stream()
                                    .map(doc -> doc.toObject(Request.class))
                                    .collect(Collectors.toList());
                        } catch (Exception e) {
                            Log.d("REQUEST ERROR:", e.getMessage());
                        }

                        return requests;
                    }
                    throw new UnexpectedException();
                });
    }


    /**
     * add request by bookId and requesterId
     * @param bookId, requesterId
     * @return Task DocumentReference for request
     */
    public Task<DocumentReference> add(String bookId, String requesterId) {
        HashMap<String, String> data = new HashMap<>();
        data.put("bookId", bookId);
        data.put("requesterId", requesterId);
        data.put("status", REQUEST_STATUS.OPENED.toString());
        return requestCollection.add(data);
    }

    /**
     * accept a request
     * @param id the request id
     * @return {@link Task} returning {@link Void}
     */
    public Task<Void> accept(String id) {
        return requestCollection.document(id).update("status", REQUEST_STATUS.ACCEPTED.toString());
    }

    /**
     * deny a request
     * @param id the request id
     * @return {@link Task} returning {@link Void}
     */
    public Task<Void> deny(String id) {
        return requestCollection.document(id).update("status", REQUEST_STATUS.DENIED.toString());
    }

    /**
     * batch deny requests
     * @param ids a {@link List} of request ids
     * @return {@link Task} returning {@link Void}
     */
    public Task<Void> batchDeny(List<String> ids) {
        WriteBatch batch = FirebaseFirestore.getInstance().batch();
        for (String id : ids) {
            DocumentReference request = requestCollection.document(id);
            batch.update(request, "status", REQUEST_STATUS.DENIED.toString());
        }
        return batch.commit();
    }

    public Request getRequestFromDocument(DocumentSnapshot documentSnapshot) {
        String requestId = documentSnapshot.getId();
        String requester = (String) documentSnapshot.get("requester");
        String bookId = (String) documentSnapshot.get("bookId");
        String status = (String) documentSnapshot.get("status");
        return new Request(requestId, requester, bookId, REQUEST_STATUS.valueOf(status));
    }

    public String getRequesterFromDocument(DocumentSnapshot documentSnapshot) {
        return (String) documentSnapshot.get("requester");
    }

    /**
     * Send a request and record it in firestore
     * @param requester the requester username
     * @param bookId the id of the book to request
     * @return {@link Task} contains the request id {@link String}
     */
    public Task<String> sendRequest(String requester, String bookId) {
        HashMap<String, String> data = new HashMap<>();
        data.put("requester", requester);
        data.put("bookId", bookId);
        data.put("status", REQUEST_STATUS.OPENED.toString());
        return requestCollection.add(data).continueWith(task -> {
            if (task.isSuccessful()) {
                return task.getResult().getId();
            }
            throw new UnexpectedException();
        });
    }

    /**
     * updates the status of a request
     * @param request the request to update
     * @param newStatus the new status to update to
     * @return {@link Task} returning the updated {@link Request}
     */
    public Task<Request> updateRequestStatus(Request request, REQUEST_STATUS newStatus) {
        return requestCollection
                .document(request.getId())
                .update("status", newStatus.toString())
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return new Request(request.getId(), request.getRequester(), request.getBookId(), newStatus);
                    }
                    throw new UnexpectedException();
                });
    }

    /**
     * add a meeting location detail to the request
     * @param id the request id
     * @param geoPoint the {@link GeoPoint} object contains the meeting location
     * @return {@link Task} returning {@link Void}
     */
    public Task<Void> addMeetingLocation(String id, GeoPoint geoPoint) {
            return requestCollection.document(id).update("meetingLocation", geoPoint);
    }
}
