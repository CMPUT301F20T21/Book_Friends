package com.cmput301f20t21.bookfriends.services;

import android.util.Log;

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

    public Task<DocumentSnapshot> getByUid(String uid) {
        return userCollection.document(uid).get();
    }

    public Task<QuerySnapshot> getByUsernameStartWith(String username) {
        if (username.length() == 0) {
            // because username is alphanumeric, according to ascii table, @ is less than 'a' and 0
            return userCollection.whereEqualTo("username", "@").get();
        }
        // return a list if 
        return userCollection
                .whereGreaterThanOrEqualTo("username", username)
                .whereLessThan("username", username.concat("~")) // ascii ~ is way larger than 'z'
                .limit(10).get();
    }
}
