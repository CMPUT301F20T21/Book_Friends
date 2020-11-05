package com.cmput301f20t21.bookfriends.repositories.api;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public interface IBookRepository {
    Task<Void> add(String isbn, String title, String author, String description, String owner);
    Task<Void> editBook(String bookId, String isbn, String title, String author, String description);
    Task<Void> delete(String id);
    Task<Uri> getImage(String imageName);
    Task<QuerySnapshot> getBooksOfOwnerId(String userName);
    Task<DocumentSnapshot> getBookById(String bookId);
    Task<QuerySnapshot> batchGetBooks( List<String> bookIds);
}
