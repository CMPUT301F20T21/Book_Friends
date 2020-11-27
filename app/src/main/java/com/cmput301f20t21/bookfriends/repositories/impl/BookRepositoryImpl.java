/*
 * BookRepositoryImpl.java
 * Version: 1.0
 * Date: October 16, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.repositories.impl;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.exceptions.UnexpectedException;
import com.cmput301f20t21.bookfriends.repositories.api.BookRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * implementation of {@link BookRepository}, contains methods that directly interact
 * with Firebase Firestore and Firebase Cloud Storage
 */
public class BookRepositoryImpl implements BookRepository {

    private static final BookRepositoryImpl instance = new BookRepositoryImpl();
    private final StorageReference imageStorageReference = FirebaseStorage.getInstance().getReference();
    private final CollectionReference bookCollection;

    private BookRepositoryImpl() {
        bookCollection = FirebaseFirestore.getInstance().collection("books");
    }

    public static BookRepositoryImpl getInstance() {
        return instance;
    }

    /**
     * add a new book to firestore and upload image file and return the book with the downloadable
     * uri in the book entity
     * @param isbn the ISBN of the book
     * @param title the title of the book
     * @param author the author of the book
     * @param owner the owner of the book(i.e. current user)
     * @param imageUrl the image URL of the book (if there's a image)
     * @return {@link Task} returning {@link Book}
     */
    public Task<Book> add(String isbn, String title, String author, String owner, String imageUrl) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("isbn", isbn);
        data.put("title", title);
        data.put("author", author);
        data.put("owner", owner);
        // when a book is first added, the status will always be "AVAILABLE"
        data.put("status", BOOK_STATUS.AVAILABLE.toString());

        if (imageUrl != null) {
            data.put("imageUrl", imageUrl);
        }

        return bookCollection.add(data).continueWith(task -> {
            if (task.isSuccessful()) {
                return new Book(task.getResult().getId(), isbn, title, author, owner, BOOK_STATUS.AVAILABLE, imageUrl);
            }
            throw new Exception();
        });
    }

    /**
     * add image to firebase storage and return the downloadable url
     *
     * @param username the image name key
     * @param imageUri  the uri FILE to upload
     * @return {@link Task} returning the {@link String} of the downloadable url
     */
    public Task<String> addImage(String username, Uri imageUri) {
        String uniqueID = UUID.randomUUID().toString();
        StorageReference fileReference = imageStorageReference.child(username+uniqueID);
        return fileReference.putFile(imageUri).continueWithTask(task -> {
            if (task.isSuccessful()) {
                if (task.getResult() != null) {
                    return fileReference.getDownloadUrl().continueWith(urlTask -> {
                        if (urlTask.isSuccessful() && urlTask.getResult() != null) {
                            return urlTask.getResult().toString();
                        }
                        throw new Exception("failed to upload image: everything succeeded but cannot get downloadable url");
                    });
                }
                throw new Exception("failed to upload image: upload task succeed but cannot get task result");
            }
            throw new Exception("failed to upload image: upload task failed or cannot get result");
        });
    }

    /**
     * edit a book. replace image with a new image file and update the downloadable link
     * or, set the link to null, but the image is not yet deleted, and we don't have to
     * @param oldBook the old book before edit
     * @param isbn the ISBN of the edited book
     * @param title the title of the edited book
     * @param author the author of the edited book
     * @param imageUrl the image URL of the edited book (if there's a image)
     * @return {@link Task} returning {@link Book}
     */
    public Task<Book> editBook(Book oldBook, String isbn, String title, String author, String imageUrl) {
        HashMap<String, Object> data = new HashMap<>();
        String id = oldBook.getId();
        data.put("isbn", isbn);
        data.put("title", title);
        data.put("author", author);
        data.put("imageUrl", imageUrl);

        return bookCollection.document(id).update(data).continueWith(updateTask -> {
            if (updateTask.isSuccessful()) {
                return new Book(id, isbn, title, author, oldBook.getOwner(), oldBook.getStatus(), imageUrl);
            }
            throw new Exception("edit Book failed: failed to update with data");
        });
    }

    /**
     * deleting a book
     * @param id the book id of the book to delete
     * @return {@link Task} returning {@link Void}
     */
    public Task<Void> delete(String id) {
        return bookCollection.document(id).delete();
    }

    /**
     * deleting an image of a book
     * @param url the url of the book image
     * @return a {@link Void} task
     */
    public Task<Void> deleteImage(@NonNull String url) {
        String path = getPathStorageFromUrl(url);
        StorageReference fileReference = imageStorageReference.child(path);
        return fileReference.delete();
    }

    /**
     * get the path storage in FireBase cloud storage from the provided url
     * @param url the url of the book image
     * @return a {@link String} that contains the storage path
     */
    private String getPathStorageFromUrl(String url) {
        String baseUrl = "https://firebasestorage.googleapis.com/v0/b/cmput301bookfriends.appspot.com/o/";
        String imagePath = url.replace(baseUrl, "");
        int indexOfEndPath = imagePath.indexOf("?");
        imagePath = imagePath.substring(0,indexOfEndPath);
        imagePath = imagePath.replace("%2F","/");
        return imagePath;
    }

    /**
     * the all the books belongs to a specific user
     * @param username the user's username to query for books
     * @return {@link Task} returning a {@link List} of {@link Book} belongs to the provided user
     */
    public Task<List<Book>> getBooksOfOwnerId(String username) {
        return bookCollection.whereEqualTo("owner", username).get().continueWith(task -> {
            if (task.isSuccessful()) {
                try {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    return documents.stream()
                            .map(document -> document.toObject(Book.class))
                            .collect(Collectors.toList());
                } catch (Exception e) {
                    Log.d("BOOK_ERROR", e.getMessage());
                    throw new UnexpectedException();
                }
            }
            throw new UnexpectedException();
        });
    }

    /**
     * get the book from the provided book id
     * @param bookId the book id of the book
     * @return {@link Task} returning the required {@link Book}
     */
    public Task<Book> getBookById(String bookId) {
        return bookCollection.document(bookId).get().continueWith(task -> {
            if (task.isSuccessful()) {
                try {
                    return task.getResult().toObject(Book.class);
                } catch (Exception e) {
                    Log.d("BOOK_ERROR", e.getMessage());
                    throw new UnexpectedException();
                }
            }
            throw new UnexpectedException();
        });
    }

    /**
     * get all the books from the list of book ids
     * @param bookIds a {@link List} of book ids
     * @return {@link Task} returning a {@link List} of {@link Book} from the provided book ids
     */
    public Task<List<Book>> batchGetBooks(List<String> bookIds) {
        return bookCollection.whereIn(FieldPath.documentId(), bookIds).get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return task.getResult().getDocuments()
                                .stream()
                                .map(doc -> doc.toObject(Book.class))
                                .collect(Collectors.toList());
                    }
                    throw new UnexpectedException();
                });
    }

    /**
     * get all the available books that the user can request for
     * excluding all the books from this user
     * @param username the username of the logged in user
     * @return {@link Task} returning a {@link List} of {@link Book} from the provided book ids
     */
    public Task<List<Book>> getAvailableBooksForUser(String username) {
        return bookCollection
                .whereNotEqualTo("owner", username)
                .whereEqualTo("status", BOOK_STATUS.AVAILABLE.toString())
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return task.getResult().getDocuments()
                                .stream()
                                .map(doc -> doc.toObject(Book.class))
                                .collect(Collectors.toList());
                    }
                    throw new UnexpectedException();
                });
    }

    public Task<QuerySnapshot> getDocumentBy(String isbn, String title, String author) {
        return bookCollection.whereEqualTo("isbn", isbn).whereEqualTo("title", title).whereEqualTo("author", author).get();
    }

    /**
     * update the status of a book
     * @param book the book to update
     * @param newStatus the new status to update
     * @return {@link Task} returning the updated {@link Book}
     */
    public Task<Book> updateBookStatus(Book book, BOOK_STATUS newStatus) {
        return bookCollection
                .document(book.getId())
                .update("status", newStatus.toString())
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return new Book(
                                book.getId(),
                                book.getIsbn(),
                                book.getTitle(),
                                book.getAuthor(),
                                book.getOwner(),
                                newStatus,
                                book.getImageUrl()
                        );
                    }
                    throw new UnexpectedException();
                });
    }

}
