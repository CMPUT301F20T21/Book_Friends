/*
 * BorrowedViewModel.java
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
import com.cmput301f20t21.bookfriends.repositories.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.RequestRepository;
import com.cmput301f20t21.bookfriends.repositories.api.IAuthRepository;
import com.cmput301f20t21.bookfriends.repositories.api.IRequestRepository;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The ViewModel for BorrowedListFragment
 */
public class BorrowedViewModel extends ViewModel {
    private IRequestRepository requestRepository = RequestRepository.getInstance();
    private BookRepository bookRepository = BookRepository.getInstance();
    private IAuthRepository authRepository = AuthRepository.getInstance();

    private MutableLiveData<List<Book>> books = new MutableLiveData<>(new ArrayList<Book>());
    private List<Book> bookData = books.getValue();
    private MutableLiveData<Integer> updatedPosition;

    public BorrowedViewModel() {
        fetchBooks();
    }

    /**
     * get the list of books from MutableLiveData
     * @return a list of books
     */
    public LiveData<List<Book>> getBooks() {
        return books;
    }

    /**
     * get the latest updated position of the book
     * @return the updated position
     */
    public LiveData<Integer> getUpdatedPosition() {
        if (updatedPosition == null) {
            updatedPosition = new MutableLiveData<>(0);
        }
        return updatedPosition;
    }
    public Book getBookByIndex(Integer index) {
        return bookData.get(index);
    }
    private void fetchBooks() {
        String username = authRepository.getCurrentUser().getUsername();

        requestRepository.getBorrowedRequestByUsername(username).addOnSuccessListener(requestDocumentSnapshots -> {
            // TODO: uncomment when request is implemented
//            List<String> bookIds = requestDocumentSnapshots.getDocuments().stream()
//                    .map(documentSnapshot -> documentSnapshot.get("bookId").toString())
//                    .collect(Collectors.toList());

            // mock book id
            // TODO: remove this when request is implemented
            List<String> bookIds = new ArrayList<>();
            bookIds.add("RmJi5i1sav1B4fKQcNHP");
            bookIds.add("asmw39EGwG2MDDPokcd7");
            bookIds.add("x1z6o0qZbcgGBFezURsS");
            bookIds.add("kEkNn53bBoANMSyPjJDZ");

            if (bookIds.isEmpty()) {
                return;
            }

            bookRepository.batchGetBooks(bookIds).addOnSuccessListener(borrowedBooks -> {
                bookData.clear();
                bookData.addAll(borrowedBooks);
                books.setValue(bookData);
            }).addOnFailureListener(e -> {
                // TODO: handle failure here
            });
        });
    }
}