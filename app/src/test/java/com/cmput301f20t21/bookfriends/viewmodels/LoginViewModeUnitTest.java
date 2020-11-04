package com.cmput301f20t21.bookfriends.viewmodels;

import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.enums.LOGIN_ERROR;
import com.cmput301f20t21.bookfriends.exceptions.InvalidLoginCredentialsException;
import com.cmput301f20t21.bookfriends.exceptions.UnexpectedException;
import com.cmput301f20t21.bookfriends.exceptions.UsernameNotExistException;
import com.cmput301f20t21.bookfriends.fakes.repositories.FakeAuthRepository;
import com.cmput301f20t21.bookfriends.fakes.callbacks.FakeFailCallbackWithMessage;
import com.cmput301f20t21.bookfriends.fakes.tasks.FakeFailTask;
import com.cmput301f20t21.bookfriends.fakes.callbacks.FakeSuccessCallback;
import com.cmput301f20t21.bookfriends.fakes.tasks.FakeSuccessTask;
import com.cmput301f20t21.bookfriends.fakes.repositories.FakeUserRepository;
import com.cmput301f20t21.bookfriends.ui.login.LoginViewModel;
import com.google.firebase.auth.AuthResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginViewModeUnitTest {
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
    FakeFailCallbackWithMessage<LOGIN_ERROR> mockFailCallbackWithMessage;

    @Test
    public void loginSuccess() {
        LoginViewModel model = new LoginViewModel(mockAuthRepository, mockUserRepository);


        User user = new User(uid, email, username);

        FakeSuccessTask<User> fakeUserTask = new FakeSuccessTask(user);
        FakeSuccessTask<AuthResult> fakeAuthResultTask = new FakeSuccessTask(null);


        when(mockUserRepository.getByUsername(username)).thenReturn(fakeUserTask);
        when(mockAuthRepository.signIn(username, email, password)).thenReturn(fakeAuthResultTask);

        model.handleLogIn(username, password, mockSuccessCallback, mockFailCallbackWithMessage);
        verify(mockSuccessCallback, times(1)).run();
    }

    @Test
    public void loginFail_usernameDoesNotExist() {
        LoginViewModel model = new LoginViewModel(mockAuthRepository, mockUserRepository);

        FakeFailTask fakeUserTask = new FakeFailTask(new UsernameNotExistException());

        when(mockUserRepository.getByUsername(username)).thenReturn(fakeUserTask);

        model.handleLogIn(username, password, mockSuccessCallback, mockFailCallbackWithMessage);

        verify(mockFailCallbackWithMessage, times(1)).run(LOGIN_ERROR.CANNOT_FIND_USERNAME);
    }

    @Test
    public void loginFail_unexpectedError() {
        LoginViewModel model = new LoginViewModel(mockAuthRepository, mockUserRepository);

        FakeFailTask fakeUserTask = new FakeFailTask(new UnexpectedException());

        when(mockUserRepository.getByUsername(username)).thenReturn(fakeUserTask);

        model.handleLogIn(username, password, mockSuccessCallback, mockFailCallbackWithMessage);

        verify(mockFailCallbackWithMessage, times(1)).run(LOGIN_ERROR.UNEXPECTED);
    }

    @Test
    public void loginFail_incorrectPassword() {
        LoginViewModel model = new LoginViewModel(mockAuthRepository, mockUserRepository);
        User user = new User(uid, email, username);

        FakeSuccessTask<User> fakeUserTask = new FakeSuccessTask(user);
        FakeFailTask fakeAuthResultTask = new FakeFailTask(new InvalidLoginCredentialsException());

        when(mockUserRepository.getByUsername(username)).thenReturn(fakeUserTask);
        when(mockAuthRepository.signIn(username, email, password)).thenReturn(fakeAuthResultTask);

        model.handleLogIn(username, password, mockSuccessCallback, mockFailCallbackWithMessage);

        verify(mockFailCallbackWithMessage, times(1)).run(LOGIN_ERROR.INCORRECT_PASSWORD);
    }
}
