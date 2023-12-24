package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities =
        {AlbumDBModel.FavoriteDBModel.class,
                AlbumDBModel.AddressDBModel.class}, version = 3, exportSchema = false)
public abstract class AlbumDatabase extends RoomDatabase {

    private static AlbumDatabase albumDatabase;

    public abstract AlbumDao.FavoriteDao favoriteDao();

    public abstract AlbumDao.AddressDao addressDao();

    public static synchronized AlbumDatabase getInstance(Context context) {
        if (albumDatabase == null) {
            albumDatabase =
                    Room.databaseBuilder(
                            context.getApplicationContext(),
                            AlbumDatabase.class,
                            "albums").fallbackToDestructiveMigration().addCallback(roomCallBack).build();
        }
        return albumDatabase;
    }

    private static final Callback roomCallBack = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new PopulateDBAsyncTask(albumDatabase).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDBAsyncTask(AlbumDatabase albumDatabase) {
            AlbumDao.FavoriteDao albumDao = albumDatabase.favoriteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
