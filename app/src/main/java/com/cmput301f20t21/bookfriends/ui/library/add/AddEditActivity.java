package com.cmput301f20t21.bookfriends.ui.library.add;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.databinding.ActivityAddEditBinding;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.GoogleBookData;
import com.cmput301f20t21.bookfriends.enums.BOOK_ACTION;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.enums.SCAN_ERROR;
import com.cmput301f20t21.bookfriends.services.GoogleBookService;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;
import com.cmput301f20t21.bookfriends.ui.scanner.ScannerAddActivity;
import com.cmput301f20t21.bookfriends.utils.ImagePainter;
import com.google.android.material.textfield.TextInputLayout;

public class AddEditActivity extends AppCompatActivity {
    public static final String NEW_BOOK_INTENT_KEY = "com.cmput301f20t21.bookfriends.NEW_BOOK";
    public static final String OLD_BOOK_INTENT_KEY = "com.cmput301f20t21.bookfriends.OLD_BOOK";
    public static final String UPDATED_BOOK_INTENT_KEY = "com.cmput301f20t21.bookfriends.UPDATED_BOOK";
    public static final int REQUEST_GET_SCANNED_ISBN = 2000;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private Button scanButton;
    private Button uploadImgButton;
    private ImageView bookImage;
    private TextInputLayout isbnLayout;
    private TextInputLayout titleLayout;
    private TextInputLayout authorLayout;

    private EditText isbnEditText;
    private EditText titleEditText;
    private EditText authorEditText;

    private FrameLayout loadingOverlay;

    private BOOK_ACTION action;
    private AddEditViewModel vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new ViewModelProvider(this).get(AddEditViewModel.class);
        setContentView(R.layout.activity_add_edit);
        setViewBindings();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_white_18);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setChildViews();

        bindBookFromIntent();
        fetchRemoteCoverImage();

        vm.getLocalImageUri().observe(this, (uri) -> ImagePainter.paintImage(bookImage, uri));
        scanButton.setOnClickListener(view -> openScanner());
        uploadImgButton.setOnClickListener(view -> {
            showImageUpdateDialog();
        });
    }

    /**
     * The initial fetch for the existing cover image of the book, stored in firebase storage
     * this fetch is called only once. It will also tell the vm whether there is an image for the book
     */
    private void fetchRemoteCoverImage() {
        Book oldBook = vm.getOldBook();
        if (oldBook == null) return;

        ImagePainter.paintImage(bookImage, oldBook.getImageUrl());
    }

    private void setChildViews() {
        uploadImgButton = findViewById(R.id.upload_cover_button);
        bookImage = findViewById(R.id.book_image_view); // will be replaced with actual image
        isbnLayout = findViewById(R.id.ISBN_layout);
        titleLayout = findViewById(R.id.title_layout);
        authorLayout = findViewById(R.id.author_layout);
        isbnEditText = isbnLayout.getEditText();
        titleEditText = titleLayout.getEditText();
        authorEditText = authorLayout.getEditText();
        scanButton = findViewById(R.id.scanner_button);
        loadingOverlay = findViewById(R.id.autofill_loading_overlay);
    }

    /**
     * https://developer.android.com/topic/libraries/data-binding/architecture#viewmodel
     * bind the xml views text fields with view model's live data
     * this is a two way binding, meaning that update view will update vm data.
     * Be very careful when setting views directly
     */
    private void setViewBindings() {
        ActivityAddEditBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit);
        binding.setLifecycleOwner(this);
        binding.setVm(vm);
    }

    /**
     * Show different floating dialog based on whether the book has an image.
     * uses the vm.hasImage flag.
     */
    private void showImageUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (!vm.hasImage()) {
            builder.setItems(R.array.image_actions_new, (dialog, which) -> {
                // for corresponding strings check values/arrays.xml:3
                if (which == 0) { // selected Upload new image
                    prepareLocalImageWithPermission();
                }
            }).show();
        } else {
            builder.setItems(R.array.image_actions_exist, (dialog, which) -> {
                // for corresponding strings check values/arrays.xml:3
                if (which == 0) { // selected Replace with new image
                    prepareLocalImageWithPermission();
                } else if (which == 1) {
                    vm.setLocalImageUri(null); // delete local image and ready to delete the remote on save
                }
            }).show();
        }
    }

    private void prepareLocalImageWithPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) { // permission not granted, ask for it
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            // pop up to request permission
            requestPermissions(permission, PERMISSION_CODE);
        } else { // permission granted
            prepareLocalImage();
        }
    }

    /**
     * bind the activity received book object into view model. this will set the text fields correspondingly
     * thanks to data binding
     */
    private void bindBookFromIntent() {
        Intent intent = getIntent();
        action = (BOOK_ACTION) intent.getSerializableExtra(BaseDetailActivity.BOOK_ACTION_KEY);
        if (action == BOOK_ACTION.EDIT) {
            Book editBook = intent.getParcelableExtra(BaseDetailActivity.BOOK_DATA_KEY);
            if (editBook != null) {
                vm.bindBook(editBook);
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
     */
    private void saveInformation() {
        if (validateFields()) {
            if (action == BOOK_ACTION.ADD) {
                // if no image is attached, bookImageUri will be passed as null
                vm.handleAddBook(
                        this::onAddSuccess,
                        this::onFailure
                );
            } else if (action == BOOK_ACTION.EDIT) {
                vm.handleEditBook(
                        this::onEditSuccess,
                        this::onFailure
                );
            }
        }
    }

    /**
     * validate the text fields, make sure they are not empty and if so set error message
     * @return boolean indicating whether all fields are valid
     */
    private boolean validateFields() {
        Boolean isValid = true;
        String isbn = isbnEditText.getText().toString();
        String title = titleEditText.getText().toString();
        String author = authorEditText.getText().toString();

        if (isbn.length() != 10 && isbn.length() != 13) {
            isbnLayout.setError(getString(R.string.isbn_invalid));
            isValid = false;
        }

        if (title.length() == 0) {
            titleLayout.setError(getString(R.string.empty_error));
            isValid = false;
        }

        if (author.length() == 0) {
            authorLayout.setError(getString(R.string.empty_error));
            isValid = false;
        }

        return isValid;
    }

    private void onAddSuccess(Book book) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(NEW_BOOK_INTENT_KEY, book);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void onEditSuccess(Book updatedBook) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(OLD_BOOK_INTENT_KEY, vm.getOldBook());
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

    private void openScanner() {
        Intent intent = new Intent(this, ScannerAddActivity.class);
        startActivityForResult(intent, REQUEST_GET_SCANNED_ISBN);
    }

    // create an activity to allow users to upload an image
    private void prepareLocalImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    /**
     * handle result of runtime permission
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    prepareLocalImage();
                } else {
                    // permission denied by user
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    /**
     * set the cover image after selecting an image from gallery
     *
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
                Uri bookImageUri = data.getData();
                // needed to make sure permission is not lost when switching activity
                getContentResolver().takePersistableUriPermission(bookImageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
                vm.setLocalImageUri(bookImageUri);
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_GET_SCANNED_ISBN) {
            String scannedIsbn = data.getStringExtra(ScannerAddActivity.ISBN_KEY);
            isbnEditText.setText(scannedIsbn); // this sets the vm.bookIsbn too!
            loadingOverlay.setVisibility(View.VISIBLE);
            GoogleBookService.getInstance()
                    .getBookInfoByIsbn(
                            scannedIsbn,
                            this::onFetchBookDataSuccess,
                            this::onFetchBookDataFail);
        }
    }

    private void onFetchBookDataSuccess(GoogleBookData data) {
        runOnUiThread(() -> {
            loadingOverlay.setVisibility(View.GONE);
            titleEditText.setText(data.getTitle());
            authorEditText.setText(String.join(", ", data.getAuthors()));
        });
    }

    private void onFetchBookDataFail(SCAN_ERROR error) {
        String message;
        if (error == SCAN_ERROR.INVALID_ISBN) {
            message = getString(R.string.scan_autofill_invalid_isbn_error);
        } else {
            message = getString(R.string.unexpected_error);
        }
        runOnUiThread(() -> {
            loadingOverlay.setVisibility(View.GONE);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }

}
