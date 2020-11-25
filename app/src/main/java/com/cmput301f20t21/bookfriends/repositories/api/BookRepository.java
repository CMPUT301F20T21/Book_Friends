package com.cmput301f20t21.bookfriends.repositories.api;

import android.net.Uri;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public interface BookRepository {
    Task<Book> add(String isbn, String title, String author, String description, String owner, String imageUrl);
    Task<Book> editBook(Book oldBook, String isbn, String title, String author, String description, String imageUrl);
    Task<String> addImage(String username, Uri imageUri);
    Task<Void> delete(String id);
    Task<Void> deleteImage(String imageName);
    Task<List<Book>> getBooksOfOwnerId(String userName);
    Task<Book> getBookById(String bookId);
    Task<List<Book>> batchGetBooks( List<String> bookIds);
    Task<List<Book>> getAvailableBooksForUser(String username);
    Task<QuerySnapshot> getDocumentBy(String isbn, String title, String author);
    Task<Book> updateBookStatus(Book book, BOOK_STATUS newStatus);
}
