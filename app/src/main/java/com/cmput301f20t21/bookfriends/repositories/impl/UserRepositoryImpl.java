/*
 * UserRepositoryImpl.java
 * Version: 1.0
 * Date: October 16, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.repositories.impl;

import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.exceptions.UnexpectedException;
import com.cmput301f20t21.bookfriends.exceptions.UserNotExistException;
import com.cmput301f20t21.bookfriends.exceptions.UsernameNotExistException;
import com.cmput301f20t21.bookfriends.repositories.api.RequestRepository;
import com.cmput301f20t21.bookfriends.repositories.api.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * implementation of {@link UserRepository}, contains methods that directly interact
 * with Firebase Firestore
 */
public class UserRepositoryImpl implements UserRepository {
    private final CollectionReference userCollection;

    private static final UserRepository instance = new UserRepositoryImpl();

    private UserRepositoryImpl() {
        userCollection = FirebaseFirestore.getInstance().collection("users");
    }

    public static UserRepository getInstance() {
        return instance;
    }

    /**
     * add a user to the collection
     * @param uid the user id created by firebase auth
     * @param username the username provided by the user
     * @param email the user provided email
     * @return {@link Task} returning {@link Void}
     */
    public Task<Void> add(String uid, String username, String email) {
        HashMap<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("email", email);
        return userCollection.document(uid).set(data);
    }

    /**
     * get a {@link User} entity object by username
     * @param username the username of the user
     * @return {@link Task} returning a {@link User} entity object
     */
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

    /**
     * get a {@link User} entity object by uid
     * @param uid the user id of the user
     * @return {@link Task} returning a {@link User} entity object
     */
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

    /**
     * get a {@link List} of {@link User} entity objects for users start with the specific keyword
     * @param username the keyword to search for
     * @return {@link Task} returning a a {@link List} of {@link User} entity object
     */
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

    /**
     * updates user email in firestore
     * @param user the {@link User} to update
     * @param email the new email to update
     * @return {@link Task} returning {@link Void}
     */
    public Task<Void> updateUserEmail(User user, String email) {
        if (user != null) {
            String userId = user.getUid();
            return userCollection.document(userId).update("email", email);
        }
        return null;
    }
}
