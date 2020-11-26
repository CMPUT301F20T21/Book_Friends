package com.cmput301f20t21.bookfriends.repositories.factories;

import com.cmput301f20t21.bookfriends.BuildConfig;
import com.cmput301f20t21.bookfriends.fakes.repositories.FakeAuthRepository;
import com.cmput301f20t21.bookfriends.repositories.api.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.impl.AuthRepositoryImpl;

public class AuthRepositoryFactory {
    private static FakeAuthRepository fake = new FakeAuthRepository();
    public static AuthRepository getRepository() {
        if (BuildConfig.IS_TESTING.get()) {
            return fake;
        }
        return AuthRepositoryImpl.getInstance();
    }
}
