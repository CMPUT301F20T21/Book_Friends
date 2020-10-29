package com.cmput301f20t21.bookfriends.ui.request;

import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.services.AuthService;
import com.cmput301f20t21.bookfriends.services.RequestService;
import com.google.firebase.firestore.DocumentReference;

public class RequestViewModel extends ViewModel {
    private String bookName;
    private String author;
    private String description;
    private String status;

    private final RequestService requestService = RequestService.getInstance();

    public interface OnViewRequestSuccessCallback {
        void run();
    }

    public interface OnFailCallBack {
        void run();
    }



    // TODO: fully implement it
    public void handleViewRequest(
            String bookId,
            OnViewRequestSuccessCallback successCallback,
            OnFailCallBack failCallBack) {
        requestService.getByBookId(bookId).addOnCompleteListener(
                viewRequestTask -> {
                    if (viewRequestTask.isSuccessful()) {
                        if (!viewRequestTask.getResult().isEmpty()) {
                            bookName = viewRequestTask.getResult().getDocuments().get(6).get("title").toString();
                            author = viewRequestTask.getResult().getDocuments().get(0).get("author").toString();
                            description = viewRequestTask.getResult().getDocuments().get(1).get("description").toString();
                            status = viewRequestTask.getResult().getDocuments().get(4).get("status").toString();
                            successCallback.run();
                        } else {
                            failCallBack.run();
                        }
                    } else {
                        failCallBack.run();
                    }
                }
        );
    }

    public String getBookName() {
        return this.bookName;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getDescription() {
        return this.description;
    }

    public String getStatus() {
        return this.status;
    }
}
