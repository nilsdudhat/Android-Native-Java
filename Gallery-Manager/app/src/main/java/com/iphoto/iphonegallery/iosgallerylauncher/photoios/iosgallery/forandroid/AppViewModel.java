package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumDBModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumsViewModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.AllMediaViewModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.foryou.ForYouViewModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.mediastore.MediaLoader;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.AddressModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.ForYouModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.BaseActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DateUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.MathUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AppViewModel extends ViewModel {

    AllMediaViewModel allMediaViewModel;

    MutableLiveData<ArrayList<MediaModel>> mediaData;
    MutableLiveData<ForYouModel> forYouData;

    ArrayList<AddressModel> addressModelArrayList = new ArrayList<>();

    ForYouViewModel forYouViewModel;
    AlbumsViewModel albumsViewModel;

    public AppViewModel() {

    }

    public void loadMediaFilesFromDatabase(Activity activity) {
        mediaData = new MutableLiveData<>();

        allMediaViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(AllMediaViewModel.class);
        albumsViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(AlbumsViewModel.class);

        allMediaViewModel.getAllMediaData().observe((BaseActivity) activity, new Observer<List<MediaModel>>() {
            @Override
            public void onChanged(List<MediaModel> mediaModels) {
                mediaData.setValue(new ArrayList<>(mediaModels));
            }
        });
    }

    public MutableLiveData<ArrayList<MediaModel>> getMediaData() {
        return mediaData;
    }

    public void loadForYouData(Activity activity, ArrayList<FileModel> fileModelArrayList) {
        if (forYouData == null) {
            forYouData = new MutableLiveData<>();
        }
        forYouViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(ForYouViewModel.class);

        new Thread(new Runnable() {
            @Override
            public void run() {

                // for recent days
                createDaysForYou(activity);

                // for recent week
                createRecentWeekForYou(activity);

                // for recent 12 months for you
                createRecent12MonthsForYou(activity);

                // for recent year for you
                createRecentYearForYou(activity);

                // for years
                createYearsForYou(activity);

                // for places
                createPlacesForYou(activity);
            }

            private void createPlacesForYou(Activity activity) {
                Log.d("--create_place--", "createPlacesForYou: ");

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        albumsViewModel.getAllAddressData().observe((LifecycleOwner) activity, new Observer<List<AlbumDBModel.AddressDBModel>>() {
                            @Override
                            public void onChanged(List<AlbumDBModel.AddressDBModel> addressDBModels) {

                                Log.d("--create_place--", "onChanged: " + addressDBModels.size());

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.d("--create_place--", "addressDBModels: " + addressDBModels.size());

                                        List<AddressModel> tempAddressModelList = new ArrayList<>();
                                        List<String> tempAddressNameList = new ArrayList<>();

                                        for (int i = 0; i < addressDBModels.size(); i++) {
                                            AlbumDBModel.AddressDBModel addressDBModel = addressDBModels.get(i);

                                            String address = addressDBModel.getAddress();

                                            Log.d("--create_place--", "address: " + address);

                                            if (tempAddressNameList.isEmpty()) {
                                                Log.d("--create_place--", "tempAddressNameList: " + tempAddressNameList.size());
                                                tempAddressNameList.add(address); // to maintain duplicates in final array

                                                ArrayList<FileModel> fileModels = new ArrayList<>();
                                                FileModel fileModel = new Gson().fromJson(addressDBModel.getFileModel(), FileModel.class);
                                                fileModels.add(fileModel);

                                                AddressModel addressModel = new AddressModel();
                                                addressModel.setAddress(address); // address name
                                                addressModel.setFileModelArrayList(fileModels); // file model list

                                                tempAddressModelList.add(addressModel);
                                            } else {
                                                if (tempAddressNameList.contains(address)) { // if address already exists
                                                    Log.d("--create_place--", "contains: " + "address already exists");
                                                    int index = tempAddressNameList.indexOf(address);
                                                    ArrayList<FileModel> fileModels = tempAddressModelList.get(index).getFileModelArrayList();

                                                    FileModel fileModel = new Gson().fromJson(addressDBModel.getFileModel(), FileModel.class);

                                                    fileModels.add(fileModel);
                                                } else { // if address not exists
                                                    Log.d("--create_place--", "contains: " + "address not exists");

                                                    tempAddressNameList.add(address); // to maintain duplicates in final array

                                                    ArrayList<FileModel> fileModels = new ArrayList<>();
                                                    FileModel fileModel = new Gson().fromJson(addressDBModel.getFileModel(), FileModel.class);
                                                    fileModels.add(fileModel);

                                                    AddressModel addressModel = new AddressModel();
                                                    addressModel.setAddress(address); // address name
                                                    addressModel.setFileModelArrayList(fileModels); // file model list

                                                    tempAddressModelList.add(addressModel);
                                                }
                                            }
                                        }

                                        Collections.sort(tempAddressModelList, new Comparator<AddressModel>() {
                                            @Override
                                            public int compare(AddressModel o1, AddressModel o2) {
                                                Integer size1 = o1.getFileModelArrayList().size();
                                                Integer size2 = o2.getFileModelArrayList().size();

                                                return size2.compareTo(size1);
                                            }
                                        });

                                        addressModelArrayList = new ArrayList<>(tempAddressModelList);

                                        Log.d("--create_place--", "addressModelArrayList: " + addressModelArrayList.size());

                                        for (int i = 0; i < addressModelArrayList.size(); i++) {
                                            createAddressForYou(activity, addressModelArrayList, i);
                                        }
                                    }

                                    private void createAddressForYou(Activity activity, ArrayList<AddressModel> addressModelArrayList, int position) {
                                        if (!forYouViewModel.isAddressExist(addressModelArrayList.get(position).getAddress())) {

                                            AddressModel addressModel = addressModelArrayList.get(position);

                                            String address = addressModel.getAddress();

                                            ArrayList<FileModel> addressFileModels = new ArrayList<>(MediaLoader.getAddressForYouList(activity, addressModel.getFileModelArrayList()));

                                            ArrayList<FileModel> addressForYouList = new ArrayList<>(MediaLoader.getForYouRandomImageList(addressFileModels));

                                            Log.d("--create_place--", "createAddressForYou: " + addressForYouList.size());

                                            if (!addressForYouList.isEmpty()) {
                                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ForYouModel forYouModel = new ForYouModel("address", address, addressForYouList);
                                                        Log.d("--create_place--", "addressForYouList ready for update: ");
                                                        forYouData.setValue(forYouModel);
                                                    }
                                                }, 5000);
                                            }
                                        }
                                    }
                                }).start();
                            }
                        });
                    }
                });
            }

            private void createYearsForYou(Activity activity) {
                ArrayList<String> yearList = new ArrayList<>(MediaLoader.getYearsForYouList(fileModelArrayList));

                Log.d("--recent_list--", "createYearsForYou: " + yearList.size());
                for (int i = 0; i < yearList.size(); i++) {
                    String strYear = yearList.get(i);
                    createYearForYou(activity, strYear);
                }
                Log.d("--year--", "createYearsForYou: " + yearList.size());
            }

            private void createRecent12MonthsForYou(Activity activity) {
                ArrayList<String> recent12MonthsList = new ArrayList<>(DateUtils.getRecent12MonthsList());
                Log.d("--recent_list--", "createRecent12MonthsForYou: " + recent12MonthsList.size());
                for (int i = 0; i < recent12MonthsList.size(); i++) {
                    String strMonth = recent12MonthsList.get(i);
                    createMonthForYou(activity, strMonth);
                }
            }

            private void createMonthForYou(Activity activity, String month) {
                Log.d("--recent_month--", "createMonthForYou: " + month);

                if (!forYouViewModel.isMonthExist(month)) {
                    ArrayList<FileModel> monthImageList = MediaLoader.getMonthImageList(activity, month, fileModelArrayList);

                    Log.d("--recent_month--", "monthImageList: " + new Gson().toJson(monthImageList));

                    ArrayList<FileModel> monthForYouList = new ArrayList<>();
                    ArrayList<Integer> randomIndexList = new ArrayList<>();

                    if (!monthImageList.isEmpty()) {
                        int forYouSize = MathUtils.getForYouSize(monthImageList.size());

                        if (forYouSize != 0) {
                            for (int i = 0; i < monthImageList.size(); i++) {
                                if (monthForYouList.size() == forYouSize) {
                                    Collections.sort(monthForYouList, new Comparator<FileModel>() {
                                        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH);

                                        @Override
                                        public int compare(FileModel lhs, FileModel rhs) {
                                            try {
                                                return Objects.requireNonNull(dateFormat.parse(lhs.getDateModified())).compareTo(dateFormat.parse(rhs.getDateModified()));
                                            } catch (ParseException e) {
                                                throw new IllegalArgumentException(e);
                                            }
                                        }
                                    });
                                    for (int j = 0; j < monthForYouList.size(); j++) {
                                        Log.d("--recent_month--", "monthForYouList: " + monthForYouList.get(j).getDateModified() + "-----path:" + monthForYouList.get(j).getPath());
                                    }
                                    if (!monthForYouList.isEmpty()) {
                                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                ForYouModel forYouModel = new ForYouModel("month", month, monthForYouList);
                                                forYouData.setValue(forYouModel);
                                            }
                                        }, 5000);
                                    }
                                    break;
                                } else {
                                    int randomIndex = MathUtils.getRandomNumber(0, monthImageList.size() - 1);

                                    if (monthForYouList.isEmpty()) {
                                        FileModel fileModel = monthImageList.get(randomIndex);

                                        monthForYouList.add(fileModel);
                                        randomIndexList.add(randomIndex);
                                    } else {
                                        if (!randomIndexList.contains(randomIndex)) {
                                            FileModel fileModel = monthImageList.get(randomIndex);

                                            monthForYouList.add(fileModel);
                                            randomIndexList.add(randomIndex);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            private void createRecentYearForYou(Activity activity) {
                String recentYearTitle = DateUtils.getRecent12Months();

                Log.d("--recent_year--", "recentYearTitle: " + recentYearTitle);

                if (!forYouViewModel.isRecentYearExist(recentYearTitle)) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            forYouViewModel.deleteAllRecentYear();
                        }
                    });

                    ArrayList<FileModel> recentYearImageList = new ArrayList<>(MediaLoader.getRecentYearImageList(activity, fileModelArrayList));

                    Log.d("--recent_year--", "recentYearImageList: " + recentYearImageList.size());

                    ArrayList<FileModel> recentYearForYouList = new ArrayList<>(MediaLoader.getForYouRandomImageList(recentYearImageList));

                    Log.d("--recent_year--", "recentWeekForYouList: " + recentYearForYouList.size());

                    if (!recentYearForYouList.isEmpty()) {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ForYouModel forYouModel = new ForYouModel("recent_year", recentYearTitle, recentYearForYouList);
                                forYouData.setValue(forYouModel);
                            }
                        }, 5000);
                    }
                }
            }

            private void createDaysForYou(Activity activity) {
                ArrayList<String> last7DaysList = new ArrayList<>(DateUtils.getRecent7DaysList());
                for (int i = 0; i < last7DaysList.size(); i++) {
                    String strDay = last7DaysList.get(i);
                    createDayForYou(activity, strDay);
                }
            }

            private void createDayForYou(Activity activity, String day) {
                Log.d("--create_day--", "day: " + day);
                if (!forYouViewModel.isDayExist(day)) {
                    Log.d("--create_day--", "day not exist: " + day);
                    ArrayList<FileModel> dayImageList = MediaLoader.getDayImageList(activity, day, fileModelArrayList);

                    Log.d("--create_day--", "dayImageList: " + dayImageList.size());

                    ArrayList<FileModel> dayForYouList = new ArrayList<>(MediaLoader.getForYouRandomImageList(dayImageList));

                    Log.d("--create_day--", "dayForYouList: " + dayForYouList.size());

                    if (!dayForYouList.isEmpty()) {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ForYouModel forYouModel = new ForYouModel("day", day, dayForYouList);
                                Log.d("--create_day--", "dayForYouList ready for update: ");
                                forYouData.setValue(forYouModel);
                            }
                        }, 5000);
                    }
                }
            }

            private void createRecentWeekForYou(Activity activity) {
                String weekTitle = DateUtils.getRecentWeekDate();

                Log.d("--recent_week--", "weekTitle: " + weekTitle);

                if (!forYouViewModel.isRecentWeekExist(weekTitle)) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            forYouViewModel.deleteAllRecentWeek();
                        }
                    });
                    ArrayList<FileModel> recentWeekImageList = new ArrayList<>(MediaLoader.getRecentWeekImageList(activity, fileModelArrayList));

                    Log.d("--date_modified--", "-----------recentWeekImageList Size-------------->: " + recentWeekImageList.size());

                    if (!recentWeekImageList.isEmpty()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                ArrayList<FileModel> recentWeekForYouList = new ArrayList<>(MediaLoader.getForYouRandomImageList(recentWeekImageList));

                                Log.d("--date_modified--", "recentWeekForYouList: " + recentWeekForYouList.size());

                                if (!recentWeekForYouList.isEmpty()) {
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("--date_modified--", "runOnUiThread: " + "add for refresh");
                                            ForYouModel forYouModel = new ForYouModel("recent_week", weekTitle, recentWeekForYouList);
                                            forYouData.setValue(forYouModel);
                                        }
                                    }, 5000);
                                }
                            }
                        }).start();
                    }
                }
            }

            private void createYearForYou(Activity activity, String year) {
                Log.d("--year--", "createYearForYou: " + year);

                if (!forYouViewModel.isYearExist(year)) {
                    ArrayList<FileModel> yearImageList = MediaLoader.getYearImageList(activity, year, fileModelArrayList);

                    Log.d("--year--", "createYearForYou: " + new Gson().toJson(yearImageList));

                    ArrayList<FileModel> yearForYouList = new ArrayList<>();
                    ArrayList<Integer> randomIndexList = new ArrayList<>();

                    if (!yearImageList.isEmpty()) {
                        int forYouSize = MathUtils.getForYouSize(yearImageList.size());

                        for (int i = 0; i < yearImageList.size(); i++) {
                            if (yearForYouList.size() == forYouSize) {
                                Collections.sort(yearForYouList, new Comparator<FileModel>() {
                                    final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH);

                                    @Override
                                    public int compare(FileModel lhs, FileModel rhs) {
                                        try {
                                            return Objects.requireNonNull(dateFormat.parse(lhs.getDateModified())).compareTo(dateFormat.parse(rhs.getDateModified()));
                                        } catch (ParseException e) {
                                            throw new IllegalArgumentException(e);
                                        }
                                    }
                                });
                                for (int j = 0; j < yearForYouList.size(); j++) {
                                    Log.d("--year--", "date: " + yearForYouList.get(j).getDateModified() + "-----path:" + yearForYouList.get(j).getPath());
                                }
                                if (!yearForYouList.isEmpty()) {
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ForYouModel forYouModel = new ForYouModel("year", year, yearForYouList);
                                            forYouData.setValue(forYouModel);
                                        }
                                    }, 5000);
                                }
                                break;
                            } else {
                                int randomIndex = MathUtils.getRandomNumber(0, yearImageList.size() - 1);

                                if (yearForYouList.isEmpty()) {
                                    FileModel fileModel = yearImageList.get(randomIndex);

                                    yearForYouList.add(fileModel);
                                    randomIndexList.add(randomIndex);
                                } else {
                                    if (!randomIndexList.contains(randomIndex)) {
                                        FileModel fileModel = yearImageList.get(randomIndex);

                                        yearForYouList.add(fileModel);
                                        randomIndexList.add(randomIndex);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }).start();
    }

    public LiveData<ForYouModel> getForYouData() {
        if (forYouData == null) {
            forYouData = new MutableLiveData<>();
        }
        return forYouData;
    }
}
