package com.cmput301f20t21.bookfriends.services;

import com.cmput301f20t21.bookfriends.entities.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserService {
    private FirebaseAuth mAuth;
    private static final UserService instance = new UserService();

    private UserService() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static UserService getInstance() {
        return instance;
    }

    public User getCurrentUser() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        return new User(
                firebaseUser.getUid(),
                firebaseUser.getDisplayName(),
                firebaseUser.getEmail(),
                firebaseUser.getPhoneNumber()
        );
    }

    public Task<AuthResult> createUser(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> signIn(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public void signOut() {
        mAuth.signOut();
    }
}
