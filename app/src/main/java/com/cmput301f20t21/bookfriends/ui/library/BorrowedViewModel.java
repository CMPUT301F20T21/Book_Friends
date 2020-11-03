package com.cmput301f20t21.bookfriends.ui.library;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.repositories.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.RequestRepository;
import com.cmput301f20t21.bookfriends.repositories.api.IAuthRepository;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class BorrowedViewModel extends ViewModel {
    private RequestRepository requestRepository = RequestRepository.getInstance();
    private BookRepository bookRepository = BookRepository.getInstance();
    private IAuthRepository authRepository = AuthRepository.getInstance();

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

            bookRepository.batchGetBooks(bookIds).addOnSuccessListener(bookDocumentsSnapshots -> {
                List<DocumentSnapshot> documents = bookDocumentsSnapshots.getDocuments();
                books.setValue(IntStream.range(0, documents.size()).mapToObj(i -> {
                    DocumentSnapshot document = documents.get(i);
                    Book book = bookRepository.getBookFromDocument(document);
                    if (document.get("imageName") != null) {
                        bookRepository.getImage((String) document.get("imageName")).addOnSuccessListener(uri -> {
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