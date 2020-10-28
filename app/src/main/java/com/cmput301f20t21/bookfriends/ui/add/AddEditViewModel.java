/*
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.add;

import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.services.AuthService;
import com.cmput301f20t21.bookfriends.services.BookService;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddEditViewModel extends ViewModel {
    public interface OnGetImageSuccessCallback {
        void run(Uri imageUri);
    }

    public interface OnSuccessCallback {
        void run();
    }

    /** called by handlers when async request failed */
    public interface OnFailCallback {
        void run(BOOK_ERROR error);
    }

    private final AuthService authService = AuthService.getInstance();
    private final BookService bookService = BookService.getInstance();

    public void handleAddBook(
            final String isbn, final String title, final String author, final String description,
            @Nullable Uri imageUri, OnSuccessCallback successCallback, OnFailCallback failCallback
    ) {
        boolean imageAttached = true;
        if (imageUri == null) {
            imageAttached = false;
        }
        String owner = authService.getCurrentUser().getUsername();
        bookService.add(isbn, title, author, description, owner, imageAttached).addOnCompleteListener(
                addBookTask -> {
                    if (addBookTask.isSuccessful()) {
                        DocumentReference result = addBookTask.getResult();
                        if (result != null) {
                            String bookId = result.getId();
                            if(imageUri != null) {
                                bookService.addImage(bookId, imageUri).addOnCompleteListener(
                                        addImageTask -> {
                                            if (addImageTask.isSuccessful()) {
                                                successCallback.run();
                                            } else {
                                                failCallback.run(BOOK_ERROR.FAIL_TO_ADD_IMAGE);
                                            }
                                        }
                                );
                            } else {
                                successCallback.run();
                            }
                        } else {
                            failCallback.run(BOOK_ERROR.UNEXPECTED);
                        }
                    } else {
                        failCallback.run(BOOK_ERROR.FAIL_TO_ADD_BOOK);
                    }
                }
        );
    }

    public void getImageFromBookId(String bookId, OnGetImageSuccessCallback successCallback, OnFailCallback failCallback) {
        bookService.getImage(bookId).addOnCompleteListener(
                getImageTask -> {
                    if(getImageTask.isSuccessful()) {
                        successCallback.run(getImageTask.getResult());
                    } else {
                        failCallback.run(BOOK_ERROR.FAIL_TO_GET_IMAGE);
                    }
                }
        );
    }

}
