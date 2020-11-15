package com.cmput301f20t21.bookfriends.fakes.repositories;

import android.net.Uri;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.repositories.api.IBookRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FakeBookRepository implements IBookRepository {

    @Override
    public Task<String> add(String isbn, String title, String author, String description, String owner) {
        return null;
    }

    @Override
    public Task<String> addImage(String bookId, Uri imageUri) {
        return null;
    }

    @Override
    public Task<Book> editBook(Book oldBook, String isbn, String title, String author, String description) {
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
    public Book getBookFromDocument(DocumentSnapshot document) {
        return null;
    }

    @Override
    public Task<Uri> getImage(String imageName) {
        return null;
    }

    @Override
    public Task<QuerySnapshot> getBooksOfOwnerId(String userName) {
        return null;
    }

    @Override
    public Task<QuerySnapshot> getBookOfBorrowerId(String uid) {
        return null;
    }

    @Override
    public Task<DocumentSnapshot> getBookById(String bookId) {
        return null;
    }

    @Override
    public Task<QuerySnapshot> batchGetBooks(List<String> bookIds) {
        return null;
    }

    @Override
    public Task<QuerySnapshot> getAvailableBooks() {
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
}
