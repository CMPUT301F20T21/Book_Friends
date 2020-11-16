package com.cmput301f20t21.bookfriends.repositories;

import android.net.Uri;

import com.cmput301f20t21.bookfriends.entities.AvailableBook;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.exceptions.UnexpectedException;
import com.cmput301f20t21.bookfriends.repositories.api.IBookRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
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

    public Task<String> add(String isbn, String title, String author, String description, String owner) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("isbn", isbn);
        data.put("title", title);
        data.put("author", author);
        data.put("description", description);
        data.put("owner", owner);
        // when a book is first added, the status will always be "AVAILABLE"
        data.put("status", BOOK_STATUS.AVAILABLE.toString());
        return bookCollection.add(data).continueWith(task -> {
            if (task.isSuccessful()) {
                return task.getResult().getId();
            }
            throw new Exception();
        });
    }

    public Task<String> addImage(String imageName, Uri imageUri) {
        StorageReference fileReference = imageStorageReference.child(imageName);
        return fileReference.putFile(imageUri).continueWith(task -> {
            if (task.isSuccessful()) {
                if (task.getResult() != null) {
                    return task.getResult().toString();
                }
                throw new Exception("failed to upload image: upload task succeed but cannot get task result");
            }
            throw new Exception("failed to upload image: upload task failed or cannot get result");
        });
    }

    public Task<Book> editBook(Book oldBook, String isbn, String title, String author, String description) {
        HashMap<String, Object> data = new HashMap<>();
        String id = oldBook.getId();
        data.put("isbn", isbn);
        data.put("title", title);
        data.put("author", author);
        data.put("description", description);
        return bookCollection.document(id).update(data).continueWith(task -> {
            if (task.isSuccessful()) {
                return new Book(id, isbn, title, author, description, oldBook.getOwner(), BOOK_STATUS.AVAILABLE);
            }
            throw new Exception();
        });
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

//  TODO ============ the possibly ideal way of us mapping doc directly to entities, works now but not enabled in this PR ========
//    public Task<ArrayList<Book>> getBooksOfOwnerIdParsed(String username) {
//        return bookCollection.whereEqualTo("owner", username).get().continueWith(qSnap -> {
//            List<DocumentSnapshot> docs = qSnap.getResult().getDocuments();
//            return (ArrayList<Book>) docs
//                    .stream()
//                    .map(doc -> doc.toObject(Book.class))
//                    .collect(Collectors.toList());
//        });
//    }

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
