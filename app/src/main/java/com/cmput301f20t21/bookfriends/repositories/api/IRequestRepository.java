package com.cmput301f20t21.bookfriends.repositories.api;

import com.cmput301f20t21.bookfriends.entities.Request;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public interface IRequestRepository {
    Task<QuerySnapshot> getByBookId(String bookId);
    Task<QuerySnapshot> getBorrowedRequestByUsername(String username);
    Task<List<Request>> getAllRequestsByUsername(String username);
    Task<DocumentReference> add(String bookId, String requesterId);
    Task<Void> accept(String id);
    Task<Void> deny(String id);
    Task<Void> batchDeny(List<String> ids);
    Request getRequestFromDocument(DocumentSnapshot documentSnapshot);
    String getRequesterFromDocument(DocumentSnapshot documentSnapshot);
    Task<String> sendRequest(String requester, String bookId);
}
