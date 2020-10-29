package com.cmput301f20t21.bookfriends.services;

import android.net.Uri;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;

public class BookService {

    private static final BookService instance = new BookService();
    private final StorageReference imageStorageReference = FirebaseStorage.getInstance().getReference();
    private final CollectionReference bookCollection;

    private BookService() {
        bookCollection = FirebaseFirestore.getInstance().collection("books");
    }

    public static BookService getInstance() {
        return instance;
    }

    public Task<DocumentReference> add(String isbn, String title, String author, String description, String owner) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("isbn", isbn);
        data.put("title", title);
        data.put("author", author);
        data.put("description", description);
        data.put("owner", owner);
        // when a book is first added, the status will always be "AVAILABLE"
        data.put("status", BOOK_STATUS.AVAILABLE.toString());
        return bookCollection.add(data);
    }

    public UploadTask addImage(String bookId, Uri imageUri) {
        StorageReference fileReference = imageStorageReference.child(bookId);
        return fileReference.putFile(imageUri);
    }

    public Task<Void> addImageNameToBook(String bookId, String imageName) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("imageName", imageName);
        return bookCollection.document(bookId).update(data);
    }

    public Task<Void> delete(String id) {
        return bookCollection.document(id).delete();
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

    public Task<QuerySnapshot> batchGetBooks(List<String> bookIds) {
        return bookCollection.whereIn(FieldPath.documentId(), bookIds).get();
    }
}
