package com.cmput301f20t21.bookfriends.entities;

import android.net.Uri;

import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;

import javax.annotation.Nullable;

public class Book {
    private String id;
    private String isbn;
    private String title;
    private String author;
    private String description;
    private String owner; // the owner's username
    private String imageName;
    private BOOK_STATUS status;
    private Uri imageUri;

    public Book(String id, String isbn, String title, String author, String description, String owner, String imageName, BOOK_STATUS status) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.description = description;
        this.owner = owner;
        this.imageName = imageName;
        this.status = status;
    }

    public Book(String id, String isbn, String title, String author, String description, String owner, String imageName, BOOK_STATUS status, Uri imageUri) {
        this(id, isbn, title, author, description, owner, imageName, status);
        this.imageUri = imageUri;
    }

    public String getId() {
        return id;
    }
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getOwner() {
        return owner;
    }

    public String getImageName() {
        return imageName;
    }

    public BOOK_STATUS getBookStatus() {
        return status;
    }

    @Nullable
    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
