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
