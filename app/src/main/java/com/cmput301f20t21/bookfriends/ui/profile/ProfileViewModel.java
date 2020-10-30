package com.cmput301f20t21.bookfriends.ui.profile;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.callbacks.OnFailCallback;
import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallbackWithMessage;
import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.services.AuthService;
import com.cmput301f20t21.bookfriends.services.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ProfileViewModel extends ViewModel {
    private final UserService userService;
    private final MutableLiveData<ArrayList<User>> searchedUsers = new MutableLiveData<>(new ArrayList<>());

    public ProfileViewModel() {
        userService = UserService.getInstance();
    }

    // the data stream output for searched list views
    public LiveData<ArrayList<User>> getSearchedUsers() {
        return searchedUsers;
    }

    // the update api for views
    public void updateSearchQuery(String query) {
        // start a search routine, update results
        userService.getByUsernameStartWith(query).addOnCompleteListener(usernameTask -> {
            if (usernameTask.isSuccessful()) {
                if (!usernameTask.getResult().isEmpty()) {
                    ArrayList<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot document: usernameTask.getResult()) {
                        users.add(new User(
                                document.getId(),
                                document.get("username").toString(),
                                "",
                                document.get("email").toString(),
                                ""
                        ));
                    }
                    searchedUsers.setValue(users);
                } else {
                    // empty email, should never happen
                    searchedUsers.setValue(new ArrayList<>());
                }
            } else {
                // fail to get username
                searchedUsers.setValue(new ArrayList<>());
            }
        });
    }

    public void getUserByUid(String uid, OnSuccessCallbackWithMessage<User> onSuccess, OnFailCallback onFail) {
        userService.getByUid(uid).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                onFail.run();
                return;
            }
            DocumentSnapshot document = task.getResult();
            if (document == null) {
                onFail.run();
                return;
            }
            User u = new User(
                    document.getId(),
                    document.get("username").toString(),
                    "",
                    document.get("email").toString(),
                    ""
            );
            onSuccess.run(u);
        });
    }

    public void updateCurrentUserEmail(String username, String TAG){
        //update email authentication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updateEmail(username)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User email address updated.");
                            }
                        }
                    });

        }
    }
    public void updateFirestoreUser(String inputEmail, String inputPhone){
        User firebaseUser = AuthService.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            FirebaseFirestore.getInstance().collection("users").document(userId)
                    .update(
                            "email", inputEmail,
                            "phone", inputPhone
                    );
        }
    }
}