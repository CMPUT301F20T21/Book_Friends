package com.cmput301f20t21.bookfriends.fakes.repositories;

import android.net.Uri;

import com.cmput301f20t21.bookfriends.repositories.api.IBookRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FakeBookRepository implements IBookRepository {

    @Override
    public Task<Void> add(String isbn, String title, String author, String description, String owner) {
        return null;
    }

    @Override
    public Task<Void> editBook(String bookId, String isbn, String title, String author, String description) {
        return null;
    }

    @Override
    public Task<Void> delete(String id) {
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
    public Task<DocumentSnapshot> getBookById(String bookId) {
        return null;
    }

    @Override
    public Task<QuerySnapshot> batchGetBooks(List<String> bookIds) {
        return null;
    }
}
