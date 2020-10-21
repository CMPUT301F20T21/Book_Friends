package com.cmput301f20t21.bookfriends.ui.add;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cmput301f20t21.bookfriends.R;
import com.google.android.material.textfield.TextInputLayout;

public class AddEditActivity extends AppCompatActivity {
    private Button scanButton;
    private Button uploadImgButton;
    private ImageView bookImage;
    private TextInputLayout isbnEditText;
    private TextInputLayout titleEditText;
    private TextInputLayout authorEditText;
    private TextInputLayout descriptionEditText;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_activity);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_black_18dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        uploadImgButton = findViewById(R.id.upload_pic);
        bookImage = findViewById(R.id.imageView); // will be replaced with actual image
        isbnEditText = findViewById(R.id.ISBN_layout);
        titleEditText = findViewById(R.id.title_layout);
        authorEditText = findViewById(R.id.author_layout);
        descriptionEditText = findViewById(R.id.description_layout);
        scanButton = findViewById(R.id.scanner_button);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScanner();
            }
        });

        uploadImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) { // permission not granted, ask for it
                        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        // pop up to request permission
                        requestPermissions(permission, PERMISSION_CODE);
                    } else { // permission granted
                        uploadImg();
                    }
                } else { // if android version is less than marshmallow 6.0.1
                    uploadImg();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.save_button:
                saveInformation();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_add_edit_menu, menu);
        return true;
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

            // eventually, we will go back to home screen after saving all information
            finish();
        }
    }

    public void openScanner() {
        // TODO: implement the scanner
    }

    public void uploadImg() {
        // TODO: implement the feature that allows user to upload cover image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    /**
     * handle result of runtime permission
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    uploadImg();
                } else {
                    // permission denied by user
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    /**
     * set the cover image after selecting an image from gallery
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            // set image to image view
            bookImage.setImageURI(data.getData());
        }
    }
}
