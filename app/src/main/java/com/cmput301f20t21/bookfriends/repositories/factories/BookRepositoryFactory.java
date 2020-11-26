package com.cmput301f20t21.bookfriends.repositories.factories;

import com.cmput301f20t21.bookfriends.BuildConfig;
import com.cmput301f20t21.bookfriends.fakes.repositories.FakeBookRepository;
import com.cmput301f20t21.bookfriends.repositories.api.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.impl.BookRepositoryImpl;

public class BookRepositoryFactory {
    private static FakeBookRepository fake = new FakeBookRepository();
    public static BookRepository getRepository() {
        if (BuildConfig.IS_TESTING.get()) {
            return fake;
        }
        return BookRepositoryImpl.getInstance();
    }
}
