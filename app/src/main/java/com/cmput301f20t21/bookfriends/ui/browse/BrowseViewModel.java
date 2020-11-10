package com.cmput301f20t21.bookfriends.ui.browse;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.AvailableBook;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.repositories.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.RequestRepository;
import com.cmput301f20t21.bookfriends.repositories.api.IAuthRepository;
import com.cmput301f20t21.bookfriends.repositories.api.IBookRepository;
import com.cmput301f20t21.bookfriends.repositories.api.IRequestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BrowseViewModel extends ViewModel {
    private final MutableLiveData<List<AvailableBook>> books = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<BOOK_ERROR> errorMessage = new MutableLiveData<>();
    private final List<AvailableBook> bookData = books.getValue();
    private final IAuthRepository authRepository;
    private final IRequestRepository requestRepository;
    private final IBookRepository bookRepository;

    public BrowseViewModel() {
        this(AuthRepository.getInstance(), RequestRepository.getInstance(), BookRepository.getInstance());
    }

    public BrowseViewModel(IAuthRepository authRepository, IRequestRepository requestRepository, IBookRepository bookRepository) {
        this.authRepository = authRepository;
        this.requestRepository = requestRepository;
        this.bookRepository = bookRepository;

        fetchBooks();
    }

    public MutableLiveData<List<AvailableBook>> getBooks() {
        return books;
    }

    public MutableLiveData<BOOK_ERROR> getErrorMessage() {
        return errorMessage;
    }

    private void fetchBooks() {
        String username = authRepository.getCurrentUser().getUsername();
        requestRepository.getAllRequestsByUsername(username).addOnSuccessListener(requests -> {
            // use this to set local requested state
            Log.d("REQUEST COUNT:", String.valueOf(requests.size()));
            List<String> requestedBookIds = requests
                    .stream()
                    .map(request -> request.getBookId())
                    .collect(Collectors.toList());
            bookRepository.getAvailableBooksForUser(username).addOnSuccessListener(availableBooks -> {
                availableBooks
                        .forEach(availableBook -> {

                            if (requestedBookIds.indexOf(availableBook.getId()) != -1) {
                                availableBook.setRequested(true);
                            }
                        });
                bookData.clear();
                bookData.addAll(availableBooks);
                books.setValue(bookData);
            }).addOnFailureListener(e -> {
                        Log.d("ERROR INDEX:", "ERROR get book");
                        errorMessage.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS);
                    }
            );
        }).addOnFailureListener(e -> {
            Log.d("ERROR INDEX:", e.getMessage());
            errorMessage.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS);
        });
    }
}
