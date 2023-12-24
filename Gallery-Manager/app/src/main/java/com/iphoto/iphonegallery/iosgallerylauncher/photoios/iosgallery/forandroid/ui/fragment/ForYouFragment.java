package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.Constant;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter.ForYouAdapter;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.AllMediaViewModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.foryou.ForYouDBModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.foryou.ForYouViewModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.FragmentForYouBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.ForYouClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.ForYouModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.ForYouActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.MainActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.CacheUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ForYouFragment extends Fragment implements ForYouClickListener {

    ForYouFragment fragment;
    Activity activity;

    FragmentForYouBinding binding;

    AllMediaViewModel allMediaViewModel;

    ForYouViewModel forYouViewModel;

    ArrayList<ForYouModel> forYouArrayList = new ArrayList<>();

    ForYouAdapter forYouAdapter;

    public ForYouFragment getInstance() {
        if (fragment == null) { // if fragment is null
            fragment = new ForYouFragment(); // initializing fragment
        }
        return fragment;
    }

    public ForYouFragment() {
        // blank constructor for proper management of fragment
    }

    @Override
    public void onPause() {
        super.onPause();

        // thread to remove cache files
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isAdded()) { // if fragment is added to fragment transaction
                    CacheUtils.deleteCache(activity);
                }
            }
        }).start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentForYouBinding.inflate(inflater, container, false); // inflating fragment layout

        activity = requireActivity();

        return binding.getRoot();
    }

    Thread thread;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        forYouAdapter = new ForYouAdapter(activity, this);
        binding.rvForYou.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        binding.rvForYou.setAdapter(forYouAdapter);

        allMediaViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(AllMediaViewModel.class);

        allMediaViewModel.getAllMediaData().observe((MainActivity) activity, new Observer<List<MediaModel>>() {
            @Override
            public void onChanged(List<MediaModel> mediaModelList) {

                if (thread != null) {
                    thread.interrupt();
                }

                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<MediaModel> tempMediaList = new ArrayList<>(mediaModelList);
                        ArrayList<FileModel> tempFileList = new ArrayList<>();

                        for (int i = 0; i < tempMediaList.size(); i++) {
                            MediaModel mediaModel = tempMediaList.get(i);

                            FileModel fileModel = new FileModel(mediaModel.getFileId(), mediaModel.getPath(), mediaModel.getDateModified(), mediaModel.getFileFormat(), mediaModel.getDuration(), mediaModel.getSize());
                            tempFileList.add(fileModel);
                        }

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH); // your own date format

                        Collections.sort(tempFileList, new Comparator<FileModel>() {
                            @Override
                            public int compare(FileModel fileModel1, FileModel fileModel2) {
                                try {
                                    return Objects.requireNonNull(simpleDateFormat.parse(fileModel2.getDateModified())).compareTo(simpleDateFormat.parse(fileModel1.getDateModified()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    return 0;
                                }
                            }
                        });

                        ((MainActivity) activity).loadForYouData(activity, tempFileList);
                    }
                });
                thread.start();
            }
        });

        forYouViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(ForYouViewModel.class);

        forYouViewModel.getAllDays().observe((LifecycleOwner) activity, new Observer<List<ForYouDBModel.DayModel>>() {
            @Override
            public void onChanged(List<ForYouDBModel.DayModel> dayForYouModels) {

                Log.d("--recent_days--", "onChanged: " + dayForYouModels.size());

                if (!dayForYouModels.isEmpty()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            updatingDaysForYouList(dayForYouModels);
                        }
                    }).start();
                }
            }
        });

        forYouViewModel.getAllRecentWeeks().observe((LifecycleOwner) activity, new Observer<List<ForYouDBModel.RecentWeekModel>>() {
            @Override
            public void onChanged(List<ForYouDBModel.RecentWeekModel> recentWeekModels) {
                Log.d("--recent_week--", "onChanged: " + recentWeekModels.size());

                if (!recentWeekModels.isEmpty()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            updatingWeekForYouList(recentWeekModels);
                        }
                    }).start();
                }
            }
        });

        forYouViewModel.getAllMonths().observe((LifecycleOwner) activity, new Observer<List<ForYouDBModel.MonthModel>>() {
            @Override
            public void onChanged(List<ForYouDBModel.MonthModel> monthModels) {
                Log.d("--months--", "onChanged: " + monthModels.size());

                if (!monthModels.isEmpty()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            updatingMonthsForYouList(monthModels);
                        }
                    }).start();
                }
            }
        });

        forYouViewModel.getAllRecentYear().observe((LifecycleOwner) activity, new Observer<List<ForYouDBModel.RecentYearModel>>() {
            @Override
            public void onChanged(List<ForYouDBModel.RecentYearModel> recentYearModels) {
                Log.d("--recent_year--", "onChanged: " + recentYearModels.size());

                if (!recentYearModels.isEmpty()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            updatingRecentYearForYouList(recentYearModels);
                        }
                    }).start();
                }
            }
        });

        forYouViewModel.getAllYears().observe((LifecycleOwner) activity, new Observer<List<ForYouDBModel.YearModel>>() {
            @Override
            public void onChanged(List<ForYouDBModel.YearModel> yearModels) {
                Log.d("--year--", "onChanged: " + yearModels.size());

                if (!yearModels.isEmpty()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            updatingYearsForYouList(yearModels);
                        }
                    }).start();
                }
            }
        });

        forYouViewModel.getAllAddress().observe((LifecycleOwner) activity, new Observer<List<ForYouDBModel.AddressModel>>() {
            @Override
            public void onChanged(List<ForYouDBModel.AddressModel> addressModels) {
                Log.d("--year--", "onChanged: " + addressModels.size());

                if (!addressModels.isEmpty()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            updatingAddressForYouList(addressModels);
                        }
                    }).start();
                }
            }
        });
    }

    private void updatingAddressForYouList(List<ForYouDBModel.AddressModel> addressModels) {
        Log.d("--address--", "onChanged: " + new Gson().toJson(addressModels));

        Type type = new TypeToken<ArrayList<FileModel>>() {
        }.getType();

        for (int i = 0; i < addressModels.size(); i++) { // starting work on available for you's
            String address = addressModels.get(i).getAddress(); // getting address @i position
            ArrayList<FileModel> tempFileModels = new Gson().fromJson(addressModels.get(i).getPathList(), type); // converting string to arraylist
            ArrayList<FileModel> fileModels = new ArrayList<>(); // creating duplicate array to remove non-existing files

            for (int j = 0; j < tempFileModels.size(); j++) { // starting work on removing non-existing files
                File file = new File(tempFileModels.get(j).getPath());

                if (file.exists()) { // checking if file exists or not
                    fileModels.add(tempFileModels.get(j));
                }
            }
            if (fileModels.size() != tempFileModels.size()) { // checking if any changes found in array size
                // CRUD for @DayDBModel
                ForYouDBModel.YearModel forYouDBModel = forYouViewModel.getYearByTitle(address); // getting @DayDBModel with help of address
                if (fileModels.size() >= 5) { // if size is more than 4
                    forYouDBModel.setPathList(new Gson().toJson(fileModels)); // converting fileModels and storing it in @DayDBModel
                    forYouViewModel.updateYear(forYouDBModel); // updating @DayDBModel

                    boolean isAlreadyExists = false;
                    for (int j = 0; j < forYouArrayList.size(); j++) {
                        String tempDate = forYouArrayList.get(j).getTitle();

                        if (tempDate.equals(address)) { // checking dates are equal or not
                            // updating existing @DayForYouModel
                            ForYouModel forYouModel = forYouArrayList.get(j);
                            forYouModel.setFileModelArrayList(fileModels);
                            forYouArrayList.set(j, forYouModel);
                            isAlreadyExists = true;
                            break;
                        }
                    }
                    if (!isAlreadyExists) {
                        // if not found then adding @DayForYouModel in Constant.dayForYouArrayList
                        ForYouModel forYouModel = new ForYouModel("address", address, fileModels);
                        forYouArrayList.add(forYouModel);
                    }
                } else { // if size lesser than 5
                    // deleting @DayDBModel from for_you
                    forYouViewModel.deleteYear(forYouDBModel);
                }
            } else {
                boolean isAlreadyExists = false;
                for (int j = 0; j < forYouArrayList.size(); j++) {
                    String tempTitle = forYouArrayList.get(j).getTitle();

                    if (tempTitle.equals(address)) { // checking dates are equal or not
                        // updating existing @DayForYouModel
                        ForYouModel forYouModel = forYouArrayList.get(j);
                        forYouModel.setFileModelArrayList(fileModels);
                        forYouArrayList.set(j, forYouModel);
                        isAlreadyExists = true;
                    }
                }
                if (!isAlreadyExists) {
                    // if not found then adding @DayForYouModel in Constant.dayForYouArrayList
                    ForYouModel forYouModel = new ForYouModel("address", address, fileModels);
                    forYouArrayList.add(forYouModel);
                }
            }
        }

        if (fragment == null) {
            fragment = new ForYouFragment();
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < forYouArrayList.size(); i++) {
                    Collections.sort(forYouArrayList.get(i).getFileModelArrayList(), new Comparator<FileModel>() {
                        final DateFormat f = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH);

                        @Override
                        public int compare(FileModel lhs, FileModel rhs) {
                            try {
                                return Objects.requireNonNull(f.parse(lhs.getDateModified())).compareTo(f.parse(rhs.getDateModified()));
                            } catch (ParseException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                    });
                }
                updateUI(forYouArrayList);
            }
        });

        Log.d("--list_for--", "onChanged: " + forYouArrayList.size());
    }

    private void updatingYearsForYouList(List<ForYouDBModel.YearModel> yearModels) {
        Log.d("--year--", "onChanged: " + new Gson().toJson(yearModels));

        Type type = new TypeToken<ArrayList<FileModel>>() {
        }.getType();

        for (int i = 0; i < yearModels.size(); i++) { // starting work on available for you's
            String title = yearModels.get(i).getTitle(); // getting title @i position
            ArrayList<FileModel> tempFileModels = new Gson().fromJson(yearModels.get(i).getPathList(), type); // converting string to arraylist
            ArrayList<FileModel> fileModels = new ArrayList<>(); // creating duplicate array to remove non-existing files

            for (int j = 0; j < tempFileModels.size(); j++) { // starting work on removing non-existing files
                File file = new File(tempFileModels.get(j).getPath());

                if (file.exists()) { // checking if file exists or not
                    fileModels.add(tempFileModels.get(j));
                }
            }
            if (fileModels.size() != tempFileModels.size()) { // checking if any changes found in array size
                // CRUD for @DayDBModel
                ForYouDBModel.YearModel forYouDBModel = forYouViewModel.getYearByTitle(title); // getting @DayDBModel with help of title
                if (fileModels.size() >= 5) { // if size is more than 4
                    forYouDBModel.setPathList(new Gson().toJson(fileModels)); // converting fileModels and storing it in @DayDBModel
                    forYouViewModel.updateYear(forYouDBModel); // updating @DayDBModel

                    boolean isAlreadyExists = false;
                    for (int j = 0; j < forYouArrayList.size(); j++) {
                        String tempDate = forYouArrayList.get(j).getTitle();

                        if (tempDate.equals(title)) { // checking dates are equal or not
                            // updating existing @DayForYouModel
                            ForYouModel forYouModel = forYouArrayList.get(j);
                            forYouModel.setFileModelArrayList(fileModels);
                            forYouArrayList.set(j, forYouModel);
                            isAlreadyExists = true;
                            break;
                        }
                    }
                    if (!isAlreadyExists) {
                        // if not found then adding @DayForYouModel in Constant.dayForYouArrayList
                        ForYouModel forYouModel = new ForYouModel("year", title, fileModels);
                        forYouArrayList.add(forYouModel);
                    }
                } else { // if size lesser than 5
                    // deleting @DayDBModel from for_you
                    forYouViewModel.deleteYear(forYouDBModel);
                }
            } else {
                boolean isAlreadyExists = false;
                for (int j = 0; j < forYouArrayList.size(); j++) {
                    String tempTitle = forYouArrayList.get(j).getTitle();

                    if (tempTitle.equals(title)) { // checking dates are equal or not
                        // updating existing @DayForYouModel
                        ForYouModel forYouModel = forYouArrayList.get(j);
                        forYouModel.setFileModelArrayList(fileModels);
                        forYouArrayList.set(j, forYouModel);
                        isAlreadyExists = true;
                    }
                }
                if (!isAlreadyExists) {
                    // if not found then adding @DayForYouModel in Constant.dayForYouArrayList
                    ForYouModel forYouModel = new ForYouModel("year", title, fileModels);
                    forYouArrayList.add(forYouModel);
                }
            }
        }

        if (fragment == null) {
            fragment = new ForYouFragment();
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < forYouArrayList.size(); i++) {
                    Collections.sort(forYouArrayList.get(i).getFileModelArrayList(), new Comparator<FileModel>() {
                        final DateFormat f = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH);

                        @Override
                        public int compare(FileModel lhs, FileModel rhs) {
                            try {
                                return Objects.requireNonNull(f.parse(lhs.getDateModified())).compareTo(f.parse(rhs.getDateModified()));
                            } catch (ParseException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                    });
                }
                updateUI(forYouArrayList);
            }
        });

        Log.d("--list_for--", "onChanged: " + forYouArrayList.size());
    }

    private void updatingRecentYearForYouList(List<ForYouDBModel.RecentYearModel> recentYearModels) {
        Log.d("--recent_year--", "onChanged: " + new Gson().toJson(recentYearModels));

        Type type = new TypeToken<ArrayList<FileModel>>() {
        }.getType();

        for (int i = 0; i < recentYearModels.size(); i++) { // starting work on available for you's
            String title = recentYearModels.get(i).getTitle(); // getting title @i position
            ArrayList<FileModel> tempFileModels = new Gson().fromJson(recentYearModels.get(i).getPathList(), type); // converting string to arraylist
            ArrayList<FileModel> fileModels = new ArrayList<>(); // creating duplicate array to remove non-existing files

            for (int j = 0; j < tempFileModels.size(); j++) { // starting work on removing non-existing files
                File file = new File(tempFileModels.get(j).getPath());

                if (file.exists()) { // checking if file exists or not
                    fileModels.add(tempFileModels.get(j));
                }
            }
            if (fileModels.size() != tempFileModels.size()) { // checking if any changes found in array size
                // CRUD for @DayDBModel
                ForYouDBModel.RecentYearModel forYouDBModel = forYouViewModel.getRecentYearByTitle(title); // getting @DayDBModel with help of title
                if (fileModels.size() >= 5) { // if size is more than 4
                    forYouDBModel.setPathList(new Gson().toJson(fileModels)); // converting fileModels and storing it in @DayDBModel
                    forYouViewModel.updateRecentYear(forYouDBModel); // updating @DayDBModel

                    boolean isAlreadyExists = false;
                    for (int j = 0; j < forYouArrayList.size(); j++) {
                        String tempDate = forYouArrayList.get(j).getTitle();

                        if (tempDate.equals(title)) { // checking dates are equal or not
                            // updating existing @DayForYouModel
                            ForYouModel forYouModel = forYouArrayList.get(j);
                            forYouModel.setFileModelArrayList(fileModels);
                            forYouArrayList.set(j, forYouModel);
                            isAlreadyExists = true;
                            break;
                        }
                    }
                    if (!isAlreadyExists) {
                        // if not found then adding @DayForYouModel in Constant.dayForYouArrayList
                        ForYouModel forYouModel = new ForYouModel("recent_year", title, fileModels);
                        forYouArrayList.add(forYouModel);
                    }
                } else { // if size lesser than 5
                    // deleting @DayDBModel from for_you
                    forYouViewModel.deleteRecentYear(forYouDBModel);
                }
            } else {
                boolean isAlreadyExists = false;
                for (int j = 0; j < forYouArrayList.size(); j++) {
                    String tempTitle = forYouArrayList.get(j).getTitle();

                    if (tempTitle.equals(title)) { // checking dates are equal or not
                        // updating existing @DayForYouModel
                        ForYouModel forYouModel = forYouArrayList.get(j);
                        forYouModel.setFileModelArrayList(fileModels);
                        forYouArrayList.set(j, forYouModel);
                        isAlreadyExists = true;
                    }
                }
                if (!isAlreadyExists) {
                    // if not found then adding @DayForYouModel in Constant.dayForYouArrayList
                    ForYouModel forYouModel = new ForYouModel("recent_year", title, fileModels);
                    forYouArrayList.add(forYouModel);
                }
            }
        }

        if (fragment == null) {
            fragment = new ForYouFragment();
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < forYouArrayList.size(); i++) {
                    Collections.sort(forYouArrayList.get(i).getFileModelArrayList(), new Comparator<FileModel>() {
                        final DateFormat f = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH);

                        @Override
                        public int compare(FileModel lhs, FileModel rhs) {
                            try {
                                return Objects.requireNonNull(f.parse(lhs.getDateModified())).compareTo(f.parse(rhs.getDateModified()));
                            } catch (ParseException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                    });
                }
                updateUI(forYouArrayList);
            }
        });

        Log.d("--list_for--", "onChanged: " + forYouArrayList.size());
    }

    private void updatingMonthsForYouList(List<ForYouDBModel.MonthModel> monthModels) {
        Log.d("--months--", "onChanged: " + new Gson().toJson(monthModels));

        Type type = new TypeToken<ArrayList<FileModel>>() {
        }.getType();

        for (int i = 0; i < monthModels.size(); i++) { // starting work on available for you's
            String title = monthModels.get(i).getTitle(); // getting title @i position
            ArrayList<FileModel> tempFileModels = new Gson().fromJson(monthModels.get(i).getPathList(), type); // converting string to arraylist
            ArrayList<FileModel> fileModels = new ArrayList<>(); // creating duplicate array to remove non-existing files

            for (int j = 0; j < tempFileModels.size(); j++) { // starting work on removing non-existing files
                File file = new File(tempFileModels.get(j).getPath());

                if (file.exists()) { // checking if file exists or not
                    fileModels.add(tempFileModels.get(j));
                }
            }
            if (fileModels.size() != tempFileModels.size()) { // checking if any changes found in array size
                // CRUD for @DayDBModel
                ForYouDBModel.MonthModel forYouDBModel = forYouViewModel.getMonthByMonthTitle(title); // getting @DayDBModel with help of title
                if (fileModels.size() >= 5) { // if size is more than 4
                    forYouDBModel.setPathList(new Gson().toJson(fileModels)); // converting fileModels and storing it in @DayDBModel
                    forYouViewModel.updateMonth(forYouDBModel); // updating @DayDBModel

                    boolean isAlreadyExists = false;
                    for (int j = 0; j < forYouArrayList.size(); j++) {
                        String tempDate = forYouArrayList.get(j).getTitle();

                        if (tempDate.equals(title)) { // checking dates are equal or not
                            // updating existing @DayForYouModel
                            ForYouModel forYouModel = forYouArrayList.get(j);
                            forYouModel.setFileModelArrayList(fileModels);
                            forYouArrayList.set(j, forYouModel);
                            isAlreadyExists = true;
                            break;
                        }
                    }
                    if (!isAlreadyExists) {
                        // if not found then adding @DayForYouModel in Constant.dayForYouArrayList
                        ForYouModel forYouModel = new ForYouModel("month", title, fileModels);
                        forYouArrayList.add(forYouModel);
                    }
                } else { // if size lesser than 5
                    // deleting @DayDBModel from for_you
                    forYouViewModel.deleteMonth(forYouDBModel);
                }
            } else {
                boolean isAlreadyExists = false;
                for (int j = 0; j < forYouArrayList.size(); j++) {
                    String tempTitle = forYouArrayList.get(j).getTitle();

                    if (tempTitle.equals(title)) { // checking dates are equal or not
                        // updating existing @DayForYouModel
                        ForYouModel forYouModel = forYouArrayList.get(j);
                        forYouModel.setFileModelArrayList(fileModels);
                        forYouArrayList.set(j, forYouModel);
                        isAlreadyExists = true;
                    }
                }
                if (!isAlreadyExists) {
                    // if not found then adding @DayForYouModel in Constant.dayForYouArrayList
                    ForYouModel forYouModel = new ForYouModel("month", title, fileModels);
                    forYouArrayList.add(forYouModel);
                }
            }
        }

        if (fragment == null) {
            fragment = new ForYouFragment();
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < forYouArrayList.size(); i++) {
                    Collections.sort(forYouArrayList.get(i).getFileModelArrayList(), new Comparator<FileModel>() {
                        final DateFormat f = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH);

                        @Override
                        public int compare(FileModel lhs, FileModel rhs) {
                            try {
                                return Objects.requireNonNull(f.parse(lhs.getDateModified())).compareTo(f.parse(rhs.getDateModified()));
                            } catch (ParseException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                    });
                }
                updateUI(forYouArrayList);
            }
        });

        Log.d("--list_for--", "onChanged: " + forYouArrayList.size());
    }

    private void updatingWeekForYouList(List<ForYouDBModel.RecentWeekModel> recentWeekModels) {
        Log.d("--weeks--", "onChanged: " + new Gson().toJson(recentWeekModels));

        Type type = new TypeToken<ArrayList<FileModel>>() {
        }.getType();

        for (int i = 0; i < recentWeekModels.size(); i++) { // starting work on available for you's
            String title = recentWeekModels.get(i).getTitle(); // getting title @i position
            ArrayList<FileModel> tempFileModels = new Gson().fromJson(recentWeekModels.get(i).getPathList(), type); // converting string to arraylist
            ArrayList<FileModel> fileModels = new ArrayList<>(); // creating duplicate array to remove non-existing files

            for (int j = 0; j < tempFileModels.size(); j++) { // starting work on removing non-existing files
                File file = new File(tempFileModels.get(j).getPath());

                if (file.exists()) { // checking if file exists or not
                    fileModels.add(tempFileModels.get(j));
                }
            }
            if (fileModels.size() != tempFileModels.size()) { // checking if any changes found in array size
                // CRUD for @DayDBModel
                ForYouDBModel.RecentWeekModel forYouDBModel = forYouViewModel.getRecentWeekByTitle(title); // getting @DayDBModel with help of title
                if (fileModels.size() >= 5) { // if size is more than 4
                    forYouDBModel.setPathList(new Gson().toJson(fileModels)); // converting fileModels and storing it in @DayDBModel
                    forYouViewModel.updateRecentWeek(forYouDBModel); // updating @DayDBModel

                    boolean isAlreadyExists = false;
                    for (int j = 0; j < forYouArrayList.size(); j++) {
                        String tempDate = forYouArrayList.get(j).getTitle();

                        if (tempDate.equals(title)) { // checking dates are equal or not
                            // updating existing @DayForYouModel
                            ForYouModel forYouModel = forYouArrayList.get(j);
                            forYouModel.setFileModelArrayList(fileModels);
                            forYouArrayList.set(j, forYouModel);
                            isAlreadyExists = true;
                            break;
                        }
                    }
                    if (!isAlreadyExists) {
                        // if not found then adding @DayForYouModel in Constant.dayForYouArrayList
                        ForYouModel forYouModel = new ForYouModel("recent_week", title, fileModels);
                        forYouArrayList.add(forYouModel);
                    }
                } else { // if size lesser than 5
                    // deleting @DayDBModel from for_you
                    forYouViewModel.deleteRecentWeek(forYouDBModel);
                }
            } else {
                boolean isAlreadyExists = false;
                for (int j = 0; j < forYouArrayList.size(); j++) {
                    String tempTitle = forYouArrayList.get(j).getTitle();

                    if (tempTitle.equals(title)) { // checking dates are equal or not
                        // updating existing @DayForYouModel
                        ForYouModel forYouModel = forYouArrayList.get(j);
                        forYouModel.setFileModelArrayList(fileModels);
                        forYouArrayList.set(j, forYouModel);
                        isAlreadyExists = true;
                    }
                }
                if (!isAlreadyExists) {
                    // if not found then adding @DayForYouModel in Constant.dayForYouArrayList
                    ForYouModel forYouModel = new ForYouModel("recent_week", title, fileModels);
                    forYouArrayList.add(forYouModel);
                }
            }
        }

        if (fragment == null) {
            fragment = new ForYouFragment();
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < forYouArrayList.size(); i++) {
                    Collections.sort(forYouArrayList.get(i).getFileModelArrayList(), new Comparator<FileModel>() {
                        final DateFormat f = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH);

                        @Override
                        public int compare(FileModel lhs, FileModel rhs) {
                            try {
                                return Objects.requireNonNull(f.parse(lhs.getDateModified())).compareTo(f.parse(rhs.getDateModified()));
                            } catch (ParseException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                    });
                }
                updateUI(forYouArrayList);
            }
        });

        Log.d("--list_for--", "onChanged: " + forYouArrayList.size());
    }

    private void updatingDaysForYouList(List<ForYouDBModel.DayModel> dayForYouModels) {
        Log.d("--days--", "onChanged: " + new Gson().toJson(dayForYouModels));

        Type type = new TypeToken<ArrayList<FileModel>>() {
        }.getType();

        for (int i = 0; i < dayForYouModels.size(); i++) { // starting work on available for you's
            String date = dayForYouModels.get(i).getTitle(); // getting date @i position
            ArrayList<FileModel> tempFileModels = new Gson().fromJson(dayForYouModels.get(i).getPathList(), type); // converting string to arraylist
            ArrayList<FileModel> fileModels = new ArrayList<>(); // creating duplicate array to remove non-existing files

            for (int j = 0; j < tempFileModels.size(); j++) { // starting work on removing non-existing files
                File file = new File(tempFileModels.get(j).getPath());

                if (file.exists()) { // checking if file exists or not
                    fileModels.add(tempFileModels.get(j));
                }
            }
            if (fileModels.size() != tempFileModels.size()) { // checking if any changes found in array size
                // CRUD for @DayDBModel
                ForYouDBModel.DayModel forYouDBModel = forYouViewModel.getDayByDate(date); // getting @DayDBModel with help of date
                if (fileModels.size() >= 5) { // if size is more than 4
                    forYouDBModel.setPathList(new Gson().toJson(fileModels)); // converting fileModels and storing it in @DayDBModel
                    forYouViewModel.updateDay(forYouDBModel); // updating @DayDBModel

                    boolean isAlreadyExists = false;
                    for (int j = 0; j < forYouArrayList.size(); j++) {
                        String tempDate = forYouArrayList.get(j).getTitle();

                        if (tempDate.equals(date)) { // checking dates are equal or not
                            // updating existing @DayForYouModel
                            ForYouModel forYouModel = forYouArrayList.get(j);
                            forYouModel.setFileModelArrayList(fileModels);
                            forYouArrayList.set(j, forYouModel);
                            isAlreadyExists = true;
                            break;
                        }
                    }
                    if (!isAlreadyExists) {
                        // if not found then adding @DayForYouModel in Constant.dayForYouArrayList
                        ForYouModel forYouModel = new ForYouModel("day", date, fileModels);
                        forYouArrayList.add(forYouModel);
                    }
                } else { // if size lesser than 5
                    // deleting @DayDBModel from for_you
                    forYouViewModel.deleteDay(forYouDBModel);
                }
            } else {
                boolean isAlreadyExists = false;
                for (int j = 0; j < forYouArrayList.size(); j++) {
                    String tempDate = forYouArrayList.get(j).getTitle();

                    if (tempDate.equals(date)) { // checking dates are equal or not
                        // updating existing @DayForYouModel
                        ForYouModel forYouModel = forYouArrayList.get(j);
                        forYouModel.setFileModelArrayList(fileModels);
                        forYouArrayList.set(j, forYouModel);
                        isAlreadyExists = true;
                    }
                }
                if (!isAlreadyExists) {
                    // if not found then adding @DayForYouModel in Constant.dayForYouArrayList
                    ForYouModel forYouModel = new ForYouModel("day", date, fileModels);
                    forYouArrayList.add(forYouModel);
                }
            }
        }

        if (fragment == null) {
            fragment = new ForYouFragment();
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUI(forYouArrayList);
            }
        });

        Log.d("--list_for--", "onChanged: " + forYouArrayList.size());
    }

    private void updateUI(ArrayList<ForYouModel> forYouArrayList) {

        if (!forYouArrayList.isEmpty()) {
            // for you available
            binding.rvForYou.setVisibility(View.VISIBLE);
            binding.txtNoForYou.setVisibility(View.GONE);

            forYouAdapter.swapList(forYouArrayList);
        } else {
            // no for you available
            binding.rvForYou.setVisibility(View.GONE);
            binding.txtNoForYou.setVisibility(View.VISIBLE);
        }
    }

    public void refreshForYouData(ForYouModel forYouModel) {

        Log.d("--date_modified--", "ForYouFragment: " + "-------------- Format: " + forYouModel.getFormat() + "-------------------- Title: " + forYouModel.getTitle());

        if (fragment == null) {
            fragment = new ForYouFragment().getInstance();
        }

        ArrayList<FileModel> forYouList = new ArrayList<>(forYouModel.getFileModelArrayList());
        String pathList = new Gson().toJson(forYouList);

        if (forYouViewModel == null) {
            forYouViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(ForYouViewModel.class);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("--date_modified--", "ForYouFragment: " + "check condition if exist");
                switch (forYouModel.getFormat()) {
                    case "day":
                        if (!forYouViewModel.isDayExist(forYouModel.getTitle())) {
                            ForYouDBModel.DayModel dayForYouDBModel = new ForYouDBModel.DayModel(forYouModel.getTitle(), forYouList.size(), pathList);
                            forYouViewModel.insertDay(dayForYouDBModel);
                        }
                        break;
                    case "recent_week":
                        if (!forYouViewModel.isRecentWeekExist(forYouModel.getTitle())) {
                            Log.d("--date_modified--", "ForYouFragment: " + "Add Week in Database");
                            ForYouDBModel.RecentWeekModel weekForYouDBModel = new ForYouDBModel.RecentWeekModel(forYouModel.getTitle(), forYouList.size(), pathList);
                            forYouViewModel.insertRecentWeek(weekForYouDBModel);
                        }
                        break;
                    case "month":
                        if (!forYouViewModel.isMonthExist(forYouModel.getTitle())) {
                            ForYouDBModel.MonthModel monthForYouDBModel = new ForYouDBModel.MonthModel(forYouModel.getTitle(), forYouList.size(), pathList);
                            forYouViewModel.insertMonth(monthForYouDBModel);
                        }
                        break;
                    case "recent_year":
                        if (!forYouViewModel.isRecentYearExist(forYouModel.getTitle())) {
                            ForYouDBModel.RecentYearModel recentYearModelForYouDBModel = new ForYouDBModel.RecentYearModel(forYouModel.getTitle(), forYouList.size(), pathList);
                            forYouViewModel.insertRecentYear(recentYearModelForYouDBModel);
                        }
                        break;
                    case "year":
                        if (!forYouViewModel.isYearExist(forYouModel.getTitle())) {
                            ForYouDBModel.YearModel recentYearModelForYouDBModel = new ForYouDBModel.YearModel(forYouModel.getTitle(), forYouList.size(), pathList);
                            forYouViewModel.insertYear(recentYearModelForYouDBModel);
                        }
                        break;
                    case "address":
                        if (!forYouViewModel.isAddressExist(forYouModel.getTitle())) {
                            ForYouDBModel.AddressModel recentYearModelForYouDBModel = new ForYouDBModel.AddressModel(forYouModel.getTitle(), forYouList.size(), pathList);
                            forYouViewModel.insertAddress(recentYearModelForYouDBModel);
                        }
                        break;
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view, String date, int position, ArrayList<FileModel> fileModelArrayList) {
        Intent intent = new Intent(activity, ForYouActivity.class);
//        intent.putExtra("fileModelArrayList", fileModelArrayList);
        Constant.INTENT_FILE_MODEL_ARRAY_LIST = new ArrayList<>(fileModelArrayList);
        ((MainActivity) activity).showInterstitialAd(activity, intent, null);
    }
}
