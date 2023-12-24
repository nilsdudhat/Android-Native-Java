package com.phototool.imageconverter.cropimage.compressphoto.resizephoto.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class BitmapUtils {

    public static Bitmap decodeUriToBitmap(Context mContext, Uri sendUri) {
        Bitmap getBitmap = null;
        try {
            InputStream image_stream;
            try {
                image_stream = mContext.getContentResolver().openInputStream(sendUri);
                getBitmap = BitmapFactory.decodeStream(image_stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBitmap;
    }

    public static void MakeSureFileWasCreatedThenMakeAvailable(Activity activity, File file) {
        MediaScannerConnection.scanFile(activity, new String[]{file.toString()}, new String[]{"image/jpeg"}, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {

            }
        });
    }

    public static Bitmap getResizedBitmap(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    public static Bitmap compressBitmap(Bitmap bitmap, int quality) {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(bitmap, 0, 0, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

        byte[] user_photo = outputStream.toByteArray();
        String encodedImage = Base64.encodeToString(user_photo, Base64.DEFAULT);
        String path = encodedImage.toString();

        byte[] b = Base64.decode(path, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    public static int getBitmapWidth(Bitmap bitmap) {
        return bitmap.getWidth();
    }

    public static int getBitmapHeight(Bitmap bitmap) {
        return bitmap.getHeight();
    }

    public static String getBitmapSize(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageInByte = stream.toByteArray();
            long size = imageInByte.length;

            return AppUtils.readableFileSize(size);
        }
        return "";
    }

    public static String getBitmapSize(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        long size = imageInByte.length;

        return AppUtils.readableFileSize(size);
    }

    public static String getResolution(@Nullable Bitmap bitmap) {
        return Objects.requireNonNull(bitmap).getWidth() + "x" + bitmap.getHeight();
    }

    public static float getAspectRatio(@Nullable Bitmap initialBitmap) {
        float aspectRatio = (float) initialBitmap.getWidth() / (float) initialBitmap.getHeight();
        Log.d("--aspect--", "getAspectRatio: " + initialBitmap.getWidth());
        Log.d("--aspect--", "getAspectRatio: " + initialBitmap.getHeight());
        Log.d("--aspect--", "getAspectRatio: " + aspectRatio);
        return aspectRatio;
    }
}