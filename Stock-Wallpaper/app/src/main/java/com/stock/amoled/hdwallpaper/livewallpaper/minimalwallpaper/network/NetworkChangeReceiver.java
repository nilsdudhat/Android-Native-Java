package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.network;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.interfaces.NetworkChangeCallBack;

public class NetworkChangeReceiver extends BroadcastReceiver {

    Activity activity;
    NetworkChangeCallBack networkChangeCallBack;

    public NetworkChangeReceiver(Activity activity, NetworkChangeCallBack networkChangeCallBack) {
        this.activity = activity;
        this.networkChangeCallBack = networkChangeCallBack;
    }

    public boolean isInternetOn(Context context) {

        // get Connectivity Manager object to check connection
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
                connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                        connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean status = isInternetOn(context);
                if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {

                    networkChangeCallBack.isConnected(status);
                }
            }
        }).start();
    }
}