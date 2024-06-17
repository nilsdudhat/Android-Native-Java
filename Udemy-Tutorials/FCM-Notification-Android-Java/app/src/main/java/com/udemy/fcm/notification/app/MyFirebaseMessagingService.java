package com.udemy.fcm.notification.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onDeletedMessages() {
        // Handle deleted messages here

    }

    @Override
    public void onMessageSent(String messageId) {
        // Handle message sending here

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle incoming messages here

        Log.d(TAG, "onMessageReceived From: " + remoteMessage.getFrom());

        // check for data payload in message
        if (!remoteMessage.getData().isEmpty()) {
            Log.d(TAG, "onMessageReceived Data Payload: " + remoteMessage.getData());

            if (true) {
                scheduleJob();
            } else {
                handleNow();
            }
        }

        // check for notification payload in the message
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "onMessageReceived Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void handleNow() {
        Log.d(TAG, "handleNow: ");
    }

    private void scheduleJob() {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class).build();
        WorkManager.getInstance(this).beginWith(workRequest).enqueue();
    }

    private void sendRegistrationToServer(String token) {

    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        String channelID = getString(R.string.fcm_notification_channel_id);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.ic_umbrella)
                .setContentTitle(getString(R.string.fcm_notification_channel_id))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel
                    = new NotificationChannel(
                    channelID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0, builder.build());
    }

    @Override
    public void onNewToken(String token) {
        // Handle new token generation here
        Log.d(TAG, "onNewToken: " + token);

        sendRegistrationToServer(token);
    }
}
