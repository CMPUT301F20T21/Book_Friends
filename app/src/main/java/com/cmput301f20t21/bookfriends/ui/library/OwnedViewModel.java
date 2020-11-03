package com.cmput301f20t21.bookfriends.ui.library;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.repositories.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.BookRepository;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OwnedViewModel extends ViewModel {

    private final AuthRepository authRepository = AuthRepository.getInstance();
    private final BookRepository bookRepository = BookRepository.getInstance();

    private final MutableLiveData<List<Book>> books = new MutableLiveData<>(new ArrayList<>());
    private final List<Book> bookData = books.getValue();

    private MutableLiveData<Integer> updatedPosition = new MutableLiveData<>(0);
    private MutableLiveData<BOOK_ERROR> errorMessageObserver = new MutableLiveData<>();

    public OwnedViewModel() {
        fetchBooks();
    }

    public LiveData<List<Book>> getBooks() {
        return books;
    }

    public LiveData<Integer> getUpdatedPosition() {
        return updatedPosition;
    }

    public LiveData<BOOK_ERROR> getErrorMessageObserver() {
        return errorMessageObserver;
    }

    public Book getBookByIndex(Integer index) {
        return bookData.get(index);
    }

    public void addBook(Book book) {
        bookData.add(book);
        books.setValue(bookData);
    }

    public void updateBook(Book oldBook, Book updatedBook) {
        Integer indexToUpdate = bookData.indexOf(oldBook);
        if (indexToUpdate != -1) {
            bookData.set(indexToUpdate, updatedBook);
            books.setValue(bookData);
        } else {
            errorMessageObserver.setValue(BOOK_ERROR.FAIL_TO_EDIT_BOOK);
        }
    }

    public void deleteBook(Book book) {
        bookRepository.delete(book.getId())
                .addOnSuccessListener(
                        res -> {
                            if (book.getImageUri() != null) {
                                bookRepository.deleteImage(book.getId());
                            }
                            bookData.remove(book);
                            books.setValue(bookData);
                        })
                .addOnFailureListener(e -> errorMessageObserver.setValue(BOOK_ERROR.FAIL_TO_DELETE_BOOK));
    }

    private void fetchBooks() {
        String currentUsername = authRepository.getCurrentUser().getUsername();
        bookRepository.getBooksOfOwnerId(currentUsername)
                .addOnSuccessListener(
                        result -> {
                            if (result == null) { // should never happen
                                errorMessageObserver.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS);
                                return;
                            }
                            List<DocumentSnapshot> documents = result.getDocuments();
                            bookData.addAll(IntStream.range(0, documents.size()).mapToObj(i -> {
                                DocumentSnapshot document = documents.get(i);
                                Book book = bookRepository.getBookFromDocument(document);
                                if (document.get("imageName") != null) {
                                    bookRepository.getImage((String) document.get("imageName")).addOnSuccessListener(uri -> {
                                        book.setImageUri(uri);
                                        updatedPosition.setValue(i);
                                    });
                                }
                                return book;
                            }).collect(Collectors.toList()));
                            books.setValue(bookData);
                        })
                .addOnFailureListener(e -> errorMessageObserver.setValue(BOOK_ERROR.FAIL_TO_GET_BOOKS));
    }

}