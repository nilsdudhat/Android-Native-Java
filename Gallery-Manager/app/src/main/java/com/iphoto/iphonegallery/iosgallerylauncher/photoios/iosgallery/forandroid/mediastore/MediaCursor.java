package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.mediastore;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DateUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.PreferenceUtils;

import java.util.ArrayList;

public class MediaCursor {

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

    public static ArrayList<FileModel> getAllMedia(@NonNull Context context) {
        Cursor cursor = makeMediaCursor(context, null, null);
        return getMedias(context, cursor);
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
    public static ArrayList<FileModel> getMedias(Context context, @Nullable final Cursor cursor) {
        ArrayList<FileModel> fileModelList = new ArrayList<>();

        int video_count = 0;
        int image_count = 0;

        try {
            if (cursor != null && cursor.moveToFirst()) {

                int column_id = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID);
                int column_path = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
                int column_date_modified = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED);
                int column_file_format = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE);
                int column_file_size = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE);

                do {
                    String id = cursor.getString(column_id);
                    String path = cursor.getString(column_path);
                    String date_modified = DateUtils.getFullDateFromLong(Long.parseLong(cursor.getString(column_date_modified)));
                    String file_format = cursor.getString(column_file_format);
                    String size = cursor.getString(column_file_size);

                    FileModel fileModel = new FileModel(id, path, date_modified, file_format, "", size);

                    fileModelList.add(fileModel);

                    if (file_format.startsWith("image")) {
                        image_count++;
                    } else if (file_format.startsWith("video")) {
                        video_count++;
                    }

                    Log.d("--path--", "getMedias: " + fileModel.getPath());
                } while (cursor.moveToNext());

                PreferenceUtils.setInteger((Activity) context, PreferenceUtils.VIDEO_COUNT, video_count);
                PreferenceUtils.setInteger((Activity) context, PreferenceUtils.IMAGE_COUNT, image_count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return fileModelList;
    }
}
