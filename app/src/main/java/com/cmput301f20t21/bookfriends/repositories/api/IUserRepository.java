package com.cmput301f20t21.bookfriends.repositories.api;

import com.cmput301f20t21.bookfriends.entities.UserDocument;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public interface IUserRepository {
    Task<Void> add(String uid, String username, String email);
    Task<UserDocument> getByUsername(String username);
    Task<DocumentSnapshot> getByUid(String uid);
    Task<QuerySnapshot> getByUsernameStartWith(String username);
    Task<Void> updateUserEmail(String email);
}
