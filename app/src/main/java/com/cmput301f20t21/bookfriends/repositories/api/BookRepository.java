package com.cmput301f20t21.bookfriends.repositories.api;

import android.net.Uri;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public interface BookRepository {
    Task<Book> add(String isbn, String title, String author, String description, String owner, Uri imageUriFile);
    Task<String> addImage(String bookId, Uri imageUri);
    Task<Book> editBook(Book oldBook, String isbn, String title, String author, String description, Uri imageUriFile, Boolean shouldDeleteImage);
    Task<Void> delete(String id);
    Task<Void> deleteImage(String imageName);
    Book getBookFromDocument(DocumentSnapshot document);
    Task<Uri> getImage(String imageName);
    Task<QuerySnapshot> getBooksOfOwnerId(String userName);
    Task<QuerySnapshot> getBookOfBorrowerId(String uid);
    Task<DocumentSnapshot> getBookById(String bookId);
    Task<List<Book>> batchGetBooks( List<String> bookIds);
    Task<QuerySnapshot> getAvailableBooks();
    Task<List<Book>> getAvailableBooksForUser(String username);
    Task<QuerySnapshot> getDocumentBy(String isbn, String title, String author);
    Task<Book> updateBookStatus(Book book, BOOK_STATUS newStatus);
}
