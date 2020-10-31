package com.cmput301f20t21.bookfriends.ui.add;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import androidx.lifecycle.ViewModelProvider;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.enums.BOOK_ACTION;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.ui.library.OwnedListFragment;
import com.cmput301f20t21.bookfriends.ui.scanner.ScannerAddActivity;
import com.cmput301f20t21.bookfriends.ui.scanner.ScannerBaseActivity;
import com.google.android.material.textfield.TextInputLayout;

public class AddEditActivity extends AppCompatActivity {
    private Button scanButton;
    private Button uploadImgButton;
    private ImageView bookImage;
    private Uri bookImageUri;
    private TextInputLayout isbnLayout;
    private TextInputLayout titleLayout;
    private TextInputLayout authorLayout;
    private TextInputLayout descriptionLayout;
    private BOOK_ACTION action;

    private AddEditViewModel model;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_activity);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_white_18);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        uploadImgButton = findViewById(R.id.upload_cover_button);
        bookImage = findViewById(R.id.book_image_view); // will be replaced with actual image
        isbnLayout = findViewById(R.id.ISBN_layout);
        titleLayout = findViewById(R.id.title_layout);
        authorLayout = findViewById(R.id.author_layout);
        descriptionLayout = findViewById(R.id.description_layout);
        scanButton = findViewById(R.id.scanner_button);

        model = new ViewModelProvider(this).get(AddEditViewModel.class);

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

        Intent intent = getIntent();
        action = (BOOK_ACTION) intent.getSerializableExtra(OwnedListFragment.BOOK_ACTION_KEY);
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
        String isbn = isbnLayout.getEditText().getText().toString();
        String title = titleLayout.getEditText().getText().toString();
        String author = authorLayout.getEditText().getText().toString();
        String description = descriptionLayout.getEditText().getText().toString();

        if (isbn.length() == 0) {
            isbnLayout.setError(getString(R.string.empty_error));
        }

        if (title.length() == 0) {
            titleLayout.setError(getString(R.string.empty_error));
        }

        if (author.length() == 0) {
            authorLayout.setError(getString(R.string.empty_error));
        }

        if (isbn.length() != 0 && title.length() != 0 && author.length() != 0) {
            if (action == BOOK_ACTION.ADD) {
                // if no image is attached, bookImageUri will be passed as null
                model.handleAddBook(isbn, title, author, description, bookImageUri,
                        this::onSuccess,
                        this::onFailure
                );
            }

        }
    }

    public void onSuccess() {
        if (action == BOOK_ACTION.ADD) {
            Toast.makeText(this, getString(R.string.add_book_successful), Toast.LENGTH_SHORT).show();
        } else if (action == BOOK_ACTION.EDIT) {
            Toast.makeText(this, getString(R.string.edit_book_successful), Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    public void onFailure(BOOK_ERROR error) {
        String errorMessage;
        switch (error) {
            case FAIL_TO_ADD_BOOK:
                errorMessage = getString(R.string.operation_failed);
                break;
            case FAIL_TO_ADD_IMAGE:
                errorMessage = getString(R.string.fail_to_add_image);
                break;
            default:
                errorMessage = getString(R.string.unexpected_error);
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    // TODO: This is an idea to load image to imageView from cloud storage if needed
    //       delete this when no longer needed
    //       also check getImageFromBookId() from AddEditViewModel
//    public void onGetImageCallback(Uri imageUri) {
//        if(imageUri != null) {
//            Glide.with(this).load(imageUri).into(bookImage);
//        }
//    }

    private void openScanner() {
        // TODO: implement the scanner
        Intent intent = new Intent(this, ScannerAddActivity.class);
        startActivity(intent); // TODO: potentially startActivityForResult
    }

    private void uploadImg() {
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
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
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
            if (data != null) {
                bookImageUri = data.getData();
                bookImage.setImageURI(bookImageUri);
            }
        }
    }
}
