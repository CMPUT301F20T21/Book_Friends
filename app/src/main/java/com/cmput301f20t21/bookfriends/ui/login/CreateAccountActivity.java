package com.cmput301f20t21.bookfriends.ui.login;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.cmput301f20t21.bookfriends.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CreateAccountActivity extends AppCompatActivity {
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;
    private EditText usernameField;
    private EditText passwordField;
    private TextInputLayout confirmLayout;
    private EditText confirmField;
    private TextInputLayout emailLayout;
    private EditText emailField;


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
        usernameField = usernameLayout.getEditText();
        usernameField.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable usernameString) {
                // remove the error if any exist
                usernameLayout.setError(null);
            }
        });

        passwordLayout = findViewById(R.id.login_password_layout);
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
            finish();
        }
  }
}
