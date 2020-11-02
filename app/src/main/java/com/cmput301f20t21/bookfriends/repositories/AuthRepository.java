package com.cmput301f20t21.bookfriends.repositories;

import com.cmput301f20t21.bookfriends.entities.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {
    private FirebaseAuth mAuth;
    private String username;

    private static final AuthRepository instance = new AuthRepository();

    private AuthRepository() {
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
                firebaseUser.getDisplayName(),
                firebaseUser.getEmail(),
                firebaseUser.getPhoneNumber()
        );
    }

    public Task<AuthResult> createUserAuth(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> signIn(String username, String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> this.username = username);
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
