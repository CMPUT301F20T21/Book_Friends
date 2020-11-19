package com.cmput301f20t21.bookfriends.ui.browse;

import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.callbacks.OnFailCallback;
import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallback;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.repositories.api.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.api.RequestRepository;
import com.cmput301f20t21.bookfriends.repositories.impl.AuthRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.impl.RequestRepositoryImpl;
import com.cmput301f20t21.bookfriends.utils.NotificationSender;

public class BrowseDetailViewModel extends ViewModel {
    private final RequestRepository requestRepository;
    private final String currentUsername;

    public BrowseDetailViewModel() {
        this(AuthRepositoryImpl.getInstance(), RequestRepositoryImpl.getInstance());
    }

    public BrowseDetailViewModel(AuthRepository authRepository, RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
        currentUsername = authRepository.getCurrentUser().getUsername();
    }

    public void sendRequest(Book book, OnSuccessCallback successCallback, OnFailCallback failCallback) {
        requestRepository
                .sendRequest(currentUsername, book.getId())
                .addOnSuccessListener(requestId -> {
                    NotificationSender.getInstance().request(book, currentUsername,
                            res -> successCallback.run(),
                            err -> {
                                err.printStackTrace();
                                failCallback.run();
                            });
                })
                .addOnFailureListener(e -> failCallback.run());
    }
}
