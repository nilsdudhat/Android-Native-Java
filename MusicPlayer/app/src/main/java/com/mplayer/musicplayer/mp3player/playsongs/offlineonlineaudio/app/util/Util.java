package com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.mplayer.musicplayer.mp3player.playsongs.offlineonlineaudio.app.R;

import java.io.File;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

    private static final String HTTPS = "https://";
    private static final String HTTP = "http://";

    public static void setMenuBackground(Activity activity) {
        // Log.d(TAG, "Enterting setMenuBackGround");
        activity.getLayoutInflater().setFactory(new LayoutInflater.Factory() {
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
                    try { // Ask our inflater to create the view
                        LayoutInflater f = activity.getLayoutInflater();
                        final View view = f.createView(name, null, attrs);
                        /* The background gets refreshed each time a new item is added the options menu.
                         * So each time Android applies the default background we need to set our own
                         * background. This is done using a thread giving the background change as runnable
                         * object */
                        new Handler().post(new Runnable() {
                            public void run() {
                                // sets the background color
                                view.setBackgroundResource(R.drawable.bg_popup);
                                // sets the text color
                                ((TextView) view).setTextColor(Color.BLACK);
                                // sets the text size
                                ((TextView) view).setTextSize(18);
                            }
                        });
                        return view;
                    } catch (InflateException e) {
                    } catch (ClassNotFoundException e) {
                    }
                }
                return null;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void deleteFiles(@Nullable File[] files, final int requestCode, Activity activity, @Nullable Intent fillInIntent) {
        if (files != null && files.length > 0) {
            deleteFiles(Arrays.asList(files), requestCode, activity, fillInIntent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void deleteFiles(@Nullable List<File> files, final int requestCode, Activity activity, @Nullable Intent fillInIntent) {
        if (files == null || files.isEmpty()) {
            return;
        }

        List<Uri> uris = files.stream().map(file -> {

            long mediaID = getFilePathToMediaID(file.getAbsolutePath(), activity);
            String fileType = getFileType(file);
            if (fileType.equalsIgnoreCase("video")) {
                return ContentUris.withAppendedId(MediaStore.Video.Media.getContentUri("external"), mediaID);
            } else if (fileType.equalsIgnoreCase("image")){
                return ContentUris.withAppendedId(MediaStore.Images.Media.getContentUri("external"), mediaID);
            } else if (fileType.equalsIgnoreCase("audio")) {
                return ContentUris.withAppendedId(MediaStore.Audio.Media.getContentUri("external"), mediaID);
            } else {
                return null;
            }
        }).collect(Collectors.toList());

        PendingIntent pi = MediaStore.createDeleteRequest(activity.getContentResolver(), uris);
        try {
            IntentSender intentSender = pi.getIntentSender();
            activity.startIntentSenderForResult(intentSender, requestCode, fillInIntent, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private static String getFileType(File file) {

        String mimeType = URLConnection.guessContentTypeFromName(file.getPath());
        if (mimeType != null) {

            if (mimeType.startsWith("video")) {
                return "video";
            } else if (mimeType.startsWith("image")) {
                return "image";
            } else if (mimeType.startsWith("audio")) {
                return "audio";
            }
        }
        return "";
    }

    private static long getFilePathToMediaID(String songPath, Context context) {
        long id = 0;
        ContentResolver cr = context.getContentResolver();

        Uri uri = MediaStore.Files.getContentUri("external");
        String selection = MediaStore.Audio.Media.DATA;
        String[] selectionArgs = {songPath};
        String[] projection = {MediaStore.Audio.Media._ID};
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = cr.query(uri, projection, selection + "=?", selectionArgs, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                id = Long.parseLong(cursor.getString(idIndex));
            }
        }

        return id;
    }

    public static void openBrowser(final Context context, String url) {

        if (!url.startsWith(HTTP) && !url.startsWith(HTTPS)) {
            url = HTTP + url;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(Intent.createChooser(intent, "Choose browser"));// Choose browser is arbitrary :)
    }

    public static void openWebPage(Activity activity, String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    public static int getActionBarSize(@NonNull Context context) {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{androidx.appcompat.R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = context.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    public static Point getScreenSize(@NonNull Context c) {
        Display display = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setStatusBarTranslucent(@NonNull Window window) {
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public static void setAllowDrawUnderStatusBar(@NonNull Window window) {
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public static void hideSoftKeyboard(@Nullable Activity activity) {
        if (activity != null) {
            View currentFocus = activity.getCurrentFocus();
            if (currentFocus != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
    }

    public static boolean isTablet(@NonNull final Resources resources) {
        return resources.getConfiguration().smallestScreenWidthDp >= 600;
    }

    public static boolean isLandscape(@NonNull final Resources resources) {
        return resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static int resolveDimensionPixelSize(@NonNull Context context, @AttrRes int dimenAttr) {
        TypedArray a = context.obtainStyledAttributes(new int[]{dimenAttr});
        int dimensionPixelSize = a.getDimensionPixelSize(0, 0);
        a.recycle();
        return dimensionPixelSize;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isRTL(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration config = context.getResources().getConfiguration();
            return config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
        } else return false;
    }

}
