package com.cmput301f20t21.bookfriends.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.fakes.repositories.FakeAuthRepository;
import com.cmput301f20t21.bookfriends.fakes.repositories.FakeBookRepository;
import com.cmput301f20t21.bookfriends.ui.library.owned.OwnedViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OwnedViewModelTest {
    private FakeAuthRepository fakeAuthRepository;
    private FakeBookRepository fakeBookRepository;
    private Book book1 = new Book("1", "isbn1", "title1", "author1", "username", BOOK_STATUS.AVAILABLE);
    private Book book2 = new Book("2", "isbn2", "title2", "author1", "test", BOOK_STATUS.AVAILABLE);
    private Book book3 = new Book("3", "isbn3", "title3", "author2", "username", BOOK_STATUS.ACCEPTED);
    private Book book4 = new Book("4", "isbn4", "title4", "author2", "username", BOOK_STATUS.ACCEPTED);
    private Book book5 = new Book("5", "isbn5", "title5", "author3", "test", BOOK_STATUS.BORROWED);
    private Book book6 = new Book("6", "isbn6", "title6", "author3", "username", BOOK_STATUS.AVAILABLE);
    private Book book7 = new Book("7", "isbn7", "title7", "author4", "username", BOOK_STATUS.REQUESTED);
    private Book book8 = new Book("8", "isbn8", "title8", "author4", "test", BOOK_STATUS.REQUESTED);

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    Observer<List<Book>> booksObserver;
    @Captor
    ArgumentCaptor<List<Book>> booksResultArgument;

    @Mock
    Observer<BOOK_ERROR> errorObserver;
    @Captor
    ArgumentCaptor<BOOK_ERROR> errorResultArgument;

    public OwnedViewModelTest() {
        fakeAuthRepository = new FakeAuthRepository();
        fakeBookRepository = new FakeBookRepository();
        fakeAuthRepository.createUserAuth("email", "password");
        fakeAuthRepository.signIn("username", "email", "password");
    }

    @Before
    public void setup() {
        // populate book database
        fakeBookRepository.add(book1);
        fakeBookRepository.add(book2);
        fakeBookRepository.add(book3);
        fakeBookRepository.add(book4);
        fakeBookRepository.add(book5);
        fakeBookRepository.add(book6);
        fakeBookRepository.add(book7);
        fakeBookRepository.add(book8);
    }

    @After
    public void tearDown() {
        fakeBookRepository.clear();
    }

    @Test
    public void testFetchBooksAllFilters() {
        OwnedViewModel vm = new OwnedViewModel(fakeAuthRepository, fakeBookRepository);

        vm.getBooks().observeForever(booksObserver);
        verify(booksObserver, times(1)).onChanged(booksResultArgument.capture());

        List<Book> expected = new ArrayList();
        expected.add(book1);
        expected.add(book3);
        expected.add(book4);
        expected.add(book6);
        expected.add(book7);
        assertEquals(booksResultArgument.getValue(), expected);
    }

    @Test
    public void testFetchBooksNoFilters() {
        OwnedViewModel vm = new OwnedViewModel(fakeAuthRepository, fakeBookRepository);

        vm.getBooks().observeForever(booksObserver);
        vm.filterBooks(false, false, false, false);
        verify(booksObserver, times(2)).onChanged(booksResultArgument.capture());

        List<Book> expected = new ArrayList();
        assertEquals(booksResultArgument.getValue(), expected);
    }

    @Test
    public void testFetchBooksFilterAvailable() {
        OwnedViewModel vm = new OwnedViewModel(fakeAuthRepository, fakeBookRepository);

        vm.getBooks().observeForever(booksObserver);
        vm.filterBooks(true, false, false, false);
        verify(booksObserver, times(2)).onChanged(booksResultArgument.capture());

        List<Book> expected = new ArrayList();
        expected.add(book1);
        expected.add(book6);
        assertEquals(booksResultArgument.getValue(), expected);
    }

    @Test
    public void testFetchBooksFilterRequested() {
        OwnedViewModel vm = new OwnedViewModel(fakeAuthRepository, fakeBookRepository);

        vm.getBooks().observeForever(booksObserver);
        vm.filterBooks(false, true, false, false);
        verify(booksObserver, times(2)).onChanged(booksResultArgument.capture());

        List<Book> expected = new ArrayList();
        expected.add(book7);
        assertEquals(booksResultArgument.getValue(), expected);
    }

    @Test
    public void testFetchBooksFilterAccepted() {
        OwnedViewModel vm = new OwnedViewModel(fakeAuthRepository, fakeBookRepository);

        vm.getBooks().observeForever(booksObserver);
        vm.filterBooks(false, false, true, false);
        verify(booksObserver, times(2)).onChanged(booksResultArgument.capture());

        List<Book> expected = new ArrayList();
        expected.add(book3);
        expected.add(book4);
        assertEquals(booksResultArgument.getValue(), expected);
    }

    @Test
    public void testFetchBooksFilterBorrowed() {
        OwnedViewModel vm = new OwnedViewModel(fakeAuthRepository, fakeBookRepository);

        vm.getBooks().observeForever(booksObserver);
        vm.filterBooks(false, false, false, true);
        verify(booksObserver, times(2)).onChanged(booksResultArgument.capture());

        List<Book> expected = new ArrayList();
        assertEquals(booksResultArgument.getValue(), expected);
    }

    @Test
    public void testAddBook() {
        OwnedViewModel vm = new OwnedViewModel(fakeAuthRepository, fakeBookRepository);

        vm.getBooks().observeForever(booksObserver);


        Book newBook = new Book("new_id", "new_isbn", "new_title", "new_author", "username", BOOK_STATUS.AVAILABLE);
        vm.addBook(newBook);

        List<Book> expected = new ArrayList();
        expected.add(book1);
        expected.add(book3);
        expected.add(book4);
        expected.add(book6);
        expected.add(book7);
        expected.add(newBook);

        verify(booksObserver, times(2)).onChanged(booksResultArgument.capture());
        assertEquals(booksResultArgument.getValue(), expected);
    }

    @Test
    public void testUpdateBookSuccess() {
        OwnedViewModel vm = new OwnedViewModel(fakeAuthRepository, fakeBookRepository);

        vm.getBooks().observeForever(booksObserver);


        Book updatedBook = new Book("update_id", "update_isbn", "update_title", "update_author", "username", BOOK_STATUS.AVAILABLE);
        vm.updateBook(book1, updatedBook);

        List<Book> expected = new ArrayList();
        expected.add(updatedBook);
        expected.add(book3);
        expected.add(book4);
        expected.add(book6);
        expected.add(book7);

        verify(booksObserver, times(2)).onChanged(booksResultArgument.capture());
        assertEquals(booksResultArgument.getValue(), expected);
    }

    @Test
    public void testUpdateBookFail() {
        OwnedViewModel vm = new OwnedViewModel(fakeAuthRepository, fakeBookRepository);

        vm.getBooks().observeForever(booksObserver);
        vm.getErrorMessageObserver().observeForever(errorObserver);

        Book updatedBook = new Book("2", "update_isbn", "update_title", "update_author", "username", BOOK_STATUS.AVAILABLE);

        // book2 is not owned by current user
        vm.updateBook(book2, updatedBook);

        List<Book> expected = new ArrayList();
        expected.add(book1);
        expected.add(book3);
        expected.add(book4);
        expected.add(book6);
        expected.add(book7);

        verify(booksObserver, times(1)).onChanged(booksResultArgument.capture());
        verify(errorObserver, times(1)).onChanged(errorResultArgument.capture());
        assertEquals(booksResultArgument.getValue(), expected);
        assertEquals(errorResultArgument.getValue(), BOOK_ERROR.FAIL_TO_EDIT_BOOK);
    }

    @Test
    public void testDeleteBook() {
        OwnedViewModel vm = new OwnedViewModel(fakeAuthRepository, fakeBookRepository);
        vm.getBooks().observeForever(booksObserver);

        vm.deleteBook(book1);

        List<Book> expected = new ArrayList();
        expected.add(book3);
        expected.add(book4);
        expected.add(book6);
        expected.add(book7);

        verify(booksObserver, times(2)).onChanged(booksResultArgument.capture());
        assertEquals(booksResultArgument.getValue(), expected);
    }
}
