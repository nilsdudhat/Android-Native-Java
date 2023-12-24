package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.File;
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

            long mediaID = MediaUtils.getFilePathToMediaID(file.getAbsolutePath(), activity);
            String fileType = MediaUtils.getFileType(file);
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
}
