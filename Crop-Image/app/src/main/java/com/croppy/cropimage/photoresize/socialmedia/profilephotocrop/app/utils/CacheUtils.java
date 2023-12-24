package com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.Objects;

public class CacheUtils {

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            freeMemory();
            deleteDir(dir);

            Log.d("--cache--", "low memory: " + isAppInLowMemory(context));
            Log.d("--cache--", "deleteCache: " + "done");
        } catch (Exception e) {
            Log.d("--cache--", "deleteCache: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        int i = 0;
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : Objects.requireNonNull(children)) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
                Log.d("--clear--", "deleteDir: " + i++);
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static boolean isAppInLowMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        return memoryInfo.lowMemory;
    }

    public static void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }
}
