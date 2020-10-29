package com.cmput301f20t21.bookfriends.ui.library;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallbackWithMessage;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.services.AuthService;
import com.cmput301f20t21.bookfriends.services.BookService;
import com.cmput301f20t21.bookfriends.services.RequestService;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class BorrowedViewModel extends ViewModel {
    private RequestService requestService = RequestService.getInstance();
    private BookService bookService = BookService.getInstance();
    private AuthService authService = AuthService.getInstance();

    private MutableLiveData<List<Book>> books;
    private MutableLiveData<Integer> updatedPosition;

    public LiveData<List<Book>> getBooks() {
        if (books == null) {
            books = new MutableLiveData<>();
            fetchBooks();
        }
        return books;
    }

    public LiveData<Integer> getUpdatedPosition() {
        if (updatedPosition == null) {
            updatedPosition = new MutableLiveData<>(0);
        }
        return updatedPosition;
    }

    private void fetchBooks() {
        String username = authService.getCurrentUser().getUsername();

        requestService.getBorrowedRequestByUsername(username).addOnSuccessListener(requestDocumentSnapshots -> {
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

            bookService.batchGetBooks(bookIds).addOnSuccessListener(bookDocumentsSnapshots -> {
                List<DocumentSnapshot> documents = bookDocumentsSnapshots.getDocuments();
                books.setValue(IntStream.range(0, documents.size()).mapToObj(i -> {
                    DocumentSnapshot document = documents.get(i);
                    Book book = bookService.getBookFromDocument(document);
                    if (document.get("imageName") != null) {
                        bookService.getImage((String) document.get("imageName")).addOnSuccessListener(uri -> {
                            book.setImageUri(uri);
                            updatedPosition.setValue(i);
                        });
                    }
                    return book;
                }).collect(Collectors.toList()));
            }).addOnFailureListener(e -> {
                // TODO: handle failure here
            });
        });
    }
}