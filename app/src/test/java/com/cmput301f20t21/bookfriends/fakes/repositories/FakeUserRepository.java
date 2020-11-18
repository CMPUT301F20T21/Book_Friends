package com.cmput301f20t21.bookfriends.fakes.repositories;

import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.repositories.api.UserRepository;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class FakeUserRepository implements UserRepository {
    @Override
    public Task<Void> add(String uid, String username, String email) {
        return null;
    }

    @Override
    public Task<User> getByUsername(String username) {
        return null;
    }

    @Override
    public Task<User> getByUid(String uid) {
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
