package com.cmput301f20t21.bookfriends.fakes.repositories;

import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.fakes.tasks.FakeFailTask;
import com.cmput301f20t21.bookfriends.fakes.tasks.FakeSuccessTask;
import com.cmput301f20t21.bookfriends.repositories.api.UserRepository;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class FakeUserRepository implements UserRepository {
    private List<User> userList;

    public FakeUserRepository() {
        userList = new ArrayList<>();
    }

    @Override
    public Task<Void> add(String uid, String username, String email) {
        userList.add(new User(uid, username, email));
        return new FakeSuccessTask(null);
    }

    @Override
    public Task<User> getByUsername(String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return new FakeSuccessTask(user);
            }
        }
        return new FakeFailTask(new Exception());
    }

    @Override
    public Task<User> getByUid(String uid) {
        for (User user : userList) {
            if (user.getUid().equals(uid)) {
                return new FakeSuccessTask(user);
            }
        }
        return new FakeFailTask(new Exception());
    }

    @Override
    public Task<List<User>> getByUsernameStartWith(String username) {
        List<User> list = new ArrayList();
        for (User user : userList) {
            if (user.getUsername().startsWith(username)) {
                list.add(user);
            }
        }
        return new FakeSuccessTask(list);
    }

    @Override
    public Task<Void> updateUserEmail(User user, String email) {
        for (User u : userList) {
            if (u.getUid().equals(user.getUid())) {
                userList.remove(u);
                userList.add(new User(u.getUid(), u.getUsername(), email));
                return new FakeSuccessTask(null);
            }
        }

        return new FakeFailTask(new Exception());
    }
}
