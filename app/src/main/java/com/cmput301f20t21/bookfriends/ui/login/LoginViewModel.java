/*
 * LoginViewModel.java
 * Version: 1.0
 * Date: October 18, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.login;

import android.widget.EditText;

import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.callbacks.OnFailCallbackWithMessage;
import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallback;
import com.cmput301f20t21.bookfriends.enums.LOGIN_ERROR;
import com.cmput301f20t21.bookfriends.exceptions.InvalidLoginCredentialsException;
import com.cmput301f20t21.bookfriends.exceptions.UsernameNotExistException;
import com.cmput301f20t21.bookfriends.repositories.impl.AuthRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.impl.UserRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.api.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.api.UserRepository;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * A view model for the LoginActivity, handles validation and authentication
 */
public class LoginViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    // production
    public LoginViewModel() {
        this(AuthRepositoryImpl.getInstance(), UserRepositoryImpl.getInstance());
    }

    // test - allow us to inject repository dependency in test
    public LoginViewModel(AuthRepository authRepository, UserRepository userRepository) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
    }

    /**
     * Check whether the provided layout's text field is empty
     *
     * @param layout the layout to validate
     * @return true if the text field is empty
     */
    public boolean isEmpty(TextInputLayout layout) {
        EditText inputField = Objects.requireNonNull(layout.getEditText());
        String userInput = inputField.getText().toString();
        return userInput.isEmpty();
    }

    /**
     * handles the login request initiated by the user
     * this function will grab the email from the username provided
     * along with the password provided to authenticate using Firebase Auth
     *
     * @param username username that the user provided
     * @param password password that the user provided
     * @param successCallback callback function called when user successfully login
     * @param failureCallback callback function called when user failed to login
     */
    public void handleLogIn(final String username, final String password,
                            final OnSuccessCallback successCallback,
                            final OnFailCallbackWithMessage<LOGIN_ERROR> failureCallback
    ) {
        userRepository.getByUsername(username).addOnSuccessListener(user -> {
            String email = user.getEmail();
            authRepository.signIn(username, email, password)
                    .addOnSuccessListener(authResult -> successCallback.run())
                    .addOnFailureListener(e -> {
                        if (e instanceof InvalidLoginCredentialsException) {
                            failureCallback.run(LOGIN_ERROR.INCORRECT_PASSWORD);
                        }
                    });
            }).addOnFailureListener(e -> {
                if (e instanceof UsernameNotExistException) {
                    failureCallback.run(LOGIN_ERROR.CANNOT_FIND_USERNAME);
                } else {
                    failureCallback.run(LOGIN_ERROR.UNEXPECTED);
                }
            });
    }
}
