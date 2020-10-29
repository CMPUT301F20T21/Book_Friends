package com.cmput301f20t21.bookfriends.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.User;
import com.cmput301f20t21.bookfriends.enums.BOOK_ACTION;
import com.cmput301f20t21.bookfriends.ui.library.LibraryFragment;

public class ProfileViewUserActivity extends AppCompatActivity {
    static final String UID_KEY = "UID_KEY";
    ProfileViewModel vm;
    private User user;

    // views
    private TextView usernameView;
    private TextView emailAddress;
    private TextView phoneNumber;
    private Button logoutBtn;
    private ImageView editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new ViewModelProvider(this).get(ProfileViewModel.class);
        setContentView(R.layout.fragment_profile);
        setFieldViews();

        Intent intent = getIntent();
        String uid = (String) intent.getStringExtra(UID_KEY);

        if (uid != null) {
            vm.getUserByUid(uid, user -> {
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
        phoneNumber = findViewById(R.id.phone);
        logoutBtn = findViewById(R.id.logout_button);
        editProfile = findViewById(R.id.image_edit);

        logoutBtn.setVisibility(View.GONE);
        editProfile.setVisibility(View.GONE); // you don't edit
    }

    private void bindUser(User u) {
        usernameView.setText(u.getUsername());
        emailAddress.setText(u.getEmail());
        phoneNumber.setText(u.getPhoneNumber());
    }
}