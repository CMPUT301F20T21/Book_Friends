package com.cmput301f20t21.bookfriends.ui.browse;

import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.callbacks.OnFailCallback;
import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallback;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.repositories.api.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.impl.AuthRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.impl.BookRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.impl.RequestRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.api.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.api.RequestRepository;

public class BrowseDetailViewModel extends ViewModel {
    private final BookRepository bookRepository;
    private final RequestRepository requestRepository;
    private final String currentUsername;

    public BrowseDetailViewModel() {
        this(AuthRepositoryImpl.getInstance(), RequestRepositoryImpl.getInstance(), BookRepositoryImpl.getInstance());
    }

    public BrowseDetailViewModel(
            AuthRepository authRepository,
            RequestRepository requestRepository,
            BookRepository bookRepository
    ) {
        this.bookRepository = bookRepository;
        this.requestRepository = requestRepository;
        currentUsername = authRepository.getCurrentUser().getUsername();
    }

    public void sendRequest(Book book, OnSuccessCallback successCallback, OnFailCallback failCallback) {
        requestRepository
                .sendRequest(currentUsername, book.getId())
                .addOnSuccessListener(requestId -> {
                    if (book.getStatus() == BOOK_STATUS.AVAILABLE) {
                        bookRepository.updateBookStatus(book.getId(), BOOK_STATUS.REQUESTED)
                                .addOnSuccessListener(aVoid -> successCallback.run())
                                .addOnFailureListener(e -> failCallback.run());
                    } else {
                        successCallback.run();
                    }
                })
                .addOnFailureListener(e -> failCallback.run());
    }
}
