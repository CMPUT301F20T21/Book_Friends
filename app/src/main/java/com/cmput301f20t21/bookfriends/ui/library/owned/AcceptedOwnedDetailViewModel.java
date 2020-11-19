package com.cmput301f20t21.bookfriends.ui.library.owned;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.cmput301f20t21.bookfriends.enums.SCAN_ERROR;
import com.cmput301f20t21.bookfriends.repositories.api.RequestRepository;
import com.cmput301f20t21.bookfriends.repositories.impl.RequestRepositoryImpl;

import java.util.Arrays;

public class AcceptedOwnedDetailViewModel extends ViewModel {
    private RequestRepository requestRepository;

    private final MutableLiveData<Request> request = new MutableLiveData<>();
    private final MutableLiveData<SCAN_ERROR> errorMessage = new MutableLiveData<>();

    public AcceptedOwnedDetailViewModel() {
        this(RequestRepositoryImpl.getInstance());
    }

    public AcceptedOwnedDetailViewModel(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public LiveData<Request> getRequest(Book book) {
        fetchRequest(book.getId());
        return request;
    }

    public MutableLiveData<SCAN_ERROR> getErrorMessage() {
        return errorMessage;
    }

    private void fetchRequest(String bookId) {
        requestRepository
                .getRequestByBookIdAndStatus(bookId, Arrays.asList(REQUEST_STATUS.ACCEPTED, REQUEST_STATUS.HANDING))
                .addOnSuccessListener(requests -> request.setValue(requests.get(0)))
                .addOnFailureListener(e -> errorMessage.setValue(SCAN_ERROR.UNEXPECTED));
    }

    public void handleScannedIsbn(String currentIsbn, String scannedIsbn) {
        if (currentIsbn.equals(scannedIsbn)) {
            requestRepository.updateRequestStatus(request.getValue(), REQUEST_STATUS.HANDING)
                    .addOnSuccessListener(updatedRequest -> request.setValue(updatedRequest))
                    .addOnFailureListener(e -> errorMessage.setValue(SCAN_ERROR.UNEXPECTED));
        } else {
            errorMessage.setValue(SCAN_ERROR.INVALID_ISBN);
        }
    }
}
