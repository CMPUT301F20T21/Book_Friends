package com.cmput301f20t21.bookfriends.ui.browse;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
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
import java.util.function.Function;
import java.util.stream.Collectors;

public class BrowseViewModel extends ViewModel {
    // exposed live data
    private final MutableLiveData<BOOK_ERROR> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<AvailableBook>> books = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>(""); // the updated search query we are using
    // the updated/filtered/calculated book results based on changed books and/or searchQuery
    private final MediatorLiveData<List<AvailableBook>> searchedBooks = new MediatorLiveData<>();

    // raw book data
    private final List<AvailableBook> bookData = books.getValue();
    // need this reference to keep adapter referring to the same array in memory or else it won't update
    private final List<AvailableBook> searchedBookData = new ArrayList<>();

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

        setupSearchedBooks(); // init the searched books before we fetch anything
        fetchBooks();
    }

    /**
     * create the searched books live data by making it listens to two source of changes:
     * 1. the original books list: any changes of the books will trigger it to re-calculate results
     * 2. the search keyword/query: changes of query should filter the searched books, too
     */
    private void setupSearchedBooks() {
        searchedBooks.setValue(searchedBookData);
        searchedBooks.addSource(searchQuery, keyword -> setSearchedBooksByKeywordAndBookData(keyword, bookData)); // keyword is new
        searchedBooks.addSource(books, bookData -> setSearchedBooksByKeywordAndBookData(searchQuery.getValue(), bookData)); // bookData is new (this is a local one!)
    }

    /**
     * filter the original bookData (or the updated bookData) with the keyword (or the updated one)
     * and set that value to searchedBookData and its live data wrapper
     *
     * @param keyword
     * @param bookData
     */
    private void setSearchedBooksByKeywordAndBookData(String keyword, List<AvailableBook> bookData) {
        Function<String, Boolean> contains = ifNotNullAndContains(keyword);
        List<AvailableBook> newBookData = bookData
                .stream()
                .filter(book -> contains.apply(book.getTitle()) ||
                        contains.apply(book.getAuthor()) ||
                        contains.apply(book.getOwner()))
                .collect(Collectors.toList());
        searchedBookData.clear();
        searchedBookData.addAll(newBookData);
        searchedBooks.setValue(searchedBookData);
    }

    private Function<String, Boolean> ifNotNullAndContains(String keyword) {
        return (attribute) -> {
            if (attribute == null || keyword == null) return true;
            return attribute.contains(keyword);
        };
    }

    public LiveData<List<AvailableBook>> getBooks() {
        return searchedBooks;
    }

    public LiveData<BOOK_ERROR> getErrorMessage() {
        return errorMessage;
    }

    /**
     * US 03.01.01
     * As a borrower, I want to specify a keyword, and search for all books that are not currently accepted or borrowed whose **description** contains the keyword.
     * NOTE: description here interpreted as all the basic information of the book: title, author, owner
     *
     * @param keyword the keyword to search with
     */
    public void filterBookWithKeyword(String keyword) {
        searchQuery.setValue(keyword); // will trigger update routine for searchedBooks
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
