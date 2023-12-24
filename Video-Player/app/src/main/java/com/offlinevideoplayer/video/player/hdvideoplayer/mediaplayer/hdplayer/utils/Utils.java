package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.R;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoModel;
import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofolders.VideoFolderModel;

import java.io.File;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Utils {

    public static final int REQUEST_PERM_DELETE_VIDEO_FILE = 1221;
    public static final int REQUEST_PERM_DELETE_FOLDER = 2112;
    private static final String HTTPS = "https://";
    private static final String HTTP = "http://";
    public static String pageSelection = "Files";
    public static boolean isInPIPMode = false;
    public static int selectedItem = 0;
    public static List<VideoModel> videoModelArrayList = new ArrayList<>();
    public static List<VideoFolderModel> videoFolderModelArrayList = new ArrayList<>();
    public static String deleteFile = "";
    public static ArrayList<VideoModel> queriedVideoModels = new ArrayList<>();
    public static boolean notifyFilesAdapter = false;
    public static boolean notifyFoldersAdapter = false;
    public static boolean refreshFiles = false;
    public static boolean refreshFolder = false;

    public static void openBrowser(final Context context, String url) {
        if (!url.startsWith(HTTP) && !url.startsWith(HTTPS)) {
            url = HTTP + url;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(Intent.createChooser(intent, "Choose browser"));// Choose browser is arbitrary :)
    }

    public static void rateApp(Activity activity) {
        try {
            Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
            Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(goMarket);
        } catch (ActivityNotFoundException e) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName());
            Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(goMarket);
        }
    }

    public static void shareApp(Activity activity) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + activity.getPackageName() + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            activity.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bottomNavigationWhiteColor(Activity activity) {
        activity.getWindow().setNavigationBarColor(activity.getColor(R.color.white));
    }

    public static void bottomNavigationBlackColor(Activity activity) {
        activity.getWindow().setNavigationBarColor(activity.getColor(R.color.black));
    }

    // Function to remove the element
    public static File[] removeTheElement(File[] arr, int index) {

        // If the array is empty
        // or the index is not in array range
        // return the original array
        if (arr == null || index < 0
                || index >= arr.length) {

            return arr;
        }

        // Create another array of size one less
        File[] anotherArray = new File[arr.length - 1];

        // Copy the elements except the index
        // from original array to the other array
        for (int i = 0, k = 0; i < arr.length; i++) {

            // if the index is
            // the removal element index
            if (i == index) {
                continue;
            }

            // if the index is not
            // the removal element index
            anotherArray[k++] = arr[i];
        }

        // return the resultant array
        return anotherArray;
    }

    @SuppressLint("NewApi")
    public static String getDateToDisplay(String dateModified) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
        LocalDate currentDate = LocalDate.parse(dateModified, format);
        int day = currentDate.getDayOfMonth();
        Month month = currentDate.getMonth();
        int year = currentDate.getYear();

        String strMonth = String.valueOf(month).substring(0, 3);
        String modifiedByUpperLower = strMonth.substring(0, 1).toUpperCase() + strMonth.substring(1).toLowerCase();

        return day + " " + modifiedByUpperLower + ", " + year;
    }

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void deleteFiles(@Nullable File[] files, final int requestCode, Activity activity, @Nullable Intent fillInIntent) {
        if (files != null && files.length > 0) {
            deleteFiles(Arrays.asList(files), requestCode, activity, fillInIntent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void deleteFiles(@Nullable List<File> files, final int requestCode, Activity activity, @Nullable Intent fillInIntent) {
        if (files == null || files.isEmpty()) {
            return;
        }

        List<Uri> uris = files.stream().map(file -> {

            long mediaID = getFilePathToMediaID(file.getAbsolutePath(), activity);
            String fileType = getFileType(file);
            if (fileType.equalsIgnoreCase("video")) {
                return ContentUris.withAppendedId(MediaStore.Video.Media.getContentUri("external"), mediaID);
            } else {
                return ContentUris.withAppendedId(MediaStore.Images.Media.getContentUri("external"), mediaID);
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

    private static String getFileType(File file) {

        String mimeType = URLConnection.guessContentTypeFromName(file.getPath());
        if (mimeType != null) {

            if (mimeType.startsWith("video")) {
                return "video";
            } else if (mimeType.startsWith("image")) {
                return "image";
            }
        }
        return "";
    }

    private static long getFilePathToMediaID(String songPath, Context context) {
        long id = 0;
        ContentResolver cr = context.getContentResolver();

        Uri uri = MediaStore.Files.getContentUri("external");
        String selection = MediaStore.Audio.Media.DATA;
        String[] selectionArgs = {songPath};
        String[] projection = {MediaStore.Audio.Media._ID};
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = cr.query(uri, projection, selection + "=?", selectionArgs, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                id = Long.parseLong(cursor.getString(idIndex));
            }
        }

        return id;
    }

    public static void openWebPage(Activity activity, String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    public static boolean contains(List<VideoFolderModel> list, String name) {
        for (VideoFolderModel item : list) {
            if (item.getTitle().equals(name)) {
                return true;
            }
        }
        return false;
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

    @SuppressLint("Range")
    public static int getFilesVideoCount(Activity activity, String folderName) {

        if (folderName.equalsIgnoreCase("Internal Storage")) {
            folderName = "0";
        }

        int filesCount = 0;

        ContentResolver contentResolver = activity.getContentResolver();

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + folderName + "%"};

        @SuppressLint("Recycle") Cursor cursor = contentResolver.query(uri, null, selection, selectionArgs, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                File file = new File(path);
                File parentFile = file.getParentFile();
                if (parentFile != null && parentFile.exists()) {
                    if (parentFile.getName().equals(folderName)) {
                        filesCount++;
                    }
                }
            } while (cursor.moveToNext());

            return filesCount;
        } else {
            return 0;
        }
    }

    @SuppressLint("DefaultLocale")
    public static String convertMillieToHMmSs(int j) {
        long j2 = j / 1000;
        long j3 = j2 % 60;
        long j4 = (j2 / 60) % 60;
        long j5 = (j2 / 3600) % 24;
        if (j5 > 0) {
            return String.format("%02d:%02d:%02d", j5, j4, j3);
        }
        return String.format("%02d:%02d", j4, j3);
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

    public static long folderSize(Activity activity, File directory) {

        if (directory.exists()) {
            long result = 0;
            File[] fileList = directory.listFiles();
            if (fileList != null) {
                for (int i = 0; i < fileList.length; i++) {
                    // Recursive call if it's a directory
                    if (fileList[i].exists()) {
                        if (fileList[i].isDirectory()) {
                            result += folderSize(activity, fileList[i]);
                        } else {
                            // Sum the file size in bytes
                            Log.d("--mime--", "folderSize: " + getMimeType(activity, Uri.fromFile(fileList[i])));
                            String mimeType = getMimeType(activity, Uri.fromFile(fileList[i]));
                            if (mimeType != null) {
                                if (mimeType.startsWith("video")) {
                                    result += fileList[i].length();
                                }
                            }
                        }
                    }
                }
            }
            return result; // return the file size
        }
        return 0;
    }

    public static String getMimeType(Activity activity, Uri uri) {
        String mimeType = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = activity.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public static String getFormattedDate(File file) {
        Date lastModDate = new Date(file.lastModified());
        String[] formatDate = lastModDate.toString().split(" ");
//        String time = formatDate[3];
//        String[] formatTime = time.split(":");
//        String date = formatTime[0] + ":" + formatTime[1];
        String substring = lastModDate.toString().substring(Math.max(lastModDate.toString().length() - 4, 0));

        return formatDate[0] + ", " + formatDate[1] + " " + formatDate[2] + ", " + substring;
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            if (activity.getWindow().getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
