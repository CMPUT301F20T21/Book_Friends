/*
 * OwnedViewModel.java
 * Version: 1.0
 * Date: November 4, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */


package com.cmput301f20t21.bookfriends.ui.library;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.repositories.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.api.IAuthRepository;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The ViewModel for OwnedListFragment
 */
public class OwnedViewModel extends ViewModel {

    private final IAuthRepository authRepository = AuthRepository.getInstance();
    private final BookRepository bookRepository = BookRepository.getInstance();

    private final MutableLiveData<List<Book>> books = new MutableLiveData<>(new ArrayList<>());
    private final List<Book> bookData = books.getValue();

    private MutableLiveData<Integer> updatedPosition = new MutableLiveData<>(0);
    private MutableLiveData<BOOK_ERROR> errorMessageObserver = new MutableLiveData<>();

    public OwnedViewModel() {
        fetchBooks();
    }

    /**
     * get a list of books
     * @return a list of book
     */
    public LiveData<List<Book>> getBooks() {
        return books;
    }

    /**
     * get the updated position
     * @return the updated position
     */
    public LiveData<Integer> getUpdatedPosition() {
        return updatedPosition;
    }

    /**
     * get the error message observer
     * @return the error message observer
     */
    public LiveData<BOOK_ERROR> getErrorMessageObserver() {
        return errorMessageObserver;
    }

    /**
     * get a book object by list index
     * @param index the index of the book to get
     * @return the book at the specified index
     */
    public Book getBookByIndex(Integer index) {
        return bookData.get(index);
    }

    /**
     * add a book to the list
     * @param book the book to add
     */
    public void addBook(Book book) {
        bookData.add(book);
        books.setValue(bookData);
    }

    /**
     * update a book that is in the list
     * @param oldBook the old book to be updated
     * @param updatedBook the new book to update
     */
    public void updateBook(Book oldBook, Book updatedBook) {
        Integer indexToUpdate = bookData.indexOf(oldBook);
        if (indexToUpdate != -1) {
            bookData.set(indexToUpdate, updatedBook);
            books.setValue(bookData);
        } else {
            errorMessageObserver.setValue(BOOK_ERROR.FAIL_TO_EDIT_BOOK);
        }
    }

    /**
     * delete a book locally and from firestore
     * @param book the book to delete
     */
    public void deleteBook(Book book) {
        bookRepository.delete(book.getId())
                .addOnSuccessListener(
                        res -> {
                            bookRepository.deleteImage(book.getCoverImageName());
                            bookData.remove(book);
                            books.setValue(bookData);
                        })
                .addOnFailureListener(e -> errorMessageObserver.setValue(BOOK_ERROR.FAIL_TO_DELETE_BOOK));
    }

    /**
     * fetch a list of book that belongs to current user
     */
    private void fetchBooks() {
        String currentUsername = authRepository.getCurrentUser().getUsername();
        bookRepository.getBooksOfOwnerId(currentUsername)
                .addOnSuccessListener(
                        result -> {
                            if (result == null) { // should never happen
                                errorMessageObserver.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS);
                                return;
                            }
                            List<DocumentSnapshot> documents = result.getDocuments();
                            bookData.clear();
                            bookData.addAll(
                                    documents.stream()
                                            .map(document -> document.toObject(Book.class))
                                            .collect(Collectors.toList())
                            );
                            books.setValue(bookData);
                        })
                .addOnFailureListener(e -> errorMessageObserver.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS));
    }

}