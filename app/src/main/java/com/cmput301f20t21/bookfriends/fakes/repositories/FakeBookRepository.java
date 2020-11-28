package com.cmput301f20t21.bookfriends.fakes.repositories;

import android.net.Uri;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.fakes.tasks.FakeFailTask;
import com.cmput301f20t21.bookfriends.fakes.tasks.FakeSuccessTask;
import com.cmput301f20t21.bookfriends.repositories.api.BookRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FakeBookRepository implements BookRepository {
    List<Book> books;
    public FakeBookRepository() {
        books = new ArrayList<>();
    }

    @Override
    public Task<Book> add(String isbn, String title, String author, String owner, String imageUrl) {
        Book book = new Book(UUID.randomUUID().toString(), isbn, title, author, owner, BOOK_STATUS.AVAILABLE);
        books.add(book);
        return new FakeSuccessTask(book);
    }

    @Override
    public Task<String> addImage(String bookId, Uri imageUri) {
        return null;
    }

    @Override
    public Task<Book> editBook(Book oldBook, String isbn, String title, String author, String imageUrl) {
        int i = books.indexOf(oldBook);
        if (i != -1) {
            Book newBook = new Book(oldBook.getId(), isbn, title, author, oldBook.getOwner(), oldBook.getStatus(), imageUrl);
            books.set(i, newBook);
            return new FakeSuccessTask(newBook);
        }
        return new FakeFailTask(new Exception("edit Book failed: failed to update with data"));
    }

    @Override
    public Task<Void> delete(String id) {
        for (Book book: books) {
            if (book.getId().equals(id)) {
                books.remove(book);
                return new FakeSuccessTask(null);
            }
        }
        return new FakeFailTask(new Exception());
    }

    @Override
    public Task<Void> deleteImage(String imageName) {
        return null;
    }

    @Override
    public Task<List<Book>> getBooksOfOwnerId(String userName) {
        List<Book> list = new ArrayList<>();
        for (Book book: books) {
            if (book.getOwner().equals(userName)) {
                list.add(book);
            }
        }
        return new FakeSuccessTask(list);
    }

    @Override
    public Task<Book> getBookById(String bookId) {
        for (Book book: books) {
            if (book.getId().equals(bookId)) {
                return new FakeSuccessTask(book);
            }
        }
        return new FakeFailTask(new Exception());
    }

    @Override
    public Task<List<Book>> batchGetBooks(List<String> bookIds) {
        List<Book> list = new ArrayList<>();
        for (Book book: books) {
            if (bookIds.indexOf(book.getId()) != -1) {
                list.add(book);
            }
        }
        return new FakeSuccessTask(list);
    }

    @Override
    public Task<List<Book>> getAvailableBooksForUser(String username) {
        List<Book> list = new ArrayList<>();
        for (Book book: books) {
            if (!book.getOwner().equals(username) && book.getStatus().equals(BOOK_STATUS.AVAILABLE)) {
                list.add(book);
            }
        }
        return new FakeSuccessTask(list);
    }

    @Override
    public Task<Book> updateBookStatus(Book book, BOOK_STATUS newStatus) {
        int i = books.indexOf(book);
        if (i != -1) {
            Book newBook = new Book(
                    book.getId(),
                    book.getIsbn(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getOwner(),
                    newStatus,
                    book.getImageUrl());
            books.set(i, newBook);
            return new FakeSuccessTask(newBook);
        }
        return new FakeFailTask(new Exception());
    }

    public Book getByIndex(int i) {
        return books.get(i);
    }


    public void add(Book book) {
        books.add(book);
    }

    public void clear() {
        books.clear();
    }
}
