package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces;

import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;

import java.util.ArrayList;

public interface DayClickListener {
    void onDayClicked(int position, ArrayList<FileModel> fileModelList);
}
