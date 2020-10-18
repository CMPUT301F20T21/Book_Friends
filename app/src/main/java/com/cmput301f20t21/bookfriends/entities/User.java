package com.cmput301f20t21.bookfriends.entities;

public class User {
    private String id;
    private String displayName;
    private String email;
    private String phoneNumber;

    public User(String id, String displayName, String email, String phoneNumber) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

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
