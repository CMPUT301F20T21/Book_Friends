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

        confirmLayout  = findViewById(R.id.Comfirm_Password_Layout);
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

        phoneLayout  = findViewById(R.id.phone_layout);
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
     * @param username - the username that the user entered
     * @return true if the username is not empty, false if username is empty
     */
    private boolean checkUsername(String username) {
        boolean isUsernameEmpty = username.isEmpty();
        if (isUsernameEmpty) {
            usernameLayout.setError("Username can not be empty");
        }
        return !isUsernameEmpty;
    }
    /**
     * check the password field for emptiness, adding error message if field is empty
     * @param password - the password that the user entered
     * @return true if the password is not empty, false if password is empty
     */
    private boolean checkPassword(String password) {
        boolean isPasswordEmpty = password.isEmpty();
        if (isPasswordEmpty) {
            passwordLayout.setError("Password can not be Empty");
        }
        return !isPasswordEmpty;
    }

    /**
     * check the confirmed password for emptiness, adding error message if field is empty
     * @param ConfirmPassword - the confirmed password that the user entered
     * @return true if the confirmed password is not empty, false if confirmed password is empty
     */
    private boolean checkConfirm(String ConfirmPassword) {
        boolean isConfirmPasswordEmpty = ConfirmPassword.isEmpty();
        if (isConfirmPasswordEmpty) {
            confirmLayout.setError("Confirm password can not be Empty");
        }
        return !isConfirmPasswordEmpty;

    }

    /**
     * check the email address for emptiness, adding error message if field is empty
     * @param email - the email address that the user entered
     * @return true if the email address is not empty, false if email is empty
     */
    private boolean checkEmail(String email) {
        boolean isEmailEmpty = email.isEmpty();
        if (isEmailEmpty) {
            emailLayout.setError("Email can not be Empty");
        }
        return !isEmailEmpty;
    }

    /**
     * check the phone number for emptiness, adding error message if field is empty
     * @param phone - the phone number that the user entered
     * @return true if the phone numnber is not empty, false if the email is empty
     */
    private final boolean checkPhone(String phone) {
        boolean isPhoneEmpty = phone.isEmpty();
        if (isPhoneEmpty) {
            phoneLayout.setError("Phone number can not be Empty");
        }
        return !isPhoneEmpty;
    }

    public void onCancelClicked(View view) {
        finish();

    }
    public void onCreateClicked(View view){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String ConfirmPassword = confirmField.getText().toString();
        String email = emailField.getText().toString();
        String phone = phoneField.getText().toString();
        if(!ConfirmPassword.equals(password)){
            confirmLayout.setError("password does not match");

        }
        else if (checkUsername(username) && checkPassword(password) &&
                checkConfirm(ConfirmPassword) && checkEmail(email) &&
                checkPhone(phone)) {
            finish();
        }

    }





}
