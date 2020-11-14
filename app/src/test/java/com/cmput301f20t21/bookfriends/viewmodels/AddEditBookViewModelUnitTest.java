package com.cmput301f20t21.bookfriends.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.enums.LOGIN_ERROR;
import com.cmput301f20t21.bookfriends.fakes.callbacks.FakeFailCallbackWithMessage;
import com.cmput301f20t21.bookfriends.fakes.callbacks.FakeSuccessCallback;
import com.cmput301f20t21.bookfriends.fakes.callbacks.FakeSuccessCallbackWithMessage;
import com.cmput301f20t21.bookfriends.fakes.repositories.FakeAuthRepository;
import com.cmput301f20t21.bookfriends.fakes.repositories.FakeBookRepository;
import com.cmput301f20t21.bookfriends.fakes.tasks.FakeSuccessTask;
import com.cmput301f20t21.bookfriends.ui.add.AddEditViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddEditBookViewModelUnitTest {
    private User user;
    // https://medium.com/pxhouse/unit-testing-with-mutablelivedata-22b3283a7819
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    FakeAuthRepository mockAuthRepository;

    @Mock
    FakeBookRepository mockBookRepository;

    @Mock
    FakeSuccessCallbackWithMessage<Book> mockSuccessCallback;

    @Mock
    FakeFailCallbackWithMessage<BOOK_ERROR> mockFailCallback;

    @Before
    public void setup() {
        user = new User("uid", "username", "email");
        when(mockAuthRepository.getCurrentUser()).thenReturn(user);
    }


    @Test
    public void addBookSuccess() {
        AddEditViewModel model = new AddEditViewModel(mockAuthRepository, mockBookRepository);

        String id = "id";
        String isbn = "isbn";
        String title = "title";
        String author = "author";
        String description = "description";
        String owner = user.getUsername();
        FakeSuccessTask<String> fakeAddBookTask = new FakeSuccessTask(id);

        when(mockBookRepository.add(isbn, title, author, description, owner)).thenReturn(fakeAddBookTask);

        model.bookIsbn.setValue("isbn");
        model.bookTitle.setValue("title");
        model.bookAuthor.setValue("author");
        model.bookDescription.setValue("description");
        model.handleAddBook(mockSuccessCallback, mockFailCallback);
        verify(mockSuccessCallback, times(1)).run(new Book(id, isbn, title, author, description, owner, BOOK_STATUS.AVAILABLE));

    }

    @Test
    public void editBookSuccess() {
        AddEditViewModel model = new AddEditViewModel(mockAuthRepository, mockBookRepository);
        Book oldBook = new Book("oldId", "oldIsbn", "oldTitle", "oldAuthor", "oldDescription", "oldOwner",  BOOK_STATUS.AVAILABLE);

        FakeSuccessTask<Book> fakeEditBookTask = new FakeSuccessTask<>(oldBook);
        FakeSuccessTask<Void> fakeDeleteImageTask = new FakeSuccessTask<>((Void) null);

        when(mockBookRepository.editBook(oldBook, "newIsbn", "newTitle", "newAuthor", "newDescription")).thenReturn(fakeEditBookTask);

        model.bindBook(oldBook);
        model.bookIsbn.setValue("newIsbn");
        model.bookTitle.setValue("newTitle");
        model.bookAuthor.setValue("newAuthor");
        model.bookDescription.setValue("newDescription");
        model.setHasImage(true);

        model.handleEditBook(mockSuccessCallback, mockFailCallback);

        verify(mockBookRepository, times(1)).editBook(oldBook, "newIsbn", "newTitle", "newAuthor", "newDescription");
        verify(mockSuccessCallback, times(1)).run(new Book("oldId", "oldIsbn", "oldTitle", "oldAuthor", "oldDescription", "oldOwner",  BOOK_STATUS.AVAILABLE));
    }
}
