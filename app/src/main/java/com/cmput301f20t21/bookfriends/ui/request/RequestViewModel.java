package com.cmput301f20t21.bookfriends.ui.request;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.services.BookService;
import com.cmput301f20t21.bookfriends.services.RequestService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

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
                        String author = (String) document.get("author");
                        String description = (String) document.get("description");
                        String imageName = (String) document.get("imageName");
                        String isbn = (String) document.get("isbn");
                        String owner = (String) document.get("owner");
                        String status = (String) document.get("status");
                        String title = (String) document.get("title");
                        book.setValue(new Book(bookId, isbn, title, author, description, owner, imageName, BOOK_STATUS.valueOf(status)));
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
        if (book == null) {
            book = new MutableLiveData<>();
        }
        return this.book;
    }
}
