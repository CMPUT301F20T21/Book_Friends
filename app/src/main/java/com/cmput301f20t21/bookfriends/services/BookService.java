package com.cmput301f20t21.bookfriends.services;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class BookService {
    // TODO not done

    private CollectionReference bookCollection;

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
        return bookCollection.add(data);
    }

    public Task<Void> delete(String id) {
        return bookCollection.document(id).delete();
    }

    public Task<QuerySnapshot> getBooksOfOwnerID(String uid) {
        return bookCollection.whereEqualTo("owner", uid).get();
    }

    public Task<QuerySnapshot> getBookOfBorrowerId(String uid) {
        // TODO placeholder here
        return bookCollection.whereEqualTo("owner", uid).get();
    }
}
