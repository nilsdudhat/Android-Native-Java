package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;

import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.MyApplication;

public class ThemeUtils {

    public static boolean isDark(Activity activity) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean isAutoTheme = sharedPreferences.getBoolean("auto_theme", true);
        if (isAutoTheme) {
            return (MyApplication.getAppContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        } else {
            return sharedPreferences.getBoolean("is_dark_theme", false);
        }
    }

    public static void setTheme(Activity activity) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean isAutoTheme = sharedPreferences.getBoolean("auto_theme", true);
        if (isAutoTheme) {
            switch (MyApplication.getAppContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
            }
        } else {
            boolean isDark = sharedPreferences.getBoolean("is_dark_theme", false);
            if (isDark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    }
}
