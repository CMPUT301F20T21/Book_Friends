package com.cmput301f20t21.bookfriends.ui.library;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.services.AuthService;
import com.cmput301f20t21.bookfriends.services.BookService;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageException;

import java.util.ArrayList;

public class OwnedViewModel extends ViewModel {
    public interface OnSuccessCallback {
        void run(ArrayList<Book> books);
    }

    public interface OnGetImageSuccessCallback {
        void run(Book book, Uri imageUri);
    }

    public interface OnFailCallback {
        void run();
    }

    private final AuthService authService = AuthService.getInstance();
    private final BookService bookService = BookService.getInstance();

    public void getBooks(OnSuccessCallback successCallback, OnFailCallback failCallback) {
        String currentUsername = authService.getCurrentUser().getUsername();
        bookService.getBooksOfOwnerId(currentUsername).addOnCompleteListener(
                getBookTask -> {
                    if (getBookTask.isSuccessful()) {
                        QuerySnapshot result = getBookTask.getResult();
                        ArrayList<Book> books = bookService.getBooksOnResult(result);
                        successCallback.run(books);
                    } else {
                        failCallback.run();
                    }
                }
        );
    }

    public void getBookImage(Book book, OnGetImageSuccessCallback successCallback) {
        bookService.getImage(book.getId()).addOnCompleteListener(
                getImageTask -> {
                    if (getImageTask.isSuccessful()) {
                        Uri imageUri = getImageTask.getResult();
                        successCallback.run(book, imageUri);
                    } else {
                        Exception error = getImageTask.getException();
                        if (error instanceof StorageException &&
                                ((StorageException) error).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND
                        ) {
                            // book image does not exist
                        }
                    }
                }
        );

    }

}