package com.example.runconnect.notify;

import android.Manifest;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.runconnect.activity.main.RunConnectMainActivity;

public class FriendPostedNotify extends Application {
    public static final String SERVICE_CHANNEL_ID = "SERVICE_CHANNEL_ID";
    public static final String CHANNEL_ID = "CHANNEL_ID";
    static private FriendPostedNotify instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        regsterNotificationChannel();
    }

    public void notification(String title, String message) {
        Intent intent = new Intent(this, RunConnectMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        NotificationManagerCompat compat = NotificationManagerCompat.from(this);
        compat.notify(100, builder.build());
    }

    private void regsterNotificationChannel() {
        CharSequence notifyPermissionName = "通知を許可する";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, notifyPermissionName, importance);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

    public static FriendPostedNotify getInstance() {
        return instance;
    }
}
