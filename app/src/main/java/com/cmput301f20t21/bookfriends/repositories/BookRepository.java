package com.cmput301f20t21.bookfriends.repositories;

import android.net.Uri;

import com.cmput301f20t21.bookfriends.entities.AvailableBook;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.exceptions.UnexpectedException;
import com.cmput301f20t21.bookfriends.repositories.api.IBookRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BookRepository implements IBookRepository {

    private static final BookRepository instance = new BookRepository();
    private final StorageReference imageStorageReference = FirebaseStorage.getInstance().getReference();
    private final CollectionReference bookCollection;

    private BookRepository() {
        bookCollection = FirebaseFirestore.getInstance().collection("books");
    }

    public static BookRepository getInstance() {
        return instance;
    }

    /**
     * add a new book to firestore and upload image file and return the book with the downloadable
     * uri in the book entity
     * @param isbn
     * @param title
     * @param author
     * @param description
     * @param owner
     * @param imageUriFile
     * @return
     */
    public Task<Book> add(String isbn, String title, String author, String description, String owner, Uri imageUriFile) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("isbn", isbn);
        data.put("title", title);
        data.put("author", author);
        data.put("description", description);
        data.put("owner", owner);
        // when a book is first added, the status will always be "AVAILABLE"
        data.put("status", BOOK_STATUS.AVAILABLE.toString());

        if (imageUriFile != null) {
            return bookCollection.add(data).continueWithTask(task -> {
                if (task.isSuccessful()) {
                    String newBookId = task.getResult().getId();
                    return addImage(newBookId + "cover", imageUriFile).continueWithTask(addImageTask -> {
                        if (addImageTask.isSuccessful()) {
                            return bookCollection.document(task.getResult().getId()).update("imageUrl", addImageTask.getResult()).continueWith(updateBookTask -> {
                                if (updateBookTask.isSuccessful()) {
                                    return new Book(newBookId, isbn, title, author, description, owner, BOOK_STATUS.AVAILABLE, addImageTask.getResult());
                                }
                                throw new Exception("add Book failed: updating book with new image download uri failed, everything else worked");
                            });
                        }
                        throw new Exception("add Book failed: upload image failed, add image task is not successful");
                    });
                }
                throw new Exception("add Book failed: failed to add new book data to collection");
            });
        }
        return bookCollection.add(data).continueWith(task -> {
            if (task.isSuccessful()) {
                return new Book(task.getResult().getId(), isbn, title, author, description, owner, BOOK_STATUS.AVAILABLE, null);
            }
            throw new Exception();
        });
    }

    /**
     * add image to firebase storage and return the downloadable url
     *
     * @param imageName the image name key
     * @param imageUri  the uri FILE to upload
     * @return Task returning the String of the downloadable url
     */
    public Task<String> addImage(String imageName, Uri imageUri) {
        StorageReference fileReference = imageStorageReference.child(imageName);
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
     * @param oldBook
     * @param isbn
     * @param title
     * @param author
     * @param description
     * @param imageUriFile
     * @return
     */
    public Task<Book> editBook(Book oldBook, String isbn, String title, String author, String description, Uri imageUriFile, Boolean shouldDeleteImage) {
        HashMap<String, Object> data = new HashMap<>();
        String id = oldBook.getId();
        data.put("isbn", isbn);
        data.put("title", title);
        data.put("author", author);
        data.put("description", description);

        // just so we don't have the old book unexpectedly changed (especially the imageUri)
        Book newBook = new Book(id, isbn, title, author, description, oldBook.getOwner(), oldBook.getStatus(), oldBook.getImageUrl());
        if (imageUriFile != null) {
            // need to upload a new image for this book
            return bookCollection.document(id).update(data).continueWithTask(updateTask -> {
                if (updateTask.isSuccessful()) {
                    return addImage(id + "cover", imageUriFile).continueWithTask(imageTask -> bookCollection.document(id).update("imageUrl", imageTask.getResult()).continueWith(updateBookTask -> {
                        if (updateBookTask.isSuccessful()) {
                            // set the updated download link
                            newBook.setImageUrl(imageTask.getResult());
                            return newBook;
                        }
                        throw new Exception("edit Book failed: updating book with new image download uri failed, everything else worked");
                    }));
                }
                throw new Exception("edit Book failed: failed to update with data");
            });
        } else {
            // there's no local image file, simply update other data
            return bookCollection.document(id).update(data).continueWithTask(updateTask -> {
                if (updateTask.isSuccessful()) {
                    if (shouldDeleteImage) {
                        // delete image
                        return bookCollection.document(id).update("imageUrl", null).continueWith(updateBookTask -> {
                            if (updateBookTask.isSuccessful()) {
                                // set the updated download link
                                newBook.setImageUrl(null); // remove image only if 1. there's no local new image file 2. oldBook does not have a image url
                                return newBook;
                            }
                            throw new Exception("edit Book failed: updating book with new image download uri failed, everything else worked");
                        });
                    } else {
                        // just return the updated book
                        return bookCollection.get().continueWith(noop -> newBook);
                    }
                }
                throw new Exception("edit Book failed: failed to update with data");
            });
        }

    }

    public Task<Void> delete(String id) {
        return bookCollection.document(id).delete();
    }

    public Task<Void> deleteImage(String imageName) {
        StorageReference fileReference = imageStorageReference.child(imageName);
        return fileReference.delete();
    }

    public Book getBookFromDocument(DocumentSnapshot document) {
        String id = document.getId();
        String isbn = (String) document.get("isbn");
        String title = (String) document.get("title");
        String author = (String) document.get("author");
        String description = (String) document.get("description");
        String owner = (String) document.get("owner");
        String status = (String) document.get("status");
        return new Book(id, isbn, title, author, description, owner, BOOK_STATUS.valueOf(status));
    }

    public Task<Uri> getImage(String imageName) {
        StorageReference pathReference = imageStorageReference.child(imageName);
        return pathReference.getDownloadUrl();
    }

    public Task<QuerySnapshot> getBooksOfOwnerId(String username) {
        return bookCollection.whereEqualTo("owner", username).get();
    }

    public Task<QuerySnapshot> getBookOfBorrowerId(String uid) {
        // TODO placeholder here
        return bookCollection.whereEqualTo("owner", uid).get();
    }

    public Task<DocumentSnapshot> getBookById(String bookId) {
        return bookCollection.document(bookId).get();
    }

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

    public Task<List<AvailableBook>> getAvailableBooksForUser(String username) {
        return bookCollection
                .whereNotEqualTo("owner", username)
                .whereEqualTo("status", BOOK_STATUS.AVAILABLE.toString())
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return task.getResult().getDocuments()
                                .stream()
                                .map(doc -> doc.toObject(AvailableBook.class))
                                .collect(Collectors.toList());
                    }
                    throw new UnexpectedException();
                });
    }

    // get all the books, will be used in browse
    public Task<QuerySnapshot> getAvailableBooks() {
        return bookCollection
                .whereEqualTo("status", BOOK_STATUS.AVAILABLE.toString())
                .whereEqualTo("status", BOOK_STATUS.REQUESTED.toString()).get();
    }

    public Task<QuerySnapshot> getDocumentBy(String isbn, String title, String author) {
        return bookCollection.whereEqualTo("isbn", isbn).whereEqualTo("title", title).whereEqualTo("author", author).get();
    }
}
