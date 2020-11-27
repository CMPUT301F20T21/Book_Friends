/*
 * SCAN_ERROR.java
 * Version: 1.0
 * Date: October 16, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.enums;

public enum SCAN_ERROR {
    INVALID_ISBN, // the scanned ISBN does not match the selected book's ISBN
    UNEXPECTED // unexpected error
}
