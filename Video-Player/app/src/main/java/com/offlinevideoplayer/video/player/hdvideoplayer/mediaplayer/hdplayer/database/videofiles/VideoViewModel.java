package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class VideoViewModel extends AndroidViewModel {

    private final VideosRepository videosRepository;

    private final LiveData<List<VideoModel>> videosList;

    public VideoViewModel(@NonNull Application application) {
        super(application);

        videosRepository = new VideosRepository(application);
        videosList = videosRepository.getVideosList();
    }

    public void insert(VideoModel videoModel) {
        videosRepository.insert(videoModel);
    }

    public void update(VideoModel videoModel) {
        videosRepository.update(videoModel);
    }

    public void delete(VideoModel videoModel) {
        videosRepository.delete(videoModel);
    }

    public VideoModel getSingleDataByPath(String path) {
        return videosRepository.getSingleDataByPath(path);
    }

    public List<VideoModel> getListByParentName(String parentName) {
        return videosRepository.getListByParentName(parentName);
    }

    public void deleteAllVideos() {
        videosRepository.deleteAllVideos();
    }

    public LiveData<List<VideoModel>> getAllVideos() {
        return videosList;
    }

    public boolean isFileExist(String path) {
        return videosRepository.isFileExist(path);
    }
}
