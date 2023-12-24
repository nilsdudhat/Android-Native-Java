package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;

import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles.VideoModel;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Common {


    public static final int READ_EXTERNAL_STORAGE_REQ_CODE = 100;
    public static final int WRITE_EXTERNAL_STORAGE_REQ_CODE = 101;

    public static final String IMAGE = "Images";
    public static final String VIDEO = "Videos";
    public static final String ALBUM = "Albums";
    public static final double ONE_BYTE_IN_MB = 0.00000095367432;
    public static final String IS_TIMELINE = "isTimeline";
    public static final String POSITION = "position";
    public static final String BUNDLE = "bundle";
    public static final String FOLDER = "MyGallery";
    public static final String IS_CUSTOM_THEME_APPLIED = "isCustomThemeApplied";
    public static final String DEFAULT_THEME = "defaultTheme";
    public static final String CURRENT_THEME = "currentTheme";
    public static final String GRADIENT = "Gradient";
    public static final String COLOR = "Color";
    public static final String GRADIENT_THEME = "GradientTheme";
    public static final String IS_GRADIENT_THEME = "isGradientTheme";
    public static final String BUCKET_NAME = "bucketName";
    public static final String FILE_TYPE = "fileType";
    public static final String COLOR_THEME_LIST = "colorThemeList";
    public static final String IS_DARK_MODE = "isDarkMode";
    public static final String SECURITY_MEDIUM = "SecurityMedium";
    public static final String PATTERN = "pattern";
    public static final String PIN = "pin";
    public static final String CURRENT_PATTERN = "currentPattern";
    public static final String CURRENT_PIN = "currentPin";
    public static final String IS_FINGERPRINT_ADDED = "isFingetPrintAdded";
    public static final String IS_APPLOCK_ENABLED = "isAppLockEnabled";
    public static final String IS_TRASHLOCK_ENABLED = "isTrashLockEnabled";
    public static final String MEDIA_GRID_COUNT = "mediaGridCount";
    public static final String ALBUM_GRID_COUNT = "albumGridCount";
    public static final String LAST_THEME_MODEL = "lastThemeModel";
    public static final String LAST_THEME_MODEL_MODE = "lastThemeMode"; // G = Gradient |C = Color
    public static final String LAST_THEME_POSITION = "LAST_GRADIENT_THEME_POSITION";
    public static final String SORT_BY = "sortBy";// sortBy options may be NAME | SIZE | DATE
    public static final String NAME = "name";
    public static final String SIZE = "size";
    public static final String DATE = "date";
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    public static final String MOD_DATE = "modifiedDate";
    public static final String SORT_ORDER = "sortOrder"; // sort order may be ASC | DESC
    public static final String IS_TRASH_ON_NAV = "isTrashOnNav";
    public static final String IS_HIDDEN_FOLDER_ON_NAV = "isHiddenFolderOnNav";
    public static final String IS_SIZE_VIEWER_ON_NAV = "isSizeViewerOnNav";
    public static final String IS_STATUS_SAVER_ON_HOME = "isStatusSaverOnHome";
    public static final String IS_KEEP_LAST = "isKeepLast";
    public static final String UPLOAD_URL = "https://api.imgbb.com/1/upload";
    public static final String IS_FULLSCREEN = "isFullscreen";
    public static final String IS_CAMERA_FAB = "isCameraFab";
    public static final String IS_FASTSCROLLER = "isFastScroller";
    public static final String IS_IMAGE_EDIT = "isImageEdit";
    public static final String IS_IMAGE_CROP = "isImageCrop";
    public static final String IS_VIDEO_PLAYER = "isVideoPlayer";
    public static final String MEDIA_QUALITY_KEY = "mediaQuality";
    public static final String SLIDE_SHOW_DURATION = "slideShowDuration";
    public static final String DEFAULT_TAB = "defaultTab";
    public static final String NAVBAR_COLOR = "navbarColor";
    public static final String IS_HIGH_BRIGHTNESS = "isHighBrightness";
    public static final String IS_START_ANIMATION = "isStartAnimation";
    public static final String HAS_SPACE = "hasSpace";
    public static final String SHOW_MIDEA_NAME = "showMediaName";
    public static final String LOCALE = "locale";
    public static int MEDIA_QUALITY = 500;
    //    public static List<HiddenFolderChildModel> LIST_OF_HIDDEN_ITEM = new ArrayList<>();
//    public static List<StoryModel> LIST_OF_STORIES = new ArrayList<>();
//    public static List<GradientThemeModel> MY_GRADIENT_THEME_LIST = new ArrayList<>();
//    public static List<ThemeModel> MY_COLOR_THEME_LIST = new ArrayList<>();
    public static List<VideoModel> LIST_OF_VIDEOS = new ArrayList<>();
    public static String GRADIENT_THEME_LIST = "gradientThemeList";
    public static String IMGBB_API = "c5ceb02d0ab8664af4f42d51da62c229";
    private static ProgressDialog progressDialog;

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void deleteFileByPath(String path, Activity activity) {
        File file = new File(path);
        file.delete();
        if (file.exists()) {
            try {
                file.getCanonicalFile().delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file.exists()) {
                activity.deleteFile(file.getName());
            }
        }
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void showDialog(Activity activity, String msg) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(msg);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public static void dismisDialog() {
        progressDialog.dismiss();
    }

    public static String longToDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    public static String longToDayDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("EEEE, d MMM yyyy", cal).toString();
        return date;
    }


    public static void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(300);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public static void slideTopLayUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,         // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                -view.getHeight());                // toYDelta
        animate.setDuration(300);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public static void slideTopLayDown(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                -view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(300);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public static void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(300);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public static void scaleDown(View view) {
        ScaleAnimation animate = new ScaleAnimation(
                1,                 // fromXDelta
                0,                 // toXDelta
                1,                 // fromYDelta
                0, view.getWidth() / 2, view.getHeight() / 2); // toYDelta
        animate.setDuration(300);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public static void scaleup(View view) {
        ScaleAnimation animate = new ScaleAnimation(
                0,
                1,
                0,
                1, view.getWidth() / 2, view.getHeight() / 2); // toYDelta
        animate.setDuration(300);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    @SuppressLint("NewApi")
    public static void setStatusBarGradiant(Activity activity, int background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, android.R.color.transparent));
            window.setNavigationBarColor(ContextCompat.getColor(activity, android.R.color.transparent));
            window.setBackgroundDrawableResource(background);
        }
    }

    public static void setStatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(color);
        window.setNavigationBarColor(color);
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

    public static int getHiddenFolderChildCount(String directory) {
        File file = new File(directory);
        File[] list = file.listFiles();
        int cnt = 0;
        for (File f : list) {
            if (isImageFile(f.getAbsolutePath()) || isVideoFile(f.getAbsolutePath())) {
                cnt++;
            }
        }
        return (cnt);
    }

    public static String getHiddenFolderThumbnail(String directory) {
        File file = new File(directory);
        File[] list = file.listFiles();
        String path = "";
        for (File f : list) {
            if (isImageFile(f.getAbsolutePath()) || isVideoFile(f.getAbsolutePath())) {
                path = f.getAbsolutePath();
                break;
            }
        }
        return path;
    }


    public static String getFilesSize(String x) {
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
