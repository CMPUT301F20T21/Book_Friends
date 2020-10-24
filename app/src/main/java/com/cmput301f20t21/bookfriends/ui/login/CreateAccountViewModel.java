package com.cmput301f20t21.bookfriends.ui.login;

import android.util.Log;
import android.widget.EditText;

import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.enums.SIGNUP_ERROR;
import com.cmput301f20t21.bookfriends.services.AuthService;
import com.cmput301f20t21.bookfriends.services.UserService;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class CreateAccountViewModel extends ViewModel {

    /**
     *
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

    /**
     * check the input field for emptiness, adding error message if field is empty
     * @param layout the input field layout that the user entered
     * @return true if the field is empty, false if field is not empty
     */
    protected boolean notEmpty(TextInputLayout layout) {
        EditText inputField = layout.getEditText();
        String userInput = inputField.getText().toString();
        //layout.setError( inputHint + getString(R.string.empty_error));
        return !userInput.isEmpty();
    }

    /**
     * check if the input field has space or not
     * @param layout
     * @return true if the field has space, false if field has no space
     */
    protected boolean noSpace(TextInputLayout layout) {
        EditText inputField = layout.getEditText();
        String userInput = inputField.getText().toString();
        // layout.setError(getString(R.string.space_error));
        return !userInput.contains(" ");
    }

    /**
     * check if the input field is alphanumeric only or not
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
     * @param emailLayout
     * @return true if the email is valid, false if the email format if not valid
     */
    protected boolean isEmailValid(TextInputLayout emailLayout) {
        EditText emailField = emailLayout.getEditText();
        String validEmail = emailField.getText().toString();
        // emailLayout.setError(getString(R.string.email_format_error));
        return android.util.Patterns.EMAIL_ADDRESS.matcher(validEmail).matches();
    }

    /**
     * check if two passwords match
     * @param passwordLayout
     * @param confirmLayout
     * @return true if two passwords match, false if two passwords do not match
     */
    protected boolean isPasswordMatch(TextInputLayout passwordLayout,TextInputLayout confirmLayout) {
        EditText passwordField = passwordLayout.getEditText();
        String password = passwordField.getText().toString();
        EditText confirmPasswordField = confirmLayout.getEditText();
        String confirmPassword  = confirmPasswordField.getText().toString();
        return confirmPassword.equals(password);
    }

    private final AuthService authService = AuthService.getInstance();
    private final UserService userService = UserService.getInstance();
    private final String TAG = "SIGNUP_ERROR";

    /**
     * handle sign up errors
     * @param username
     * @param email
     * @param password
     * @param successCallback
     * @param failCallback
     */
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
