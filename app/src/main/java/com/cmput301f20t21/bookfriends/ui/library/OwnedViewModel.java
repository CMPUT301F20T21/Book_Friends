package com.cmput301f20t21.bookfriends.ui.library;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.callbacks.OnFailCallback;
import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallbackWithMessage;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.services.AuthService;
import com.cmput301f20t21.bookfriends.services.BookService;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OwnedViewModel extends ViewModel {
  
    private final AuthService authService = AuthService.getInstance();
    private final BookService bookService = BookService.getInstance();
    
    private MutableLiveData<List<Book>> books;
    private MutableLiveData<Integer> updatedPosition;
    private MutableLiveData<BOOK_ERROR> errorMessageObserver;

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

    public LiveData<BOOK_ERROR> getErrorMessageObserver() {
        if (errorMessageObserver == null) {
            errorMessageObserver = new MutableLiveData<>();
        }
        return errorMessageObserver;
    }

    public void deleteBook(Book book, OnSuccessCallbackWithMessage<Book> successCallback, OnFailCallback failCallback) {
        bookService.delete(book.getId()).addOnCompleteListener(
                deleteBookTask -> {
                    if (deleteBookTask.isSuccessful()) {
                        Uri imageUri = book.getImageUri();
                        if (imageUri != null) {
                            bookService.deleteImage(book.getId()).addOnCompleteListener(
                                    deleteImageTask -> successCallback.run(book)
                            );
                        } else {
                            successCallback.run(book);
                        }
                    } else {
                        failCallback.run();
                    }
                }
        );
    }

    private void fetchBooks() {
        String currentUsername = authService.getCurrentUser().getUsername();
        bookService.getBooksOfOwnerId(currentUsername).addOnCompleteListener(
                getBookTask -> {
                    if (getBookTask.isSuccessful()) {
                        QuerySnapshot result = getBookTask.getResult();
                        if (result == null) { // should never happen
                            errorMessageObserver.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS);
                            return;
                        }
                        List<DocumentSnapshot> documents = result.getDocuments();
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
                    } else {
                        errorMessageObserver.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS);
                    }
                }
        );
    }

}