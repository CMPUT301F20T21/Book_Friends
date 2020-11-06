package com.cmput301f20t21.bookfriends.repositories;

import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.exceptions.UnexpectedException;
import com.cmput301f20t21.bookfriends.exceptions.UserNotExistException;
import com.cmput301f20t21.bookfriends.exceptions.UsernameNotExistException;
import com.cmput301f20t21.bookfriends.repositories.api.IUserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    public Task<User> getByUsername(String username) {
        return userCollection.whereEqualTo("username", username).get().continueWith(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().isEmpty()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    try {
                        return document.toObject(User.class);
                    } catch (Exception e) {
                        throw new UnexpectedException();
                    }
                } else {
                    throw new UsernameNotExistException();
                }
            } else {
                throw new UnexpectedException();
            }
        });
    }

    public Task<User> getByUid(String uid) {
        return userCollection.document(uid)
                .get()
                .continueWith(task -> {
                    if (task.getResult() != null) {
                        return task.getResult().toObject(User.class);
                    }
                    throw new UserNotExistException();
                });
    }

    public Task<List<User>> getByUsernameStartWith(String username) {
        final Query userQuery;
        if (username.length() == 0) {
            // because username is alphanumeric, according to ascii table, @ is less than 'a' and 0
            userQuery = userCollection.whereEqualTo("username", "@");
        } else {
            userQuery = userCollection
                    .whereGreaterThanOrEqualTo("username", username)
                    .whereLessThan("username", username.concat("~")) // ascii ~ is way larger than 'z'
                    .limit(10);
        }
        return userQuery
                .get()
                .continueWith(task -> {
                    if (!task.getResult().isEmpty()) {
                        return (ArrayList<User>) task.getResult().getDocuments()
                                .stream()
                                .map(doc -> doc.toObject(User.class))
                                .collect(Collectors.toList());
                    }
                    return new ArrayList<>();
                });
    }

    public Task<Void> updateUserEmail(String email) {
        User firebaseUser = AuthRepository.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            return userCollection.document(userId).update("email", email);
        }
        return null;
    }
}
