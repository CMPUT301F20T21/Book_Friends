package com.cmput301f20t21.bookfriends.ui.library;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.services.AuthService;
import com.cmput301f20t21.bookfriends.services.BookService;
import com.cmput301f20t21.bookfriends.services.RequestService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BorrowedViewModel extends ViewModel {
    private RequestService requestService = RequestService.getInstance();
    private BookService bookService = BookService.getInstance();
    private AuthService authService = AuthService.getInstance();

    private MutableLiveData<List<Book>> books;


    public LiveData<List<Book>> getBooks() {
        if (books == null) {
            books = new MutableLiveData<>();
            fetchBooks();
        }
        return books;
    }

    private void fetchBooks() {
        String username = authService.getCurrentUser().getUsername();
        requestService.getBorrowedRequestByUsername(username).addOnSuccessListener(requestDocumentSnapshots -> {
            List<String> bookIds = requestDocumentSnapshots.getDocuments().stream()
                    .map(documentSnapshot -> documentSnapshot.get("bookId").toString())
                    .collect(Collectors.toList());
            bookService.batchGetBooks(bookIds).addOnSuccessListener(bookDocumentsSnapshots -> {
                bookDocumentsSnapshots.getDocuments().stream().map(documentSnapshot -> {
                    return documentSnapshot;
                }).collect(Collectors.toList());
            });

        });
    }

}