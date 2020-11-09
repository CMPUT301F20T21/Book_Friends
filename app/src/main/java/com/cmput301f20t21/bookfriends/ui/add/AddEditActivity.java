package com.cmput301f20t21.bookfriends.ui.add;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_ACTION;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.ui.library.OwnedListFragment;
import com.cmput301f20t21.bookfriends.ui.scanner.ScannerAddActivity;
import com.cmput301f20t21.bookfriends.utils.GlideApp;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddEditActivity extends AppCompatActivity {
    private Button scanButton;
    private Button uploadImgButton;
    private ImageView bookImage;
    private Uri bookImageUri;
    private TextInputLayout isbnLayout;
    private EditText isbnEditText;
    private TextInputLayout titleLayout;
    private TextInputLayout authorLayout;
    private TextInputLayout descriptionLayout;
    private BOOK_ACTION action;

    private AddEditViewModel model;
    private Book editBook; // book currently being edited

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    public static final String NEW_BOOK_INTENT_KEY = "com.cmput301f20t21.bookfriends.NEW_BOOK";
    public static final String OLD_BOOK_INTENT_KEY = "com.cmput301f20t21.bookfriends.OLD_BOOK";
    public static final String UPDATED_BOOK_INTENT_KEY = "com.cmput301f20t21.bookfriends.UPDATED_BOOK";

    public static final int REQUEST_GET_SCANNED_ISBN = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_activity);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_white_18);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        uploadImgButton = findViewById(R.id.upload_cover_button);
        bookImage = findViewById(R.id.book_image_view); // will be replaced with actual image
        isbnLayout = findViewById(R.id.ISBN_layout);
        isbnEditText = findViewById(R.id.isbn_edit_text);
        titleLayout = findViewById(R.id.title_layout);
        authorLayout = findViewById(R.id.author_layout);
        descriptionLayout = findViewById(R.id.description_layout);
        scanButton = findViewById(R.id.scanner_button);

        model = new ViewModelProvider(this).get(AddEditViewModel.class);

        scanButton.setOnClickListener(view -> openScanner());

        uploadImgButton.setOnClickListener(view -> {
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
        });

        Intent intent = getIntent();
        action = (BOOK_ACTION) intent.getSerializableExtra(OwnedListFragment.BOOK_ACTION_KEY);
        if (action == BOOK_ACTION.EDIT) {
            editBook = intent.getParcelableExtra(OwnedListFragment.BOOK_EDIT_KEY);
            if (editBook != null) {
                loadBookInformation();
            }
        }
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

        if (isbn.length() != 13) {
            isbnLayout.setError(getString(R.string.isbn_invalid));
        }

        if (title.length() == 0) {
            titleLayout.setError(getString(R.string.empty_error));
        }

        if (author.length() == 0) {
            authorLayout.setError(getString(R.string.empty_error));
        }

        if (isbn.length() == 13 && title.length() != 0 && author.length() != 0) {
            if (action == BOOK_ACTION.ADD) {
                // if no image is attached, bookImageUri will be passed as null
                model.handleAddBook(isbn, title, author, description, bookImageUri,
                        this::onAddSuccess,
                        this::onFailure
                );
            } else if (action == BOOK_ACTION.EDIT) {
                model.handleEditBook(editBook, isbn, title, author, description, bookImageUri,
                        this::onEditSuccess,
                        this::onFailure
                );
            }
        }
    }

    private void onAddSuccess(Book book) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(NEW_BOOK_INTENT_KEY, book);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void onEditSuccess(Book updatedBook) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(OLD_BOOK_INTENT_KEY, editBook);
        resultIntent.putExtra(UPDATED_BOOK_INTENT_KEY, updatedBook);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void onFailure(BOOK_ERROR error) {
        String errorMessage;
        switch (error) {
            case FAIL_TO_ADD_BOOK:
                errorMessage = getString(R.string.operation_failed);
                break;
            case FAIL_TO_ADD_IMAGE:
                errorMessage = getString(R.string.fail_to_add_image);
                break;
            case FAIL_TO_EDIT_BOOK:
                errorMessage = getString(R.string.operation_failed);
            default:
                errorMessage = getString(R.string.unexpected_error);
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void loadBookInformation() {
        isbnLayout.getEditText().setText(editBook.getIsbn());
        titleLayout.getEditText().setText(editBook.getTitle());
        authorLayout.getEditText().setText(editBook.getAuthor());
        descriptionLayout.getEditText().setText(editBook.getDescription());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(editBook.getCoverImageName());
        GlideApp.with(this)
                .load(storageReference)
                .placeholder(R.drawable.no_image)
                .into(bookImage);
    }

    private void openScanner() {
        // TODO: implement the scanner
        Intent intent = new Intent(this, ScannerAddActivity.class);
        startActivityForResult(intent, REQUEST_GET_SCANNED_ISBN);
    }

    private void uploadImg() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
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
                // needed to make sure permission is not lost when switching activity
                getContentResolver().takePersistableUriPermission(bookImageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
                bookImage.setImageURI(bookImageUri);
            }
        }
        else if (resultCode == RESULT_OK && requestCode == REQUEST_GET_SCANNED_ISBN) {
            isbnEditText.setText(data.getStringExtra(ScannerAddActivity.ISBN_KEY));
        }
    }

}
