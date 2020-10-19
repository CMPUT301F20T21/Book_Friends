package com.cmput301f20t21.bookfriends.ui.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cmput301f20t21.bookfriends.MainActivity;
import com.cmput301f20t21.bookfriends.R;
import com.google.android.material.textfield.TextInputLayout;

public class AddEditActivity extends AppCompatActivity {
    private Button cancelButton;
    private Button scanButton;
    private Button saveButton;
    private Button viewRequestButton;

    private ImageView bookImage;

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
        bookImage = findViewById(R.id.imageView); // will be replaced with actual image
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
                // TODO: implement view all requests
                // open new activity
            }
        });
    }

    /**
     * when cancel button is clicked, go back to the home screen
     */
    public void backToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Users should be able to manually fill in or user the scanner to retrieve the information
     * for the book they want to add
     * After checking all required fields, save button is clicked
     * TODO: this is just a placeholder
     */
    public void saveInformation() {
        String isbn = isbnEditText.getEditText().getText().toString();
        String title = titleEditText.getEditText().getText().toString();
        String author = authorEditText.getEditText().getText().toString();
        if (isbn.length() == 0) {
            isbnEditText.setError("Cannot be empty");
        }

        if (title.length() == 0) {
            titleEditText.setError("Cannot be empty");
        }

        if (author.length() == 0) {
            authorEditText.setError("Cannot be empty");
        }

        if (isbn.length() != 0 && title.length() != 0 && author.length() != 0) {
            isbnTextView.setText(isbn);
            titleTextView.setText(title);
            authorTextView.setText(author);

            // eventually, we will go back to home screen after saving all information
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void openScanner() {
        // TODO: implement the scanner
    }
}
