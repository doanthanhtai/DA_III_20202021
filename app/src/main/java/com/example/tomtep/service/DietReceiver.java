package com.example.tomtep.service;

import static com.example.tomtep.MyApplication.CHANNEL_ID_1;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.tomtep.ExpandDietActivity;
import com.example.tomtep.MainActivity;
import com.example.tomtep.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class DietReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("EATED")) {
            Log.e("TOMTEP","onReceive");
            String accountId = intent.getStringExtra("account_id");
            String lakeId = intent.getStringExtra("lake_id");
            String strTitle = intent.getStringExtra("title");
            String strContent = intent.getStringExtra("content");
            Intent i = new Intent(context, ExpandDietActivity.class);
            i.setAction(lakeId);
            i.putExtra("accountId", accountId);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_1)
                    .setSmallIcon(R.drawable.ic_sand_watch_24px)
                    .setContentTitle(strTitle)
                    .setContentText(strContent)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(getNotificaitonId(), builder.build());
            turnOffDiet(lakeId);
        }
    }

    private void turnOffDiet(String lakeId) {
        FirebaseDatabase.getInstance().getReference("Lake").child(lakeId)
                .child("diet")
                .child("condition")
                .setValue(false).addOnCompleteListener(task -> {

        });
    }

    private int getNotificaitonId() {
        return (int) new Date().getTime();
    }
}
