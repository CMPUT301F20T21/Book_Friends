package com.cmput301f20t21.bookfriends;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.cmput301f20t21.bookfriends.repositories.api.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.api.UserRepository;
import com.cmput301f20t21.bookfriends.repositories.impl.AuthRepositoryImpl;
import com.cmput301f20t21.bookfriends.ui.login.LoginActivity;
import com.cmput301f20t21.bookfriends.utils.NotificationSender;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.concurrent.atomic.AtomicInteger;


public class MainActivity extends AppCompatActivity {

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
                R.id.navigation_browse,
                R.id.navigation_profile
        )
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        subscribeToNotifications(); // subscribe to incoming notifications
        initNotificationSender(); // prepare util singleton for future notification sending
    }

    private void subscribeToNotifications() {
        String TAG = "bfriends_messaging";
        final String username = AuthRepositoryImpl.getInstance().getCurrentUser().getUsername();
        FirebaseMessaging.getInstance().subscribeToTopic(username)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e(TAG, "failed sub to " + username);
                    }
                    Log.e(TAG, "successfully sub to " + username);
                });
    }

    private void initNotificationSender() {
        NotificationSender.getInstance();
    }
}