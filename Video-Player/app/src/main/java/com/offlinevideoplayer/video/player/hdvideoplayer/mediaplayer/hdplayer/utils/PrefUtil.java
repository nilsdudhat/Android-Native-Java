package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

//Complete
public class PrefUtil {
    // Put data or store Data into sharedPreference
    public static void putbooleanPref(String key, boolean value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    // clear Data From sharedPreference
    public static void removeBoolean(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    // get Data From sharedPreference
    public static boolean getbooleanPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, false);
    }

    public static void putstringPref(String key, String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("PutString", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public static String getstringPref(String key, Context context) {
        SharedPreferences references = context.getSharedPreferences("PutString", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = references.edit();
        editor.apply();
        return references.getString(key, "");
    }

    public static void removeString(String key, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("PutString", Context.MODE_PRIVATE);
        preferences.edit().remove(key).apply();
    }

    public static void clearPreference(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("PutString", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
    }
}