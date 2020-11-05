package com.cmput301f20t21.bookfriends.fakes.repositories;

import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.repositories.api.IUserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FakeUserRepository implements IUserRepository {
    @Override
    public Task<Void> add(String uid, String username, String email) {
        return null;
    }

    @Override
    public Task<User> getByUsername(String username) {
        return null;
    }

    @Override
    public Task<DocumentSnapshot> getByUid(String uid) {
        return null;
    }

    @Override
    public Task<List<User>> getByUsernameStartWith(String username) {
        return null;
    }

    @Override
    public Task<Void> updateUserEmail(String email) {
        return null;
    }
}
