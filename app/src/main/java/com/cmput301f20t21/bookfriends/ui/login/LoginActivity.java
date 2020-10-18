package com.cmput301f20t21.bookfriends.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.cmput301f20t21.bookfriends.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Activity that will display the login page and handle user inputs
 */
public class LoginActivity extends AppCompatActivity {
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;
    private TextInputEditText usernameField;
    private TextInputEditText passwordField;

    /**
     * android lifecycle method, grab the Layout and EditText fields
     * as well as adding a simple textChangedListener
     * @param savedInstanceState - the saved objects, should contain nothing for this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameLayout = findViewById(R.id.login_username_layout);
        usernameField = findViewById(R.id.login_username_field);
        usernameField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable usernameString) {
                String username = usernameString.toString();
                checkUsername(username); // return value not needed
            }
        });
        passwordLayout = findViewById(R.id.login_password_layout);
        passwordField = findViewById(R.id.login_password_field);
        passwordField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable passwordString) {
                String password = passwordString.toString();
                checkPassword(password); // return value not needed
            }
        });
    }

    /**
     * check the username field for emptiness, adding error message if field is empty
     * @param username - the username that the user entered
     * @return true if the username is not empty, false if username is empty
     */
    private boolean checkUsername(String username) {
        boolean isUsernameEmpty = username.isEmpty();
        if (isUsernameEmpty) {
            usernameLayout.setError(getString(R.string.username_empty_error));
        } else {
            usernameLayout.setError(null);
        }
        return !isUsernameEmpty;
    }

    /**
     * check the username field for emptiness, adding error message if field is empty
     * @param password - the password that the user entered
     * @return true if the password is not empty, false if password is empty
     */
    private boolean checkPassword(String password) {
        boolean isPasswordEmpty = password.isEmpty();
        if (isPasswordEmpty) {
            passwordLayout.setError(getString(R.string.password_empty_error));
        } else {
            passwordLayout.setError(null);
        }
        return !isPasswordEmpty;
    }

    /**
     * TODO: fill out javadoc comments once finished
     * @param view
     */
    public void onCreateAccountClicked(View view) {
        // start the activity here, no result is needed
        // created user will be stored directly to firestore
    }

    /**
     * method called when user clicked the "login" button
     * the function will validate the inputs and authenticate the user
     * user will be redirected to the main screen if authentication is successful
     * @param view - the view associated with the button
     */
    public void onLoginClicked(View view) {
        Editable usernameText = usernameField.getText();
        Editable passwordText = passwordField.getText();
        // getText() should never return null, error handling just for safety
        if (usernameText == null || passwordText == null) {
            String nullErrorMessage = getString(R.string.login_error_message);
            usernameLayout.setError(nullErrorMessage);
            passwordLayout.setError(nullErrorMessage);
            return;
        }

        String username = usernameText.toString();
        String password = passwordText.toString();
        if (checkUsername(username) && checkPassword(password)) {
             /* TODO: create a User entity object and call UserService to authenticate user
              * if user passed the authentication, grab all the user information
              * and store it in the userIntent and pass to the main activity
              */
//            setResult(RESULT_OK, userIntent);
//            finish();
        }
    }
}
