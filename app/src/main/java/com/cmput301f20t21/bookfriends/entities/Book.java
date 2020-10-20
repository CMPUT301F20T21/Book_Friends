package com.cmput301f20t21.bookfriends.entities;

public class Book {
    private String ownerId;
    private String isbn;

    public Book(String ownerId, String isbn) {
        this.ownerId = ownerId;
        this.isbn = isbn;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getIsbn() {
        return isbn;
    }
}
