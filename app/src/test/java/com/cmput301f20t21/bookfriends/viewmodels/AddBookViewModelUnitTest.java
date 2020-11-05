package com.cmput301f20t21.bookfriends.viewmodels;

import com.cmput301f20t21.bookfriends.fakes.repositories.FakeAuthRepository;
import com.cmput301f20t21.bookfriends.fakes.repositories.FakeBookRepository;
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
public class AddBookViewModelUnitTest {

    @Mock
    FakeAuthRepository mockAuthRepository;

    @Mock
    FakeBookRepository mockBookRepository;

//    @Test
//    public void addBookSuccess() {
//        AddEditViewModel model = new AddEditViewModel(mockBookRepository, mockAuthRepository);
//        // TODO: :(
//    }
}
