package com.cmput301f20t21.bookfriends.ui.login;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.cmput301f20t21.bookfriends.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CreateAccountActivity extends AppCompatActivity {
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;
    private TextInputEditText usernameField;
    private TextInputEditText passwordField;
    private TextInputLayout confirmLayout;
    private TextInputEditText confirmField;
    private TextInputLayout emailLayout;
    private TextInputEditText emailField;
    private TextInputLayout phoneLayout;
    private TextInputEditText phoneField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        this.getSupportActionBar().hide();
        // hides the status bar, deprecated in API 30
        View decorView = this.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        usernameLayout = findViewById(R.id.login_username_layout);
        usernameField = (TextInputEditText) usernameLayout.getEditText();
        usernameField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable usernameString) {
                // remove the error if any exist
                usernameLayout.setError(null);
            }
        });

        passwordLayout = findViewById(R.id.login_password_layout);
        passwordField = (TextInputEditText) passwordLayout.getEditText();
        passwordField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable passwordString) {
                // remove the error if any exist
                passwordLayout.setError(null);
            }
        });

        confirmLayout = findViewById(R.id.confirm_password_layout);
        confirmField = (TextInputEditText)confirmLayout.getEditText();
        confirmField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable comfirmString) {
                // remove the error if any exist
                confirmLayout.setError(null);
            }
        });

        emailLayout = findViewById(R.id.email_layout);
        emailField = (TextInputEditText)emailLayout.getEditText();
        emailField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable emailString) {
                // remove the error if any exist
                emailLayout.setError(null);
            }
        });

        phoneLayout = findViewById(R.id.phone_layout);
        phoneField = (TextInputEditText) phoneLayout.getEditText();
        phoneField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable phoneNumber) {
                // remove the error if any exist
                phoneLayout.setError(null);
            }
        });

    }
    /**
     * check the username field for emptiness, adding error message if field is empty
     * @param username the username that the user entered
     * @return true if the username is not empty, false if username is empty
     */
    private boolean checkUsername(String username) {
        boolean isUsernameEmpty = username.isEmpty();
        if (isUsernameEmpty) {
            usernameLayout.setError(
                    getString(R.string.string_username) + getString(R.string.empty_error)
            );
        }
        return !isUsernameEmpty;
    }

    /**
     * check the password field for emptiness, adding error message if field is empty
     * @param password the password that the user entered
     * @return true if the password is not empty, false if password is empty
     */
    private boolean checkPassword(String password) {
        boolean isPasswordEmpty = password.isEmpty();
        if (isPasswordEmpty) {
            passwordLayout.setError(
                    getString(R.string.string_password) + getString(R.string.empty_error)
            );
        }
        return !isPasswordEmpty;
    }

    /**
     * check the confirmed password for emptiness, adding error message if field is empty
     * @param confirmPassword the confirmed password that the user entered
     * @return true if the confirmed password is not empty, false if confirmed password is empty
     */
    private boolean checkConfirm(String confirmPassword) {
        boolean isConfirmPasswordEmpty = confirmPassword.isEmpty();
        if (isConfirmPasswordEmpty) {
            confirmLayout.setError(
                    getString(R.string.string_confirm_password) + getString(R.string.empty_error)
            );
        }
        return !isConfirmPasswordEmpty;

    }

    /**
     * check the email address for emptiness, adding error message if field is empty
     * @param email the email address that the user entered
     * @return true if the email address is not empty, false if email is empty
     */
    private boolean checkEmail(String email) {
        boolean isEmailEmpty = email.isEmpty();
        if (isEmailEmpty) {
            emailLayout.setError(
                    getString(R.string.string_email) + getString(R.string.empty_error)
            );
        }
        return !isEmailEmpty;
    }

    /**
     * check the phone number for emptiness, adding error message if field is empty
     * @param phone the phone number that the user entered
     * @return true if the phone [number] is not empty and false if [phone number] is empty
     */
    private boolean checkPhone(String phone) {
        boolean isPhoneEmpty = phone.isEmpty();
        if (isPhoneEmpty) {
            phoneLayout.setError(
                     getString(R.string.string_phone_number) + getString(R.string.empty_error)
            );
        }
        return !isPhoneEmpty;
    }

    public void onCancelClicked(View view) {
        finish();
    }

    public void onCreateClicked(View view){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String confirmPassword = confirmField.getText().toString();
        String email = emailField.getText().toString();
        String phone = phoneField.getText().toString();
        if(!confirmPassword.equals(password)){
            confirmLayout.setError(getString(R.string.password_match_error));

        }
        else if (checkUsername(username) && checkPassword(password) &&
                checkConfirm(confirmPassword) && checkEmail(email) &&
                checkPhone(phone)) {
            finish();
        }
  }
}
