package com.cmput301f20t21.bookfriends;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cmput301f20t21.bookfriends.repositories.factories.AuthRepositoryFactory;
import com.cmput301f20t21.bookfriends.repositories.impl.AuthRepositoryImpl;
import com.cmput301f20t21.bookfriends.utils.NotificationSender;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;


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
        final String username = AuthRepositoryFactory.getRepository().getCurrentUser().getUsername();
        freshSubscribeToTopic(username);
    }

    private void initNotificationSender() {
        NotificationSender.getInstance();
    }

    /**
     * clean up all the previous subscription and subscribe to this particular topic only
     *
     * @param newTopic the topic to subscribe to.
     */
    private void freshSubscribeToTopic(String newTopic) {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String oldTopic = sharedPref.getString(getString(R.string.saved_cloud_messaging_topic), null);
        FirebaseMessaging.getInstance().subscribeToTopic(newTopic).continueWith(Void -> {
            if (oldTopic != null && !oldTopic.equals(newTopic)) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(oldTopic).continueWith(Void1 -> {
                    return null;
                });
            }
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.saved_cloud_messaging_topic), newTopic);
            editor.apply();
            return null;
        });
    }

}