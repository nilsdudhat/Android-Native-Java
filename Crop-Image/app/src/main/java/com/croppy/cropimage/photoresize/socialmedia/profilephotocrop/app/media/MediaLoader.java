package com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.media;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.croppy.cropimage.photoresize.socialmedia.profilephotocrop.app.models.ImageModel;

import java.util.ArrayList;

public class MediaLoader {

    public static ArrayList<ImageModel> loadFilesFromFolder(Activity activity, String folderName) {

        ArrayList<ImageModel> pathList = new ArrayList<>();

        ContentResolver contentResolver = activity.getContentResolver();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Images.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + folderName + "%"};

        @SuppressLint("Recycle") Cursor cursor = contentResolver.query(uri, null, selection, selectionArgs, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                ImageModel imageModel = new ImageModel(path);
                pathList.add(imageModel);
            } while (cursor.moveToNext());
        }

        return pathList;
    }
}
