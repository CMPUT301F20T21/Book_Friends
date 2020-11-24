package com.cmput301f20t21.bookfriends.ui.library.owned;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.cmput301f20t21.bookfriends.enums.SCAN_ERROR;
import com.cmput301f20t21.bookfriends.repositories.api.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.api.RequestRepository;
import com.cmput301f20t21.bookfriends.repositories.impl.BookRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.impl.RequestRepositoryImpl;

import java.util.Arrays;

public class BorrowedOwnedDetailViewModel extends ViewModel {
    private RequestRepository requestRepository;
    private BookRepository bookRepository;

    private final MutableLiveData<Request> request = new MutableLiveData<>();
    private final MutableLiveData<SCAN_ERROR> errorMessage = new MutableLiveData<>();

    public BorrowedOwnedDetailViewModel() {
        this(RequestRepositoryImpl.getInstance(), BookRepositoryImpl.getInstance());
    }

    public BorrowedOwnedDetailViewModel(RequestRepository requestRepository, BookRepository bookRepository) {
        this.requestRepository = requestRepository;
        this.bookRepository = bookRepository;
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
                .getRequestsByBookIdAndStatus(bookId, Arrays.asList(REQUEST_STATUS.RETURNING,REQUEST_STATUS.CLOSED))
                .addOnSuccessListener(requests -> request.setValue(requests.get(0)))
                .addOnFailureListener(e -> errorMessage.setValue(SCAN_ERROR.UNEXPECTED));
    }

    public void handleScannedIsbn(Book currentBook, String currentIsbn, String scannedIsbn) {
        if (currentIsbn.equals(scannedIsbn)) {
            requestRepository.updateRequestStatus(request.getValue(), REQUEST_STATUS.CLOSED)
                    .addOnSuccessListener(updatedRequest -> {
                        bookRepository.updateBookStatus(currentBook, BOOK_STATUS.AVAILABLE)
                                .addOnSuccessListener(updatedBook -> {
                                    request.setValue(updatedRequest);
                                })
                                .addOnFailureListener(e -> errorMessage.setValue(SCAN_ERROR.UNEXPECTED));
                    })
                    .addOnFailureListener(e -> errorMessage.setValue(SCAN_ERROR.UNEXPECTED));
        } else {
            errorMessage.setValue(SCAN_ERROR.INVALID_ISBN);
        }
    }
}
