package com.cmput301f20t21.bookfriends.repositories;

import com.cmput301f20t21.bookfriends.entities.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class UserRepository {
    private CollectionReference userCollection;

    private static final UserRepository instance = new UserRepository();

    private UserRepository() {
        userCollection = FirebaseFirestore.getInstance().collection("users");
    }

    public static UserRepository getInstance() {
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

    public Task<Void> updateUserEmail(String email){
        User firebaseUser = AuthRepository.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            return userCollection.document(userId).update("email", email);
        }
        return null;
    }
}
