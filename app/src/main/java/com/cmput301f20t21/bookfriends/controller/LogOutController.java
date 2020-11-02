package com.cmput301f20t21.bookfriends.controller;

import com.cmput301f20t21.bookfriends.repositories.AuthRepository;

public class LogOutController {
    private AuthRepository authRepository;
    private ILogOutListener listener;

    public interface ILogOutListener {
        void onLogOutSuccess();
        void onLogOutFail();
    }

    public LogOutController(ILogOutListener listener) {
        authRepository = AuthRepository.getInstance();
        this.listener = listener;
    }

    public void handleLogOut() {
        try {
            authRepository.signOut();
            listener.onLogOutSuccess();
        } catch (Exception e) {
            listener.onLogOutFail();
        }
    }
}
