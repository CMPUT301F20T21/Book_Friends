/*
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.login;


import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.cmput301f20t21.bookfriends.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityTestRule = new ActivityScenarioRule<>(LoginActivity.class);


    @Test
    public void testLoginSuccess() throws InterruptedException {
        ViewInteraction usernameField = onView(
                allOf(withId(R.id.login_username_field), isDisplayed()));
        usernameField.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction passwordField = onView(
                allOf(withId(R.id.login_password_field), isDisplayed()));
        passwordField.perform(replaceText("bookfriendstest"), closeSoftKeyboard());

        ViewInteraction loginButton = onView(
                allOf(withId(R.id.login_btn), isDisplayed()));
        loginButton.perform(click());

        // needed for async operations (i.e. authentication)
        Thread.sleep(3000);

        ViewInteraction addButton = onView(
                allOf(withId(R.id.add_button), isDisplayed()));
        addButton.check(matches(isDisplayed()));
    }

    @Test
    public void testIncorrectPassword() throws InterruptedException {
        ViewInteraction usernameField = onView(
                allOf(withId(R.id.login_username_field), isDisplayed()));
        usernameField.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction passwordField = onView(
                allOf(withId(R.id.login_password_field), isDisplayed()));
        passwordField.perform(replaceText("incorrect password"), closeSoftKeyboard());

        ViewInteraction loginButton = onView(
                allOf(withId(R.id.login_btn), isDisplayed()));
        loginButton.perform(click());

        // needed for async operations (i.e. authentication)
        Thread.sleep(3000);

        ViewInteraction errorTextView = onView(
                allOf(withId(R.id.textinput_error), withText("Password is incorrect"), isDisplayed()));
        errorTextView.check(matches(withText("Password is incorrect")));
    }

    @Test
    public void testIncorrectUsername() throws InterruptedException {
        ViewInteraction usernameField = onView(
                allOf(withId(R.id.login_username_field), isDisplayed()));
        usernameField.perform(replaceText("Username WiTh Spaces"), closeSoftKeyboard());

        ViewInteraction passwordField = onView(
                allOf(withId(R.id.login_password_field), isDisplayed()));
        passwordField.perform(replaceText("password does not matter"), closeSoftKeyboard());

        ViewInteraction loginButton = onView(
                allOf(withId(R.id.login_btn), isDisplayed()));
        loginButton.perform(click());

        // needed for async operations (i.e. authentication)
        Thread.sleep(3000);

        ViewInteraction errorTextView = onView(
                allOf(withId(R.id.textinput_error), withText("Cannot find username"), isDisplayed()));
        errorTextView.check(matches(withText("Cannot find username")));
    }

    @Test
    public void testOpenSignup() {
        ViewInteraction createAccountButton = onView(
                allOf(withId(R.id.create_account_button), isDisplayed()));
        createAccountButton.perform(click());

        ViewInteraction createButton = onView(
                allOf(withId(R.id.signup_create), isDisplayed()));
        createButton.check(matches(isDisplayed()));
    }

}
