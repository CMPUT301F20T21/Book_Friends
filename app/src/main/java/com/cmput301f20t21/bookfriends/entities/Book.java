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
    private String owner; // the owner's username
    private BOOK_STATUS status;
    private String imageUrl; // the downloadable, public url of the cover image

    // used for firebase document toObject call
    public Book() {
    }

    public Book(String id, String isbn, String title, String author, String owner, BOOK_STATUS status) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.owner = owner;
        this.status = status;
        this.imageUrl = null;
    }

    public Book(String id, String isbn, String title, String author, String owner, BOOK_STATUS status, String imageUrl) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.owner = owner;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String url) {
        this.imageUrl = url;
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

    public String getOwner() {
        return owner;
    }

    public BOOK_STATUS getStatus() {
        return status;
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
                owner.equals(book.owner) &&
                (
                        (imageUrl != null && imageUrl.equals(book.imageUrl))  || (imageUrl == null && book.imageUrl == null)
                ) &&
                status == book.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isbn, title, author, owner, status, imageUrl);
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
        dest.writeString(owner);
        dest.writeString(status.toString());
        dest.writeString(imageUrl);
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
        owner = in.readString();
        status = BOOK_STATUS.valueOf(in.readString());
        imageUrl = in.readString();
    }
}
