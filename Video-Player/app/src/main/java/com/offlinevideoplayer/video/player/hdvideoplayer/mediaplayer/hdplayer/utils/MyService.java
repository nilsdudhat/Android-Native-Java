package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    public static long millisecs;
    Context context;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;

        CountDownTimer countDownTimer = new CountDownTimer(millisecs, 1000) {
            @Override
            public void onTick(long j) {
                Log.e("Count ", " : :" + j);
            }

            @Override
            public void onFinish() {
                Log.e("Finish ", " : :");
                stopSelf();
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}