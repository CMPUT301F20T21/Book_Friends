package com.cmput301f20t21.bookfriends.controller;

import androidx.annotation.NonNull;

import com.cmput301f20t21.bookfriends.services.AuthService;
import com.cmput301f20t21.bookfriends.services.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.QuerySnapshot;

public class SignUpController {
    private AuthService authService;
    private UserService userService;
    private ISignUpListener listener;

    public interface ISignUpListener {
        void onSignUpSuccess();
        void onSignUpFail();
    }

    public SignUpController(ISignUpListener listener) {
        authService = AuthService.getInstance();
        userService = UserService.getInstance();
        this.listener = listener;
    }

    public void handleSignUp(final String username, final String email, final String password) {
        // check if the email has been registered by another user
        userService.getUserByEmail(email).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // if email has not been registered
                    if (task.getResult().isEmpty()) {
                        userService.add(username, email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // task only succeed if username is unique
                                if (task.isSuccessful()) {
                                    authService.createUserAuth(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            // handle sign up result
                                            if (task.isSuccessful()) {
                                                listener.onSignUpSuccess();
                                            } else {
                                                listener.onSignUpFail();
                                            }
                                        }
                                    });
                                } else {
                                    // username already exists
                                    // TODO: handle failure
                                }
                            }
                        });
                    }
                } else {
                    // email already exists
                    // TODO: handle failure
                }
            }
        });
    }
}
