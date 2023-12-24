package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.utils;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class BitmapUtils {

    public static Bitmap getBitmapFromAsset(Activity activity, String strName) {
        AssetManager assetManager = activity.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("Raw/" + strName);
        } catch (IOException e) {
            Log.d("--catch--", "name: " + strName + " ----- " + e.getCause());
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(inputStream);
    }

    public static Bitmap getBitmapFromPath(String path) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(path, bmOptions);
    }

    public static Bitmap getBitmapFromURL(String path) {
        try {
            URL url = new URL(path);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
