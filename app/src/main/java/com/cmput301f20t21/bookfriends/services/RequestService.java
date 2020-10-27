package com.cmput301f20t21.bookfriends.services;

import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RequestService {
    private CollectionReference requestCollection;

    private static final RequestService instance = new RequestService();

    private RequestService() {
        requestCollection = FirebaseFirestore.getInstance().collection("requests");
    }

    public static RequestService getInstance() {
        return instance;
    }

    /**
     * get request by bookId
     * @param bookId
     * @return Task QuerySnapshot for request
     */
    public Task<QuerySnapshot> getByBookId(String bookId) {
        return requestCollection.whereEqualTo("bookId", bookId).get();
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
}
