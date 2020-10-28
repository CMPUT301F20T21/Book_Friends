package com.cmput301f20t21.bookfriends.ui.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.callbacks.OnFailCallback;
import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallback;
import com.cmput301f20t21.bookfriends.services.AuthService;
import com.cmput301f20t21.bookfriends.services.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginViewModel extends ViewModel {
    private final AuthService authService = AuthService.getInstance();
    private final UserService userService = UserService.getInstance();

    public void handleLogIn(final String username, final String password, final OnSuccessCallback successCallback, final OnFailCallback failureCallback) {
        userService.getByUsername(username).addOnCompleteListener(usernameTask -> {
            if (usernameTask.isSuccessful()) {
                if (!usernameTask.getResult().isEmpty()) {
                    String email = usernameTask.getResult().getDocuments().get(0).get("email").toString();
                    authService.signIn(username, email, password).addOnCompleteListener(authTask -> {
                        if (authTask.isSuccessful()) {
                            successCallback.run();
                        } else {
                            failureCallback.run();
                        }
                    });
                } else {
                    // empty email, should never happen
                    // TODO: handle error
                    failureCallback.run();
                }
            } else {
                // fail to get username
                // TODO: handle error
                failureCallback.run();
            }
        });
    }
}
