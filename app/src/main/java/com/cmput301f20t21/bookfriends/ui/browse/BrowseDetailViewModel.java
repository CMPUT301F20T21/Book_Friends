package com.cmput301f20t21.bookfriends.ui.browse;

import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.callbacks.OnFailCallback;
import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallback;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.repositories.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.RequestRepository;
import com.cmput301f20t21.bookfriends.repositories.api.IAuthRepository;
import com.cmput301f20t21.bookfriends.repositories.api.IRequestRepository;

public class BrowseDetailViewModel extends ViewModel {
    private final IRequestRepository requestRepository;
    private final String currentUsername;

    public BrowseDetailViewModel() {
        this(AuthRepository.getInstance(), RequestRepository.getInstance());
    }

    public BrowseDetailViewModel(IAuthRepository authRepository, IRequestRepository requestRepository) {
        this.requestRepository = requestRepository;
        currentUsername = authRepository.getCurrentUser().getUsername();
    }

    public void sendRequest(Book book, OnSuccessCallback successCallback, OnFailCallback failCallback) {
        requestRepository
                .sendRequest(currentUsername, book.getId())
                .addOnSuccessListener(requestId -> successCallback.run())
                .addOnFailureListener(e -> failCallback.run());
    }
}
