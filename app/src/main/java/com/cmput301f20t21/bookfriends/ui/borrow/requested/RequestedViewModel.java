package com.cmput301f20t21.bookfriends.ui.borrow.requested;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.cmput301f20t21.bookfriends.repositories.factories.AuthRepositoryFactory;
import com.cmput301f20t21.bookfriends.repositories.factories.BookRepositoryFactory;
import com.cmput301f20t21.bookfriends.repositories.impl.AuthRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.impl.BookRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.impl.RequestRepositoryImpl;
import com.cmput301f20t21.bookfriends.repositories.api.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.api.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.api.RequestRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RequestedViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final BookRepository bookRepository;
    private final RequestRepository requestRepository;


    private final MutableLiveData<List<Book>> books = new MutableLiveData<>(new ArrayList<>());
    private final List<Book> bookData = books.getValue();
    private final MutableLiveData<BOOK_ERROR> errorMessage = new MutableLiveData<>();

    // production
    public RequestedViewModel() {
        this(AuthRepositoryFactory.getRepository(), BookRepositoryFactory.getRepository(), RequestRepositoryImpl.getInstance());
    }

    // test
    public RequestedViewModel(AuthRepository authRepository, BookRepository bookRepository, RequestRepository requestRepository){
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
    
    public Book getBookByIndex(Integer index) {
        return bookData.get(index);
    }
  
    private void fetchBooks() {
        String username = authRepository.getCurrentUser().getUsername();
        requestRepository.getRequestsByUsernameAndStatus(username, Arrays.asList(REQUEST_STATUS.OPENED)).addOnSuccessListener(requests -> {
            List<String> requestedBookIds = requests
                    .stream()
                    .map(Request::getBookId)
                    .collect(Collectors.toList());
            if (requestedBookIds.size() > 0) {
                bookRepository.batchGetBooks(requestedBookIds).addOnSuccessListener(requestedBooks -> {
                    if (bookData != null) {
                        bookData.clear();
                        bookData.addAll(requestedBooks);
                        books.setValue(bookData);
                    }
                }).addOnFailureListener(error -> errorMessage.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS));
            }
        }).addOnFailureListener(error -> errorMessage.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS));
    }  

}