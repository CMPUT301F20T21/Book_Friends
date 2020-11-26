package com.cmput301f20t21.bookfriends.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.fakes.callbacks.FakeFailCallbackWithMessage;
import com.cmput301f20t21.bookfriends.fakes.callbacks.FakeSuccessCallbackWithMessage;
import com.cmput301f20t21.bookfriends.fakes.repositories.FakeAuthRepository;
import com.cmput301f20t21.bookfriends.fakes.repositories.FakeBookRepository;
import com.cmput301f20t21.bookfriends.ui.library.add.AddEditViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AddEditBookViewModelUnitTest {
    private User user;
    // https://medium.com/pxhouse/unit-testing-with-mutablelivedata-22b3283a7819
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    FakeAuthRepository fakeAuthRepository;

    FakeBookRepository fakeBookRepository;

    @Mock
    FakeSuccessCallbackWithMessage<Book> mockSuccessCallback;

    @Mock
    FakeFailCallbackWithMessage<BOOK_ERROR> mockFailCallback;

    @Before
    public void setup() {
        user = new User("uid", "username", "email");
        fakeAuthRepository = new FakeAuthRepository();
        fakeBookRepository = new FakeBookRepository();
        fakeAuthRepository.createUserAuth("email", "password");
        fakeAuthRepository.signIn("username", "email", "password");
    }


    @Test
    public void addBookSuccess() {
        AddEditViewModel model = new AddEditViewModel(fakeAuthRepository, fakeBookRepository);
        model.bookIsbn.setValue("isbn");
        model.bookTitle.setValue("title");
        model.bookAuthor.setValue("author");
        model.handleAddBook(mockSuccessCallback, mockFailCallback);
        verify(mockSuccessCallback, times(1)).run(fakeBookRepository.getByIndex(0));

    }

    @Test
    public void editBookSuccess() {
        AddEditViewModel model = new AddEditViewModel(fakeAuthRepository, fakeBookRepository);
        fakeBookRepository.add("oldIsbn", "oldTitle", "oldAuthor", "oldOwner", null);
        Book oldBook = fakeBookRepository.getByIndex(0);
        model.bindBook(oldBook);
        model.bookIsbn.setValue("newIsbn");
        model.bookTitle.setValue("newTitle");
        model.bookAuthor.setValue("newAuthor");
        model.setHasImage(false);

        model.handleEditBook(mockSuccessCallback, mockFailCallback);
        Book newBook = new Book(oldBook.getId(), "newIsbn", "newTitle", "newAuthor", "oldOwner",  BOOK_STATUS.AVAILABLE);
        verify(mockSuccessCallback, times(1)).run(newBook);
    }
}
