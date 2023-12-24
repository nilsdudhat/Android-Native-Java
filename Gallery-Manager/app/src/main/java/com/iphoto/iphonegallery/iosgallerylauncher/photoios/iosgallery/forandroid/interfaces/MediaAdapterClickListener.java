package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces;

import android.view.View;

import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;

import java.util.ArrayList;

public interface MediaAdapterClickListener {
    void onClick(View view, ArrayList<FileModel> fileModelArrayList, int position, String mediaType, String subMediaType);
}
