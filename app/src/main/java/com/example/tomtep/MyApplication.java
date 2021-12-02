package com.example.tomtep;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class MyApplication extends Application {
    public static final String CHANNEL_ID_1 = "TOMTEP CHENNEL";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        String strDescription = "The alarm channel is finished";
        NotificationChannel notificationChannelDiet = new NotificationChannel(CHANNEL_ID_1, getText(R.string.channelname_diet), NotificationManager.IMPORTANCE_HIGH);
        notificationChannelDiet.setDescription(strDescription);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannelDiet);
    }
}
