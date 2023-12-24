package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces;

import android.view.View;

import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;

import java.util.ArrayList;

public interface ForYouClickListener {
    void onClick(View view, String date, int position, ArrayList<FileModel> fileModelArrayList);
}
