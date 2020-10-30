package com.cmput301f20t21.bookfriends.ui.request;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.cmput301f20t21.bookfriends.services.BookService;
import com.cmput301f20t21.bookfriends.services.RequestService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RequestViewModel extends ViewModel {
    private MutableLiveData<Book> book;
    private MutableLiveData<Uri> imageUri;
    private MutableLiveData<List<Request>> requesters;

    private final RequestService requestService = RequestService.getInstance();
    private final BookService bookService = BookService.getInstance();

    /**
     * Function to get the book information from FireStore
     * @param bookId we will query the book information based on the bookID
     */
    public void getBookInfo(String bookId) {
        bookService.getBookById(bookId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String author = (String) document.get("author");
                        String description = (String) document.get("description");
                        String imageName = (String) document.get("imageName");
                        String isbn = (String) document.get("isbn");
                        String owner = (String) document.get("owner");
                        String status = (String) document.get("status");
                        String title = (String) document.get("title");
                        bookService.getImage(imageName).addOnSuccessListener(uri -> {
                           imageUri.setValue(uri);
                        });
                        book.setValue(new Book(bookId, isbn, title, author, description, owner, BOOK_STATUS.valueOf(status)));
                    }
                }
            }
        });
    }

    /**
     * get all the requesters of a book based on its bookId
     * @param bookId
     */
    public void fetchRequesters(String bookId) {
        requestService.getByBookId(bookId).addOnSuccessListener(requesterDocumentsSnapShots -> {
           List<DocumentSnapshot> documents = requesterDocumentsSnapShots.getDocuments();
           requesters.setValue(IntStream.range(0, documents.size()).mapToObj(i -> {
               DocumentSnapshot document = documents.get(i);
               Request request = requestService.getRequestFromDocument(document);
               return request;
           }).collect(Collectors.toList()));
        });
    }

    /**
     * function to notify the content displayed on device when the data is changed
     * @param bookId we get book information by book ID then pass the content to display
     * @return
     */
    public MutableLiveData<Book> getBook(String bookId) {
        if (book == null) {
            book = new MutableLiveData<>();
            getBookInfo(bookId);
        }
        return this.book;
    }

    /**
     * similar, we get the image of the current book
     * @return
     */
    public MutableLiveData<Uri> getImageUri() {
        if (imageUri == null) {
            imageUri = new MutableLiveData<>();
        }
        return this.imageUri;
    }

    public MutableLiveData<List<Request>> getRequesters(String bookId) {
        if (requesters == null) {
            requesters = new MutableLiveData<>();
            fetchRequesters(bookId);
        }
        return this.requesters;
    }

    public void removeRequest(String requesterId) {
        requestService.deny(requesterId).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // not sure what to put here xD
            }
        });
    }

    public void acceptRequest(String requesterId) {
        requestService.accept(requesterId).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public void removeAllRequest(List<String> ids) {
        requestService.batchDeny(ids).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }
}
