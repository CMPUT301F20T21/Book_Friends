package com.cmput301f20t21.bookfriends.services;

import android.net.Uri;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookService {
    // TODO not done

    private CollectionReference bookCollection;
    private final StorageReference imageStorageReference = FirebaseStorage.getInstance().getReference();

    private static final BookService instance = new BookService();

    private BookService() {
        bookCollection = FirebaseFirestore.getInstance().collection("books");
    }

    public static BookService getInstance() {
        return instance;
    }

    public Task<DocumentReference> add(String isbn, String title, String author, String description, String owner) {
        HashMap<String, String> data = new HashMap<>();
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

    public Task<Void> delete(String id) {
        return bookCollection.document(id).delete();
    }

    public ArrayList<Book> getBooksOnResult(QuerySnapshot resultSnapshot) {
        ArrayList<Book> books = new ArrayList<>();
        for (QueryDocumentSnapshot document : resultSnapshot) {
            Map<String, Object> data = document.getData();
            String id = document.getId();
            String isbn = (String) data.get("isbn");
            String title = (String) data.get("title");
            String author = (String) data.get("author");
            String description = (String) data.get("description");
            String owner = (String) data.get("owner");
            String status = (String) data.get("status");
            Book book = new Book(id, isbn, title, author, description, owner, BOOK_STATUS.valueOf(status));
            books.add(book);
        }
        return books;
    }

    public Task<Uri> getImage(String bookId) {
        StorageReference pathReference = imageStorageReference.child(bookId);
        return pathReference.getDownloadUrl();
    }

    public Task<QuerySnapshot> getBooksOfOwnerId(String username) {
        return bookCollection.whereEqualTo("owner", username).get();
    }

    public Task<QuerySnapshot> getBookOfBorrowerId(String uid) {
        // TODO placeholder here
        return bookCollection.whereEqualTo("owner", uid).get();
    }
}
