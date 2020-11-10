package com.cmput301f20t21.bookfriends.ui.add;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
    private EditText isbnEditText;
    private TextInputLayout titleLayout;
    private TextInputLayout authorLayout;
    private TextInputLayout descriptionLayout;
    private Dialog imageUpdateDialog;

    private boolean imageExists = false;
    private boolean shouldDeleteImage = false;
    private BOOK_ACTION action;
    private AddEditViewModel vm;
    private Book editBook; // book currently being edited

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

        bindBookFromIntent();

        vm = new ViewModelProvider(this).get(AddEditViewModel.class);

        vm.getBookImageUri().observe(this, this::paintImage);

        scanButton.setOnClickListener(view -> openScanner());
        uploadImgButton.setOnClickListener(view -> {
            showImageUpdateDialog(vm.getBookImageUri().getValue());
        });
    }

    private void showImageUpdateDialog(Uri uri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (uri == null && !imageExists) {
            imageUpdateDialog = builder.setItems(R.array.image_actions_new, (dialog, which) -> {
                // for corresponding strings check values/arrays.xml:3
                if (which == 0) { // selected Upload new image
                    prepareLocalImageWithPermission();
                }
            }).show();
        } else {
            imageUpdateDialog = builder.setItems(R.array.image_actions_exist, (dialog, which) -> {
                // for corresponding strings check values/arrays.xml:3
                if (which == 0) { // selected Replace with new image
                    prepareLocalImageWithPermission();
                } else if (which == 1) {
                    flushLocalImage();
                }
            }).show();
        }
    }

    private void flushLocalImage() {
        vm.setBookImageUri(null);
        shouldDeleteImage = true;
        imageExists = false;
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

    private void bindBookFromIntent() {
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
                vm.handleAddBook(isbn, title, author, description, vm.getBookImageUri().getValue(),
                        this::onAddSuccess,
                        this::onFailure
                );
            } else if (action == BOOK_ACTION.EDIT) {
                vm.handleEditBook(editBook, isbn, title, author, description, vm.getBookImageUri().getValue(), shouldDeleteImage,
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
        paintImageAndDetectIfExists(storageReference);
    }

    private void paintImageAndDetectIfExists(StorageReference ref) {
        GlideApp.with(this)
                .load(ref)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageExists = false;
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageExists = true;
                        return false;
                    }
                })
                .placeholder(R.drawable.no_image)
                .into(bookImage);
    }

    private void paintImage(Uri uri) {
        GlideApp.with(this)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.no_image)
                .into(bookImage);
    }


    private void openScanner() {
        // TODO: implement the scanner
        Intent intent = new Intent(this, ScannerAddActivity.class);
        startActivityForResult(intent, REQUEST_GET_SCANNED_ISBN);
    }

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
                vm.setBookImageUri(bookImageUri);
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_GET_SCANNED_ISBN) {
            isbnEditText.setText(data.getStringExtra(ScannerAddActivity.ISBN_KEY));
        }
    }

}
