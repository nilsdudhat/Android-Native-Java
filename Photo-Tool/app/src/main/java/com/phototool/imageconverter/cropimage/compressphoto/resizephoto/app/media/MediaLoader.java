package com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.media;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.models.ImageModel;

import java.util.ArrayList;

public class MediaLoader {

    public static ArrayList<ImageModel> loadFilesFromFolder(Activity activity, String folderName) {

        ArrayList<ImageModel> pathList = new ArrayList<>();

        ContentResolver contentResolver = activity.getContentResolver();

        Uri uri = MediaStore.Files.getContentUri("external");
//        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Files.FileColumns.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + folderName + "%"};

        @SuppressLint("Recycle") Cursor cursor = contentResolver.query(uri, null, selection, selectionArgs, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                ImageModel imageModel = new ImageModel(path);
                pathList.add(imageModel);
            } while (cursor.moveToNext());
        }

        return pathList;
    }
}
