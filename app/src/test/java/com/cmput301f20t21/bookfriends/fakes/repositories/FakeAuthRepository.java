package com.cmput301f20t21.bookfriends.fakes.repositories;

import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.repositories.api.AuthRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class FakeAuthRepository implements AuthRepository {
    @Override
    public User getCurrentUser() {
        return null;
    }

    @Override
    public Task<AuthResult> createUserAuth(String email, String password) {
        return null;
    }

    @Override
    public Task<AuthResult> signIn(String username, String email, String password) {
        return null;
    }

    @Override
    public void signOut() {

    }

    @Override
    public Task<Void> updateEmail(String email) {
        return null;
    }
}
