package com.cmput301f20t21.bookfriends.ui.login;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.enums.SIGNUP_ERROR;
import com.cmput301f20t21.bookfriends.services.AuthService;
import com.cmput301f20t21.bookfriends.services.UserService;

import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountViewModel extends ViewModel {
    /**
     * interface for callback lambda function
     * will/should be called by request-related handlers when async request succeeded
     */
    public interface OnSuccessCallback {
        void run();
    }
    /** called by handlers when async request failed */
    public interface OnFailCallback {
        void run(SIGNUP_ERROR error);
    }

    private final AuthService authService = AuthService.getInstance();
    private final UserService userService = UserService.getInstance();
    private final String TAG = "SIGNUP_ERROR";

    public void handleSignUp(final String username, final String email, final String password,
                             OnSuccessCallback successCallback, OnFailCallback failCallback) {
        // check if username is registered
        userService.getByUsername(username).addOnCompleteListener(usernameTask -> {
            if (usernameTask.isSuccessful()) {
                if (usernameTask.getResult().isEmpty()) {
                    // create authentication
                    authService.createUserAuth(email, password).addOnCompleteListener(authTask -> {
                        if (authTask.isSuccessful()) {
                            FirebaseUser user = authTask.getResult().getUser();
                            // create username after create auth successful
                            userService.add(user.getUid(), username, email).addOnCompleteListener(addUserTask -> {
                                if (addUserTask.isSuccessful()) {
                                    successCallback.run();
                                } else {
                                    // Clean up auth if we cannot create username
                                    // This should never happen
                                    user.delete().addOnCompleteListener(deleteAuthTask -> {
                                        failCallback.run(SIGNUP_ERROR.UNEXPECTED);
                                    });
                                }
                            });
                        } else {
                            try {
                                throw authTask.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                failCallback.run(SIGNUP_ERROR.EMAIL_EXISTS);
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                failCallback.run(SIGNUP_ERROR.UNEXPECTED);
                            }
                        }
                    });
                } else {
                    failCallback.run(SIGNUP_ERROR.USERNAME_EXISTS);
                }
            } else {
                failCallback.run(SIGNUP_ERROR.UNEXPECTED);
            }
        });
    }
}
