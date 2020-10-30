/*
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.enums;

public enum BOOK_ACTION {
    ADD(0),
    EDIT(1),
    VIEW(2);

    private int code;

    BOOK_ACTION(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
};
