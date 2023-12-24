package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.Constant;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.adapter.AddressAdapter;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums.AlbumDBModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.databinding.ActivityPlacesBinding;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces.PlaceClickListener;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.AddressModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.ThemeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PlacesActivity extends BaseActivity implements PlaceClickListener {

    ActivityPlacesBinding binding;

    HashMap<String, AddressModel> addressModelHashMap = new HashMap<>();

    AddressAdapter addressAdapter;

    boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setTheme(PlacesActivity.this);
        super.onCreate(savedInstanceState);
        binding = ActivityPlacesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        loadInterstitialAd(PlacesActivity.this);
        showBannerAd(PlacesActivity.this, binding.adBanner);

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.rvPlacesGrid.setLayoutManager(new GridLayoutManager(PlacesActivity.this, 2));

        addressAdapter = new AddressAdapter(PlacesActivity.this, this);
        binding.rvPlacesGrid.setAdapter(addressAdapter);

        if (!Constant.INTENT_ADDRESS_MODEL_HASHMAP.isEmpty()) {
            addressModelHashMap = Constant.INTENT_ADDRESS_MODEL_HASHMAP;
        }

        if (addressModelHashMap == null) {
            finish();
        } else {
            if (addressModelHashMap.isEmpty()) {
                finish();
            } else {
                addressAdapter.swapList(addressModelHashMap);

                albumsViewModel.getAllAddressData().observe(PlacesActivity.this, new Observer<List<AlbumDBModel.AddressDBModel>>() {
                    @Override
                    public void onChanged(List<AlbumDBModel.AddressDBModel> addressDBModels) {
                        if (!isFirstTime) {
                            refreshAddressData(addressDBModels);
                        } else {
                            isFirstTime = false;
                        }
                    }
                });
            }
        }
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tempAddressHashMap.isEmpty()) {
                            finish();
                        } else {
                            Object[] addressList = tempAddressHashMap.keySet().toArray();

                            for (Object o : addressList) {
                                String addressName = (String) o;

                                AddressModel addressModel = tempAddressHashMap.get(addressName);

                                if (addressModel != null) {
                                    ArrayList<FileModel> fileModels = addressModel.getFileModelArrayList();

                                    if (fileModels != null) {
                                        if (!fileModels.isEmpty()) {
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
                                            addressModel.setFileModelArrayList(fileModels);

                                            tempAddressHashMap.put(addressName, addressModel);
                                        }
                                    }
                                }
                            }

                            addressModelHashMap = new HashMap<>(tempAddressHashMap);

                            addressAdapter.swapList(addressModelHashMap);
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onPlaceClick(String place, ArrayList<FileModel> fileModelArrayList) {
        Intent intent = new Intent(PlacesActivity.this, PlacesMediaActivity.class);
        intent.putExtra("address", place);
//        intent.putExtra("fileModelArrayList", fileModelArrayList);
        Constant.INTENT_FILE_MODEL_ARRAY_LIST = new ArrayList<>(fileModelArrayList);
//        startActivity(intent);
        showInterstitialAd(PlacesActivity.this, intent, null);
    }
}
