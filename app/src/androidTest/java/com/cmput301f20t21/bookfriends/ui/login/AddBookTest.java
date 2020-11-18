package com.cmput301f20t21.bookfriends.ui.login;


import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.repositories.impl.BookRepositoryImpl;
import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddBookTest {

    BookRepositoryImpl bookRepository = BookRepositoryImpl.getInstance();
    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityTestRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setup() throws InterruptedException {
        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.login_username_field), isDisplayed()));
        textInputEditText.perform(replaceText("trung"), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.login_password_field), isDisplayed()));
        textInputEditText2.perform(replaceText("123456"), closeSoftKeyboard());

        ViewInteraction constraintLayout = onView(
                allOf(withId(R.id.login_btn), isDisplayed()));
        constraintLayout.perform(click());

        Thread.sleep(3000);

        ViewInteraction floatingAddButton = onView(
                allOf(withId(R.id.add_button), isDisplayed()));
        floatingAddButton.check(matches(isDisplayed()));
        floatingAddButton.perform(click());
    }

    @Test
    public void testISBN() throws InterruptedException {

        ViewInteraction isbnEditText = onView(
                allOf(withId(R.id.isbn_edit_text), isDisplayed()));
        isbnEditText.perform(replaceText("123456789"), closeSoftKeyboard());
        isbnEditText.check(matches(withText("123456789")));
    }

    @Test
    public void testTitle() throws InterruptedException {
        ViewInteraction titleEditText = onView(
                allOf(withId(R.id.title_field), isDisplayed()));
        titleEditText.perform(replaceText("Test title"), closeSoftKeyboard());
        titleEditText.check(matches(withText("Test title")));
    }

    @Test
    public void testAuthor() throws InterruptedException {
        ViewInteraction authorEditText = onView(
                allOf(withId(R.id.author_field), isDisplayed()));
        authorEditText.perform(replaceText("Test author"), closeSoftKeyboard());
        authorEditText.check(matches(withText("Test author")));
    }

    @Test
    public void testDescription() throws InterruptedException {
        ViewInteraction descriptionEditText = onView(
                allOf(withId(R.id.description_field), isDisplayed()));
        descriptionEditText.perform(replaceText("Test description... Random Stuff"), closeSoftKeyboard());
        descriptionEditText.check(matches(withText("Test description... Random Stuff")));
    }

    @Test
    public void testAddBook() throws  InterruptedException {
        testISBN();
        testTitle();
        testAuthor();
        testDescription();

        ViewInteraction saveButton = onView(allOf(withId(R.id.save_button), isDisplayed()));
        saveButton.perform(click());
    }

    @After
    public void tearDown() throws InterruptedException {
        // TODO: delete the book
        bookRepository.getDocumentBy("123456789",
                "Test title",
                "Test author").addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            for (int i = 0; i < documents.size(); i++) {
                DocumentSnapshot document = documents.get(i);
                bookRepository.delete(document.getId());
            }
        });
    }
}
