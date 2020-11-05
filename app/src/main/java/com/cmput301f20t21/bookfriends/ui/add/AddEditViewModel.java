/*
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


    public void handleAddBook(
            final String isbn, final String title, final String author, final String description,
            @Nullable Uri imageUri, OnSuccessCallbackWithMessage<Book> successCallback, OnFailCallbackWithMessage<BOOK_ERROR> failCallback
    ) {
        String owner = authRepository.getCurrentUser().getUsername();
        bookRepository.add(isbn, title, author, description, owner).addOnCompleteListener(
                addBookTask -> {
                    if (addBookTask.isSuccessful()) {
                        DocumentReference result = addBookTask.getResult();
                        if (result != null) {
                            String bookId = result.getId();
                            Book book = new Book(bookId, isbn, title, author, description, owner, BOOK_STATUS.AVAILABLE);
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
                        } else {
                            failCallback.run(BOOK_ERROR.UNEXPECTED);
                        }
                    } else {
                        failCallback.run(BOOK_ERROR.FAIL_TO_ADD_BOOK);
                    }
                }
        );
    }

    public void handleEditBook(
            final Book oldBook, final String isbn, final String title, final String author, final String description,
            @Nullable Uri newUri, OnSuccessCallbackWithMessage<Book> successCallback, OnFailCallbackWithMessage<BOOK_ERROR> failCallback
    ) {
        String bookId = oldBook.getId();
        bookRepository.editBook(bookId, isbn, title, author, description).addOnCompleteListener(
                editBookTask -> {
                    if (editBookTask.isSuccessful()) {
                        Book book = new Book(bookId, isbn, title, author, description, oldBook.getOwner(), BOOK_STATUS.AVAILABLE);
                        // addImage will also replace if file with imageName already exist
                        if (newUri != null) {
                            // when the image is updated
                            bookRepository.addImage(book.getCoverImageName(), newUri).addOnCompleteListener(
                                    addImageTask -> {
                                        if (addImageTask.isSuccessful()) {
                                            successCallback.run(book);
                                        } else {
                                            failCallback.run(BOOK_ERROR.FAIL_TO_ADD_IMAGE);
                                        }
                                    }
                            );
                        } else {
                            // image is not changed
                            successCallback.run(book);
                        }
                    } else {
                        failCallback.run(BOOK_ERROR.FAIL_TO_EDIT_BOOK);
                    }
                }
        );
    }

}
