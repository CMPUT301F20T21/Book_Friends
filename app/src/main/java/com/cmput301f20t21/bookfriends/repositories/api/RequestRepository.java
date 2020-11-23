package com.cmput301f20t21.bookfriends.repositories.api;

import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public interface RequestRepository {
    DocumentReference getRefById(String requestId);
    Task<QuerySnapshot> getOpenedRequestByBookId(String bookId);
    Task<List<Request>> getRequestsByBookIdAndStatus(String bookId, List<REQUEST_STATUS> statusList);
    Task<List<Request>> getRequestsByUsernameAndStatus(String username, List<REQUEST_STATUS> statusList);
    Task<DocumentReference> add(String bookId, String requesterId);
    Task<Void> accept(String id);
    Task<Void> deny(String id);
    Task<Void> batchDeny(List<String> ids);
    Request getRequestFromDocument(DocumentSnapshot documentSnapshot);
    String getRequesterFromDocument(DocumentSnapshot documentSnapshot);
    Task<String> sendRequest(String requester, String bookId);
    Task<Request> updateRequestStatus(Request request, REQUEST_STATUS newStatus);
    Task<Void> addMeetingLocation (String id, GeoPoint geoPoint);
}
