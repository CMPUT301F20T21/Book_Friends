package com.cmput301f20t21.bookfriends.ui.login;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.enums.SIGNUP_ERROR;
import com.google.android.material.textfield.TextInputLayout;

public class CreateAccountActivity extends AppCompatActivity {
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmLayout;
    private TextInputLayout emailLayout;
    private EditText usernameField;
    private EditText passwordField;
    private EditText confirmField;
    private EditText emailField;

    private CreateAccountViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        this.getSupportActionBar().hide();
        // hides the status bar, deprecated in API 30
        View decorView = this.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        model = new ViewModelProvider(this).get(CreateAccountViewModel.class);

        usernameLayout = findViewById(R.id.signup_username_layout);
        usernameField = usernameLayout.getEditText();
        usernameField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable usernameString) {
                // remove the error if any exist
                usernameLayout.setError(null);
            }
        });

        passwordLayout = findViewById(R.id.signup_password_layout);
        passwordField = passwordLayout.getEditText();
        passwordField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable passwordString) {
                // remove the error if any exist
                passwordLayout.setError(null);
            }
        });

        confirmLayout = findViewById(R.id.confirm_password_layout);
        confirmField = confirmLayout.getEditText();
        confirmField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable confirmString) {
                // remove the error if any exist
                confirmLayout.setError(null);
            }
        });

        emailLayout = findViewById(R.id.email_layout);
        emailField = emailLayout.getEditText();
        emailField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable emailString) {
                // remove the error if any exist
                emailLayout.setError(null);
            }
        });
    }

    /**
     * check the input field for emptiness, adding error message if field is empty
     * @param layout the input field layout that the user entered
     * @return true if the field is empty, false if field is not empty
     */
    private boolean checkEmpty(TextInputLayout layout) {
        EditText inputField = layout.getEditText();
        String userInput  = inputField.getText().toString();
        String inputHint = inputField.getHint().toString();
        boolean isFieldEmpty = userInput.isEmpty();

        if (isFieldEmpty) {
            layout.setError( inputHint + getString(R.string.empty_error));
        }
        return isFieldEmpty;
    }

    public void onCancelClicked(View view) {
        finish();
    }

    public void onCreateClicked(View view){
        String confirmPassword = confirmField.getText().toString();
        String password = passwordField.getText().toString();
        if(!confirmPassword.equals(password)){
            confirmLayout.setError(getString(R.string.password_match_error));
        }
        else if (!checkEmpty(usernameLayout) && !checkEmpty(passwordLayout) &&
                !checkEmpty(confirmLayout) && !checkEmpty(emailLayout)) {
            String username = usernameField.getText().toString();
            String email = emailField.getText().toString();
            model.handleSignUp(username, email, password,
                    () -> onSignUpSuccess(),
                    (SIGNUP_ERROR error) -> onSignUpFail(error));
        }
  }

    public void onSignUpSuccess() {
        finish();
    }

    public void onSignUpFail(SIGNUP_ERROR error) {
        switch (error) {
            case EMAIL_EXISTS:
                emailLayout.setError(getString(R.string.email_already_registered_error));
                emailField.requestFocus();
                return;
            case USERNAME_EXISTS:
                usernameLayout.setError(getString(R.string.username_already_registered_error));
                usernameField.requestFocus();
                return;
            default:
                // should display unexpected error here
                return;
        }
    }
}
