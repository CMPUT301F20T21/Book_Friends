/*
 * AddEditViewModel.java
 * Version: 1.0
 * Date: November 4, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.library.add;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.callbacks.OnFailCallbackWithMessage;
import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallbackWithMessage;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.repositories.factories.AuthRepositoryFactory;
import com.cmput301f20t21.bookfriends.repositories.factories.BookRepositoryFactory;
import com.cmput301f20t21.bookfriends.repositories.impl.AuthRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.impl.BookRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.api.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.api.BookRepository;

import javax.annotation.Nullable;

/**
 * The ViewModel for AddEditActivity
 */
public class AddEditViewModel extends ViewModel {
    // data-binding attributes, two way bound with the xml
    public final MutableLiveData<String> bookIsbn = new MutableLiveData<>();
    public final MutableLiveData<String> bookTitle = new MutableLiveData<>();
    public final MutableLiveData<String> bookAuthor = new MutableLiveData<>();
    private final AuthRepository authRepository;
    private final BookRepository bookRepository;
    // the local, updated image uri that might update after first remote image fetch
    private final MutableLiveData<Uri> localImageUri = new MutableLiveData<>();
    // the book we are editing
    private Book oldBook;
    private String currentUsername;

    /** the boolean indicating whether we should remove the image on save
     * Also represents if there is a cover image displayed.
     *
     * Think of hasImage exactly the same as localImageUri except that it also considers
     * if the book has a remote cover image
     */
    private boolean hasImage = false;
    private boolean hasNewImage = false;

    // production
    public AddEditViewModel() {
        this(AuthRepositoryFactory.getRepository(), BookRepositoryFactory.getRepository());
    }

    // test - allow us to inject repository dependency in test
    public AddEditViewModel(AuthRepository authRepository, BookRepository bookRepository) {
        this.authRepository = authRepository;
        this.bookRepository = bookRepository;

        currentUsername = authRepository.getCurrentUser().getUsername();
    }

    // let the activity to bind received book data into the view model
    public void bindBook(Book book) {
        bookIsbn.setValue(book.getIsbn());
        bookTitle.setValue(book.getTitle());
        bookAuthor.setValue(book.getAuthor());
        this.oldBook = book;
        if (book.getImageUrl() != null) setHasImage(true);
    }

    public Book getOldBook() {
        return oldBook;
    }

    public LiveData<Uri> getLocalImageUri() {
        return localImageUri;
    }

    public void setLocalImageUri(@Nullable Uri uri) {
        localImageUri.setValue(uri);
        setHasImage(uri != null); // if the local uri is null, it means users deleted image
        hasNewImage = true;
    }

    // exposed for Glide callback to tell if remote image is available on first load
    public void setHasImage(Boolean hasImage) {
        this.hasImage = hasImage;
    }

    // used to create dialog based on whether they have cover
    public boolean hasImage() {
        return hasImage;
    }

    /**
     * handles the add book functionality when user clicks the "Save" button in AddEditActivity
     * adds the book to the "book" collection and the image to FireBase Cloud Storage(if there is an image)
     *
     * @param successCallback async callback that is called upon successfully completing all operations
     * @param failCallback    async callback that is called if any operation failed
     */
    public void handleAddBook(
            OnSuccessCallbackWithMessage<Book> successCallback, OnFailCallbackWithMessage<BOOK_ERROR> failCallback
    ) {
        final String isbn = bookIsbn.getValue();
        final String title = bookTitle.getValue();
        final String author = bookAuthor.getValue();
        final String description = ""; // we don't have description. TODO: delete description field in book entity too.
        final Uri imageUri = localImageUri.getValue();

        if (imageUri != null) {
            bookRepository.addImage(currentUsername, imageUri).addOnSuccessListener(imageUrl -> {
                bookRepository.add(isbn, title, author, description, currentUsername, imageUrl).addOnSuccessListener(
                        successCallback::run
                ).addOnFailureListener(e -> {
                    failCallback.run(BOOK_ERROR.FAIL_TO_ADD_BOOK);
                });
            });
        } else {
            bookRepository.add(isbn, title, author, description, currentUsername, null).addOnSuccessListener(
                    successCallback::run
            ).addOnFailureListener(e -> {
                failCallback.run(BOOK_ERROR.FAIL_TO_ADD_BOOK);
            });
        }
    }

    /**
     * handles the edit book functionality when user clicks the "Save" button in AddEditActivity
     * edit the book to the "book" collection and the image to FireBase Cloud Storage(if there is an image)
     *
     * @param successCallback async callback that is called upon successfully completing all operations
     * @param failCallback    async callback that is called if any operation failed
     */
    public void handleEditBook(
            OnSuccessCallbackWithMessage<Book> successCallback, OnFailCallbackWithMessage<BOOK_ERROR> failCallback
    ) {
        final String isbn = bookIsbn.getValue();
        final String title = bookTitle.getValue();
        final String author = bookAuthor.getValue();
        final String description = "";
        final Uri imageUri = localImageUri.getValue();

        // 3 image cases:
        // 1. Update local image with a new image
        // 2. Update local image with no image (book previously have image, but we removed it)
        // 3. Did not touch image, keep old image
        if (hasNewImage) {
            if (imageUri != null) {     // if we have new image, we upload it to database then edit book
                bookRepository.addImage(currentUsername, imageUri).addOnSuccessListener(imageUrl -> {
                    bookRepository.editBook(oldBook, isbn, title, author, description, imageUrl).addOnSuccessListener(
                            successCallback::run
                    ).addOnFailureListener(e -> {
                        failCallback.run(BOOK_ERROR.FAIL_TO_EDIT_BOOK);
                    });
                });
            } else {    // if we removed the book image, we update the book with no image url
                bookRepository.editBook(oldBook, isbn, title, author, description, null).addOnSuccessListener(
                        successCallback::run
                ).addOnFailureListener(e -> {
                    failCallback.run(BOOK_ERROR.FAIL_TO_EDIT_BOOK);
                });
            }
        } else {        // if we didn't update the book image, we use the old book image url
            bookRepository.editBook(oldBook, isbn, title, author, description, oldBook.getImageUrl()).addOnSuccessListener(
                    successCallback::run
            ).addOnFailureListener(e -> {
                failCallback.run(BOOK_ERROR.FAIL_TO_EDIT_BOOK);
            });
        }
    }
}
