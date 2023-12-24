package com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;

import androidx.annotation.RequiresApi;

import java.io.File;

public class Utils {

    public static void openBrowser(final Context context, String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(Intent.createChooser(intent, "Choose browser")); // Choose browser is arbitrary :)
    }

    public static void rateApp(Activity activity) {
        try {
            Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
            Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(goMarket);
        } catch (ActivityNotFoundException e) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName());
            Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(goMarket);
        }
    }

    public static void shareApp(Activity activity) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + activity.getPackageName() + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            activity.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void setBottomNavigationColor(Activity activity, int color) {
        activity.getWindow().setNavigationBarColor(color);
    }

    public static boolean stringIsNotEmpty(String string) {
        if (string != null && !string.equals("null")) {
            return !string.trim().equals("");
        }
        return false;
    }

    public static void MakeSureFileWasCreatedThenMakeAvailable(Activity activity, File file) {
        MediaScannerConnection.scanFile(activity, new String[]{file.toString()}, new String[]{"image/jpeg"}, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {

            }
        });
    }
}
