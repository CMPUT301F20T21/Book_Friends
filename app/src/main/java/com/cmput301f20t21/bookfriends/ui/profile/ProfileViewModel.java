/*
 * ProfileViewModel.java
 * Version: 1.0
 * Date: November 4, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */


package com.cmput301f20t21.bookfriends.ui.profile;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.callbacks.OnFailCallback;
import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallbackWithMessage;
import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.repositories.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.UserRepository;
import com.cmput301f20t21.bookfriends.repositories.api.IUserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * The ViewModel for ProfileFragment, ProfileSearchFragment, and ProfileViewUserActivity
 */
public class ProfileViewModel extends ViewModel {
    private final IUserRepository userRepository;
    private final MutableLiveData<ArrayList<User>> searchedUsers = new MutableLiveData<>(new ArrayList<>());

    public ProfileViewModel() {
        this(UserRepository.getInstance());
    }

    public ProfileViewModel(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * get a list of users that is matches the keyword during search
     * @return a list of matching users
     */
    public LiveData<ArrayList<User>> getSearchedUsers() {
        return searchedUsers;
    }

    /**
     * search for a list of users in firestore using the query/keyword
     * @param query the keyword to search for
     */
    public void updateSearchQuery(String query) {
        // start a search routine, update results
        userRepository.getByUsernameStartWith(query).addOnSuccessListener(users -> {
            searchedUsers.setValue((ArrayList<User>) users);
        });
    }

    /**
     *
     * @param uid the user id
     * @param onSuccess async callback that is called upon successfully completing all operations
     * @param onFail async callback that is called if any operation failed
     */
    public void getUserByUid(String uid, OnSuccessCallbackWithMessage<User> onSuccess, OnFailCallback onFail) {
        userRepository.getByUid(uid)
                .addOnSuccessListener(onSuccess::run)
                .addOnFailureListener(e -> onFail.run());
    }

    /**
     * update the email of the current user for the "user" collection in Firestore
     * @param inputEmail the email to update
     * @param TAG the tag to use when update is successful
     */
    public void updateCurrentUserEmail(String inputEmail, String TAG){
        //update email authentication
        Task<Void> updateEmail = AuthRepository.getInstance().updateEmail(inputEmail);
        updateEmail.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // TODO: change one of the update function to have async callback with a success message
                Log.d(TAG, "User email address updated.");
            }
        });
    }

    /**
     * update the email of the current user for Firebase Auth
     * @param inputEmail the email to update
     * @param TAG the tag to use when update is successful
     */
    public void updateFirestoreUserEmail(String inputEmail, String TAG){
        //update "email" field
        Task<Void> updateUser = userRepository.updateUserEmail(inputEmail);
        updateUser.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // TODO: change one of the update function to have async callback with a success message
                Log.d(TAG, "User email address updated.");
            }
        });

    }
}