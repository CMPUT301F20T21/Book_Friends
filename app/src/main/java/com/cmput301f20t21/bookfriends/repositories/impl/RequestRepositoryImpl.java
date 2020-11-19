package com.cmput301f20t21.bookfriends.repositories.impl;

import android.util.Log;

import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.cmput301f20t21.bookfriends.exceptions.UnexpectedException;
import com.cmput301f20t21.bookfriends.repositories.api.RequestRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RequestRepositoryImpl implements RequestRepository {
    private CollectionReference requestCollection;

    private static final RequestRepository instance = new RequestRepositoryImpl();

    private RequestRepositoryImpl() {
        requestCollection = FirebaseFirestore.getInstance().collection("requests");
    }

    public static RequestRepository getInstance() {
        return instance;
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

    public Task<QuerySnapshot> getBorrowedRequestByUsername(String username) {
        return requestCollection.whereEqualTo("requester", username)
                .whereEqualTo("status", REQUEST_STATUS.BORROWED.toString()).get();
    }

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
     * @param id
     * @return Task Void
     */
    public Task<Void> accept(String id) {
        return requestCollection.document(id).update("status", REQUEST_STATUS.ACCEPTED.toString());
    }

    /**
     * deny a request
     * @param id
     * @return Task Void
     */
    public Task<Void> deny(String id) {
        return requestCollection.document(id).update("status", REQUEST_STATUS.DENIED.toString());
    }

    /**
     * batch deny requests
     * @param ids
     * @return Task Void
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
}
