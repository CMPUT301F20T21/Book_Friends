package com.cmput301f20t21.bookfriends.controller;

import android.os.Debug;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cmput301f20t21.bookfriends.services.AuthService;
import com.cmput301f20t21.bookfriends.services.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LogInController {
    private AuthService authService;
    private UserService userService;
    private ILogInListener listener;

    public interface ILogInListener {
        void onLogInSuccess();
        void onLogInFail();
    }

    public LogInController(ILogInListener listener) {
        authService = AuthService.getInstance();
        userService = UserService.getInstance();
        this.listener = listener;
    }

    public void handleLogIn(final String username, final String password) {
        userService.getEmailByUsername(username).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String email = task.getResult().get("email").toString();
                    if (!email.isEmpty()) {
                        authService.signIn(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    listener.onLogInSuccess();
                                } else {
                                    listener.onLogInFail();
                                }
                            }
                        });
                    } else {
                        // empty email, should never happen
                        // TODO: handle error
                        listener.onLogInFail();
                    }
                } else {
                    // fail to get username
                    // TODO: handle error
                    listener.onLogInFail();
                }
            }
        });
    }
}
