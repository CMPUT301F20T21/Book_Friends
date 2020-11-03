package com.cmput301f20t21.bookfriends.repositories.api;

import com.cmput301f20t21.bookfriends.entities.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface IAuthRepository {
    User getCurrentUser();
    Task<AuthResult> createUserAuth(String email, String password);
    Task<AuthResult> signIn(String username, String email, String password);
    void signOut();
    Task<Void> updateEmail(String email);
}
