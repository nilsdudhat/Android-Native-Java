package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.interfaces;

import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;

import java.util.ArrayList;

public interface AlbumClickListener {
    void onAlbumClick(String albumName, ArrayList<FileModel> fileModelList);
}
