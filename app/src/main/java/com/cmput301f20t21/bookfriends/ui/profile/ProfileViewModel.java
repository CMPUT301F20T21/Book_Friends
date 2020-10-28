package com.cmput301f20t21.bookfriends.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.services.UserService;
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
                                "123123"
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
}