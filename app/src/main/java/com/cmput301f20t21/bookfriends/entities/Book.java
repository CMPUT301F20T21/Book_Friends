package com.cmput301f20t21.bookfriends.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.google.firebase.firestore.DocumentId;

import java.util.Objects;

public class Book implements Parcelable {
    @DocumentId
    private String id;
    private String isbn;
    private String title;
    private String author;
    private String description;
    private String owner; // the owner's username
    private BOOK_STATUS status;

    // used for firebase document toObject call
    public Book() {
    }

    public Book(String id, String isbn, String title, String author, String description, String owner, BOOK_STATUS status) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.description = description;
        this.owner = owner;
        this.status = status;
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

    public BOOK_STATUS getStatus() {
        return status;
    }

    public String getCoverImageName() {
        return getId() + "cover";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id.equals(book.id) &&
                isbn.equals(book.isbn) &&
                title.equals(book.title) &&
                author.equals(book.author) &&
                Objects.equals(description, book.description) &&
                owner.equals(book.owner) &&
                status == book.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isbn, title, author, description, owner, status);
    }

    // Implement parcelable boilerplate
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(isbn);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(description);
        dest.writeString(owner);
        dest.writeString(status.toString());
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    private Book(Parcel in) {
        id = in.readString();
        isbn = in.readString();
        title = in.readString();
        author = in.readString();
        description = in.readString();
        owner = in.readString();
        status = BOOK_STATUS.valueOf(in.readString());
    }
}
