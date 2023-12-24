package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.Constant;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter.AddressAdapter;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter.AlbumAdapter;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumDBModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.FragmentAlbumsBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.AlbumClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.PlaceClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.mediastore.MediaLoader;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.AddressModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.AlbumMediaActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.AlbumsActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.MainActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.PlacesActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities.PlacesMediaActivity;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.CacheUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AlbumFragment extends Fragment implements AlbumClickListener, PlaceClickListener {

    AlbumFragment fragment;
    Activity activity;

    FragmentAlbumsBinding binding;

    AlbumAdapter myAlbumAdapter;
    AlbumAdapter moreAlbumAdapter;
    AddressAdapter addressAdapter;

    HashMap<String, ArrayList<FileModel>> myAlbumHashMap = new HashMap<>();
    HashMap<String, ArrayList<FileModel>> moreAlbumHashMap = new HashMap<>();
    HashMap<String, AddressModel> addressModelHashMap = new HashMap<>();

    ArrayList<FileModel> fileModelArrayList = new ArrayList<>();

    ArrayList<String> myAlbumNameList = new ArrayList<>(Arrays.asList("Favorites", "Recents", "Camera", "Videos", "Downloads", "Screenshots"));

    MutableLiveData<HashMap<String, ArrayList<FileModel>>> myAlbumLiveData;
    MutableLiveData<HashMap<String, ArrayList<FileModel>>> moreAlbumLiveData;
    MutableLiveData<HashMap<String, AddressModel>> addressLiveData;

    public AlbumFragment getInstance() {
        if (fragment == null) { // if fragment is null
            fragment = new AlbumFragment(); // initializing fragment
        }
        return fragment;
    }

    public AlbumFragment() {
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
        binding = FragmentAlbumsBinding.inflate(inflater, container, false); // inflating fragment layout

        activity = requireActivity();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) activity).showProgressDialog();

        binding.rvMyAlbum.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        binding.rvMoreAlbums.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        binding.rvPlaces.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        myAlbumAdapter = new AlbumAdapter(activity, "AlbumFragment", AlbumFragment.this);
        binding.rvMyAlbum.setAdapter(myAlbumAdapter);

        moreAlbumAdapter = new AlbumAdapter(activity, "AlbumFragment", AlbumFragment.this);
        binding.rvMoreAlbums.setAdapter(moreAlbumAdapter);

        addressAdapter = new AddressAdapter(activity, AlbumFragment.this);
        binding.rvPlaces.setAdapter(addressAdapter);

        if (myAlbumLiveData == null) {
            myAlbumLiveData = new MutableLiveData<>();
        }

        if (moreAlbumLiveData == null) {
            moreAlbumLiveData = new MutableLiveData<>();
        }

        if (addressLiveData == null) {
            addressLiveData = new MutableLiveData<>();
        }

        myAlbumLiveData.observe((MainActivity) activity, new Observer<HashMap<String, ArrayList<FileModel>>>() {
            @Override
            public void onChanged(HashMap<String, ArrayList<FileModel>> myAlbumLiveHashMap) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        myAlbumHashMap = new HashMap<>(myAlbumLiveHashMap);

                        if (myAlbumHashMap.isEmpty()) {
                            if (addressModelHashMap.isEmpty() && moreAlbumHashMap.isEmpty()) {
                                binding.constraintData.setVisibility(View.GONE);
                                binding.txtNoAlbums.setVisibility(View.VISIBLE);
                            }
                        } else {
                            binding.constraintData.setVisibility(View.VISIBLE);
                            binding.txtNoAlbums.setVisibility(View.GONE);
                        }

                        ((MainActivity) activity).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((MainActivity) activity).albumsViewModel.getAllFavorites().observe((MainActivity) activity, new Observer<List<AlbumDBModel.FavoriteDBModel>>() {
                                    @Override
                                    public void onChanged(List<AlbumDBModel.FavoriteDBModel> favoriteDBModels) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ArrayList<FileModel> tempFavoriteList = new ArrayList<>();
                                                ArrayList<AlbumDBModel.FavoriteDBModel> tempFavoritesList = new ArrayList<>(favoriteDBModels);

                                                for (int i = 0; i < tempFavoritesList.size(); i++) {
                                                    AlbumDBModel.FavoriteDBModel favoriteDBModel = tempFavoritesList.get(i);

                                                    for (int j = 0; j < fileModelArrayList.size(); j++) {
                                                        FileModel fileModel = fileModelArrayList.get(j);

                                                        if (favoriteDBModel.getPath().equals(fileModel.getPath())) {
                                                            tempFavoriteList.add(fileModel);
                                                            break;
                                                        }
                                                    }
                                                }

                                                ArrayList<FileModel> favoritesList = new ArrayList<>(tempFavoriteList);
                                                myAlbumHashMap.put("Favorites", favoritesList);

                                                activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (myAlbumHashMap.size() > 3) {
                                                            binding.txtMyAll.setVisibility(View.VISIBLE);
                                                        } else {
                                                            binding.txtMyAll.setVisibility(View.GONE);
                                                        }
                                                        myAlbumAdapter.swapList("My Albums", myAlbumHashMap);

                                                        ((MainActivity) activity).dismissProgressDialog();
                                                    }
                                                });
                                            }
                                        }).start();
                                    }
                                });
                            }
                        });
                    }
                }).start();
            }
        });

        moreAlbumLiveData.observe((MainActivity) activity, new Observer<HashMap<String, ArrayList<FileModel>>>() {
            @Override
            public void onChanged(HashMap<String, ArrayList<FileModel>> moreAlbumLiveHashMap) {
                moreAlbumHashMap = new HashMap<>(moreAlbumLiveHashMap);

                if (moreAlbumHashMap.isEmpty()) {
                    if (addressModelHashMap.isEmpty() && myAlbumHashMap.isEmpty()) {
                        binding.constraintData.setVisibility(View.GONE);
                        binding.txtNoAlbums.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.constraintData.setVisibility(View.VISIBLE);
                    binding.txtNoAlbums.setVisibility(View.GONE);

                    if (moreAlbumHashMap.size() > 3) {
                        binding.txtMoreAll.setVisibility(View.VISIBLE);
                    } else {
                        binding.txtMoreAll.setVisibility(View.GONE);
                    }
                }

                moreAlbumAdapter.swapList("More Albums", moreAlbumHashMap);

                ((MainActivity) activity).dismissProgressDialog();
            }
        });

        addressLiveData.observe((MainActivity) activity, new Observer<HashMap<String, AddressModel>>() {
            @Override
            public void onChanged(HashMap<String, AddressModel> tempAddressHashMap) {
                updateAddressData(tempAddressHashMap);
            }
        });

        ((MainActivity) activity).albumsViewModel.getAllAddressData().observe((MainActivity) activity, new Observer<List<AlbumDBModel.AddressDBModel>>() {
            @Override
            public void onChanged(List<AlbumDBModel.AddressDBModel> addressDBModels) {
                refreshAddressData(addressDBModels);
            }
        });

        binding.txtSeeAllPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PlacesActivity.class);
                Constant.INTENT_ADDRESS_MODEL_HASHMAP = new HashMap<>(addressModelHashMap);
//                intent.putExtra("placesHashMap", addressModelHashMap);
                ((MainActivity) activity).showInterstitialAd(activity, intent, null);
            }
        });

        binding.txtMyAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AlbumsActivity.class);
                intent.putExtra("album_type", "My Albums");
//                intent.putExtra("albumHashMap", myAlbumHashMap);
                Constant.INTENT_ALBUM_MODEL_HASHMAP = new HashMap<>(myAlbumHashMap);
                ((MainActivity) activity).showInterstitialAd(activity, intent, null);
            }
        });

        binding.txtMoreAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AlbumsActivity.class);
                intent.putExtra("album_type", "More Albums");
//                intent.putExtra("albumHashMap", moreAlbumHashMap);
                Constant.INTENT_ALBUM_MODEL_HASHMAP = new HashMap<>(moreAlbumHashMap);
                ((MainActivity) activity).showInterstitialAd(activity, intent, null);
            }
        });
    }

    private void updateAddressData(HashMap<String, AddressModel> tempAddressHashMap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (tempAddressHashMap.isEmpty()) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            binding.llPlaces.setVisibility(View.GONE);
                        }
                    });
                } else {
                    Object[] addressList = tempAddressHashMap.keySet().toArray();

                    for (Object o : addressList) {
                        String addressName = (String) o;

                        AddressModel addressModel = tempAddressHashMap.get(addressName);

                        if (addressModel != null) {
                            ArrayList<FileModel> fileModels = addressModel.getFileModelArrayList();

                            if (fileModels != null) {
                                if (!fileModels.isEmpty()) {
                                    try {
                                        Collections.sort(fileModels, new Comparator<FileModel>() {
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
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    addressModel.setFileModelArrayList(fileModels);

                                    tempAddressHashMap.put(addressName, addressModel);
                                }
                            }
                        }
                    }
                    addressModelHashMap = new HashMap<>(tempAddressHashMap);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            binding.llPlaces.setVisibility(View.VISIBLE);

                            if (tempAddressHashMap.size() > 3) {
                                binding.txtSeeAllPlaces.setVisibility(View.VISIBLE);
                            } else {
                                binding.txtSeeAllPlaces.setVisibility(View.GONE);
                            }

                            addressAdapter.swapList(addressModelHashMap);

                            ((MainActivity) activity).dismissProgressDialog();
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

                HashMap<String, AddressModel> tempAddressHashMap = new HashMap<>();

                for (int i = 0; i < addressDBModels.size(); i++) {
                    AlbumDBModel.AddressDBModel addressDBModel = addressDBModels.get(i);

                    String address = addressDBModel.getAddress();

                    if (tempAddressHashMap.containsKey(address)) {
                        AddressModel addressModel = tempAddressHashMap.get(address);

                        ArrayList<FileModel> fileModels = new ArrayList<>();
                        FileModel fileModel = new Gson().fromJson(addressDBModel.getFileModel(), FileModel.class);

                        if (addressModel == null) {
                            fileModels = new ArrayList<>();
                            fileModels.add(fileModel);

                            addressModel = new AddressModel();
                            addressModel.setAddress(address);
                        } else {
                            fileModels = new ArrayList<>(addressModel.getFileModelArrayList());
                            fileModels.add(fileModel);
                        }
                        addressModel.setFileModelArrayList(fileModels);
                        tempAddressHashMap.put(address, addressModel);
                    } else {
                        ArrayList<FileModel> fileModels = new ArrayList<>();
                        FileModel fileModel = new Gson().fromJson(addressDBModel.getFileModel(), FileModel.class);
                        fileModels.add(fileModel);

                        AddressModel addressModel = new AddressModel();
                        addressModel.setAddress(address);
                        addressModel.setFileModelArrayList(fileModels);

                        tempAddressHashMap.put(address, addressModel);
                    }
                }

                Log.d("==address==", "tempAddressHashMap: " + tempAddressHashMap.size());

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        addressLiveData.setValue(tempAddressHashMap);
                    }
                });
            }
        }).start();
    }

    public void refreshData(List<MediaModel> mediaModelList) {
        if (fragment == null) {
            fragment = new AlbumFragment().getInstance();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<MediaModel> mediaModelArrayList = new ArrayList<>(mediaModelList);
                ArrayList<FileModel> tempFileModelList = new ArrayList<>();

                for (int i = 0; i < mediaModelArrayList.size(); i++) {
                    MediaModel mediaModel = mediaModelArrayList.get(i);

                    FileModel fileModel = new FileModel(mediaModel.getFileId(), mediaModel.getPath(), mediaModel.getDateModified(), mediaModel.getFileFormat(), mediaModel.getDuration(), mediaModel.getSize());
                    tempFileModelList.add(fileModel);
                }

                try {
                    Collections.sort(tempFileModelList, new Comparator<FileModel>() {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

                fileModelArrayList = new ArrayList<>(tempFileModelList);

                HashMap<String, ArrayList<FileModel>> tempAlbumHashMap = new HashMap<>(MediaLoader.getAllAlbumHashMap(fileModelArrayList));
                HashMap<String, ArrayList<FileModel>> tempMyAlbumHashMap = new HashMap<>();

                tempAlbumHashMap.put("Recents", fileModelArrayList);
                tempMyAlbumHashMap.put("Recents", fileModelArrayList);

                ArrayList<FileModel> cameraList = new ArrayList<>(MediaLoader.getListFromStringMatch("/Camera/", fileModelArrayList));
                if (!cameraList.isEmpty()) {
                    tempAlbumHashMap.put("Camera", cameraList);
                    tempMyAlbumHashMap.put("Camera", cameraList);
                }

                ArrayList<FileModel> videoList = new ArrayList<>(MediaLoader.getVideosList(fileModelArrayList));
                if (!videoList.isEmpty()) {
                    tempAlbumHashMap.put("Videos", videoList);
                    tempMyAlbumHashMap.put("Videos", videoList);
                }

                ArrayList<FileModel> downloadList = new ArrayList<>(MediaLoader.getListFromStringMatch("/Download/", fileModelArrayList));
                if (!downloadList.isEmpty()) {
                    tempAlbumHashMap.put("Download", downloadList);
                    tempMyAlbumHashMap.put("Download", downloadList);
                }

                ArrayList<FileModel> screenshotList = new ArrayList<>(MediaLoader.getListFromStringMatch("/Screenshots/", fileModelArrayList));
                if (!screenshotList.isEmpty()) {
                    tempAlbumHashMap.put("Screenshots", screenshotList);
                    tempMyAlbumHashMap.put("Screenshots", screenshotList);
                }

                HashMap<String, ArrayList<FileModel>> tempMoreAlbumHashMap = new HashMap<>();

                tempMoreAlbumHashMap = new HashMap<>(tempAlbumHashMap);
                for (int i = 0; i < myAlbumNameList.size(); i++) {
                    tempMoreAlbumHashMap.remove(myAlbumNameList.get(i));
                }

                HashMap<String, ArrayList<FileModel>> finalTempMoreAlbumHashMap = tempMoreAlbumHashMap;

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (myAlbumLiveData == null) {
                            myAlbumLiveData = new MutableLiveData<>();
                        }
                        if (moreAlbumLiveData == null) {
                            moreAlbumLiveData = new MutableLiveData<>();
                        }

                        myAlbumLiveData.setValue(tempMyAlbumHashMap);
                        moreAlbumLiveData.setValue(finalTempMoreAlbumHashMap);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onAlbumClick(String albumName, ArrayList<FileModel> fileModelArrayList) {
        Intent intent = new Intent(activity, AlbumMediaActivity.class);
        intent.putExtra("albumName", albumName);
        Constant.INTENT_FILE_MODEL_ARRAY_LIST = new ArrayList<>(fileModelArrayList);
//        intent.putExtra("fileModelArrayList", fileModelArrayList);
//        startActivity(intent);
        ((MainActivity) activity).showInterstitialAd(activity, intent, null);
    }

    @Override
    public void onPlaceClick(String place, ArrayList<FileModel> fileModelArrayList) {
        Intent intent = new Intent(activity, PlacesMediaActivity.class);
        intent.putExtra("address", place);
//        intent.putExtra("fileModelArrayList", fileModelArrayList);
        Constant.INTENT_FILE_MODEL_ARRAY_LIST = new ArrayList<>(fileModelArrayList);
//        startActivity(intent);
        ((MainActivity) activity).showInterstitialAd(activity, intent, null);
    }
}