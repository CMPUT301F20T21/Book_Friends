package com.cmput301f20t21.bookfriends.viewmodels;

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
import org.junit.Test;
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

        model.handleAddBook(isbn, title, author, description, null, mockSuccessCallback, mockFailCallback);
        verify(mockSuccessCallback, times(1)).run(new Book(id, isbn, title, author, description, owner, BOOK_STATUS.AVAILABLE));

    }

    @Test
    public void editBookSuccess() {
        AddEditViewModel model = new AddEditViewModel(mockAuthRepository, mockBookRepository);
        Book oldBook = new Book("oldId", "oldIsbn", "oldTitle", "oldAuthor", "oldDescription", "oldOwner",  BOOK_STATUS.AVAILABLE);
        FakeSuccessTask<Book> fakeEditBookTask = new FakeSuccessTask(oldBook);

        when(mockBookRepository.editBook(oldBook, "newIsbn", "newTitle", "newAuthor", "newDescription")).thenReturn(fakeEditBookTask);

        model.handleEditBook(oldBook, "newIsbn", "newTitle", "newAuthor", "newDescription", null, mockSuccessCallback, mockFailCallback);
        verify(mockSuccessCallback, times(1)).run(oldBook);
    }
}
