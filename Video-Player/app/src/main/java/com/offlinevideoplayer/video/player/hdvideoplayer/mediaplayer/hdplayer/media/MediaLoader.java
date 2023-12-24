package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.media;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoViewModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofolders.VideoFolderModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofolders.VideoFoldersViewModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MediaLoader {

    public static void storeVideoFilesInDatabase(Activity activity, VideoViewModel videoViewModel, VideoFoldersViewModel videoFoldersViewModel) {

        Log.d("--date--", "date start: " + new Date());

        ArrayList<VideoFolderModel> videoFolderModelList = new ArrayList<>();

        ContentResolver contentResolver = activity.getContentResolver();

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        @SuppressLint("Recycle") Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Log.d("--cursor--", "storeVideoFilesInDatabase: " + cursor.getCount());
            do {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                @SuppressLint("Range") String resolution = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION));
                @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));

                File file = new File(path);
                File parentFile = file.getParentFile();

                String date = Utils.getFormattedDate(new File(path));

                VideoModel videoModel = new VideoModel(title, duration, path, Objects.requireNonNull(parentFile).getName(), date, resolution, size);

                videoViewModel.insert(videoModel);

                Log.d("--videos--", "path: " + path);

                String folderName = parentFile.getName();
                String folderPath = parentFile.getAbsolutePath();

                if (folderName.equalsIgnoreCase("0")) {
                    folderName = "Internal Storage";
                }
                VideoFolderModel videoFolderModel = new VideoFolderModel(folderName, folderPath, 0);

                if (!Utils.contains(videoFolderModelList, videoFolderModel.getTitle())) {
                    int filesCount = Utils.getFilesVideoCount(activity, folderName);
                    videoFolderModel.setFilesCount(filesCount);
                    videoFolderModelList.add(videoFolderModel);
                    videoFoldersViewModel.insert(videoFolderModel);
                }
            } while (cursor.moveToNext());
        }

        Log.d("--date--", "date done: " + new Date());
    }

    public static ArrayList<VideoModel> loadVideoFilesFromFolder(Activity activity, VideoFolderModel videoFolderModel) {

        ArrayList<VideoModel> videoModelList = new ArrayList<>();

        ContentResolver contentResolver = activity.getContentResolver();

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String folderName = videoFolderModel.getTitle();
        if (folderName.equalsIgnoreCase("Internal Storage")) {
            folderName = "0";
        }

        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + folderName + "%"};

        @SuppressLint("Recycle") Cursor cursor = contentResolver.query(uri, null, selection, selectionArgs, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));

                File file = new File(path);
                File parentFile = file.getParentFile();

                if (parentFile != null && parentFile.exists()) {
                    if (parentFile.getName().equals(videoFolderModel.getTitle()) || parentFile.getName().equals("0")) {

                        @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                        @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                        @SuppressLint("Range") String resolution = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION));
                        @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));

                        String date = Utils.getFormattedDate(new File(path));

                        VideoModel videoModel = new VideoModel(title, duration, path, parentFile.getName(), date, resolution, size);

                        videoModelList.add(videoModel);

                        Log.d("--videos--", "path: " + path);
                    }
                }
            } while (cursor.moveToNext());
        }

        return videoModelList;
    }
}
