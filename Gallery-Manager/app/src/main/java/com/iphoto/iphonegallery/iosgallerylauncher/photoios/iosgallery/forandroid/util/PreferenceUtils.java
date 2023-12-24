package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    public static final String IMAGE_COUNT = "image_count";
    public static final String VIDEO_COUNT = "video_count";
    public static final String MAIN_ACTIVITY = "main_activity";
    public static final String LIBRARY_FRAGMENT = "library_fragment";

    public static String getString(Activity activity, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return sharedPreferences.getString(key, "");
    }

    public static void setString(Activity activity, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static boolean getBoolean(Activity activity, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void setBoolean(Activity activity, String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static int getInteger(Activity activity, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return sharedPreferences.getInt(key, -1);
    }

    public static void setInteger(Activity activity, String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        sharedPreferences.edit().putInt(key, value).apply();
    }
}
