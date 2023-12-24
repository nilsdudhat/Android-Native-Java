package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class WebUtils {

    public static void openBrowser(final Context context, String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(Intent.createChooser(intent, "Choose Browser")); // Choose browser is arbitrary :)
    }
}
