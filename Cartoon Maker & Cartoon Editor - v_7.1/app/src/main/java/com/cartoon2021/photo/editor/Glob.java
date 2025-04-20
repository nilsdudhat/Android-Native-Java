package com.cartoon2021.photo.editor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.cartoon2021.photo.editor.R;

public class Glob {
    public static String Edit_Folder_name = String.valueOf(R.string.app_name);
    public static String app_link = "https://play.google.com/store/apps/details?id=com.cartoon2021.photo.editor";
    public static String app_name = "Cartoon Photo Maker & Editor";
    public static Bitmap finalBitmap = null;
    public static Bitmap finalBitmap1 = null;
    public static Bitmap gallery_piv = null;
    public static String shareUri;
    public static Uri uri;

    public static void onBackPressedIntent(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static boolean getBoolPref(Context context, String str) {
        return context.getSharedPreferences(context.getPackageName(), 0).getBoolean(str, false);
    }

    public static int getPref(Context context, String str) {
        return context.getSharedPreferences(context.getPackageName(), 0).getInt(str, 1);
    }

    public static void setBoolPref(Context context, String str, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(context.getPackageName(), 0).edit();
        edit.putBoolean(str, z);
        edit.apply();
    }

    public static void setPref(Context context, String str, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(context.getPackageName(), 0).edit();
        edit.putInt(str, i);
        edit.apply();
    }
}