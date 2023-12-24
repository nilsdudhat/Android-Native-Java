package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.AppViewModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.R;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumDBModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumsViewModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.AllMediaViewModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.foryou.ForYouDBModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.foryou.ForYouViewModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.mediastore.MediaCursor;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.mediastore.MediaLoader;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.AddressDetailModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.ForYouModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DateUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.LocationUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.PreferenceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Danger do not edit this class without understanding it properly
 */
public class BaseActivity extends PermissionManagerActivity {

    public AppViewModel appViewModel;
    public AllMediaViewModel allMediaViewModel;
    public ForYouViewModel forYouViewModel;
    public AlbumsViewModel albumsViewModel;

    Observer<ArrayList<MediaModel>> mediaDataObserver;
    Observer<ForYouModel> forYouListObserver;
    Observer<List<AlbumDBModel.FavoriteDBModel>> favoriteDBModelObserver;

    Dialog progressDialog;

    ArrayList<MediaModel> mediaModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new Dialog(BaseActivity.this, R.style.CustomDialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // initiating AppVieModel for Data Management
        appViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(AppViewModel.class);

        // initiating AllMediaViewModel for Data Management
        allMediaViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(AllMediaViewModel.class);

        // initiating ForYouViewModel for Data Management
        forYouViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(ForYouViewModel.class);

        // initiating AlbumsViewModel for Data Management
        albumsViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(AlbumsViewModel.class);

        initObservers();
    }

    public void loadAddressDataForFirstTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<FileModel> fileModelArrayList = MediaLoader.getAllFileModelList(BaseActivity.this);

                for (int i = 0; i < fileModelArrayList.size(); i++) {
                    Log.d("--load_address---", "run: " + "i: " + i);
                    FileModel fileModel = fileModelArrayList.get(i);

                    if (!albumsViewModel.isFileModelWithAddressExist(fileModel.getPath())) {
                        AddressDetailModel addressDetailModel = LocationUtils.getAddressDetailModel(BaseActivity.this, fileModel.getPath());

                        if (addressDetailModel != null) {
                            Log.d("--address_update--", "path: " + fileModel.getPath());

                            if (!addressDetailModel.getAddressLine().isEmpty() ||
                                    !addressDetailModel.getAreaName().isEmpty() ||
                                    !addressDetailModel.getCityName().isEmpty() ||
                                    !addressDetailModel.getCountryName().isEmpty() ||
                                    !addressDetailModel.getPostalCode().isEmpty() ||
                                    !addressDetailModel.getStateName().isEmpty()) {

                                String address = LocationUtils.getAddressForPlaces(addressDetailModel);
                                String strFileModel = new Gson().toJson(fileModel);

                                AlbumDBModel.AddressDBModel addressDBModel = new AlbumDBModel.AddressDBModel(address, fileModel.getPath(), strFileModel, new Gson().toJson(addressDetailModel));
                                albumsViewModel.insertAddress(addressDBModel);
                            }
                        }
                    }
                }

                PreferenceUtils.setBoolean(BaseActivity.this, "isAddressLoaded", true);
            }
        }).start();
    }

    private void initObservers() {

        /* observer callback for forYouList */
        forYouListObserver = new Observer<ForYouModel>() {
            @Override
            public void onChanged(ForYouModel forYouModel) {
                /* assigning value to method for child activities callback */
                Log.d("--date_modified--", "BaseActivity: " + "-------------- Format: " + forYouModel.getFormat() + "-------------------- Title: " + forYouModel.getTitle());
                refreshForYouData(forYouModel);
            }
        };

        /* get yesterday for you list */
        appViewModel.getForYouData().observe(BaseActivity.this, forYouListObserver);

        /* observer callback for favoritesList */
        favoriteDBModelObserver = new Observer<List<AlbumDBModel.FavoriteDBModel>>() {
            @Override
            public void onChanged(List<AlbumDBModel.FavoriteDBModel> favoriteDBModels) {
                Log.d("--fav_list--", "BaseActivity: " + favoriteDBModels.size());
                refreshFavoritesData(favoriteDBModels);
            }
        };

        /* get favorites list */
        albumsViewModel.getAllFavorites().observe(BaseActivity.this, favoriteDBModelObserver);
    }

    protected void refreshFavoritesData(List<AlbumDBModel.FavoriteDBModel> favoriteDBModels) {

    }

    public void refreshForYouData(ForYouModel forYouModel) {
        /* return callback of Yesterday For You Data for child activities */
        Log.d("--load--", "loadForYouData: " + forYouModel.getFileModelArrayList().size());
    }

    public void showProgressDialog() {
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    public void loadDataInLoop() {
        Log.d("--loop_data--", "loadDataInLoop: ");

        mediaDataObserver = new Observer<ArrayList<MediaModel>>() {
            @Override
            public void onChanged(ArrayList<MediaModel> mediaModels) {
                Log.d("--loop_data--", "loadDataInLoop: " + "mediaDataObserver mediaModels:" + mediaModels.size());

                mediaModelArrayList = new ArrayList<>(mediaModels);

                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loadDataInLoop(thread, BaseActivity.this, mediaModelArrayList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();

                refreshData(mediaModels);
            }
        };

        appViewModel.loadMediaFilesFromDatabase(BaseActivity.this);

        appViewModel.getMediaData().observe(BaseActivity.this, mediaDataObserver);
    }

    Thread thread;

    protected void refreshData(ArrayList<MediaModel> mediaModels) {

    }

    private void loadDataInLoop(Thread thread, Activity activity, ArrayList<MediaModel> mediaModels) throws Exception {
        if (thread.getId() == this.thread.getId()) {

            long time = System.currentTimeMillis();

            Log.d("--loop_data--", "System.currentTimeMillis(): " + time + "------------------" + "thread id: " + this.thread.getId());

            loadMediaData(activity);

            removeNonExistingMediaFiles(mediaModels);

            if ((System.currentTimeMillis() - time) <= 1000) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            loadDataInLoop(thread, activity, mediaModels);
        } else {
            Log.d("--loop_data--", "startLoopingDataWithoutHandler: ");
            throw new Exception();
        }
    }

    private void removeNonExistingMediaFiles(List<MediaModel> mediaModels) {
        ArrayList<MediaModel> mediaModelArrayList = new ArrayList<>(mediaModels);

        for (int i = 0; i < mediaModelArrayList.size(); i++) {
            MediaModel mediaModel = mediaModelArrayList.get(i);
            String path = mediaModel.getPath();

            Log.d("--remove--", "removeNonExistingMediaFiles: " + path);

            if (!new File(path).exists()) {
                Log.d("--database--", "delete: " + path);
                allMediaViewModel.deleteMedia(mediaModel);
            }
        }
    }

    private void loadMediaData(Activity activity) {

        ArrayList<FileModel> fileModelArrayList = new ArrayList<>(MediaCursor.getAllMedia(activity));

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!fileModelArrayList.isEmpty()) {
                    for (int i = 0; i < fileModelArrayList.size(); i++) {
                        FileModel fileModel = fileModelArrayList.get(i);
                        String path = fileModel.getPath();

                        if (!allMediaViewModel.isMediaExist(path)) {
                            Log.d("--database--", "insert: " + path);

                            MediaModel mediaModel = new MediaModel(
                                    fileModel.getId(),
                                    fileModel.getPath(),
                                    fileModel.getDateModified(),
                                    fileModel.getFileFormat(),
                                    fileModel.getDuration(),
                                    fileModel.getSize());
                            allMediaViewModel.insertMedia(mediaModel);
                        } else {
                            Log.d("--store--", "false path: " + path);
                        }
                    }
                }
            }
        }).start();
    }

    public void loadForYouData(Activity activity, ArrayList<FileModel> fileModelArrayList) {
        /* return callback of Yesterday For You Data for child activities */
        Log.d("--load--", "loadForYouData: " + fileModelArrayList.size());

        appViewModel.loadForYouData(activity, fileModelArrayList);

        ArrayList<String> recent7DaysList = new ArrayList<>(DateUtils.getRecent7DaysList());
        scrapOtherDays(recent7DaysList);

        ArrayList<String> recent12MonthsList = new ArrayList<>(DateUtils.getRecent12MonthsList());
        scrapOtherMonths(recent12MonthsList);
    }

    private void scrapOtherMonths(ArrayList<String> recent12MonthsList) {
        if (!recent12MonthsList.isEmpty()) {
            Log.d("--scrap_other_months--", "scrapOtherMonths: " + recent12MonthsList.size());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    forYouViewModel.getAllMonths().observe(BaseActivity.this, new Observer<List<ForYouDBModel.MonthModel>>() {
                        @Override
                        public void onChanged(List<ForYouDBModel.MonthModel> monthForYouDBModels) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("--scrap_other_months--", "onChanged: " + monthForYouDBModels.size());

                                    List<ForYouDBModel.MonthModel> monthsForYouList = new ArrayList<>();

                                    for (int i = 0; i < monthForYouDBModels.size(); i++) {
                                        if (DateUtils.isMonthFormat(monthForYouDBModels.get(i).getTitle())) {
                                            Log.d("--scrap_other_months--", "title: " + monthForYouDBModels.get(i).getTitle());
                                            monthsForYouList.add(monthForYouDBModels.get(i));
                                        }
                                    }

                                    Log.d("--scrap_other_months--", "monthsForYouList: " + monthsForYouList.size());

                                    for (int i = 0; i < monthsForYouList.size(); i++) {
                                        if (!recent12MonthsList.contains(monthsForYouList.get(i).getTitle())) {
                                            Log.d("--scrap_other_months--", "month to delete: " + monthsForYouList.get(i).getTitle());
                                            ForYouDBModel.MonthModel forYouDBModel = forYouViewModel.getMonthByMonthTitle(monthsForYouList.get(i).getTitle());
                                            int finalI = i;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    forYouViewModel.deleteMonth(forYouDBModel);

                                                    Log.d("--scrap_other_months--", "month to delete done: " + monthsForYouList.get(finalI).getTitle());
                                                }
                                            });
                                        }
                                    }
                                }
                            }).start();
                        }
                    });
                }
            });
        }
    }

    private void scrapOtherDays(ArrayList<String> recent7DaysList) {
        if (!recent7DaysList.isEmpty()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    forYouViewModel.getAllDays().observe((LifecycleOwner) BaseActivity.this, new Observer<List<ForYouDBModel.DayModel>>() {
                        @Override
                        public void onChanged(List<ForYouDBModel.DayModel> dayForYouDBModels) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("--for_you_list--", "onChanged: " + dayForYouDBModels.size());

                                    List<ForYouDBModel.DayModel> dayForYouList = new ArrayList<>();

                                    for (int i = 0; i < dayForYouDBModels.size(); i++) {
                                        if (DateUtils.isDateFormat(dayForYouDBModels.get(i).getTitle())) {
                                            dayForYouList.add(dayForYouDBModels.get(i));
                                        }
                                    }

                                    for (int i = 0; i < dayForYouList.size(); i++) {
                                        if (!recent7DaysList.contains(dayForYouDBModels.get(i).getTitle())) {
                                            Log.d("--day_date--", "onChanged: " + dayForYouDBModels.get(i).getTitle());
                                            ForYouDBModel.DayModel forYouDBModel = forYouViewModel.getDayByDate(dayForYouDBModels.get(i).getTitle());
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    forYouViewModel.deleteDay(forYouDBModel);
                                                }
                                            });
                                        }
                                    }
                                }
                            }).start();
                        }
                    });
                }
            });
        }
    }

    String addressLocation = "";

    public void loadAddressData() {

        allMediaViewModel.getAllMediaData().observe(BaseActivity.this, new Observer<List<MediaModel>>() {
            @Override
            public void onChanged(List<MediaModel> mediaModelList) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaModelList.isEmpty()) {
                            albumsViewModel.deleteAllAddress();
                        } else {
                            ArrayList<MediaModel> mediaModelArrayList = new ArrayList<>(mediaModelList);
                            ArrayList<FileModel> tempFileModelList = new ArrayList<>();

                            for (int i = 0; i < mediaModelArrayList.size(); i++) {
                                MediaModel mediaModel = mediaModelArrayList.get(i);

                                FileModel fileModel = new FileModel(mediaModel.getFileId(), mediaModel.getPath(), mediaModel.getDateModified(), mediaModel.getFileFormat(), mediaModel.getDuration(), mediaModel.getSize());
                                tempFileModelList.add(fileModel);
                            }

                            for (int i = 0; i < tempFileModelList.size(); i++) {
                                String path = tempFileModelList.get(i).getPath();

                                Log.d("--address_load--", "i: " + i + " ====== tempFileModelList:" + tempFileModelList.size());

                                if (!albumsViewModel.isFileModelWithAddressExist(path)) {
                                    AddressDetailModel addressDetailModel = LocationUtils.getAddressDetailModel(BaseActivity.this, tempFileModelList.get(i).getPath());

                                    if (addressDetailModel != null) {
                                        Log.d("--address_update--", "path: " + path);

                                        if (!addressDetailModel.getAddressLine().isEmpty() ||
                                                !addressDetailModel.getAreaName().isEmpty() ||
                                                !addressDetailModel.getCityName().isEmpty() ||
                                                !addressDetailModel.getCountryName().isEmpty() ||
                                                !addressDetailModel.getPostalCode().isEmpty() ||
                                                !addressDetailModel.getStateName().isEmpty()) {

                                            String address = LocationUtils.getAddressForPlaces(addressDetailModel);
                                            if (addressLocation.isEmpty()) {
                                                addressLocation = address;

                                                String fileModel = new Gson().toJson(tempFileModelList.get(i));

                                                AlbumDBModel.AddressDBModel addressDBModel = new AlbumDBModel.AddressDBModel(address, path, fileModel, new Gson().toJson(addressDetailModel));
                                                albumsViewModel.insertAddress(addressDBModel);
                                            } else {
                                                if (address.equals(addressLocation)) {
                                                    String fileModel = new Gson().toJson(tempFileModelList.get(i));

                                                    AlbumDBModel.AddressDBModel addressDBModel = new AlbumDBModel.AddressDBModel(address, path, fileModel, new Gson().toJson(addressDetailModel));
                                                    albumsViewModel.insertAddress(addressDBModel);
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {

                                    albumsViewModel.getAllAddressData().observe(BaseActivity.this, new Observer<List<AlbumDBModel.AddressDBModel>>() {
                                        @Override
                                        public void onChanged(List<AlbumDBModel.AddressDBModel> addressDBModels) {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    for (int i = 0; i < addressDBModels.size(); i++) {
                                                        AlbumDBModel.AddressDBModel addressDBModel = addressDBModels.get(i);

                                                        File file = new File(addressDBModel.getPath());

                                                        if (!file.exists()) {
                                                            albumsViewModel.deleteAddress(addressDBModel);
                                                        }
                                                    }
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
        });
    }
}