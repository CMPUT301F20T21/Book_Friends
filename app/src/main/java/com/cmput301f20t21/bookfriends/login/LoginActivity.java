package com.cmput301f20t21.bookfriends.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cmput301f20t21.bookfriends.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout usernameLayout;
    TextInputLayout passwordLayout;
    TextInputEditText usernameField;
    TextInputEditText passwordField;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.login_button);
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

    private boolean checkUsername(String username) {
        boolean isUsernameEmpty = username.isEmpty();
        if (isUsernameEmpty) {
            usernameLayout.setError(getString(R.string.username_empty_error));
        } else {
            usernameLayout.setError(null);
        }
        return !isUsernameEmpty;
    }

    private boolean checkPassword(String password) {
        boolean isPasswordEmpty = password.isEmpty();
        if (isPasswordEmpty) {
            passwordLayout.setError(getString(R.string.password_empty_error));
        } else {
            passwordLayout.setError(null);
        }
        return !isPasswordEmpty;
    }

    public void onCreateAccountClicked(View view) {

    }

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
            // create a User entity object and call UserService to authenticate user
            finish();
        }
    }
}
