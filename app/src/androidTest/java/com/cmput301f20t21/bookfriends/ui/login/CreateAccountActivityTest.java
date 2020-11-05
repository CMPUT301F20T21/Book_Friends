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
public class CreateAccountActivityTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityTestRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testCreateAccountSuccess() throws InterruptedException {
        ViewInteraction createAccountButton = onView(
                allOf(withId(R.id.create_account_button), isDisplayed()));
        createAccountButton.perform(click());

        ViewInteraction usernameField = onView(
                allOf(withId(R.id.signup_username_field), isDisplayed()));
        usernameField.perform(replaceText("test5User"), closeSoftKeyboard());

        ViewInteraction passwordField = onView(
                allOf(withId(R.id.signup_password_field), isDisplayed()));
        passwordField.perform(replaceText("testPassword"), closeSoftKeyboard());

        ViewInteraction confirmPasswordField = onView(
                allOf(withId(R.id.confirm_password_field), isDisplayed()));
        confirmPasswordField.perform(replaceText("testPassword"), closeSoftKeyboard());

        ViewInteraction emailField = onView(
                allOf(withId(R.id.email_field), isDisplayed()));
        emailField.perform(replaceText("test3@gmail.com"), closeSoftKeyboard());

        ViewInteraction createButton = onView(
                allOf(withId(R.id.signup_create), isDisplayed()));
        createButton.perform(click());

        Thread.sleep(3000);

        ViewInteraction loginButton = onView(
                allOf(withId(R.id.login_btn), isDisplayed()));
        loginButton.check(matches(isDisplayed()));
    }

    @Test
    public void testIncorrectInput() throws InterruptedException {
        ViewInteraction createAccountButton = onView(
                allOf(withId(R.id.create_account_button), isDisplayed()));
        createAccountButton.perform(click());

        ViewInteraction usernameField = onView(
                allOf(withId(R.id.signup_username_field), isDisplayed()));
        usernameField.perform(replaceText("! User"), closeSoftKeyboard());

        ViewInteraction passwordField = onView(
                allOf(withId(R.id.signup_password_field), isDisplayed()));
        passwordField.perform(replaceText("tes"), closeSoftKeyboard());

        ViewInteraction confirmPasswordField = onView(
                allOf(withId(R.id.confirm_password_field), isDisplayed()));
        confirmPasswordField.perform(replaceText("incorrect Password"), closeSoftKeyboard());

        ViewInteraction emailField = onView(
                allOf(withId(R.id.email_field), isDisplayed()));
        emailField.perform(replaceText("testgmail"), closeSoftKeyboard());
        // needed for async operations (i.e. authentication)


        ViewInteraction createButton = onView(
                allOf(withId(R.id.signup_create), isDisplayed()));
        createButton.perform(click());

        // needed for async operations (i.e. authentication)
        Thread.sleep(3000);
        ViewInteraction errorUser = onView(
                allOf(withId(R.id.textinput_error), withText("Only letters and numbers are allowed"), isDisplayed()));
        errorUser.check(matches(withText("Only letters and numbers are allowed")));
        ViewInteraction passwordError = onView(
                allOf(withId(R.id.textinput_error), withText("Password should be at least 6 characters"), isDisplayed()));
        passwordError.check(matches(withText("Password should be at least 6 characters")));
        ViewInteraction confirmPassword = onView(
                allOf(withId(R.id.textinput_error), withText("Password does not match"), isDisplayed()));
        confirmPassword.check(matches(withText("Password does not match")));
        ViewInteraction emailError = onView(
                allOf(withId(R.id.textinput_error), withText("Invalid Email"), isDisplayed()));
        emailError.check(matches(withText("Invalid Email")));
    }
}
