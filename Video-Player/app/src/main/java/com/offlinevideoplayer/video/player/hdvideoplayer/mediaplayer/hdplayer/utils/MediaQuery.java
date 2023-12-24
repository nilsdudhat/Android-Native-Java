package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.models.FolderModel;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("LoopStatementThatDoesntLoop")
public class MediaQuery {
    private final Context context;

//    public static final Map<Path, String> sPathDocumentIdCache = Collections.synchronizedMap(
//            new WeakHashMap<>());


    public MediaQuery(Context context) {
        this.context = context;
    }

    public static void deleteImages(List<Long> list, Activity context) {
        for (Long longValue : list) {
            context.getContentResolver().delete(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, longValue.longValue()), null, null);
        }
    }

    public List<FolderModel> getfolderList() {
        String[] projection = {MediaStore.Video.Media.BUCKET_ID};
        String selection = null;
        Cursor cursor;
        cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection, selection, null, null);

        LinkedHashSet<String> v1 = new LinkedHashSet<>();

        v1.clear();
        List<String> f1;
        while (cursor.moveToNext()) {
            v1.add(cursor.getString(0));
        }
        f1 = new ArrayList<>(v1);


        String[] projection1 = {MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.BUCKET_ID
        };

        String selection1 = MediaStore.Video.Media.BUCKET_ID + " =?";
        List<FolderModel> folders = new ArrayList<>();
        for (int i = 0; i < f1.size(); i++) {
            String[] selectionArgs = new String[]{f1.get(i)};
            cursor = context.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    projection1,
                    selection1, selectionArgs,
                    MediaStore.Video.Media.DATE_MODIFIED + " DESC");
            FolderModel folder;
            while (cursor.moveToNext()) {
                folder = new FolderModel();
                folder.setBucket(cursor.getString(0));
                folder.setData(cursor.getString(1));
                folder.setBid(cursor.getString(2));
                folder.setCount(String.valueOf(cursor.getCount()));
                String fName = String.valueOf(folder.getBucket().charAt(0));
                if (!fName.equalsIgnoreCase(".")) {
                    folders.add(folder);
                }
                break;
            }
        }
        cursor.close();
        return folders;
    }

    public ArrayList<FolderModel> getFolderList() {
        String[] projection = {MediaStore.Video.Media.BUCKET_ID,};
        String selection = null;
        String x = null;
        Cursor cursor;

        cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection, selection, null, null);

        LinkedHashSet<String> v1 = new LinkedHashSet<>();

        List<String> f1;
        while (cursor.moveToNext()) {
            v1.add(cursor.getString(0));
        }
        f1 = new ArrayList<>(v1);

        String[] projection1 = {MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DATE_MODIFIED
        };

        String selection1 = MediaStore.Video.Media.BUCKET_ID + " =?";
        ArrayList<FolderModel> folders = new ArrayList<>();
        for (int i = 0; i < f1.size(); i++) {
            String[] selectionArgs = new String[]{f1.get(i)};
            cursor = context.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection1,
                    selection1, selectionArgs, MediaStore.Video.Media.DATE_MODIFIED + " DESC");

            FolderModel folder;
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    folder = new FolderModel();
                    folder.setBucket(cursor.getString(0));
                    folder.setData(cursor.getString(1));
                    folder.setBid(cursor.getString(2));
                    folder.setDate(date(cursor.getString(4)));
                    folder.setCount(String.valueOf(cursor.getCount()));
                    String fName = String.valueOf(folder.getBucket().charAt(0));
                    long fSize = 0;
                    for (int p = 0; p < cursor.getCount(); p++) {
                        fSize += cursor.getLong(3);
                    }
                    folder.setSize(size(String.valueOf(fSize)));
                    folder.setFolderSize(fSize);
                    if (!fName.equalsIgnoreCase(".")) {
                        folders.add(folder);
                    }
                    break;
                }
            }

            cursor.close();
        }
        return folders;
    }

    public List<VideoModel> getAllVideo(String foldername, int order) {
        Cursor cursor;
        String x = null;
        String selection = MediaStore.Video.Media.BUCKET_ID + " =?";
        String[] selectionArgs = new String[]{null};
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION
        };
        switch (order) {
            case 0:
                x = MediaStore.Video.Media.DATE_MODIFIED + " DESC";
                break;
            case 1:
                x = MediaStore.Video.Media.DATE_MODIFIED;
                break;
            case 2:
                x = MediaStore.Video.Media.DURATION + " DESC";
                break;
            case 3:
                x = MediaStore.Video.Media.DURATION;
                break;
            case 4:
                x = MediaStore.Video.Media.DISPLAY_NAME;
                break;
            case 5:
                x = MediaStore.Video.Media.DISPLAY_NAME + " DESC";
                break;
            case 6:
                x = MediaStore.Video.Media.SIZE + " DESC";
                break;
            case 7:
                x = MediaStore.Video.Media.SIZE;
                break;
        }
        cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection, null,
                MediaStore.Video.Media.DATE_MODIFIED + " DESC");

        List<VideoModel> videoItems = new ArrayList<>();
        VideoModel videoItem;
        while (cursor.moveToNext()) {
            videoItem = new VideoModel();
            videoItem.setId(cursor.getInt(0));
            videoItem.setSize(size(cursor.getString(1)));
            videoItem.setDate(date(cursor.getString(2)));
            videoItem.setPath(cursor.getString(3));
            videoItem.setTitle(cursor.getString(4));
            videoItem.setDuration(duration(cursor.getString(5)));
            videoItem.setSize(cursor.getString(1));
            videoItems.add(videoItem);
        }
        cursor.close();
        return videoItems;
    }

    public List<VideoModel> getAllVideoWithName() {
        Cursor cursor;
        String selection = MediaStore.Video.Media.BUCKET_ID + " =?";
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION
        };
        cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null, null,
                MediaStore.Video.Media.DATE_MODIFIED + " DESC");

        List<VideoModel> videoItems = new ArrayList<>();
        VideoModel videoItem;
        while (cursor.moveToNext()) {
            videoItem = new VideoModel();
            videoItem.setId(cursor.getInt(0));
            videoItem.setSize(size(cursor.getString(1)));
            videoItem.setDate(date(cursor.getString(2)));
            videoItem.setPath(cursor.getString(3));
            videoItem.setTitle(cursor.getString(4));
            videoItem.setDuration(duration(cursor.getString(5)));
            videoItem.setSize(cursor.getString(1));
            videoItems.add(videoItem);
        }
        cursor.close();
        return videoItems;
    }

    public List<VideoModel> getAllImageWithName() {
        Cursor cursor;
        String selection = MediaStore.Images.Media.BUCKET_ID + " =?";
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME
        };
        cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null, null,
                MediaStore.Images.Media.DATE_MODIFIED + " DESC");

        List<VideoModel> videoItems = new ArrayList<>();
        VideoModel videoItem;
        while (cursor.moveToNext()) {
            videoItem = new VideoModel();
            videoItem.setId(cursor.getInt(0));
            videoItem.setSize(size(cursor.getString(1)));
            videoItem.setDate(date(cursor.getString(2)));
            videoItem.setPath(cursor.getString(3));
            videoItem.setTitle(cursor.getString(4));
            videoItem.setDuration(duration(cursor.getString(5)));
            videoItem.setSize(cursor.getString(1));
            videoItems.add(videoItem);
        }
        cursor.close();
        return videoItems;
    }

    public List<VideoModel> getAllLockVideo(String foldername) {
        Cursor cursor;
        String selection = MediaStore.Video.Media.BUCKET_DISPLAY_NAME + " =?";
        String[] selectionArgs = new String[]{foldername};
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION
        };

        Log.e("foldername ", " : " + foldername);

        cursor = context.getContentResolver().query(MediaStore.Video.Media.getContentUri(foldername),
                projection,
                selection, selectionArgs,
                MediaStore.Video.Media.DATE_MODIFIED + " DESC");

        Log.e("cursor ", " : " + cursor.getCount());

        List<VideoModel> videoItems = new ArrayList<>();
        VideoModel videoItem;
        while (cursor.moveToNext()) {
            videoItem = new VideoModel();
            videoItem.setId(cursor.getInt(0));
            videoItem.setSize(size(cursor.getString(1)));
            videoItem.setDate(date(cursor.getString(2)));
            videoItem.setPath(cursor.getString(3));
            videoItem.setTitle(cursor.getString(4));
            videoItem.setDuration(duration(cursor.getString(5)));
            videoItems.add(videoItem);
        }
        cursor.close();
        return videoItems;
    }

    private String duration(String x) {
        try {
            int y = Integer.parseInt(x);
            long ho = TimeUnit.MILLISECONDS.toHours(y);
            long mo = TimeUnit.MILLISECONDS.toMinutes(y) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(y));
            long so = TimeUnit.MILLISECONDS.toSeconds(y) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(y));

            if (ho >= 1) return String.format("%02d:%02d:%02d", ho, mo, so);
            else return String.format("%02d:%02d", mo, so);
        } catch (Exception e) {
//            Crashlytics.setString("duration",x);
            return "00:00";
        }
    }

    private String date(String x) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date(Long.parseLong(x) * 1000));
    }

    private String size(String x) {
        if (x == null) {
            x = "123";
        }
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.FLOOR);
        double s = Double.parseDouble(x);
        s = s / 1024;
        if (s < 1024) {
            return df.format(s) + " KB";
        } else {
            s = s / 1024;
            if (s < 1024) {
                return df.format(s) + " MB";
            } else {
                s = s / 1024;
                return df.format(s) + " GB";
            }

        }
    }

}
