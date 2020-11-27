/*
 * ProfileViewUserActivity.java
 * Version: 1.0
 * Date: November 4, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.User;

/**
 * Activity shows other user's profile after clicking on an item in {@link ProfileSearchFragment}
 */
public class ProfileViewUserActivity extends AppCompatActivity {
    public static final String USERNAME_KEY = "com.cmput301f20t21.bookfriends.USERNAME_KEY";
    ProfileViewModel vm;
    private User user;

    // views
    private TextView usernameView;
    private TextView emailAddress;
    private Button logoutBtn;
    private ImageView editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new ViewModelProvider(this).get(ProfileViewModel.class);
        setContentView(R.layout.fragment_profile);
        setFieldViews();

        Intent intent = getIntent();
        String username = intent.getStringExtra(USERNAME_KEY);

        if (username != null) {
            vm.getUserByUsername(username, user -> {
                this.user = user;
                bindUser(user);
            }, () -> {});
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFieldViews() {
        emailAddress = findViewById(R.id.email);
        usernameView = findViewById(R.id.username);
        logoutBtn = findViewById(R.id.logout_button);
        editProfile = findViewById(R.id.image_edit);

        logoutBtn.setVisibility(View.GONE);
        editProfile.setVisibility(View.GONE); // you don't edit
    }

    private void bindUser(User u) {
        usernameView.setText(u.getUsername());
        emailAddress.setText(u.getEmail());
    }
}