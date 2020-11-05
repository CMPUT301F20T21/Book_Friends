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
public class ProfileEditTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityTestRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void profileEditEmailSuccess() throws InterruptedException {
        ViewInteraction nameField = onView(
                allOf(withId(R.id.login_username_field), isDisplayed()));
        nameField.perform(replaceText("Q"), closeSoftKeyboard());

        ViewInteraction passwordField = onView(
                allOf(withId(R.id.login_password_field), isDisplayed()));
        passwordField.perform(replaceText("000000"), closeSoftKeyboard());

        ViewInteraction loginButton = onView(
                allOf(withId(R.id.login_btn), isDisplayed()));
        loginButton.perform(click());
        Thread.sleep(3000);

        ViewInteraction navigationToMe = onView(
                allOf(withId(R.id.navigation_profile), isDisplayed()));
        navigationToMe.check(matches(isDisplayed()));
        navigationToMe.perform(click());

        ViewInteraction editIcon = onView(
                allOf(withId(R.id.image_edit), isDisplayed()));
        editIcon.perform(click());

        ViewInteraction editEmailField = onView(
                allOf(withId(R.id.edit_email), withText("0@gmail.com"), isDisplayed()));
        editEmailField.perform(replaceText("1@gmail.com"));

        ViewInteraction onConfirm = onView(
                allOf(withId(R.id.text_confirm), withText("CONFIRM"), isDisplayed()));
        onConfirm.perform(click());

        Thread.sleep(3000);

        ViewInteraction editIconAgain = onView(
                allOf(withId(R.id.image_edit), isDisplayed()));
        editIconAgain.perform(click());

        ViewInteraction editEmailBack = onView(
                allOf(withId(R.id.edit_email), withText("1@gmail.com"), isDisplayed()));
        editEmailBack.perform(replaceText("0@gmail.com"));

        ViewInteraction ConfirmAgain = onView(
                allOf(withId(R.id.text_confirm), withText("CONFIRM"), isDisplayed()));
        ConfirmAgain.perform(click());

        ViewInteraction logout = onView(
                allOf(withId(R.id.logout_button), isDisplayed()));
        logout.perform(click());
    }

    @Test
    public void profileEditEmailUnchanged() throws InterruptedException {
        ViewInteraction nameField = onView(
                allOf(withId(R.id.login_username_field), isDisplayed()));
        nameField.perform(replaceText("Q"), closeSoftKeyboard());

        ViewInteraction passwordField = onView(
                allOf(withId(R.id.login_password_field), isDisplayed()));
        passwordField.perform(replaceText("000000"), closeSoftKeyboard());

        ViewInteraction loginButton = onView(
                allOf(withId(R.id.login_btn), isDisplayed()));
        loginButton.perform(click());
        Thread.sleep(3000);

        ViewInteraction navigationToMe = onView(
                allOf(withId(R.id.navigation_profile), isDisplayed()));
        navigationToMe.check(matches(isDisplayed()));
        navigationToMe.perform(click());

        ViewInteraction editIcon = onView(
                allOf(withId(R.id.image_edit), isDisplayed()));
        editIcon.perform(click());

        ViewInteraction editEmailField = onView(
                allOf(withId(R.id.edit_email), withText("0@gmail.com"), isDisplayed()));
        editEmailField.perform(replaceText("testprofile@"));

        ViewInteraction onCancel = onView(
                allOf(withId(R.id.text_cancel), withText("CANCEL"), isDisplayed()));
        onCancel.perform(click());

        ViewInteraction logout = onView(
                allOf(withId(R.id.logout_button), isDisplayed()));
        logout.perform(click());
    }

    @Test
    public void profileEditEmailInvalid() throws InterruptedException {
        ViewInteraction nameField = onView(
                allOf(withId(R.id.login_username_field), isDisplayed()));
        nameField.perform(replaceText("Q"), closeSoftKeyboard());

        ViewInteraction passwordField = onView(
                allOf(withId(R.id.login_password_field), isDisplayed()));
        passwordField.perform(replaceText("000000"), closeSoftKeyboard());

        ViewInteraction loginButton = onView(
                allOf(withId(R.id.login_btn), isDisplayed()));
        loginButton.perform(click());
        Thread.sleep(3000);

        ViewInteraction navigationToMe = onView(
                allOf(withId(R.id.navigation_profile), isDisplayed()));
        navigationToMe.check(matches(isDisplayed()));
        navigationToMe.perform(click());

        ViewInteraction editIcon = onView(
                allOf(withId(R.id.image_edit), isDisplayed()));
        editIcon.perform(click());

        ViewInteraction editEmailField = onView(
                allOf(withId(R.id.edit_email), withText("0@gmail.com"), isDisplayed()));
        editEmailField.perform(replaceText("testprofile@"));

        ViewInteraction onConfirm = onView(
                allOf(withId(R.id.text_confirm), withText("CONFIRM"), isDisplayed()));
        onConfirm.perform(click());

        ViewInteraction onCancel = onView(
                allOf(withId(R.id.text_cancel), withText("CANCEL"), isDisplayed()));
        onCancel.perform(click());

        ViewInteraction logout = onView(
                allOf(withId(R.id.logout_button), isDisplayed()));
        logout.perform(click());
    }

}
