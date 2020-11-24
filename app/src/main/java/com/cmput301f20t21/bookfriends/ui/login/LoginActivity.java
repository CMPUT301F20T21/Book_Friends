/*
 * LoginActivity.java
 * Version: 1.0
 * Date: October 18, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301f20t21.bookfriends.MainActivity;
import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.enums.LOGIN_ERROR;
import com.cmput301f20t21.bookfriends.ui.component.ProgressButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * Activity that will display the login page and handle user inputs
 */
public class LoginActivity extends AppCompatActivity {
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;
    private EditText usernameField;
    private EditText passwordField;
    private ProgressButton progressButton;
    private LoginViewModel model;

    /**
     * android lifecycle method, grab the Layout and EditText fields
     * as well as adding a simple textChangedListener
     * @param savedInstanceState the saved objects, should contain nothing for this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        model = new ViewModelProvider(this).get(LoginViewModel.class);

        Objects.requireNonNull(this.getSupportActionBar()).hide();
        // hides the status bar, deprecated in API 30
        View decorView = this.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        usernameLayout = findViewById(R.id.login_username_layout);
        usernameField = Objects.requireNonNull(usernameLayout.getEditText());
        usernameField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable usernameString) {
                // remove the error if any exist
                usernameLayout.setError(null);
            }
        });
        passwordLayout = findViewById(R.id.login_password_layout);
        passwordField = Objects.requireNonNull(passwordLayout.getEditText());
        passwordField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable passwordString) {
                // remove the error if any exist
                passwordLayout.setError(null);
            }
        });

        View loginButton = findViewById(R.id.login_btn);
        progressButton = new ProgressButton(
                this, loginButton,
                getString(R.string.string_login),
                getString(R.string.string_logged_in));
        loginButton.setOnClickListener(this::onLoginClick);
    }

    /**
     * method called when the "Create Account" button is clicked
     * user will be redirected to an new activity to create an account
     * @param view the view associated with the button
     */
    public void onCreateAccountClicked(View view) {
        startActivity(new Intent(this, CreateAccountActivity.class));
    }

    /**
     * method called when user clicked the "login" button
     * the function will validate the inputs and authenticate the user
     * user will be redirected to the main screen if authentication is successful
     * @param view the view associated with the button
     */
    public void onLoginClick(View view) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        boolean isInputValid = true;
        if (model.isEmpty(usernameLayout)) {
            usernameLayout.setError(getString(R.string.username_empty_error));
            isInputValid = false;
        }
        if (model.isEmpty(passwordLayout)) {
            passwordLayout.setError(getString(R.string.password_empty_error));
            isInputValid = false;
        }
        if (isInputValid) {
            progressButton.onClick();
            model.handleLogIn(username, password, this::onLoginSuccess, this::onLoginFail);
        }
    }

    /**
     * callback function called when user successfully login
     */
    private void onLoginSuccess() {
        progressButton.onSuccess();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }, 500);
    }

    /**
     * callback function called when user failed the authentication process
     */
    private void onLoginFail(LOGIN_ERROR error) {
        progressButton.onError();
        switch (error) {
            case CANNOT_FIND_USERNAME:
                usernameLayout.setError(getString(R.string.login_username_error));
                break;
            case INCORRECT_PASSWORD:
                passwordLayout.setError(getString(R.string.login_password_error));
                break;
            default:
                Toast.makeText(this, getString(R.string.unexpected_error), Toast.LENGTH_SHORT).show();
        }
    }

}
