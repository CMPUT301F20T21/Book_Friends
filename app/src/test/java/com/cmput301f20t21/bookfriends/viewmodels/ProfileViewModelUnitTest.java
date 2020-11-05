package com.cmput301f20t21.bookfriends.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.exceptions.UserNotExistException;
import com.cmput301f20t21.bookfriends.fakes.callbacks.FakeFailCallback;
import com.cmput301f20t21.bookfriends.fakes.callbacks.FakeSuccessCallbackWithMessage;
import com.cmput301f20t21.bookfriends.fakes.repositories.FakeUserRepository;
import com.cmput301f20t21.bookfriends.fakes.tasks.FakeFailTask;
import com.cmput301f20t21.bookfriends.fakes.tasks.FakeSuccessTask;
import com.cmput301f20t21.bookfriends.ui.profile.ProfileViewModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProfileViewModelUnitTest {
    // https://medium.com/pxhouse/unit-testing-with-mutablelivedata-22b3283a7819
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();
    @Mock
    FakeUserRepository mockUserRepository;
    // mock the observer. we want it to receive changes and we want to know what change it got
    @Mock
    Observer<ArrayList<User>> usersObserver;
    // the captor is used specifically for the observer, to catch what the observer received, used to get value and assert
    @Captor
    ArgumentCaptor<ArrayList<User>> usersResultArgument;

    @Mock
    FakeFailCallback fakeFailUserCallback;
    @Mock
    FakeSuccessCallbackWithMessage<User> fakeSuccessUserCallback;

    @Test
    public void searchSuccess_noResultsOnEmptyInput() {
        ProfileViewModel vm = new ProfileViewModel(mockUserRepository);

        // start the mock observer
        vm.getSearchedUsers().observeForever(usersObserver);

        // setup fake parameter to vm
        String usernameQuery = "";
        // setup fake response to vm
        FakeSuccessTask<List<User>> usersTask = new FakeSuccessTask<>(new ArrayList<>());
        when(mockUserRepository.getByUsernameStartWith(usernameQuery)).thenReturn(usersTask);

        vm.updateSearchQuery(usernameQuery);

        // the first observe call is an empty array
        verify(usersObserver, times(2)).onChanged(usersResultArgument.capture());
        verify(mockUserRepository).getByUsernameStartWith(usernameQuery);
        // assert that the results given from vm and repo are the same
        ArrayList<User> usersResult = usersResultArgument.getValue(); // getting the latest result
        assertEquals(usersResult, new ArrayList<User>());
    }

    @Test
    public void searchSuccess_noMultipleResults() {
        ProfileViewModel vm = new ProfileViewModel(mockUserRepository);

        // start the mock observer
        vm.getSearchedUsers().observeForever(usersObserver);

        String usernameQuery = "test";
        ArrayList<User> usersResultReal = new ArrayList<>();
        usersResultReal.add(new User("uid1", "test1", "no1@no.no"));
        usersResultReal.add(new User("uid2", "test2", "no2@no.no"));
        usersResultReal.add(new User("uid3", "test3", "no3@no.no"));
        FakeSuccessTask<List<User>> usersTask = new FakeSuccessTask<>(usersResultReal);

        when(mockUserRepository.getByUsernameStartWith(usernameQuery)).thenReturn(usersTask);

        vm.updateSearchQuery(usernameQuery);

        // the first observe call is an empty array
        verify(usersObserver, times(2)).onChanged(usersResultArgument.capture());
        verify(mockUserRepository).getByUsernameStartWith(usernameQuery);
        // assert that the results given from vm and repo are the same
        ArrayList<User> usersResult = usersResultArgument.getValue();
        assertEquals(usersResult, usersResultReal);
    }

    @Test
    public void getUserByUidSuccess() {
        // TODO
    }

    @Test
    public void getUserByUidFailure() {
        ProfileViewModel vm = new ProfileViewModel(mockUserRepository);
        String uid = "testUid";

        FakeSuccessCallbackWithMessage<User> mockOnSuccess = new FakeSuccessCallbackWithMessage<>();
        FakeFailTask<User> fakeTask = new FakeFailTask<>(new UserNotExistException());

        when(mockUserRepository.getByUid(uid)).thenReturn(fakeTask);
        vm.getUserByUid(uid, mockOnSuccess, fakeFailUserCallback);

        verify(fakeFailUserCallback, times(1)).run();
    }
}
