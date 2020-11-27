/*
 * BOOK_ACTION.java
 * Version: 1.0
 * Date: October 16, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */


package com.cmput301f20t21.bookfriends.enums;

public enum BOOK_ACTION {
    ADD(0), // add a book
    EDIT(1), // edit a book
    VIEW(2), // view a book
    SEND_REQUEST(3), // send request to borrow a book
    VIEW_REQUESTS(4); // view all requests of a requested book

    private int code;

    BOOK_ACTION(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
};
