package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AllMediaViewModel extends AndroidViewModel {

    AllMediaRepository allMediaRepository;

    public AllMediaViewModel(@NonNull Application application) {
        super(application);

        allMediaRepository = new AllMediaRepository(application);
    }

    public LiveData<List<MediaModel>> getAllMediaData() {
        return allMediaRepository.getAllMediaData();
    }

    public void insertMedia(MediaModel mediaModel) {
        allMediaRepository.insertMedia(mediaModel);
    }

    public void deleteMedia(MediaModel mediaModel) {
        allMediaRepository.deleteMedia(mediaModel);
    }

    public boolean isMediaExist(String path) {
        return allMediaRepository.isMediaExist(path);
    }

    public MediaModel getMediaModelByPath(String path) {
        return allMediaRepository.getMediaModelByPath(path);
    }

    public void deleteDuplicates() {
        allMediaRepository.deleteDuplicates();
    }
}
