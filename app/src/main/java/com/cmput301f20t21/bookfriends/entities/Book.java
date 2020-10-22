package com.cmput301f20t21.bookfriends.entities;

public class Book {

    // TODO not done
    private String isbn;
    private String author;

    private String owner; // the owner User.id
    private String title;

    // TODO testing constructor, to be replaced
    public Book(String title) {
        this.isbn = "12345678";
        this.author = "J.R.R. Tolkien";
        this.owner = "lucas";
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getAuthor() {
        return author;
    }

    public String getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }
}
