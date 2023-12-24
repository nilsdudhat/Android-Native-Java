package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "inbrodcaset", Toast.LENGTH_SHORT).show();
        context.sendBroadcast(new Intent("Stop_play_video"));
        Log.e("Finish ", " : ");
    }
}
