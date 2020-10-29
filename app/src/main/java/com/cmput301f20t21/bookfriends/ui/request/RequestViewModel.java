package com.cmput301f20t21.bookfriends.ui.request;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.services.AuthService;
import com.cmput301f20t21.bookfriends.services.BookService;
import com.cmput301f20t21.bookfriends.services.RequestService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class RequestViewModel extends ViewModel {
    private MutableLiveData<Book> book;

    private final RequestService requestService = RequestService.getInstance();
    private final BookService bookService = BookService.getInstance();

    public interface OnViewRequestSuccessCallback {
        void run();
    }

    public interface OnFailCallBack {
        void run();
    }

    public void getBookInfo(
            String bookId,
            OnViewRequestSuccessCallback successCallback,
            OnFailCallBack failCallBack) {
        bookService.getBookById(bookId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                    }
                }
            }
        });
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

    public MutableLiveData<Book> getBook() {
        return this.book;
    }
}
