package com.cmput301f20t21.bookfriends.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.entities.UserDocument;
import com.cmput301f20t21.bookfriends.exceptions.UnexpectedException;
import com.cmput301f20t21.bookfriends.exceptions.UsernameNotExistException;
import com.cmput301f20t21.bookfriends.repositories.api.IUserRepository;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class UserRepository implements IUserRepository {
    private CollectionReference userCollection;

    private static final IUserRepository instance = new UserRepository();

    private UserRepository() {
        userCollection = FirebaseFirestore.getInstance().collection("users");
    }

    public static IUserRepository getInstance() {
        return instance;
    }

    public Task<Void> add(String uid, String username, String email) {
        HashMap<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("email", email);
        return userCollection.document(uid).set(data);
    }

    public Task<UserDocument> getByUsername(String username) {
        return userCollection.whereEqualTo("username", username).get().continueWith(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().isEmpty()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    UserDocument userDocument = document.toObject(UserDocument.class);
                    return userDocument;
                } else {
                    throw new UsernameNotExistException();
                }
            } else {
                throw new UnexpectedException();
            }
        });
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
