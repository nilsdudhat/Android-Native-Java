package com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteMediaManager {

    public static void deleteFileTill10(List<File> files, Activity activity) {
        for (File file : files) {
            if (!file.delete()) {
                Toast.makeText(activity, file.getName() + " - could not delete this file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void deleteFilesAbove10(@Nullable File[] files, final int requestCode, Activity activity, @Nullable Intent fillInIntent) {
        if (files != null && files.length > 0) {
            deleteFilesAbove10(Arrays.asList(files), requestCode, activity, fillInIntent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void deleteFilesAbove10(@Nullable List<File> files, final int requestCode, Activity activity, @Nullable Intent fillInIntent) {
        if (files == null || files.isEmpty()) {
            return;
        }
        Log.d("--delete_files--", "deleteFilesAbove10: " + files.size());

        List<Uri> uris = files.stream().map(file -> {

            long mediaID = getFilePathToMediaID(file.getAbsolutePath(), activity);
            String fileType = getFileType(file);
            if (fileType.equalsIgnoreCase("video")) {
                return ContentUris.withAppendedId(MediaStore.Video.Media.getContentUri("external"), mediaID);
            } else if (fileType.equalsIgnoreCase("image")) {
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

    public static String getFileType(File file) {

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

    public static long getFilePathToMediaID(String songPath, Context context) {
        long id = 0;
        ContentResolver cr = context.getContentResolver();

        Uri uri = MediaStore.Files.getContentUri("external");
        String selection = MediaStore.Audio.Media.DATA;
        String[] selectionArgs = {songPath};
        String[] projection = {MediaStore.Audio.Media._ID};
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        @SuppressLint("Recycle") Cursor cursor = cr.query(uri, projection, selection + "=?", selectionArgs, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                id = Long.parseLong(cursor.getString(idIndex));
            }
        }

        return id;
    }
}
