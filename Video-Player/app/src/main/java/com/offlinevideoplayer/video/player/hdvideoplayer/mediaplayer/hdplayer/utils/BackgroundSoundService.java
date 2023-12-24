package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.R;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.ui.activities.HomeActivity;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.ui.activities.VideoPlayerActivity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BackgroundSoundService extends Service {

    public static final int PRIORITY_MAX = 2;
    private static final String TAG = null;
    public static MediaPlayer player;
    public static Notification customNotification;
    public static long notpos;
    public static Bitmap bitVideoThumb;
    public static NotificationManager notificationmanager;
    private static RemoteViews notificationLayout;
    private static int a = 0;
    private static List<VideoModel> videoList = new ArrayList<VideoModel>();
    private static int currentPosition = 0;
    private static int seekpos = 0;
    private final BroadcastReceiver shutdownReceiver = new ShutdownReceiver();
    Intent intent;
    private String CHANNEL_ID = "no";
    private SharedPreferences appPreferences;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        registerBroadcastReceiver();
        this.intent = intent;


        VideoModel vv = new VideoModel();

        vv.setPath("/storage/emulated/0/Z1/all bindi.mp4");

        //  videoList.add(vv);
        appPreferences = getApplicationContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);

        videoList = (List<VideoModel>) intent.getSerializableExtra("list");
        currentPosition = appPreferences.getInt("position", 0);
        seekpos = (int) appPreferences.getLong("seek_position", 0);
        Log.e("videoList 1: " + videoList.size(), " : " + currentPosition);


        player = MediaPlayer.create(this, Uri.parse(videoList.get(currentPosition).getPath()));
        player.setVolume(100, 100);
        player.seekTo(seekpos);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (currentPosition == videoList.size() - 1) {
                    player.stop();
                } else {
                    currentPosition++;
                    Log.d("position", "pos" + currentPosition);
                    Log.d("position", "size" + videoList.size());
                    String vidurl = (String) videoList.get(currentPosition).getPath();
                    String vidname = new File(vidurl).getName();
                    player = MediaPlayer.create(getApplicationContext(), Uri.parse(videoList.get(currentPosition).getPath()));
                    player.setVolume(100, 100);
                    player.start();
                    notificationLayout.setTextViewText(R.id.title, new File(videoList.get(currentPosition).getPath()).getName());
                    bitVideoThumb = getBitmapFromPath(videoList.get(currentPosition).getPath());
                    notificationLayout.setImageViewBitmap(R.id.custimage, bitVideoThumb);
                    notificationmanager.notify(0, customNotification);
                }
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {


            notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            bitVideoThumb = getBitmapFromPath(videoList.get(currentPosition).getPath());
            notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_small);

            customNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_play)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(notificationLayout)
                    .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, VideoPlayerActivity.class).putExtra("list", (Serializable) videoList).putExtra("position", currentPosition).putExtra("current", (long) player.getCurrentPosition()),
                            PendingIntent.FLAG_UPDATE_CURRENT))
                    .setPriority(PRIORITY_MAX)
                    .build();
            customNotification.flags = Notification.FLAG_ONGOING_EVENT;

            customNotification.contentView = notificationLayout;
            // customNotification.contentIntent = pendingNotificationIntent;
            customNotification.flags |= Notification.FLAG_NO_CLEAR;
            Intent playIntent = new Intent(this, playPauseButtonListener.class);
            PendingIntent pendingPlayIntent = PendingIntent.getBroadcast(this, 0,
                    playIntent, 0);
            Intent forwardIntent = new Intent(this, forwardButtonListener.class);
            PendingIntent pendingForwardIntent = PendingIntent.getBroadcast(this, 0,
                    forwardIntent, 0);
            Intent backwardIntent = new Intent(this, backwardButtonListener.class);
            PendingIntent pendingBackwardIntent = PendingIntent.getBroadcast(this, 0,
                    backwardIntent, 0);
            Intent closeIntent = new Intent(this, closeButtonListener.class);
            PendingIntent pendingCloseIntent = PendingIntent.getBroadcast(this, 0,
                    closeIntent, 0);
            notificationLayout.setOnClickPendingIntent(R.id.imageView3, pendingPlayIntent);
            notificationLayout.setOnClickPendingIntent(R.id.imageView4, pendingForwardIntent);
            notificationLayout.setOnClickPendingIntent(R.id.imageView2, pendingBackwardIntent);
            notificationLayout.setOnClickPendingIntent(R.id.imageView5, pendingCloseIntent);

            notificationLayout.setImageViewResource(R.id.imageView2, R.drawable.previous);
            notificationLayout.setImageViewResource(R.id.imageView3, R.drawable.pause);
            notificationLayout.setImageViewResource(R.id.imageView4, R.drawable.forward);
            notificationLayout.setImageViewResource(R.id.imageView5, R.drawable.ic_close_black_24dp);
            notificationLayout.setTextViewText(R.id.title, new File(videoList.get(currentPosition).getPath()).getName());
            notificationLayout.setTextColor(R.id.title, Color.WHITE);
            Log.e("bitmap", String.valueOf(bitVideoThumb));
            notificationLayout.setImageViewBitmap(R.id.custimage, bitVideoThumb);
            notificationmanager.notify(0, customNotification);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            bitVideoThumb = getBitmapFromPath(videoList.get(currentPosition).getPath());
            notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_small);
            customNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_play)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(notificationLayout)
                    .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, VideoPlayerActivity.class).putExtra("list", (Serializable) videoList).putExtra("position", currentPosition).putExtra("current", (long) player.getCurrentPosition()),
                            PendingIntent.FLAG_UPDATE_CURRENT))
                    .setPriority(PRIORITY_MAX)
                    .build();
            customNotification.flags = Notification.FLAG_ONGOING_EVENT;
            Intent notificationIntent = new Intent(this, HomeActivity.class);
            notificationIntent.putExtra("key", "hello");
            PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);
            customNotification.contentView = notificationLayout;
            // customNotification.contentIntent = pendingNotificationIntent;
            customNotification.flags |= Notification.FLAG_NO_CLEAR;
            Intent playIntent = new Intent(this, playPauseButtonListener.class);
            PendingIntent pendingPlayIntent = PendingIntent.getBroadcast(this, 0,
                    playIntent, 0);
            Intent forwardIntent = new Intent(this, forwardButtonListener.class);
            PendingIntent pendingForwardIntent = PendingIntent.getBroadcast(this, 0,
                    forwardIntent, 0);
            Intent backwardIntent = new Intent(this, backwardButtonListener.class);
            PendingIntent pendingBackwardIntent = PendingIntent.getBroadcast(this, 0,
                    backwardIntent, 0);
            Intent closeIntent = new Intent(this, closeButtonListener.class);
            PendingIntent pendingCloseIntent = PendingIntent.getBroadcast(this, 0,
                    closeIntent, 0);
            notificationLayout.setOnClickPendingIntent(R.id.imageView3, pendingPlayIntent);
            notificationLayout.setOnClickPendingIntent(R.id.imageView4, pendingForwardIntent);
            notificationLayout.setOnClickPendingIntent(R.id.imageView2, pendingBackwardIntent);
            notificationLayout.setOnClickPendingIntent(R.id.imageView5, pendingCloseIntent);

            notificationLayout.setImageViewResource(R.id.imageView2, R.drawable.previous);
            notificationLayout.setImageViewResource(R.id.imageView3, R.drawable.pause);
            notificationLayout.setImageViewResource(R.id.imageView4, R.drawable.next);
            notificationLayout.setImageViewResource(R.id.imageView5, R.drawable.ic_close_black_24dp);
            notificationLayout.setTextViewText(R.id.title, new File(videoList.get(currentPosition).getPath()).getName());
            notificationLayout.setTextColor(R.id.title, Color.BLACK);
            Log.e("bitmap", String.valueOf(bitVideoThumb));
            notificationLayout.setImageViewBitmap(R.id.custimage, bitVideoThumb);
            notificationmanager.notify(0, customNotification);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            bitVideoThumb = getBitmapFromPath(videoList.get(currentPosition).getPath());
            notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_small);
            customNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_play)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(notificationLayout)
                    .setPriority(PRIORITY_MAX)
                    .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, VideoPlayerActivity.class).putExtra("list", (Serializable) videoList).putExtra("position", currentPosition).putExtra("current", (long) player.getCurrentPosition()),
                            PendingIntent.FLAG_UPDATE_CURRENT))
                    .setChannelId(CHANNEL_ID)
                    .build();
            customNotification.flags = Notification.FLAG_ONGOING_EVENT;

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, "hello", importance);
            customNotification.contentView = notificationLayout;
            // customNotification.contentIntent = pendingNotificationIntent;
            customNotification.flags |= Notification.FLAG_NO_CLEAR;


            Intent layoutIntent = new Intent(this, layoytClickListener.class);

            PendingIntent pendinlayoutIntent = PendingIntent.getBroadcast(this, 0,
                    layoutIntent, 0);

            notificationLayout.setOnClickPendingIntent(R.id.notilayout, pendinlayoutIntent);

            Intent playIntent = new Intent(this, playPauseButtonListener.class);

            PendingIntent pendingPlayIntent = PendingIntent.getBroadcast(this, 0,
                    playIntent, 0);
            Intent forwardIntent = new Intent(this, forwardButtonListener.class);
            PendingIntent pendingForwardIntent = PendingIntent.getBroadcast(this, 0,
                    forwardIntent, 0);
            Intent backwardIntent = new Intent(this, backwardButtonListener.class);
            PendingIntent pendingBackwardIntent = PendingIntent.getBroadcast(this, 0,
                    backwardIntent, 0);
            Intent closeIntent = new Intent(this, closeButtonListener.class);
            PendingIntent pendingCloseIntent = PendingIntent.getBroadcast(this, 0,
                    closeIntent, 0);
            notificationLayout.setOnClickPendingIntent(R.id.imageView3, pendingPlayIntent);
            notificationLayout.setOnClickPendingIntent(R.id.imageView4, pendingForwardIntent);
            notificationLayout.setOnClickPendingIntent(R.id.imageView2, pendingBackwardIntent);
            notificationLayout.setOnClickPendingIntent(R.id.imageView5, pendingCloseIntent);

            notificationLayout.setImageViewResource(R.id.imageView2, R.drawable.previous);
            notificationLayout.setImageViewResource(R.id.imageView3, R.drawable.pause);
            notificationLayout.setImageViewResource(R.id.imageView4, R.drawable.next);
            notificationLayout.setImageViewResource(R.id.imageView5, R.drawable.ic_close_black_24dp);
            notificationLayout.setTextViewText(R.id.title, new File(videoList.get(currentPosition).getPath()).getName());
            notificationLayout.setTextColor(R.id.title, Color.BLACK);
            Log.e("bitmap", String.valueOf(bitVideoThumb));
            notificationLayout.setImageViewBitmap(R.id.custimage, bitVideoThumb);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationmanager.createNotificationChannel(mChannel);
            }
            notificationmanager.notify(0, customNotification);
        }

        player.start();


        return 1;
    }


    @Override
    public void onCreate() {
        super.onCreate();

    }


    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        registerReceiver(this.shutdownReceiver, intentFilter);
    }


    private void sendNotification(String title, String messageBody) {

/*        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0  , intent,
                PendingIntent.FLAG_ONE_SHOT);*/

        String channelId = "channel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            notificationBuilder.setColor(Color.parseColor("#393838"));
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public Bitmap getBitmapFromPath(String path) {
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path,
                MediaStore.Images.Thumbnails.MINI_KIND);
        return thumb;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        // Toast.makeText(this, "taskremove", Toast.LENGTH_SHORT).show();
       /* int pd = player.getCurrentPosition();
        ((AlarmManager) Objects.requireNonNull(getSystemService(ALARM_SERVICE))).set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 500, PendingIntent.getService(this, 3,
                new Intent(this,
                        BackgroundSoundService.class)
                        .putExtra("position",currentPosition)
                        .putExtra("current",pd)
                        .putExtra("list", (Serializable) videoList)
                , 0));
*/

    /*    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();*/
/*        Intent intent = new Intent(this, BackgroundSoundService.class);
        stopService(intent);*/
    }

    @Override
    public void onDestroy() {
        // Toast.makeText(this, "destrory", Toast.LENGTH_SHORT).show();
        PreferenceUtil.getInstance(this).saveResumBool(true);
        player.stop();
        player.release();
        customNotification.flags = Notification.FLAG_ONGOING_EVENT;
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    public void onLowMemory() {

    }

    public static class layoytClickListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {


            Intent i = new Intent(context, VideoPlayerActivity.class).putExtra("list", (Serializable) videoList)
                    .putExtra("position", currentPosition)
                    .putExtra("current", (long) player.getCurrentPosition());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public static class playPauseButtonListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {


            if (a % 2 == 0) {
                try {
                    notpos = player.getCurrentPosition();
                } catch (Exception e) {
                    notpos = 0;
                }
                player.stop();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                    notificationLayout.setImageViewResource(R.id.imageView3, R.drawable.play);
                else
                    notificationLayout.setImageViewResource(R.id.imageView3, R.drawable.play);
            } else {
                Toast.makeText(context, "stop", Toast.LENGTH_SHORT).show();
                player.stop();
                player = MediaPlayer.create(context, Uri.parse(videoList.get(currentPosition).getPath()));
                player.setVolume(100, 100);
                player.seekTo((int) notpos);
                player.start();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                    notificationLayout.setImageViewResource(R.id.imageView3, R.drawable.pause);
                else
                    notificationLayout.setImageViewResource(R.id.imageView3, R.drawable.pause);

            }
            a++;
            notificationmanager.notify(0, customNotification);
        }
    }

    public static class forwardButtonListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (currentPosition < videoList.size() - 1) {

                Toast.makeText(context, "forwar", Toast.LENGTH_SHORT).show();
                player.stop();
                a = 0;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                    notificationLayout.setImageViewResource(R.id.imageView3, R.drawable.pause);
                else
                    notificationLayout.setImageViewResource(R.id.imageView3, R.drawable.pause);
                currentPosition++;

                player = MediaPlayer.create(context, Uri.parse(videoList.get(currentPosition).getPath()));
                player.setVolume(100, 100);
                player.start();
                notificationLayout.setTextViewText(R.id.title, new File(videoList.get(currentPosition).getPath()).getName());
                bitVideoThumb = getBitmapFromPath(videoList.get(currentPosition).getPath());
                notificationLayout.setImageViewBitmap(R.id.custimage, bitVideoThumb);
                notificationmanager.notify(0, customNotification);
            }
        }

        public Bitmap getBitmapFromPath(String path) {
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path,
                    MediaStore.Images.Thumbnails.MINI_KIND);
            return thumb;
        }
    }

    public static class backwardButtonListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (currentPosition > 0) {
                player.stop();
                a = 0;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                    notificationLayout.setImageViewResource(R.id.imageView3, R.drawable.pause);
                else
                    notificationLayout.setImageViewResource(R.id.imageView3, R.drawable.pause);
                currentPosition--;

                player = MediaPlayer.create(context, Uri.parse(videoList.get(currentPosition).getPath()));
                player.setVolume(100, 100);
                player.start();
                notificationLayout.setTextViewText(R.id.title, new File(videoList.get(currentPosition).getPath()).getName());
                bitVideoThumb = getBitmapFromPath(videoList.get(currentPosition).getPath());
                notificationLayout.setImageViewBitmap(R.id.custimage, bitVideoThumb);
                notificationmanager.notify(0, customNotification);
            }
        }

        public Bitmap getBitmapFromPath(String path) {
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path,
                    MediaStore.Images.Thumbnails.MINI_KIND);
            return thumb;
        }
    }

    public static class closeButtonListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            Intent i = new Intent(context, BackgroundSoundService.class);
            context.stopService(i);
        }
    }
}