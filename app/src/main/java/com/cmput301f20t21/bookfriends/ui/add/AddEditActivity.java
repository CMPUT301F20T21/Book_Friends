package com.cmput301f20t21.bookfriends.ui.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cmput301f20t21.bookfriends.MainActivity;
import com.cmput301f20t21.bookfriends.R;
import com.google.android.material.textfield.TextInputLayout;

public class AddEditActivity extends AppCompatActivity {
    Button cancelButton;
    Button scanButton;
    Button saveButton;
    Button viewRequestButton;

    private TextView titleTextView;
    private TextView isbnTextView;
    private TextView authorTextView;

    private TextInputLayout isbnEditText;
    private TextInputLayout titleEditText;
    private TextInputLayout authorEditText;
    private TextInputLayout descriptionEditText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_activity);
        titleTextView = findViewById(R.id.title_text_view);
        isbnTextView = findViewById(R.id.isbn_text_view);
        authorTextView = findViewById(R.id.author_text_view);
        isbnEditText = findViewById(R.id.ISBN_layout);
        titleEditText = findViewById(R.id.title_layout);
        authorEditText = findViewById(R.id.author_layout);
        descriptionEditText = findViewById(R.id.description_layout);

        cancelButton = findViewById(R.id.cancel_button);
        scanButton = findViewById(R.id.scanner_button);
        saveButton = findViewById(R.id.save_button);
        viewRequestButton = findViewById(R.id.view_request_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToHome();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInformation();
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScanner();
            }
        });

        viewRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open new activity
            }
        });
    }

    public void backToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void saveInformation() {
        if (isbnEditText.getEditText().getText().length() == 0) {
            isbnEditText.setError("Cannot be empty");
        }

        if (titleEditText.getEditText().getText().length() == 0) {
            titleEditText.setError("Cannot be empty");
        }

        if (authorEditText.getEditText().getText().length() == 0) {
            authorEditText.setError("Cannot be empty");
        }

        if (isbnEditText.getEditText().getText().length() != 0
            && titleEditText.getEditText().getText().length() != 0
            && authorEditText.getEditText().getText().length() != 0) {

            String isbn = isbnEditText.getEditText().getText().toString();
            isbnTextView.setText(isbn);

            String title = titleEditText.getEditText().getText().toString();
            titleTextView.setText(title);

            String author = authorEditText.getEditText().getText().toString();
            authorTextView.setText(author);

            // eventually, we will go back to home screen after saving all information
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void openScanner() {

    }
}
