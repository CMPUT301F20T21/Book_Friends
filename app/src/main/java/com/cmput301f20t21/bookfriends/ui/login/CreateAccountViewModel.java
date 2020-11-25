/*
 * CreateAccountViewModel.java
 * Version: 1.0
 * Date: November 4, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.login;

import android.widget.EditText;

import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.callbacks.OnFailCallbackWithMessage;
import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallback;
import com.cmput301f20t21.bookfriends.enums.SIGNUP_ERROR;
import com.cmput301f20t21.bookfriends.exceptions.UsernameNotExistException;
import com.cmput301f20t21.bookfriends.repositories.impl.AuthRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.impl.UserRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.api.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.api.UserRepository;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/**
 * The view model for the CreateAccountActivity
 */
public class CreateAccountViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    // production
    public CreateAccountViewModel() {
        this(AuthRepositoryImpl.getInstance(), UserRepositoryImpl.getInstance());
    }

    // test
    public CreateAccountViewModel(AuthRepository authRepository, UserRepository userRepository){
        this.authRepository = authRepository;
        this.userRepository = userRepository;
    }

    /**
     * check the input field for emptiness, adding error message if field is empty
     *
     * @param layout the input field layout that the user entered
     * @return true if the field is empty, false if field is not empty
     */
    protected boolean isEmpty(TextInputLayout layout) {
        EditText inputField = Objects.requireNonNull(layout.getEditText());
        String userInput = inputField.getText().toString();
        return userInput.isEmpty();
    }

    /**
     * check if the input field is alphanumeric only or not
     *
     * @param usernameLayout the username layout for the username text field
     * @return true if the field is alphanumeric, false if the field is not alphanumeric
     */
    protected boolean isUsernameValid(TextInputLayout usernameLayout) {
        EditText usernameField = Objects.requireNonNull(usernameLayout.getEditText());
        String username = usernameField.getText().toString();
        return username.matches("[a-zA-Z0-9]+");
    }

    /**
     * check if the password has the length more than 6 characters
     *
     * @param passwordLayout the password layout for the password text field
     * @return true if the password has 6 characters of longer, false if the password has less than 6 characters
     */
    protected boolean correctPasswordLength(TextInputLayout passwordLayout) {
        EditText passwordField = Objects.requireNonNull(passwordLayout.getEditText());
        String password = passwordField.getText().toString();
        return (password.length() >= 6);
    }

    /**
     * check if the format of input email is valid
     *
     * @param emailLayout the email layout for the email text field
     * @return true if the email is valid, false if the email format if not valid
     */
    protected boolean isEmailValid(TextInputLayout emailLayout) {
        EditText emailField = Objects.requireNonNull(emailLayout.getEditText());
        String validEmail = emailField.getText().toString();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(validEmail).matches();
    }

    /**
     * check if two passwords match
     *
     * @param passwordLayout the password layout for the password text field
     * @param confirmLayout the confirm password layout for the confirm password field
     * @return true if two passwords match, false if two passwords do not match
     */
    protected boolean isPasswordMatch(TextInputLayout passwordLayout, TextInputLayout confirmLayout) {
        EditText passwordField = Objects.requireNonNull(passwordLayout.getEditText());
        String password = passwordField.getText().toString();
        EditText confirmPasswordField = Objects.requireNonNull(confirmLayout.getEditText());
        String confirmPassword = confirmPasswordField.getText().toString();
        return confirmPassword.equals(password);
    }

    /**
     * handle sign up
     *
     * @param username the username that is entered by user
     * @param email the email that is entered by user
     * @param password the password that is entered by user
     * @param successCallback callback function called when the account is successfully created
     * @param failCallback callback function called when some step in the creation process failed
     */
    public void handleSignUp(final String username, final String email, final String password,
                             OnSuccessCallback successCallback, OnFailCallbackWithMessage<SIGNUP_ERROR> failCallback) {
        // check if username is registered
        // only success if username exist in database
        userRepository.getByUsername(username)
                .addOnSuccessListener(user -> failCallback.run(SIGNUP_ERROR.USERNAME_EXISTS))
                .addOnFailureListener(e -> {
                    if (e instanceof UsernameNotExistException) {
                        authRepository.createUserAuth(email, password).addOnSuccessListener(authResult -> {
                            FirebaseUser user = Objects.requireNonNull(authResult.getUser());
                            // create username after create auth successful
                            userRepository.add(user.getUid(), username, email)
                                    .addOnSuccessListener(res -> successCallback.run())
                                    .addOnFailureListener(addException -> {
                                        // Clean up auth if we cannot create username
                                        // This should never happen
                                        user.delete();
                                        failCallback.run(SIGNUP_ERROR.UNEXPECTED);
                                    });
                        }).addOnFailureListener(authException -> {
                            if (authException instanceof FirebaseAuthUserCollisionException) {
                                failCallback.run(SIGNUP_ERROR.EMAIL_EXISTS);
                            } else {
                                failCallback.run(SIGNUP_ERROR.UNEXPECTED);
                            }
                        });
                    } else {
                        failCallback.run(SIGNUP_ERROR.UNEXPECTED);
                    }
                });
    }
}
