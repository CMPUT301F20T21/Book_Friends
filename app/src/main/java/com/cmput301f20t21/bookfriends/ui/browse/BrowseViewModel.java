package com.cmput301f20t21.bookfriends.ui.browse;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.AvailableBook;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
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

    // production
    public BrowseViewModel() {
        this(AuthRepository.getInstance(), RequestRepository.getInstance(), BookRepository.getInstance());
    }

    // dependency injection for unit test
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
        // get logged in user's username
        String username = authRepository.getCurrentUser().getUsername();
        // first, get all the request made by this user.
        requestRepository.getAllRequestsByUsername(username).addOnSuccessListener(requests -> {
            // extract book id from fetched requests
            // book ids will be unique because each user can only request a book once at a time
            List<String> requestedBookIds = requests
                    .stream()
                    .map(request -> request.getBookId())
                    .collect(Collectors.toList());
            // get available book for current user (book in available status and not owned by this user)
            bookRepository.getAvailableBooksForUser(username).addOnSuccessListener(availableBooks -> {
                // check if available book has been requested
                availableBooks
                        .forEach(availableBook -> {
                            if (requestedBookIds.indexOf(availableBook.getId()) != -1) {
                                availableBook.setRequested(true);
                            }
                        });
                // insert into local data
                bookData.clear();
                bookData.addAll(availableBooks);
                books.setValue(bookData);
            }).addOnFailureListener(e -> errorMessage.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS));
        }).addOnFailureListener(e -> errorMessage.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS));
    }
}
