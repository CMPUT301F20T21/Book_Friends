package com.cmput301f20t21.bookfriends.repositories.impl;

import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.exceptions.InvalidLoginCredentialsException;
import com.cmput301f20t21.bookfriends.repositories.api.AuthRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepositoryImpl implements AuthRepository {
    private FirebaseAuth mAuth;
    private String username;

    private static final AuthRepository instance = new AuthRepositoryImpl();

    private AuthRepositoryImpl() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static AuthRepository getInstance() {
        return instance;
    }

    public User getCurrentUser() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        return new User(
                firebaseUser.getUid(),
                username,
                firebaseUser.getEmail()
        );
    }

    public Task<AuthResult> createUserAuth(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

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

    public void signOut() {
        mAuth.signOut();
    }

    public Task<Void> updateEmail(String email) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            return user.updateEmail(email);
        }
        return null;
    }
}
