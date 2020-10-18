package com.cmput301f20t21.bookfriends;

import android.content.Intent;
import android.os.Bundle;

import com.cmput301f20t21.bookfriends.ui.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    public enum ActivityRequestCode {
        LOGIN(0);

        private final int requestCode;

        ActivityRequestCode(int requestCode) {
            this.requestCode = requestCode;
        }

        public int getRequestCode() {
            return this.requestCode;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_library,
                R.id.navigation_borrow,
                R.id.navigation_notifications,
                R.id.navigation_profile
        )
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // if the user is already logged in when starting the app
        // there is no need to redirect them to the login page
        // we need to store the User info upon onPause() or onStop()
        // and then it can be retrieved in "savedInstanceState"
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginIntent, ActivityRequestCode.LOGIN.getRequestCode());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        super.onActivityResult(requestCode, resultCode, resultIntent);
        if (requestCode == ActivityRequestCode.LOGIN.getRequestCode()) {
            // Returned from login activity, resultIntent will have the user class
            // store the user as a class attribute?
        }
    }

}