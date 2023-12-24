package com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class Share {

    public static final String IMAGE_PATH;
    public static Uri SAVED_BITMAP = null;
    public static String croppedImage;
    public static Boolean has_text;
    public static String imageUrl;
    public static Boolean is_sticker_mode;

    public static ArrayList<String> lst_album_image = new ArrayList<>();
    public static File shareFile;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getPath());
        sb.append(File.separator);
        sb.append("Gif Effect Display Picture");
        IMAGE_PATH = sb.toString();
        has_text = false;
        is_sticker_mode = false;
    }

    Exception e = null;

    @SuppressLint("WrongConstant")
    public static Boolean RestartApp(Activity activity) {
        Intent launchIntentForPackage = activity.getBaseContext().getPackageManager().getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
        launchIntentForPackage.addFlags(67108864);
        activity.startActivity(launchIntentForPackage);
        return Boolean.FALSE;
    }

    public static String saveImage(Bitmap finalBitmap, Activity activity) {
        long name = System.currentTimeMillis();
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + activity.getString(R.string.full_app_name);
        File filePath = new File(new File(path), String.valueOf(name));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            final ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, filePath.getName());
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/" + activity.getString(R.string.full_app_name));

            final ContentResolver resolver = activity.getContentResolver();
            Uri uri = null;

            try {
                final Uri contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
                uri = resolver.insert(contentUri, values);

                if (uri == null)
                    throw new IOException("Failed to create new MediaStore record.");

                try (final OutputStream stream = resolver.openOutputStream(uri)) {
                    if (stream == null) {
                        Toast.makeText(activity, "Failed to Save.", Toast.LENGTH_SHORT).show();
                        throw new IOException("Failed to open output stream.");
                    }

                    if (!finalBitmap.compress(CompressFormat.JPEG, 100, stream)) {
                        Toast.makeText(activity, "Failed to Save.", Toast.LENGTH_SHORT).show();
                        throw new IOException("Failed to save bitmap.");
                    }
                }
                Toast.makeText(activity, "Path: " + filePath.getAbsolutePath(), Toast.LENGTH_LONG).show();
                return filePath.getAbsolutePath();
            } catch (IOException e) {
                if (uri != null) {
                    // Don't leave an orphan entry in the MediaStore
                    resolver.delete(uri, null, null);
                }
                Toast.makeText(activity, "Failed to Save.", Toast.LENGTH_SHORT).show();
                return "";
            }
        } else {
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            Log.d("--filePath--", "saveImage: " + filePath.getAbsolutePath());

            try {
                FileOutputStream out = new FileOutputStream(filePath);
                finalBitmap.compress(CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory())));

                Toast.makeText(activity, "Path: " + filePath.getAbsolutePath(), Toast.LENGTH_LONG).show();
                return filePath.getAbsolutePath();
            } catch (Exception e) {
                Toast.makeText(activity, "Failed to Save.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

                return "";
            }
        }
    }

    public static String saveFaceInternalStorage(Bitmap bitmap, Context context) {
        // Throwable th = null;

        File file = new File(new ContextWrapper(context).getDir("imageDir", 0), "profile.png");
        if (bitmap != null) {
            FileOutputStream fileOutputStream = null;
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                try {
                    bitmap.compress(CompressFormat.PNG, 100, fileOutputStream2);
                } catch (Exception e) {
                    fileOutputStream = fileOutputStream2;
                    try {
                        e.printStackTrace();
                        fileOutputStream.close();
                        return file.getAbsolutePath();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
                try {
                    fileOutputStream2.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        } else {
            Log.e("TAGF", "Not Saved Image ------------------------------------------------------->");
        }
        return file.getAbsolutePath();
    }

    public static class KEYNAME {
        public static final String ALBUM_NAME = "album_name";
        public static final String SELECTED_PHONE_IMAGE = "selected_phone_image";

        public KEYNAME() {
        }
    }
}
