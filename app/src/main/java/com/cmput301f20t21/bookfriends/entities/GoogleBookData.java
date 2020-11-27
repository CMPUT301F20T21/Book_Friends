/*
 * GoogleBookData.java
 * Version: 1.0
 * Date: October 16, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.entities;

public class GoogleBookData {
    int totalItems;
    Item[] items;

    public int getTotalItems() {
        return totalItems;
    }

    public String getTitle() {
        if (totalItems >= 1) {
            return items[0].volumeInfo.title;
        }
        return null;
    }

    public String[] getAuthors() {
        if (totalItems >= 1) {
            return items[0].volumeInfo.authors;
        }
        return null;
    }
}

class Item {
    VolumeInfo volumeInfo;
}

class VolumeInfo {
    String title;
    String[] authors;
}
