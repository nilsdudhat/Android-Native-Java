package com.udemy.airplanemode.app.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AirplaneModeReceiver extends BroadcastReceiver {

    /**
     * this method gets callback when change for specific provided even of your mobile
     * */
    @Override
    public void onReceive(Context context, Intent intent) {

        if ((intent.getAction() != null)
                && intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {

            boolean isAirplaneModeOn = intent.getBooleanExtra("state", false);

            String message = isAirplaneModeOn ? "Airplane Mode is On" : "Airplane Mode is Off";

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
