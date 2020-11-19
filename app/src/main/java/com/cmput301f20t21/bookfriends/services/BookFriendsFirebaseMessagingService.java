package com.cmput301f20t21.bookfriends.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.repositories.impl.BookRepositoryImpl;
import com.cmput301f20t21.bookfriends.ui.borrow.accepted.AcceptedDetailActivity;
import com.cmput301f20t21.bookfriends.ui.borrow.requested.RequestedDetailActivity;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BookFriendsFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "bfriends_messaging";
    private final AtomicInteger onetimeId = new AtomicInteger();

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "Refreshed token: " + s);
    }

    // https://firebase.google.com/docs/cloud-messaging/android/send-multiple#java_3
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                .setSmallIcon(R.drawable.no_image)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat nm = NotificationManagerCompat.from(this);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        Task<PendingIntent> buildIntentTask = buildIntent(remoteMessage);
        if (buildIntentTask != null) {
            buildIntentTask.addOnCompleteListener(pendingIntentTask -> {
                if (pendingIntentTask == null) {
                    nm.notify(onetimeId.get(), builder.build());
                    return;
                }
                PendingIntent pendingIntent = pendingIntentTask.getResult();
                if (pendingIntent != null) builder.setContentIntent(pendingIntent);
                nm.notify(onetimeId.get(), builder.build());
            });
        } else {
            nm.notify(onetimeId.get(), builder.build());
        }
    }

    private Task<PendingIntent> buildIntent(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() <= 0) {
            return null;
        }
        Map<String, String> data = remoteMessage.getData();
        if (data.get("type") == null) {
            return null;
        }

        if (data.get("type").equals("request")) {
            Intent resultIntent = new Intent(this, RequestedDetailActivity.class);
            return BookRepositoryImpl.getInstance().getBookById(data.get("bookId")).continueWith(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    resultIntent.putExtra(BaseDetailActivity.BOOK_DATA_KEY, document.toObject(Book.class));
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                    stackBuilder.addNextIntentWithParentStack(resultIntent);
                    // Get the PendingIntent containing the entire back stack
                    return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                }
                return null;
            });
        }

        if (data.get("type").equals("accept")) {
            Intent resultIntent = new Intent(this, AcceptedDetailActivity.class);
            return BookRepositoryImpl.getInstance().getBookById(data.get("bookId")).continueWith(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    resultIntent.putExtra(BaseDetailActivity.BOOK_DATA_KEY, document.toObject(Book.class));
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                    stackBuilder.addNextIntentWithParentStack(resultIntent);
                    // Get the PendingIntent containing the entire back stack
                    return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                }
                return null;
            });
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_name);
            String description = getString(R.string.notification_channel_description);
            String CHANNEL_ID = getString(R.string.notification_channel_id);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
