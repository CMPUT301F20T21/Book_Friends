package com.cmput301f20t21.bookfriends.viewmodels;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.fakes.callbacks.FakeSuccessCallback;
import com.cmput301f20t21.bookfriends.fakes.repositories.FakeAuthRepository;
import com.cmput301f20t21.bookfriends.fakes.repositories.FakeBookRepository;
import com.cmput301f20t21.bookfriends.fakes.tasks.FakeSuccessTask;
import com.cmput301f20t21.bookfriends.ui.add.AddEditViewModel;

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

    @Mock
    FakeAuthRepository mockAuthRepository;

    @Mock
    FakeBookRepository mockBookRepository;

    @Test
    public void addBookSuccess() {
        AddEditViewModel model = new AddEditViewModel(mockAuthRepository, mockBookRepository);
//        Book book = new Book()
//        FakeSuccessTask<Book> task = new FakeSuccessTask<>();
    }

    @Test
    public void editBookSuccess() {
        AddEditViewModel model = new AddEditViewModel(mockAuthRepository, mockBookRepository);
    }
}
