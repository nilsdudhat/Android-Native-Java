package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AllMediaRepository {

    private final AllMediaDao allMediaDao;

    public AllMediaRepository(Application application) {
        AllMediaDatabase allMediaDatabase = AllMediaDatabase.getInstance(application);

        allMediaDao = allMediaDatabase.allMediaDao();
    }

    public LiveData<List<MediaModel>> getAllMediaData() {
        return allMediaDao.getAllMediaData();
    }

    public void insertMedia(MediaModel mediaModel) {
        allMediaDao.insertMedia(mediaModel);
    }

    public void deleteMedia(MediaModel mediaModel) {
        allMediaDao.deleteMedia(mediaModel);
    }

    public boolean isMediaExist(String path) {
        return allMediaDao.isMediaExist(path) != 0;
    }

    public void deleteDuplicates() {
        allMediaDao.deleteDuplicates();
    }

    public MediaModel getMediaModelByPath(String path) {
        return allMediaDao.getMediaByPath(path);
    }
}