package com.example.android.squawker.fcm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.squawker.MainActivity;
import com.example.android.squawker.R;
import com.example.android.squawker.provider.SquawkerContract;
import com.example.android.squawker.provider.SquawkerProvider;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class SquawkerFirebaseMessageService extends FirebaseMessagingService {

    private static final String JSON_KEY_AUTHOR = SquawkerContract.COLUMN_AUTHOR;
    private static final String JSON_KEY_AUTHOR_KEY = SquawkerContract.COLUMN_AUTHOR_KEY;
    private static final String JSON_KEY_MESSAGE = SquawkerContract.COLUMN_MESSAGE;
    private static final String JSON_KEY_DATE = SquawkerContract.COLUMN_DATE;
    private static final String SQUAWK_NOTIFICATION_CHANNEL_ID = "new_squawk_notification_channel";
    private static final String SQUAWK_NOTIFICATION_CHANNEL_NAME = "Squawk";
    private static final int SQUAWK_NOTIFICATION_ID = 9999;
    private static final int NOTIFICATION_MAX_CHARACTERS = 30;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();

        if (data.size() > 0) {
            Log.d("D/SquawkerFirebase", String.valueOf(data));
            sendNotification(data);
            insertSquawk(data);
        }
    }

    private void sendNotification(Map<String, String> data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, SQUAWK_NOTIFICATION_ID, intent, PendingIntent.FLAG_ONE_SHOT);

        String author = data.get(JSON_KEY_AUTHOR);
        String message = data.get(JSON_KEY_MESSAGE);

        if (message.length() > NOTIFICATION_MAX_CHARACTERS) {
            message = message.substring(0, NOTIFICATION_MAX_CHARACTERS) + "\u2026";
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                SQUAWK_NOTIFICATION_CHANNEL_ID,
                SQUAWK_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, SQUAWK_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle(String.format(getString(R.string.notification_message), author))
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationManager.IMPORTANCE_HIGH);

        notificationManager.notify(SQUAWK_NOTIFICATION_ID, notificationBuilder.build());
    }

    @SuppressLint("StaticFieldLeak")
    private void insertSquawk(final Map<String, String> data) {
         new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(SquawkerContract.COLUMN_AUTHOR, data.get(JSON_KEY_AUTHOR));
                contentValues.put(SquawkerContract.COLUMN_MESSAGE, data.get(JSON_KEY_MESSAGE).trim());
                contentValues.put(SquawkerContract.COLUMN_DATE, data.get(JSON_KEY_DATE));
                contentValues.put(SquawkerContract.COLUMN_AUTHOR_KEY, data.get(JSON_KEY_AUTHOR_KEY));

                getContentResolver().insert(SquawkerProvider.Squawker.CONTENT_URI, contentValues);

                return null;
            }
        }.execute();
    }
}
