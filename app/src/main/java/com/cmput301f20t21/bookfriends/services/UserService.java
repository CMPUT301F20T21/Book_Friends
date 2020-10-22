package com.cmput301f20t21.bookfriends.services;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class UserService {
    private CollectionReference userCollection;

    private static final UserService instance = new UserService();

    private UserService() {
        userCollection = FirebaseFirestore.getInstance().collection("users");
    }

    public static UserService getInstance() {
        return instance;
    }

    public Task<Void> add(String uid, String username, String email) {
        HashMap<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("email", email);
        return userCollection.document(uid).set(data);
    }

    public Task<QuerySnapshot> getByUsername(String username) {
        return userCollection.whereEqualTo("username", username).get();
    }
}
