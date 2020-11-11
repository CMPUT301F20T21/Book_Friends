package com.cmput301f20t21.bookfriends.viewmodels;

import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.enums.SIGNUP_ERROR;
import com.cmput301f20t21.bookfriends.fakes.callbacks.FakeFailCallbackWithMessage;
import com.cmput301f20t21.bookfriends.fakes.callbacks.FakeSuccessCallback;
import com.cmput301f20t21.bookfriends.fakes.repositories.FakeAuthRepository;
import com.cmput301f20t21.bookfriends.fakes.repositories.FakeUserRepository;
import com.cmput301f20t21.bookfriends.fakes.tasks.FakeSuccessTask;
import com.cmput301f20t21.bookfriends.ui.login.CreateAccountViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateAccountViewModelUnitTest {
    private String uid = "uid";
    private String username = "username";
    private String email = "email";
    private String password = "password";

    @Mock
    FakeAuthRepository mockAuthRepository;

    @Mock
    FakeUserRepository mockUserRepository;

    @Mock
    FakeSuccessCallback mockSuccessCallback;

    @Mock
    FakeFailCallbackWithMessage<SIGNUP_ERROR> mockFailCallbackWithMessage;

    @Test
    public void signUpSuccess() {
        CreateAccountViewModel model = new CreateAccountViewModel(mockAuthRepository, mockUserRepository);
        User user = new User(uid, username, email);

        FakeSuccessTask<User> fakeUserTask = new FakeSuccessTask(user);


        when(mockUserRepository.getByUsername(username)).thenReturn(fakeUserTask);


        model.handleSignUp(username, email, password, mockSuccessCallback, mockFailCallbackWithMessage);
        verify(mockFailCallbackWithMessage, times(1)).run(SIGNUP_ERROR.USERNAME_EXISTS);

    }

}
