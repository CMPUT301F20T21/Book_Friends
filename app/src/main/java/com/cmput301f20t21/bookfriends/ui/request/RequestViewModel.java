package com.cmput301f20t21.bookfriends.ui.request;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.Request;
import com.cmput301f20t21.bookfriends.repositories.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.RequestRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RequestViewModel extends ViewModel {
    private MutableLiveData<Book> book;
    private MutableLiveData<Uri> imageUri;
    private MutableLiveData<List<Request>> requests;
    private List<Request> requestsData;
    private MutableLiveData<Integer> updatedPosition;

    private final RequestRepository requestService = RequestRepository.getInstance();
    private final BookRepository bookService = BookRepository.getInstance();

    /**
     * Function to get the book information from FireStore
     * @param bookId we will query the book information based on the bookID
     */
    public void fetchBook(String bookId) {
        bookService.getBookById(bookId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String imageName = (String) document.get("imageName");
                    bookService.getImage(imageName).addOnSuccessListener(uri -> imageUri.setValue(uri));
                    book.setValue(bookService.getBookFromDocument(document));
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
           requestsData.addAll(IntStream.range(0, documents.size()).mapToObj(i -> {
               DocumentSnapshot document = documents.get(i);
               Request request = requestService.getRequestFromDocument(document);
               updatedPosition.setValue(i);
               return request;
           }).collect(Collectors.toList()));
           requests.setValue(requestsData);
        });
    }

    public MutableLiveData<Integer> getUpdatedPosition() {
        if (updatedPosition == null) {
            updatedPosition = new MutableLiveData<>(0);
        }
        return updatedPosition;
    }

    /**
     * function to notify the content displayed on device when the data is changed
     * @param bookId we get book information by book ID then pass the content to display
     * @return
     */
    public MutableLiveData<Book> getBook(String bookId) {
        if (book == null) {
            book = new MutableLiveData<>();
            fetchBook(bookId);
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

    /**
     * live data of requesters retrieving from FireStore
     * @param bookId
     * @return
     */
    public MutableLiveData<List<Request>> getRequests(String bookId) {
        if (requests == null) {
            requests = new MutableLiveData<>(new ArrayList<>());
            requestsData = requests.getValue();
            fetchRequesters(bookId);
        }
        return this.requests;
    }

    /**
     * When the requester is denied, remove his/her request.
     * update the status of this requester to DENIED
     * @param position
     */
    public void removeRequest(Integer position) {
        Request request = requestsData.get(position);
        requestService.deny(request.getId()).addOnSuccessListener(aVoid -> {
            requestsData.remove(request);
            requests.setValue(requestsData);
        });
    }

    /**
     * When the requester is accepted, update the status of this requester to ACCEPTED
     * @param position
     */
    public void acceptRequest(Integer position) {
        Request request = requestsData.get(position);
        requestService.accept(request.getId()).addOnSuccessListener(aVoid -> {
            requestsData.remove(request);
            requests.setValue(requestsData);
            List<String> ids = new ArrayList<>();
            for (int i = 0; i < requestsData.size(); i++) {
                ids.add(requestsData.get(i).getId());
            }
            requestService.batchDeny(ids).addOnSuccessListener(aVoid1 -> {
                requestsData.clear();
                requests.setValue(requestsData);
            });
        });
    }

    /**
     * function is called when 1 requester is accepted, remove all other requests
     * @param ids
     */
    public void removeAllRequest(List<String> ids) {
        requestService.batchDeny(ids).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }
}
