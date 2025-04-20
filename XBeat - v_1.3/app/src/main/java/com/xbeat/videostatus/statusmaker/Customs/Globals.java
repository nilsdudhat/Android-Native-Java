package com.xbeat.videostatus.statusmaker.Customs;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Globals {
    public static int getDensity(double d) {
        double d2 = (double) Resources.getSystem().getDisplayMetrics().density;
        Double.isNaN(d2);
        Double.isNaN(d2);
        Double.isNaN(d2);
        Double.isNaN(d2);
        return (int) (d * d2);
    }

    public static boolean isNetworkAvailable(Context context) {
//        return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo() != null;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting() || !connectivityManager.getActiveNetworkInfo().isAvailable() || !connectivityManager.getActiveNetworkInfo().isConnected()) {
            return false;
        }
        return true;
    }

}
