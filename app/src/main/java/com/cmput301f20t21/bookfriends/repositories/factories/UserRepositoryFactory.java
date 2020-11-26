package com.cmput301f20t21.bookfriends.repositories.factories;

import com.cmput301f20t21.bookfriends.BuildConfig;
import com.cmput301f20t21.bookfriends.fakes.repositories.FakeUserRepository;
import com.cmput301f20t21.bookfriends.repositories.api.UserRepository;
import com.cmput301f20t21.bookfriends.repositories.impl.UserRepositoryImpl;

public class UserRepositoryFactory {
    private static FakeUserRepository fake = new FakeUserRepository();
    public static UserRepository getRepository() {
        if (BuildConfig.IS_TESTING.get()) {
            return fake;
        }
        return UserRepositoryImpl.getInstance();
    }
}
