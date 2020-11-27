/*
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.enums;

public enum BOOK_STATUS {
    AVAILABLE, // no user have requested this book or all requests are rejected
    REQUESTED, // some user(s) requested this book
    ACCEPTED, // one of the user request is accepted by the owner
    BORROWED // the owner handed over the book to the borrower and borrower received the book
}
