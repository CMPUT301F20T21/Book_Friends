/*
 * User.java
 * Version: 1.0
 * Date: October 16, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.entities;

import com.google.firebase.firestore.DocumentId;

/**
 * User entity object
 */
public class User {
    @DocumentId
    private String uid;
    private String username;
    private String email;

    public User() {
        this("", "", "");
    }

    public User(String uid, String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {return username; }

    public String getEmail() {
        return email;
    }
}
