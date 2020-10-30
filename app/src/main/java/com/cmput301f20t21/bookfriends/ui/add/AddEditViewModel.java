/*
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.add;

import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallback;
import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallbackWithMessage;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.services.AuthService;
import com.cmput301f20t21.bookfriends.services.BookService;
import com.google.firebase.firestore.DocumentReference;

public class AddEditViewModel extends ViewModel {
    public interface OnGetImageSuccessCallback {
        void run(Uri imageUri);
    }

    /** called by handlers when async request failed */
    public interface OnFailCallback {
        void run(BOOK_ERROR error);
    }

    private final AuthService authService = AuthService.getInstance();
    private final BookService bookService = BookService.getInstance();

    public void handleAddBook(
            final String isbn, final String title, final String author, final String description,
            @Nullable Uri imageUri, OnSuccessCallbackWithMessage<Book> successCallback, OnFailCallback failCallback
    ) {
        String owner = authService.getCurrentUser().getUsername();
        bookService.add(isbn, title, author, description, owner).addOnCompleteListener(
                addBookTask -> {
                    if (addBookTask.isSuccessful()) {
                        DocumentReference result = addBookTask.getResult();
                        if (result != null) {
                            String bookId = result.getId();
                            Book book = new Book(bookId, isbn, title, author, description, owner, BOOK_STATUS.AVAILABLE, imageUri);
                            if(imageUri != null) {
                                // not using string resource because this is not displayed to user
                                String imageName = bookId + "cover";
                                bookService.addImage(imageName, imageUri).addOnCompleteListener(
                                        addImageTask -> {
                                            if (addImageTask.isSuccessful()) {
                                                bookService.addImageNameToBook(bookId, imageName).addOnCompleteListener(
                                                        addNameTask -> {
                                                            successCallback.run(book);
                                                        }
                                                );
                                            } else {
                                                failCallback.run(BOOK_ERROR.FAIL_TO_ADD_IMAGE);
                                            }
                                        }
                                );
                            } else {
                                successCallback.run(book);
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
