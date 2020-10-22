package com.cmput301f20t21.bookfriends.entities;

public class User {
    private String uid;
    private String username;
    private String displayName;
    private String email;
    private String phoneNumber;

    public User(String uid, String username, String displayName, String email, String phoneNumber) {
        this.uid = uid;
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {return username; }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
