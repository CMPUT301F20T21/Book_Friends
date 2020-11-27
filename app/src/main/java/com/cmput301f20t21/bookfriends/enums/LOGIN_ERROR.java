/*
 * LOGIN_ERROR.java
 * Version: 1.0
 * Date: October 16, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */


package com.cmput301f20t21.bookfriends.enums;

public enum LOGIN_ERROR {
    CANNOT_FIND_USERNAME, // username does not exist in the system
    INCORRECT_PASSWORD, // the username exist but the password is incorrect
    UNEXPECTED // unexpected error
}
