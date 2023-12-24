package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;

public class WallpaperUtils {

    public static void setHomeScreenWallpaper(Activity activity, Bitmap bitmap) throws IOException {
        WallpaperManager wallpaperManager = (WallpaperManager) activity.getSystemService(Context.WALLPAPER_SERVICE);
        wallpaperManager.setBitmap(bitmap);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void setLockScreenWallpaper(Activity activity, Bitmap bitmap) {
        WallpaperManager wallpaperManager = (WallpaperManager) activity.getSystemService(Context.WALLPAPER_SERVICE);
        try {
            wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setWallpaperOnBoth(Activity activity, Bitmap bitmap) throws IOException {
        WallpaperManager wallpaperManager = (WallpaperManager) activity.getSystemService(Context.WALLPAPER_SERVICE);
        wallpaperManager.setBitmap(bitmap);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
        }
    }
}
