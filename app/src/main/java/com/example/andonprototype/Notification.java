package com.example.andonprototype;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notification extends Application {
    public static final String Problem_Notification = "ProblemNotification";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ProblemNotification = new NotificationChannel(
                    Problem_Notification,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            ProblemNotification.setDescription("This is Channel 1");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(ProblemNotification);
        }
    }
}