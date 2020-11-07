/*
 * AddEditViewModel.java
 * Version: 1.0
 * Date: November 4, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.add;

import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.callbacks.OnFailCallbackWithMessage;
import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallbackWithMessage;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.repositories.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.api.IAuthRepository;
import com.cmput301f20t21.bookfriends.repositories.api.IBookRepository;
import com.google.firebase.firestore.DocumentReference;

/**
 * The ViewModel for AddEditActivity
 */
public class AddEditViewModel extends ViewModel {
    private final IAuthRepository authRepository;
    private final IBookRepository bookRepository;

    //production
    public AddEditViewModel() {
        this(AuthRepository.getInstance(), BookRepository.getInstance());
    }

    // test - allow us to inject repository dependecy in test
    public AddEditViewModel(IAuthRepository authRepository, IBookRepository bookRepository) {
        this.authRepository = authRepository;
        this.bookRepository = bookRepository;
    }


    /**
     * handles the add book functionality when user clicks the "Save" button in AddEditActivity
     * adds the book to the "book" collection and the image to FireBase Cloud Storage(if there is an image)
     * @param isbn the isbn of the book
     * @param title the title of the book
     * @param author the author of the book
     * @param description the book description
     * @param imageUri the uri of the image, can be null if no image is added
     * @param successCallback async callback that is called upon successfully completing all operations
     * @param failCallback async callback that is called if any operation failed
     */
    public void handleAddBook(
            final String isbn, final String title, final String author, final String description,
            @Nullable Uri imageUri, OnSuccessCallbackWithMessage<Book> successCallback, OnFailCallbackWithMessage<BOOK_ERROR> failCallback
    ) {
        String owner = authRepository.getCurrentUser().getUsername();
        bookRepository.add(isbn, title, author, description, owner).addOnSuccessListener(
                id -> {
                    Book book = new Book(id, isbn, title, author, description, owner, BOOK_STATUS.AVAILABLE);
                    if (imageUri != null) {
                        bookRepository.addImage(book.getCoverImageName(), imageUri).addOnCompleteListener(
                                addImageTask -> {
                                    if (addImageTask.isSuccessful()) {
                                        successCallback.run(book);
                                    } else {
                                        failCallback.run(BOOK_ERROR.FAIL_TO_ADD_IMAGE);
                                    }
                                }
                        );
                    } else {
                        successCallback.run(book);
                    }
                }
        ).addOnFailureListener(e -> {
            failCallback.run(BOOK_ERROR.FAIL_TO_ADD_BOOK);
        });
    }

    /**
     * handles the edit book functionality when user clicks the "Save" button in AddEditActivity
     * edit the book to the "book" collection and the image to FireBase Cloud Storage(if there is an image)
     * @param oldBook the book before it's being edited
     * @param isbn the isbn of the book after edit
     * @param title the title of the book after edit
     * @param author the author of the book after edit
     * @param description the book description after edit
     * @param newUri the new image that the user uploaded, can be null if user did not upload a new image
     * @param successCallback async callback that is called upon successfully completing all operations
     * @param failCallback async callback that is called if any operation failed
     */
    public void handleEditBook(
            final Book oldBook, final String isbn, final String title, final String author, final String description,
            @Nullable Uri newUri, OnSuccessCallbackWithMessage<Book> successCallback, OnFailCallbackWithMessage<BOOK_ERROR> failCallback
    ) {
        String bookId = oldBook.getId();
        bookRepository.editBook(oldBook, isbn, title, author, description).addOnSuccessListener(
                newBook -> {
                    // addImage will also replace if file with imageName already exist
                    if (newUri != null) {
                        // when the image is updated
                        bookRepository.addImage(newBook.getCoverImageName(), newUri).addOnCompleteListener(
                                addImageTask -> {
                                    if (addImageTask.isSuccessful()) {
                                        successCallback.run(newBook);
                                    } else {
                                        failCallback.run(BOOK_ERROR.FAIL_TO_ADD_IMAGE);
                                    }
                                }
                        );
                    } else {
                        // image is not changed
                        successCallback.run(newBook);
                    }
                }
        ).addOnFailureListener(e -> {
            failCallback.run(BOOK_ERROR.FAIL_TO_EDIT_BOOK);
        });
    }

}
