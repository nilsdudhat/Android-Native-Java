package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class MediaUtils {

    public static String getMediaType(String file_format) {
        if (file_format.startsWith("image")) {
            return "image";
        } else if (file_format.startsWith("video")) {
            return "video";
        } else {
            return "";
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

    public static String getDuration(String path, String media_type) {
        if (media_type.startsWith("video") || media_type.startsWith("audio")) {
            try {
                MediaPlayer mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource(path);
                mMediaPlayer.prepare();
                long duration = mMediaPlayer.getDuration();
                mMediaPlayer.release();
                return convertMillisToMinutes(String.valueOf(duration));
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    public static String getResolution(String path, String media_type) {
        if (media_type.equals("video")) {
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            metaRetriever.setDataSource(path);
            String height = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            String width = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);

            return width + "x" + height;
        } else if (media_type.equals("image")) {
            BitmapFactory.Options bitMapOption = new BitmapFactory.Options();
            bitMapOption.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, bitMapOption);
            int imageWidth = bitMapOption.outWidth;
            int imageHeight = bitMapOption.outHeight;

            return imageWidth + "x" + imageHeight;
        } else {
            return "";
        }
    }

    public static String getStringSizeLengthFile(long size) {

        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;

        if (size < sizeMb) {
            return df.format(size / sizeKb) + " KB";
        } else if (size < sizeGb) {
            return df.format(size / sizeMb) + " MB";
        } else if (size < sizeTerra) {
            return df.format(size / sizeGb) + " GB";
        }

        return "";
    }

    @SuppressLint("DefaultLocale")
    public static String convertMillisToMinutes(String duration) {
        try {
            int y = Integer.parseInt(duration);
            long ho = TimeUnit.MILLISECONDS.toHours(y);
            long mo = TimeUnit.MILLISECONDS.toMinutes(y) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(y));
            long so = TimeUnit.MILLISECONDS.toSeconds(y) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(y));

            if (ho >= 1) return String.format("%02d:%02d:%02d", ho, mo, so);
            else return String.format("%02d:%02d", mo, so);
        } catch (Exception e) {
            return "00:00";
        }
    }
}
