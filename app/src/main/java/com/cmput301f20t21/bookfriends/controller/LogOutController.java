package com.cmput301f20t21.bookfriends.controller;

import com.cmput301f20t21.bookfriends.services.AuthService;
import com.cmput301f20t21.bookfriends.services.UserService;

public class LogOutController {
    private AuthService authService;
    private ILogOutListener listener;

    public interface ILogOutListener {
        void onLogOutSuccess();
        void onLogOutFail();
    }

    public LogOutController(ILogOutListener listener) {
        authService = AuthService.getInstance();
        this.listener = listener;
    }

    public void handleLogOut() {
        try {
            authService.signOut();
            listener.onLogOutSuccess();
        } catch (Exception e) {
            listener.onLogOutFail();
        }
    }
}
