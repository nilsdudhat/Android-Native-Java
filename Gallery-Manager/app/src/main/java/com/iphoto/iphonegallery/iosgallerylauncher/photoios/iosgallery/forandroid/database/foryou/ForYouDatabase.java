package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.foryou;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities =
        {ForYouDBModel.DayModel.class,
                ForYouDBModel.RecentWeekModel.class,
                ForYouDBModel.MonthModel.class,
                ForYouDBModel.RecentYearModel.class,
                ForYouDBModel.YearModel.class,
                ForYouDBModel.AddressModel.class}, version = 1, exportSchema = false)

public abstract class ForYouDatabase extends RoomDatabase {

    private static ForYouDatabase forYouDatabase;

    public abstract ForYouDao.DayDao daysDao();

    public abstract ForYouDao.RecentWeekDao weeksDao();

    public abstract ForYouDao.MonthDao monthDao();

    public abstract ForYouDao.RecentYearDao recentYearDao();

    public abstract ForYouDao.YearDao yearDao();

    public abstract ForYouDao.AddressDao addressDao();

    public static synchronized ForYouDatabase getInstance(Context context) {
        if (forYouDatabase == null) {
            forYouDatabase =
                    Room.databaseBuilder(
                            context.getApplicationContext(),
                            ForYouDatabase.class,
                            "for_you").fallbackToDestructiveMigration().addCallback(roomCallBack).build();
        }
        return forYouDatabase;
    }

    private static final Callback roomCallBack = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new PopulateDBAsyncTask(forYouDatabase).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDBAsyncTask(ForYouDatabase forYouDatabase) {
            ForYouDao.DayDao dayDao = forYouDatabase.daysDao();
            ForYouDao.RecentWeekDao recentWeekDao = forYouDatabase.weeksDao();
            ForYouDao.MonthDao monthDao = forYouDatabase.monthDao();
            ForYouDao.RecentYearDao recentYearDao = forYouDatabase.recentYearDao();
            ForYouDao.YearDao yearDao = forYouDatabase.yearDao();
            ForYouDao.AddressDao addressDao = forYouDatabase.addressDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}