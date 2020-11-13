package com.cmput301f20t21.bookfriends.ui.borrow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.cmput301f20t21.bookfriends.repositories.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.RequestRepository;
import com.cmput301f20t21.bookfriends.repositories.api.IAuthRepository;
import com.cmput301f20t21.bookfriends.repositories.api.IBookRepository;
import com.cmput301f20t21.bookfriends.repositories.api.IRequestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RequestedViewModel extends ViewModel {
    private final IAuthRepository authRepository;
    private final IBookRepository bookRepository;
    private final IRequestRepository requestRepository;


    private final MutableLiveData<List<Book>> books = new MutableLiveData<>(new ArrayList<>());
    private final List<Book> bookData = books.getValue();
    private final MutableLiveData<BOOK_ERROR> errorMessage = new MutableLiveData<>();

    // production
    public RequestedViewModel() {
        this(AuthRepository.getInstance(), BookRepository.getInstance(), RequestRepository.getInstance());
    }

    // test
    public RequestedViewModel(IAuthRepository authRepository, IBookRepository bookRepository, IRequestRepository requestRepository){
        this.authRepository = authRepository;
        this.bookRepository = bookRepository;
        this.requestRepository = requestRepository;

        fetchBooks();
    }

    public LiveData<List<Book>> getBooks() {
        return books;
    }

    public MutableLiveData<BOOK_ERROR> getErrorMessage() {
        return errorMessage;
    }

    private void fetchBooks() {
        String username = authRepository.getCurrentUser().getUsername();
        requestRepository.getAllRequestsByUsername(username, REQUEST_STATUS.OPENED).addOnSuccessListener(requests -> {
            List<String> requestedBookIds = requests
                    .stream()
                    .map(Request::getBookId)
                    .collect(Collectors.toList());
            bookRepository.batchGetBooks(requestedBookIds).addOnSuccessListener(requestedBooks -> {
                bookData.clear();
                bookData.addAll(requestedBooks);
                books.setValue(bookData);
            }).addOnFailureListener(error -> errorMessage.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS));
        }).addOnFailureListener(error -> errorMessage.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS));
    }

}