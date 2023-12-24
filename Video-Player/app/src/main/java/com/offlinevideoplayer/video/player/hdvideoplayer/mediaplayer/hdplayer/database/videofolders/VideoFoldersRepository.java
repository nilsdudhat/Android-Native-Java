package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.database.videofolders;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class VideoFoldersRepository {

    private final VideoFoldersDao videoFoldersDao;

    private final LiveData<List<VideoFolderModel>> videoFoldersList;

    VideoFoldersRepository(Application application) {
        VideoFoldersDatabase videoFoldersDatabase = VideoFoldersDatabase.getInstance(application);
        videoFoldersDao = videoFoldersDatabase.videoFoldersDao();
        videoFoldersList = videoFoldersDao.getAllVideoFolders();
    }

    public void insert(VideoFolderModel videoFolderModel) {
        new InsertVideoFolderAsyncTask(videoFoldersDao).execute(videoFolderModel);
    }

    public VideoFolderModel getSingleRowDataByPath(String path) {
        return videoFoldersDao.getSingleRowData(path);
    }

    public void update(VideoFolderModel videoFolderModel) {
        new UpdateVideoFolderAsync(videoFoldersDao).execute(videoFolderModel);
    }

    public void delete(VideoFolderModel videoFolderModel) {
        new DeleteVideoFolderAsync(videoFoldersDao).execute(videoFolderModel);
    }

    public void deleteAllVideoFolders() {
        new DeleteAllVideoFolders(videoFoldersDao).execute();
    }

    public LiveData<List<VideoFolderModel>> getVideoFoldersList() {
        return videoFoldersList;
    }

    private static class InsertVideoFolderAsyncTask extends AsyncTask<VideoFolderModel, Void, Void> {

        private final VideoFoldersDao videoFoldersDao;

        public InsertVideoFolderAsyncTask(VideoFoldersDao videoFoldersDao) {
            this.videoFoldersDao = videoFoldersDao;
        }

        @Override
        protected Void doInBackground(VideoFolderModel... videoFolderModels) {
            videoFoldersDao.insert(videoFolderModels[0]);
            return null;
        }
    }

    private static class UpdateVideoFolderAsync extends AsyncTask<VideoFolderModel, Void, Void> {

        private VideoFoldersDao videoFoldersDao;

        public UpdateVideoFolderAsync(VideoFoldersDao videoFoldersDao) {
            this.videoFoldersDao = videoFoldersDao;
        }

        @Override
        protected Void doInBackground(VideoFolderModel... videoFolderModels) {
            videoFoldersDao.update(videoFolderModels[0]);
            return null;
        }
    }

    private static class DeleteVideoFolderAsync extends AsyncTask<VideoFolderModel, Void, Void> {

        VideoFoldersDao videoFoldersDao;

        public DeleteVideoFolderAsync(VideoFoldersDao videoFoldersDao) {
            this.videoFoldersDao = videoFoldersDao;
        }

        @Override
        protected Void doInBackground(VideoFolderModel... videoFolderModels) {
            videoFoldersDao.delete(videoFolderModels[0]);
            return null;
        }
    }

    private static class DeleteAllVideoFolders extends AsyncTask<Void, Void, Void> {

        VideoFoldersDao videoFoldersDao;

        public DeleteAllVideoFolders(VideoFoldersDao videoFoldersDao) {
            this.videoFoldersDao = videoFoldersDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            videoFoldersDao.deleteAllVideoFolders();
            return null;
        }
    }
}
