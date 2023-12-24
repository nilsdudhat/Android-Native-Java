package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofiles;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class VideosRepository {

    private final VideosDao videosDao;

    private final LiveData<List<VideoModel>> videosList;

    VideosRepository(Application application) {
        VideosDatabase videosDatabase = VideosDatabase.getInstance(application);
        videosDao = videosDatabase.videosDao();
        videosList = videosDao.getVideos();
    }

    public void insert(VideoModel videoModel) {
        videosDao.insert(videoModel);
    }

    public VideoModel getSingleDataByPath(String path) {
        return videosDao.getSingleDataByPath(path);
    }

    public List<VideoModel> getListByParentName(String parentName) {
        return videosDao.getListByParentName(parentName);
    }

    public LiveData<List<VideoModel>> getVideosList() {
        return videosList;
    }

    public boolean isFileExist(String path) {
        return videosDao.isVideoModelExist(path);
    }


    public void update(VideoModel videoModel) {
        videosDao.update(videoModel);
    }

    public void delete(VideoModel videoModel) {
        videosDao.delete(videoModel);
    }

    public void deleteAllVideos() {
        videosDao.deleteAllVideos();
    }

}