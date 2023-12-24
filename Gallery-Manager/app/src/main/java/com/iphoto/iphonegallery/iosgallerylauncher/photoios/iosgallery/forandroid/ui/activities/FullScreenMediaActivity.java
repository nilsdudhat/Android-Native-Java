package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.Constant;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter.BottomMediaIndicatorAdapter;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter.MediaPagerAdapter;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.coverflow.CoverFlowLayoutManger;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumDBModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.ActivityFullScreenMediaBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.MediaPagerClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.VideoClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.mediastore.MediaLoader;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.AddressDetailModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.CacheUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.ColorUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DateUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DeleteMediaManager;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.LocationUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.MediaUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.ThemeUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.WallpaperUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FullScreenMediaActivity extends BaseActivity implements MediaPagerClickListener, VideoClickListener, BottomMediaIndicatorAdapter.BottomIndicatorClicked {

    ActivityFullScreenMediaBinding binding;

    ArrayList<FileModel> fileModelArrayList = new ArrayList<>();

    String mediaType = "";
    String subMediaType = "";

    int position = 0;

    MediaPagerAdapter mediaPagerAdapter;
    BottomMediaIndicatorAdapter bottomMediaIndicatorAdapter;

    ArrayList<String> pathList = new ArrayList<>();

    boolean isFirstTime = true;

    @Override
    protected void onPause() {
        super.onPause();

        new Thread(() -> CacheUtils.deleteCache(FullScreenMediaActivity.this)).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setTheme(FullScreenMediaActivity.this); // providing theme for activity before @onCreate
        super.onCreate(savedInstanceState);
        binding = ActivityFullScreenMediaBinding.inflate(getLayoutInflater()); // inflating binding
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide(); // removing Action Bar

        showBannerAd(FullScreenMediaActivity.this, binding.adBanner);
        loadInterstitialAd(FullScreenMediaActivity.this);

        binding.imgBack.setOnClickListener(v -> onBackPressed());

        gestureDetector = new GestureDetector(FullScreenMediaActivity.this, new SwipeDetector());

        mediaPagerAdapter = new MediaPagerAdapter(FullScreenMediaActivity.this, this, this);
        binding.pagerFullScreenMedia.setAdapter(mediaPagerAdapter);

        bottomMediaIndicatorAdapter = new BottomMediaIndicatorAdapter(FullScreenMediaActivity.this, FullScreenMediaActivity.this);
        binding.rvMediaIndicator.setAdapter(bottomMediaIndicatorAdapter);

        Intent intent = getIntent();

        if (!Constant.INTENT_FILE_MODEL_ARRAY_LIST.isEmpty()) { // if fileModelArrayList is passed through intent
            fileModelArrayList = new ArrayList<>(Constant.INTENT_FILE_MODEL_ARRAY_LIST);
        }

        if (intent.hasExtra("position")) { // if position is passed through intent
            position = intent.getIntExtra("position", 0);
        }

        if (intent.hasExtra("mediaType")) { // if mediaType is passed through intent
            mediaType = intent.getStringExtra("mediaType");
        }

        if (intent.hasExtra("subMediaType")) { // if subMediaType is passed through intent
            subMediaType = intent.getStringExtra("subMediaType");
        }

        if (!fileModelArrayList.isEmpty()) { // if fileModelArrayList is not empty

            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < fileModelArrayList.size(); i++) {
                        pathList.add(fileModelArrayList.get(i).getPath());
                    }
                }
            }).start();

            mediaPagerAdapter.swapList(fileModelArrayList); // swapping and updating pager adapter with fileModelArrayList
            bottomMediaIndicatorAdapter.swapList(fileModelArrayList); // swapping and updating media indicator adapter with fileModelArrayList

            if (position != 0) {
                Objects.requireNonNull((CoverFlowLayoutManger) binding.rvMediaIndicator.getLayoutManager()).smoothScrollToPosition(binding.rvMediaIndicator, null, position);
                binding.pagerFullScreenMedia.setCurrentItem(position);
            }

            editVisibility(position);

            setScrollListeners();

            clickListeners();
        }

        allMediaViewModel.getAllMediaData().observe(FullScreenMediaActivity.this, new Observer<List<MediaModel>>() {
            @Override
            public void onChanged(List<MediaModel> mediaModels) {
                if (!isFirstTime) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<MediaModel> mediaModelArrayList = new ArrayList<>(mediaModels);

                            Log.d("--delete--", "media_list: " + mediaModelArrayList.size());

                            if (mediaModelArrayList.isEmpty()) {
                                finish();
                            } else {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("--delete--", "media_list: " + "mediaModelArrayList.isEmpty");
                                        switch (mediaType) {
                                            case "All":
                                                refreshAll(mediaModelArrayList);
                                                break;
                                            case "Place":
                                                refreshPlace(mediaModelArrayList);
                                                break;
                                            case "Album":
                                                refreshAlbum(mediaModelArrayList);
                                                break;
                                            case "Day":
                                                refreshDay(mediaModelArrayList);
                                                break;
                                        }
                                    }
                                }).start();
                            }
                        }
                    }).start();
                } else {
                    isFirstTime = false;
                }
            }
        });
    }

    private void refreshPlace(ArrayList<MediaModel> mediaModelList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mediaModelList.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            albumsViewModel.getAllAddressData().observe(FullScreenMediaActivity.this, new Observer<List<AlbumDBModel.AddressDBModel>>() {
                                @Override
                                public void onChanged(List<AlbumDBModel.AddressDBModel> addressDBModels) {
                                    refreshAddressData(addressDBModels);
                                }
                            });
                        }
                    });
                }
            }
        }).start();
    }

    private void refreshAddressData(List<AlbumDBModel.AddressDBModel> addressDBModels) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<FileModel> tempFileList = new ArrayList<>();

                for (int i = 0; i < addressDBModels.size(); i++) {
                    AlbumDBModel.AddressDBModel addressDBModel = addressDBModels.get(i);

                    if (subMediaType.equals(addressDBModel.getAddress())) {
                        FileModel fileModel = new Gson().fromJson(addressDBModel.getFileModel(), FileModel.class);
                        tempFileList.add(fileModel);
                    }
                }

                Collections.sort(tempFileList, new Comparator<FileModel>() {
                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH);

                    @Override
                    public int compare(FileModel lhs, FileModel rhs) {
                        try {
                            return Objects.requireNonNull(simpleDateFormat.parse(lhs.getDateModified())).compareTo(simpleDateFormat.parse(rhs.getDateModified()));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });

                fileModelArrayList = new ArrayList<>(tempFileList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (fileModelArrayList.isEmpty()) {
                            finish();
                        } else {
                            mediaPagerAdapter.swapList(fileModelArrayList); // swapping and updating pager adapter with fileModelArrayList
                            bottomMediaIndicatorAdapter.swapList(fileModelArrayList); // swapping and updating media indicator adapter with fileModelArrayList
                        }
                    }
                });
            }
        }).start();
    }

    private void refreshAlbum(ArrayList<MediaModel> mediaModelList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mediaModelList.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                } else {
                    ArrayList<MediaModel> mediaModelArrayList = new ArrayList<>(mediaModelList);
                    ArrayList<FileModel> fileModelList = new ArrayList<>();

                    for (int i = 0; i < mediaModelArrayList.size(); i++) {
                        MediaModel mediaModel = mediaModelArrayList.get(i);

                        FileModel fileModel = new FileModel(mediaModel.getFileId(), mediaModel.getPath(), mediaModel.getDateModified(), mediaModel.getFileFormat(), mediaModel.getDuration(), mediaModel.getSize());
                        fileModelList.add(fileModel);
                    }

                    Collections.sort(fileModelList, new Comparator<FileModel>() {
                        final DateFormat f = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH);

                        @Override
                        public int compare(FileModel lhs, FileModel rhs) {
                            try {
                                return Objects.requireNonNull(f.parse(rhs.getDateModified())).compareTo(f.parse(lhs.getDateModified()));
                            } catch (ParseException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                    });

                    HashMap<String, ArrayList<FileModel>> tempAlbumHashMap = new HashMap<>(MediaLoader.getAllAlbumHashMap(fileModelList));

                    Log.d("--albums--", "tempAlbumHashMap: " + tempAlbumHashMap.size());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            albumsViewModel.getAllFavorites().observe(FullScreenMediaActivity.this, new Observer<List<AlbumDBModel.FavoriteDBModel>>() {
                                @Override
                                public void onChanged(List<AlbumDBModel.FavoriteDBModel> favoriteDBModels) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ArrayList<FileModel> favoritesList = new ArrayList<>();
                                            ArrayList<AlbumDBModel.FavoriteDBModel> tempFavoritesList = new ArrayList<>(favoriteDBModels);

                                            for (int i = 0; i < tempFavoritesList.size(); i++) {
                                                AlbumDBModel.FavoriteDBModel favoriteDBModel = tempFavoritesList.get(i);

                                                for (int j = 0; j < fileModelList.size(); j++) {
                                                    FileModel fileModel = fileModelList.get(j);

                                                    if (fileModel.getPath().equals(favoriteDBModel.getPath())) {
                                                        favoritesList.add(fileModel);
                                                    }
                                                }
                                            }

                                            if (!favoritesList.isEmpty()) {
                                                tempAlbumHashMap.put("Favorites", favoritesList);
                                            }

                                            if (!fileModelList.isEmpty()) {
                                                tempAlbumHashMap.put("Recents", fileModelList);
                                            }

                                            ArrayList<FileModel> cameraList = new ArrayList<>(MediaLoader.getListFromStringMatch("/Camera/", fileModelList));
                                            if (!cameraList.isEmpty()) {
                                                tempAlbumHashMap.put("Camera", cameraList);
                                            }

                                            ArrayList<FileModel> videoList = new ArrayList<>(MediaLoader.getVideosList(fileModelList));
                                            if (!videoList.isEmpty()) {
                                                tempAlbumHashMap.put("Videos", videoList);
                                            }

                                            ArrayList<FileModel> downloadList = new ArrayList<>(MediaLoader.getListFromStringMatch("/Download/", fileModelList));
                                            if (!downloadList.isEmpty()) {
                                                tempAlbumHashMap.put("Download", downloadList);
                                            }

                                            ArrayList<FileModel> screenshotList = new ArrayList<>(MediaLoader.getListFromStringMatch("/Screenshots/", fileModelList));
                                            if (!screenshotList.isEmpty()) {
                                                tempAlbumHashMap.put("Screenshots", screenshotList);
                                            }

                                            fileModelArrayList = new ArrayList<>();
                                            fileModelArrayList = tempAlbumHashMap.get(subMediaType);

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (fileModelArrayList == null) {
                                                        finish();
                                                    } else {
                                                        if (fileModelArrayList.isEmpty()) {
                                                            finish();
                                                        } else {
                                                            mediaPagerAdapter.swapList(fileModelArrayList); // swapping and updating pager adapter with fileModelArrayList
                                                            bottomMediaIndicatorAdapter.swapList(fileModelArrayList); // swapping and updating media indicator adapter with fileModelArrayList
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }).start();
                                }
                            });
                        }
                    });
                }
            }
        }).start();
    }

    private void refreshDay(ArrayList<MediaModel> mediaModelArrayList) {
        Log.d("--delete--", "mediaType: " + mediaType);
        HashMap<String, ArrayList<FileModel>> tempDayHashMap = new HashMap<>(MediaLoader.getDayHashMap(mediaModelArrayList));

        if (tempDayHashMap.isEmpty()) {
            finish();
        } else {
            String day = DateUtils.convertDateFormat("dd/MM/yyyy - HH:mm:ss", "dd MMMM, yyyy", subMediaType);

            if (tempDayHashMap.get(day) == null) {
                finish();
            } else {
                ArrayList<FileModel> tempDayFileList = new ArrayList<>(Objects.requireNonNull(tempDayHashMap.get(day)));

                if (tempDayFileList.isEmpty()) {
                    finish();
                } else {
                    fileModelArrayList = new ArrayList<>(tempDayFileList);

                    pathList = new ArrayList<>();
                    for (int j = 0; j < tempDayFileList.size(); j++) {
                        pathList.add(tempDayFileList.get(j).getPath());
                    }

                    Log.d("--delete--", "fileModelArrayList: " + fileModelArrayList.size());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mediaPagerAdapter.swapList(fileModelArrayList); // swapping and updating pager adapter with tempFileList
                            bottomMediaIndicatorAdapter.swapList(fileModelArrayList); // swapping and updating media indicator adapter with tempFileList
                        }
                    });
                }
            }
        }
    }

    private void refreshAll(ArrayList<MediaModel> mediaModelArrayList) {
        Log.d("--delete--", "mediaType: " + mediaType);
        ArrayList<FileModel> tempFileList = new ArrayList<>(MediaLoader.getAllFileList(mediaModelArrayList));

        Collections.sort(tempFileList, new Comparator<FileModel>() {
            final DateFormat f = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH);

            @Override
            public int compare(FileModel lhs, FileModel rhs) {
                try {
                    return Objects.requireNonNull(f.parse(rhs.getDateModified())).compareTo(f.parse(lhs.getDateModified()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        if (pathList.size() == tempFileList.size()) {
            for (int i = 0; i < tempFileList.size(); i++) {
                if (!pathList.contains(tempFileList.get(i).getPath())) {
                    pathList = new ArrayList<>();
                    for (int j = 0; j < tempFileList.size(); j++) {
                        pathList.add(tempFileList.get(i).getPath());
                    }

                    fileModelArrayList = new ArrayList<>(tempFileList);

                    Log.d("--delete--", "fileModelArrayList: " + fileModelArrayList.size());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mediaPagerAdapter.swapList(fileModelArrayList); // swapping and updating pager adapter with tempFileList
                            bottomMediaIndicatorAdapter.swapList(fileModelArrayList); // swapping and updating media indicator adapter with tempFileList
                        }
                    });
                    break;
                }
            }
        } else {
            fileModelArrayList = new ArrayList<>(tempFileList);

            pathList = new ArrayList<>();
            for (int j = 0; j < tempFileList.size(); j++) {
                pathList.add(tempFileList.get(j).getPath());
            }

            Log.d("--delete--", "fileModelArrayList: " + fileModelArrayList.size());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mediaPagerAdapter.swapList(fileModelArrayList); // swapping and updating pager adapter with tempFileList
                    bottomMediaIndicatorAdapter.swapList(fileModelArrayList); // swapping and updating media indicator adapter with tempFileList
                }
            });
        }
    }

    private void clickListeners() {
        binding.rlShare.setOnClickListener(v -> {
            shareMediaFile();
        });

        binding.rlFavorite.setOnClickListener(v -> {
            new Thread(() -> {
                if (albumsViewModel.isFavoriteExist(fileModelArrayList.get(binding.pagerFullScreenMedia.getCurrentItem()).getPath())) {
                    AlbumDBModel.FavoriteDBModel albumDBModel = albumsViewModel.getFavoriteByPath(fileModelArrayList.get(binding.pagerFullScreenMedia.getCurrentItem()).getPath());
                    albumsViewModel.deleteFavorite(albumDBModel);

                    setFavoriteImage(binding.imgFavorite, R.drawable.ic_favorite_not_selected);
                } else {
                    FileModel fileModel = fileModelArrayList.get(binding.pagerFullScreenMedia.getCurrentItem());
                    AlbumDBModel.FavoriteDBModel albumDBModel = new AlbumDBModel.FavoriteDBModel(
                            fileModel.getId(),
                            fileModel.getPath(),
                            fileModel.getDateModified(),
                            fileModel.getFileFormat(),
                            fileModel.getDuration(),
                            fileModel.getSize());
                    albumsViewModel.insertFavorite(albumDBModel);

                    setFavoriteImage(binding.imgFavorite, R.drawable.ic_favorite_selected);
                }
            }).start();
        });

        binding.rlEdit.setOnClickListener(v -> {
            Intent intent = new Intent(FullScreenMediaActivity.this, CropActivity.class);

            intent.putExtra("fileModel", fileModelArrayList.get(binding.pagerFullScreenMedia.getCurrentItem()));

            showInterstitialAd(FullScreenMediaActivity.this, intent, null);
        });

        binding.rlDelete.setOnClickListener(view -> {
            deleteMediaFile();
        });

        binding.rlMore.setOnClickListener(this::showPopup);
    }

    Bitmap bitmapWallpaper;

    private void showPopup(View view) {
        int position = binding.pagerFullScreenMedia.getCurrentItem();
        FileModel fileModel = fileModelArrayList.get(position);

        File file = new File(fileModel.getPath());

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") final View popupView = layoutInflater.inflate(R.layout.popup_full_screen, null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                (int) getResources().getDimension(com.intuit.sdp.R.dimen._125sdp),
                (int) getResources().getDimension(com.intuit.sdp.R.dimen._75sdp), true);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(() -> {
        });
        popupWindow.showAsDropDown(view);

        if (fileModel.getFileFormat().startsWith("video")) {
            popupView.findViewById(R.id.txt_set_wallpaper).setAlpha(0.25f);
            popupView.findViewById(R.id.txt_set_wallpaper).setEnabled(false);
        } else {
            popupView.findViewById(R.id.txt_set_wallpaper).setAlpha(1f);
            popupView.findViewById(R.id.txt_set_wallpaper).setEnabled(true);
        }

        popupView.findViewById(R.id.txt_set_wallpaper).setOnClickListener(view1 -> {

            Dialog dialogScreenChooser = new Dialog(FullScreenMediaActivity.this, R.style.CustomDialog);
            dialogScreenChooser.setCancelable(true);
            dialogScreenChooser.setCanceledOnTouchOutside(true);
            dialogScreenChooser.setContentView(R.layout.dialog_wallpaper);
            dialogScreenChooser.getWindow().setGravity(Gravity.BOTTOM);
            dialogScreenChooser.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogScreenChooser.show();

            LinearLayout ll_home = dialogScreenChooser.findViewById(R.id.ll_home);
            LinearLayout ll_lock = dialogScreenChooser.findViewById(R.id.ll_lock);
            LinearLayout ll_both = dialogScreenChooser.findViewById(R.id.ll_both);
            TextView txt_cancel = dialogScreenChooser.findViewById(R.id.txt_cancel);

            Dialog dialogWallpaper = new Dialog(FullScreenMediaActivity.this, R.style.CustomDialog);
            dialogWallpaper.setCancelable(true);
            dialogWallpaper.setCanceledOnTouchOutside(true);
            dialogWallpaper.setContentView(R.layout.dialog_set_wallpaper);
            dialogWallpaper.getWindow().setGravity(Gravity.BOTTOM);
            dialogWallpaper.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            TextView txt_title = dialogWallpaper.findViewById(R.id.txt_title);
            TextView txt_ok = dialogWallpaper.findViewById(R.id.txt_ok);
            TextView txt_no = dialogWallpaper.findViewById(R.id.txt_no);

            new Thread(() -> {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bitmapWallpaper = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
            }).start();

            ll_both.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialogScreenChooser.dismiss();
                    dialogWallpaper.show();

                    txt_title.setText("Do you want to set this image as your Home and Lock Screen Wallpaper?");

                    txt_ok.setOnClickListener(v112 -> {
                        dialogWallpaper.dismiss();

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            try {
                                WallpaperUtils.setWallpaperOnBoth(FullScreenMediaActivity.this, bitmapWallpaper);

                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }, 500);
                    });
                }
            });

            ll_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();

                    dialogScreenChooser.dismiss();
                    dialogWallpaper.show();

                    txt_title.setText("Do you want to set this image as your Home Screen Wallpaper?");

                    txt_ok.setOnClickListener(v1131 -> {
                        dialogWallpaper.dismiss();
                        popupWindow.dismiss();

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            try {
                                WallpaperUtils.setHomeScreenWallpaper(FullScreenMediaActivity.this, bitmapWallpaper);

                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }, 500);
                    });
                }
            });

            ll_lock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();

                    dialogScreenChooser.dismiss();
                    dialogWallpaper.show();

                    txt_title.setText("Do you want to set this image as your Lock Screen Wallpaper?");

                    txt_ok.setOnClickListener(v1141 -> {
                        dialogWallpaper.dismiss();

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                WallpaperUtils.setLockScreenWallpaper(FullScreenMediaActivity.this, bitmapWallpaper);
                            }
                        }, 500);
                    });
                }
            });

            txt_no.setOnClickListener(v115 -> dialogWallpaper.dismiss());

            txt_cancel.setOnClickListener(v11 -> dialogScreenChooser.dismiss());
        });

        popupView.findViewById(R.id.txt_details).setOnClickListener(v12 -> {
            popupWindow.dismiss();
            String nameWithExtension = file.getName();

            /*1*/
            String name = nameWithExtension.substring(0, nameWithExtension.lastIndexOf("."));
            /*2*/
            String path = file.getAbsolutePath();
            /*3*/
            String parentFileName = Objects.requireNonNull(file.getParentFile()).getName();
            /*4*/
            String mimeType = fileModel.getFileFormat();
            /*5*/
            String duration = MediaUtils.getDuration(path, mimeType);
            /*6*/
            String size = MediaUtils.getStringSizeLengthFile(Long.parseLong(fileModel.getSize()));
            /*7*/
            String resolution = MediaUtils.getResolution(path, MediaUtils.getMediaType(mimeType));
            /*8*/
            String dateModified = fileModel.getDateModified();

            AddressDetailModel addressDetailModel = null;

            try {
                ExifInterface exifInterface = new ExifInterface(path);

                // getting lat long values from exif
                String LatLong = LocationUtils.getLatLongFromEXIF(exifInterface);

                if (!LatLong.isEmpty()) {
                    String[] separated = LatLong.split(",");

                    double latitude = Double.parseDouble(separated[0]);
                    double longitude = Double.parseDouble(separated[1]);

                    addressDetailModel = LocationUtils.getAddress(FullScreenMediaActivity.this, latitude, longitude);
                }
            } catch (IOException e) {
                Log.d("--exif--", "onClick: " + e.getMessage());
                e.printStackTrace();
            }

            Log.d("--details---", "details: \n" +
                    "name: " + name + "\n" +
                    "path: " + path + "\n" +
                    "mimeType: " + mimeType + "\n" +
                    "dateModified: " + dateModified + "\n" +
                    "size: " + size + "\n" +
                    "resolution: " + resolution + "\n" +
                    "parentFileName: " + parentFileName + "\n" +
                    "duration: " + duration);

            Dialog dialog = new Dialog(FullScreenMediaActivity.this, R.style.CustomDialog);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dialog_media_details);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();

            dialog.getWindow().getDecorView().setOnTouchListener((v121, event) -> manageMotionEvents(event, dialog));

            TextView txt_name = dialog.findViewById(R.id.txt_name);
            TextView txt_path = dialog.findViewById(R.id.txt_path);
            TextView txt_folder = dialog.findViewById(R.id.txt_folder);
            TextView txt_mime_type = dialog.findViewById(R.id.txt_mime_type);
            TextView txt_duration = dialog.findViewById(R.id.txt_duration);
            LinearLayout ll_duration = dialog.findViewById(R.id.ll_duration);
            TextView txt_size = dialog.findViewById(R.id.txt_size);
            TextView txt_resolution = dialog.findViewById(R.id.txt_resolution);
            TextView txt_date_modified = dialog.findViewById(R.id.txt_date_modified);
            TextView txt_address = dialog.findViewById(R.id.txt_address);
            LinearLayout ll_address = dialog.findViewById(R.id.ll_address);

            txt_name.setText(name);
            txt_path.setText(path);
            txt_folder.setText(parentFileName);
            txt_mime_type.setText(mimeType);
            if (mimeType.startsWith("video")) {
                ll_duration.setVisibility(View.VISIBLE);
                txt_duration.setText(duration);
            }
            txt_size.setText(size);
            txt_resolution.setText(resolution);
            txt_date_modified.setText(dateModified);

            if (addressDetailModel != null) {
                if (!addressDetailModel.getAddressLine().isEmpty()) {

                    txt_address.setText(addressDetailModel.getAddressLine());

                    ll_address.setVisibility(View.VISIBLE);

                    AddressDetailModel finalAddressDetailModel = addressDetailModel;
                    txt_address.setOnClickListener(
                            view1 -> {
                                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + finalAddressDetailModel.getAddressLine());
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            });
                } else {
                    ll_address.setVisibility(View.GONE);
                }
            } else {
                ll_address.setVisibility(View.GONE);
            }
        });
    }

    private boolean manageMotionEvents(MotionEvent event, Dialog dialog) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dialog.dismiss();
                // touch down code
                break;

            case MotionEvent.ACTION_MOVE:
                // touch move code
                break;

            case MotionEvent.ACTION_UP:
                // touch up code
                break;
        }
        return true;
    }

    private void deleteMediaFile() {
        int position = binding.pagerFullScreenMedia.getCurrentItem();

        FileModel fileModel = fileModelArrayList.get(position);

        File file = new File(fileModel.getPath());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                DeleteMediaManager.deleteFilesAbove10(Collections.singletonList(file), 2022, FullScreenMediaActivity.this, new Intent());
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Dialog dialog = new Dialog(FullScreenMediaActivity.this, R.style.CustomDialog);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dialog_delete);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();

            TextView txt_title = dialog.findViewById(R.id.txt_title);
            TextView txt_cancel = dialog.findViewById(R.id.txt_create);
            TextView txt_ok = dialog.findViewById(R.id.txt_ok);

            String fileNameWithExtension = file.getName();

            txt_title.setText("Do you want to delete " + fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf(".")) + "?");
            txt_cancel.setOnClickListener(v -> dialog.dismiss());

            txt_ok.setOnClickListener(v -> {
                dialog.dismiss();

                if (file.exists()) {
                    if (file.delete()) {
                        MediaScannerConnection.scanFile(FullScreenMediaActivity.this,
                                new String[]{Environment.getExternalStorageDirectory().toString()}, null, (path, uri) -> {

                                });
                        removeFromDataBase();
                    } else {
                        Toast.makeText(this, "Could not delete this file.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();
        }
    }

    private void removeFromDataBase() {

        new Thread(() -> {
            FileModel fileModel = fileModelArrayList.get(binding.pagerFullScreenMedia.getCurrentItem());

            if (allMediaViewModel.isMediaExist(fileModel.getPath())) {
                MediaModel mediaModel = allMediaViewModel.getMediaModelByPath(fileModel.getPath());
                allMediaViewModel.deleteMedia(mediaModel);
            }
            if (albumsViewModel.isFavoriteExist(fileModel.getPath())) {
                AlbumDBModel.FavoriteDBModel favoriteDBModel = albumsViewModel.getFavoriteByPath(fileModel.getPath());
                albumsViewModel.deleteFavorite(favoriteDBModel);
            }
            if (albumsViewModel.isFileModelWithAddressExist(fileModel.getPath())) {
                AlbumDBModel.AddressDBModel addressDBModel = albumsViewModel.getAddressByPath(fileModel.getPath());
                albumsViewModel.deleteAddress(addressDBModel);
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 2022) {
                removeFromDataBase();
            }
        }
    }

    private void shareMediaFile() {
        int position = binding.pagerFullScreenMedia.getCurrentItem();

        FileModel fileModel = fileModelArrayList.get(position);

        File file = new File(fileModel.getPath());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            final Uri contentUri = Uri.fromFile(file);
            scanIntent.setData(contentUri);
            sendBroadcast(scanIntent);
        } else {
            final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
            sendBroadcast(intent);
        }

        String shareTitle;
        String mimeType;

        if (fileModel.getFileFormat().startsWith("image")) {
            shareTitle = "Share Image";
            mimeType = "image/*";
        } else {
            shareTitle = "Share Video";
            mimeType = "video/*";
        }

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getAbsolutePath()));
        shareIntent.setType(mimeType);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        // Launch sharing dialog for image
        startActivity(Intent.createChooser(shareIntent, shareTitle));
    }

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private GestureDetector gestureDetector;

    private class SwipeDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (e1 != null && e2 != null) {
                // Check movement along the Y-axis. If it exceeds SWIPE_MAX_OFF_PATH,
                // then dismiss the swipe.
                if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH)
                    return false;

                // Swipe from left to right.
                // The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE)
                // and a certain velocity (SWIPE_THRESHOLD_VELOCITY).
                if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    onBackPressed();
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TouchEvent dispatcher.
        if (gestureDetector != null) {
            if (gestureDetector.onTouchEvent(ev))
                // If the gestureDetector handles the event, a swipe has been
                // executed and no more needs to be done.
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private void setScrollListeners() {

        binding.pagerFullScreenMedia.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if ((positionOffsetPixels == 0) && (positionOffset == 0.0)) {
                    binding.rvMediaIndicator.setOnItemSelectedListener(coverFlowLayoutManager);
                } else {
                    binding.rvMediaIndicator.setOnItemSelectedListener(null);
                }

                Log.d("--position--", "positionOffset: " + positionOffset + "------------------positionOffsetPixels: " + positionOffsetPixels);

                String filename = new File(fileModelArrayList.get(position).getPath()).getName();
                String result = filename.substring(0, filename.lastIndexOf("."));
                binding.txtMediaTitle.setText(result);
                binding.txtMediaDate.setText(DateUtils.convertDateFormat("dd/MM/yyyy - HH:mm:ss", "dd MMMM, yyyy", fileModelArrayList.get(position).getDateModified()));

                editVisibility(position);

                new Thread(() -> {
                    if (albumsViewModel.isFavoriteExist(fileModelArrayList.get(position).getPath())) {
                        setFavoriteImage(binding.imgFavorite, R.drawable.ic_favorite_selected);
                    } else {
                        setFavoriteImage(binding.imgFavorite, R.drawable.ic_favorite_not_selected);
                    }
                }).start();

                Objects.requireNonNull((CoverFlowLayoutManger) binding.rvMediaIndicator.getLayoutManager()).smoothScrollToPosition(binding.rvMediaIndicator, null, position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("--position--", "pager: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.rvMediaIndicator.setOnItemSelectedListener(coverFlowLayoutManager);
    }

    CoverFlowLayoutManger.OnSelected coverFlowLayoutManager = new CoverFlowLayoutManger.OnSelected() {
        @Override
        public void onItemSelected(int position) {
            Log.d("--position--", "indicator: " + position);
            binding.pagerFullScreenMedia.setCurrentItem(position);
        }
    };

    private void setFavoriteImage(ImageView imgFavorite, int favorite_drawable) {
        runOnUiThread(() -> imgFavorite.setImageDrawable(ContextCompat.getDrawable(FullScreenMediaActivity.this, favorite_drawable)));
    }

    private void editVisibility(int position) {
        String fileType = MediaUtils.getMediaType(fileModelArrayList.get(position).getFileFormat());
        if (fileType.equals("image")) {
            binding.rlEdit.setVisibility(View.VISIBLE);
        } else {
            binding.rlEdit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPagerClick(int position) {
        Window window = getWindow();

        Log.d("--full_screen--", "onPagerClick: " + System.currentTimeMillis());

        if (binding.rlTitle.getVisibility() == View.VISIBLE) {
            binding.rlTitle.setVisibility(View.GONE);
            binding.constraintBottom.setVisibility(View.GONE);
            binding.constraintMain.setBackgroundColor(Color.BLACK);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);
                window.setNavigationBarColor(Color.BLACK);
            }
        } else {
            binding.rlTitle.setVisibility(View.VISIBLE);
            binding.constraintBottom.setVisibility(View.VISIBLE);
            binding.constraintMain.setBackgroundColor(ColorUtils.getAttributeColor(FullScreenMediaActivity.this, R.attr.main_bg));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ColorUtils.getAttributeColor(FullScreenMediaActivity.this, R.attr.main_bg));
                window.setNavigationBarColor(ColorUtils.getAttributeColor(FullScreenMediaActivity.this, R.attr.main_bg));
            }
        }
    }

    @Override
    public void onVideoClick(int position) {
        FileModel fileModel = fileModelArrayList.get(position);

        String mediaFormat = fileModel.getFileFormat();

        if (mediaFormat.startsWith("video")) {
            Intent intent = new Intent(FullScreenMediaActivity.this, CustomPlayerActivity.class);
            intent.putExtra("position", position);
            Constant.INTENT_FILE_MODEL_ARRAY_LIST = new ArrayList<>(fileModelArrayList);
//            startActivity(intent);
            showInterstitialAd(FullScreenMediaActivity.this, intent, null);
        }
    }

    @Override
    public void onBottomIndicatorClicked(int position) {

    }
}
