/*
 * BOOK_ERROR.java
 * Version: 1.0
 * Date: October 16, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */


package com.cmput301f20t21.bookfriends.enums;

public enum BOOK_ERROR {
    FAIL_TO_ADD_BOOK, // failed to record the action to add a book
    FAIL_TO_ADD_IMAGE, // failed to upload the book image to cloud
    FAIL_TO_EDIT_BOOK, // failed to record the action to edit a book
    FAIL_TO_DELETE_BOOK, // failed to record the action to delete a book
    FAIL_TO_GET_BOOKS, // failed to get a list of books
    FAIL_TO_GET_IMAGE, // failed to obtain the image for one or more books
    UNEXPECTED // unexpected error
}
