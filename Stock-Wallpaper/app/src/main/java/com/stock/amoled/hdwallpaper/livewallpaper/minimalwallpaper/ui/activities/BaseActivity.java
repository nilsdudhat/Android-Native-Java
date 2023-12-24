package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.ui.activities;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.adsintegration.AdsBaseActivity;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.interfaces.NetworkChangeCallBack;
import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.network.NetworkChangeReceiver;

public class BaseActivity extends AdsBaseActivity implements NetworkChangeCallBack {

    NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialising NetworkChangeReceiver
        networkChangeReceiver = new NetworkChangeReceiver(BaseActivity.this, this);

        // registering Network Change Receiver in onResume Method
        registerReceiver(networkChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onResume() {
        super.onResume();

//        // registering Network Change Receiver in onResume Method
//        registerReceiver(networkChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    public void isConnected(boolean isConnected) {
        Log.d("--network--", "isConnected: " + isConnected);
    }

    @Override
    protected void onPause() {
        super.onPause();

//        // un-registering Network Change Receiver in onPause Method
//        unregisterReceiver(networkChangeReceiver);
    }
}
