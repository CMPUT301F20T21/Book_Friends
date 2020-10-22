package com.cmput301f20t21.bookfriends.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.services.AuthService;
import com.cmput301f20t21.bookfriends.services.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginViewModel extends ViewModel {
    /**
     * interface for callback lambda function
     * will/should be called by request-related handlers when async request succeeded
     */
    public interface OnSuccessCallback {
        void run();
    }
    /** called by handlers when async request failed */
    public interface OnFailCallback {
        void run();
    }

    private final AuthService authService = AuthService.getInstance();
    private final UserService userService = UserService.getInstance();

    public void handleLogIn(final String username, final String password, final OnSuccessCallback successCallback, final OnFailCallback failureCallback) {
        userService.getByUsername(username).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        String email = task.getResult().getDocuments().get(0).get("email").toString();
                        authService.signIn(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    successCallback.run();
                                } else {
                                    failureCallback.run();
                                }
                            }
                        });
                    } else {
                        // empty email, should never happen
                        // TODO: handle error
                        failureCallback.run();
                    }
                } else {
                    // fail to get username
                    // TODO: handle error
                    failureCallback.run();
                }
            }
        });
    }
}
