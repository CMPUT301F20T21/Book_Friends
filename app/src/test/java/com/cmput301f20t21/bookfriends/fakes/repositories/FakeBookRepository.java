package com.cmput301f20t21.bookfriends.fakes.repositories;

import android.net.Uri;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.repositories.api.BookRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FakeBookRepository implements BookRepository {
    @Override
    public Task<Book> add(String isbn, String title, String author, String description, String owner, String imageUrl) {
        return null;
    }

    @Override
    public Task<String> addImage(String bookId, Uri imageUri) {
        return null;
    }

    @Override
    public Task<Book> editBook(Book oldBook, String isbn, String title, String author, String description, String imageUrl) {
        return null;
    }

    @Override
    public Task<Void> delete(String id) {
        return null;
    }

    @Override
    public Task<Void> deleteImage(String imageName) {
        return null;
    }

    @Override
    public Task<List<Book>> getBooksOfOwnerId(String userName) {
        return null;
    }

    @Override
    public Task<Book> getBookById(String bookId) {
        return null;
    }

    @Override
    public Task<List<Book>> batchGetBooks(List<String> bookIds) {
        return null;
    }

    @Override
    public Task<List<Book>> getAvailableBooksForUser(String username) {
        return null;
    }

    @Override
    public Task<QuerySnapshot> getDocumentBy(String isbn, String title, String author) {
        return null;
    }

    @Override
    public Task<Book> updateBookStatus(Book book, BOOK_STATUS newStatus) {
        return null;
    }
}
