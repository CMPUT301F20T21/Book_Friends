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


public class CreateAccountViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final String TAG = "SIGNUP_ERROR";

    // production
    public CreateAccountViewModel() {
        this(AuthRepositoryImpl.getInstance(), UserRepositoryImpl.getInstance());
    }
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
        EditText inputField = layout.getEditText();
        String userInput = inputField.getText().toString();
        return userInput.isEmpty();
    }

    /**
     * check if the input field is alphanumeric only or not
     *
     * @param usernameLayout
     * @return true if the field is alphanumeric, false if the field is not alphanumeric
     */
    protected boolean isUsernameValid(TextInputLayout usernameLayout) {
        EditText usernameField = usernameLayout.getEditText();
        String username = usernameField.getText().toString();
        return username.matches("[a-zA-Z0-9]+");
    }

    /**
     * check if the password has the length more than 6 characters
     *
     * @param passwordLayout
     * @return true if the password has 6 characters of longer, false if the password has less than 6 characters
     */
    protected boolean correctPasswordLength(TextInputLayout passwordLayout) {
        EditText passwordField = passwordLayout.getEditText();
        String password = passwordField.getText().toString();
        return (password.length() >= 6);
    }

    /**
     * check if the format of input email is valid
     *
     * @param emailLayout
     * @return true if the email is valid, false if the email format if not valid
     */
    protected boolean isEmailValid(TextInputLayout emailLayout) {
        EditText emailField = emailLayout.getEditText();
        String validEmail = emailField.getText().toString();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(validEmail).matches();
    }

    /**
     * check if two passwords match
     *
     * @param passwordLayout
     * @param confirmLayout
     * @return true if two passwords match, false if two passwords do not match
     */
    protected boolean isPasswordMatch(TextInputLayout passwordLayout, TextInputLayout confirmLayout) {
        EditText passwordField = passwordLayout.getEditText();
        String password = passwordField.getText().toString();
        EditText confirmPasswordField = confirmLayout.getEditText();
        String confirmPassword = confirmPasswordField.getText().toString();
        return confirmPassword.equals(password);
    }

    /**
     * handle sign up
     *
     * @param username
     * @param email
     * @param password
     * @param successCallback
     * @param failCallback
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
                            FirebaseUser user = authResult.getUser();
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
