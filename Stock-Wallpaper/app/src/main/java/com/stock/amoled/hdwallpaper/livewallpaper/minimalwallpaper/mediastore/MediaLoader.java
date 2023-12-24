package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.mediastore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models.Wallpaper;

import java.util.ArrayList;

public class MediaLoader {

    // Return only video and image metadata.
    static String BASE_SELECTION = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
            + " OR "
            + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

    static String[] BASE_PROJECTION = {
            MediaStore.Files.FileColumns._ID, // file id
            MediaStore.Files.FileColumns.DATA, // file path
            MediaStore.Files.FileColumns.DATE_MODIFIED, // date modified
            MediaStore.Files.FileColumns.MIME_TYPE, // full format of file
            MediaStore.Files.FileColumns.SIZE // full format of file
    };

    public static ArrayList<Wallpaper.Detail> getAllMedia(@NonNull Context context) {
        Cursor cursor = makeMediaCursor(context, null, null);
        return getMedias(cursor);
    }

    @Nullable
    public static Cursor makeMediaCursor(@NonNull final Context context, @Nullable final String selection, final String[] selectionValues) {
        return makeMediaCursor(context, selection, selectionValues, MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC");
    }

    @Nullable
    public static Cursor makeMediaCursor(@NonNull final Context context, @Nullable String selection, String[] selectionValues, final String sortOrder) {
        if (selection != null && !selection.trim().equals("")) {
            selection = BASE_SELECTION + " AND " + selection;
        } else {
            selection = BASE_SELECTION;
        }

        try {
            return context.getContentResolver().query(MediaStore.Files.getContentUri("external"),
                    BASE_PROJECTION, selection, selectionValues, sortOrder);
        } catch (SecurityException e) {
            return null;
        }
    }

    @NonNull
    public static ArrayList<Wallpaper.Detail> getMedias(@Nullable final Cursor cursor) {
        ArrayList<Wallpaper.Detail> wallpaperModelList = new ArrayList<>();

        try {
            if (cursor != null && cursor.moveToFirst()) {

                int column_id = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID);
                int column_path = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
                int column_file_size = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE);

                do {
                    String id = cursor.getString(column_id);
                    String path = cursor.getString(column_path);
                    String size = cursor.getString(column_file_size); // needed here for solving

                    Wallpaper.Detail wallpaperModel = new Wallpaper.Detail();
                    wallpaperModel.setId(id);
                    wallpaperModel.setImage(path);
                    wallpaperModel.setSize(Double.valueOf(size));

                    wallpaperModelList.add(wallpaperModel);

                    Log.d("--path--", "getMedias: " + wallpaperModel.getImage());
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return wallpaperModelList;
    }

    public static ArrayList<Wallpaper.Detail> getMediaFilesFromFolder(Activity activity, String folderName) {

        ArrayList<Wallpaper.Detail> wallpaperModelArrayList = new ArrayList<>();

        ContentResolver contentResolver = activity.getContentResolver();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        if (folderName.equalsIgnoreCase("Internal Storage")) {
            folderName = "0";
        }

        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + folderName + "%"};

        @SuppressLint("Recycle") Cursor cursor = contentResolver.query(uri, null, selection, selectionArgs, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {

                int column_id = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID);
                int column_path = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
                int column_file_size = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE);

                do {
                    String id = cursor.getString(column_id);
                    String path = cursor.getString(column_path);
                    String size = cursor.getString(column_file_size);

                    Wallpaper.Detail wallpaperModel = new Wallpaper.Detail();
                    wallpaperModel.setId(id);
                    wallpaperModel.setImage(path);
                    wallpaperModel.setSize(Double.valueOf(size));

                    wallpaperModelArrayList.add(wallpaperModel);

                    Log.d("--path--", "getMedias: " + wallpaperModel.getImage());
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return wallpaperModelArrayList;
    }
}