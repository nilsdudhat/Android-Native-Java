package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofolders;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class VideoFoldersViewModel extends AndroidViewModel {

    private final VideoFoldersRepository videoFoldersRepository;

    private final LiveData<List<VideoFolderModel>> videoFoldersList;

    public VideoFoldersViewModel(@NonNull Application application) {
        super(application);

        videoFoldersRepository = new VideoFoldersRepository(application);
        videoFoldersList = videoFoldersRepository.getVideoFoldersList();
    }

    public void insert(VideoFolderModel videoFolderModel) {
        videoFoldersRepository.insert(videoFolderModel);
    }

    public void update(VideoFolderModel videoFolderModel) {
        videoFoldersRepository.update(videoFolderModel);
    }

    public void delete(VideoFolderModel videoFolderModel) {
        videoFoldersRepository.delete(videoFolderModel);
    }

    public VideoFolderModel getSingleRowDataByPath(String path) {
        return videoFoldersRepository.getSingleRowDataByPath(path);
    }

    public void deleteAllVideoFolders() {
        videoFoldersRepository.deleteAllVideoFolders();
    }

    public LiveData<List<VideoFolderModel>> getAllVideoFolders() {
        return videoFoldersList;
    }
}
