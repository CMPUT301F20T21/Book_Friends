/*
 * REQUEST_STATUS.java
 * Version: 1.0
 * Date: October 16, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.enums;

public enum REQUEST_STATUS {
    OPENED,     // when a request is opened
    CLOSED,     // when a request is closed/resolved
    ACCEPTED,   // when owner accept a request
    DENIED,     // when owner deny a request
    BORROWED,   // when borrower has the book
    HANDING,    // when owner scan the book before handing to borrower
    RETURNING   // when borrower scan the book before returning to owner
}
