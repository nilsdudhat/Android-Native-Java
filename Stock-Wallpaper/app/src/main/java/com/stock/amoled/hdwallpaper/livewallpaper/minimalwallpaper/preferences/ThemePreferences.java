package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.preferences;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;

import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.MyApplication;

public class ThemePreferences {

    public static boolean isDark() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        boolean isAutoTheme = sharedPreferences.getBoolean("auto_theme", true);
        if (isAutoTheme) {
            return (MyApplication.getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        } else {
            return sharedPreferences.getBoolean("is_dark_theme", false);
        }
    }

    public static void setTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        boolean isDark = sharedPreferences.getBoolean("is_dark_theme", false);
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
