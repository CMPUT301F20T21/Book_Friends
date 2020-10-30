package com.cmput301f20t21.bookfriends.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmput301f20t21.bookfriends.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthService {
    private FirebaseAuth mAuth;
    private String username;

    private static final AuthService instance = new AuthService();

    private AuthService() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static AuthService getInstance() {
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

    public void updateEmail(String username, String TAG){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.updateEmail(username)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User email address updated.");
                            }
                        }
                    });

        }

    }
}
