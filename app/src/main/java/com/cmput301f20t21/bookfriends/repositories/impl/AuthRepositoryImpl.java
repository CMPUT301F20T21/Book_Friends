/*
 * AuthRepositoryImpl.java
 * Version: 1.0
 * Date: October 16, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.repositories.impl;

import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.exceptions.InvalidLoginCredentialsException;
import com.cmput301f20t21.bookfriends.repositories.api.AuthRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * implementation of {@link AuthRepository}, contains methods that directly interact
 * with {@link FirebaseAuth}
 */
public class AuthRepositoryImpl implements AuthRepository {
    private final FirebaseAuth mAuth;
    private String username;

    private static final AuthRepository instance = new AuthRepositoryImpl();

    private AuthRepositoryImpl() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static AuthRepository getInstance() {
        return instance;
    }

    /**
     * get the current user logged in
     * @return a {@link User} object
     */
    public User getCurrentUser() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        return new User(
                firebaseUser.getUid(),
                username,
                firebaseUser.getEmail()
        );
    }

    /**
     * create a new user auth with the provided email and password
     * @param email the provided email
     * @param password the provided password
     * @return a {@link AuthResult} task
     */
    public Task<AuthResult> createUserAuth(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    /**
     * handles signing in to the application
     * @param username the provided username
     * @param email the email obtained from the provided username
     * @param password the provided password
     * @return a {@link AuthResult} task
     */
    public Task<AuthResult> signIn(String username, String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> this.username = username).continueWith(authResultTask -> {
                    if (authResultTask.isSuccessful()) {
                        return authResultTask.getResult();
                    } else {
                        throw new InvalidLoginCredentialsException();
                    }
                });
    }

    /**
     * handles signing out from the application
     */
    public void signOut() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getCurrentUser().getUsername()).continueWith(Void -> {
            mAuth.signOut();
            return null;
        });
    }

    /**
     * update the email in firebase auth for the current user
     * @param email the new email to update
     * @return a {@link Void} task
     */
    public Task<Void> updateEmail(String email) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            return user.updateEmail(email);
        }
        return null;
    }
}
