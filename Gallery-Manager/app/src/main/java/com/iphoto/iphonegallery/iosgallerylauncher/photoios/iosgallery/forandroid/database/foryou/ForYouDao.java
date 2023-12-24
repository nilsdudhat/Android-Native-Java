package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.foryou;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public class ForYouDao {

    @Dao
    public interface DayDao {

        @Insert
        void insertDay(ForYouDBModel.DayModel dayModel);

        @Update
        void updateDay(ForYouDBModel.DayModel dayModel);

        @Delete
        void deleteDay(ForYouDBModel.DayModel dayModel);

        @Query("SELECT EXISTS(SELECT * FROM day_for_you WHERE title = :title)")
        boolean isDayExist(String title);

        @Query("DELETE FROM day_for_you")
        void deleteAllDays();

        @Query("SELECT * FROM day_for_you WHERE title = :title")
        ForYouDBModel.DayModel getDayByDate(String title);

        @Query("SELECT * FROM day_for_you ORDER BY title DESC")
        LiveData<List<ForYouDBModel.DayModel>> getDaysData();
    }

    @Dao
    public interface RecentWeekDao {

        @Insert
        void insertRecentWeek(ForYouDBModel.RecentWeekModel recentWeekModel);

        @Update
        void updateRecentWeek(ForYouDBModel.RecentWeekModel recentWeekModel);

        @Delete
        void deleteRecentWeek(ForYouDBModel.RecentWeekModel recentWeekModel);

        @Query("SELECT EXISTS(SELECT * FROM recent_week_for_you WHERE title = :title)")
        boolean isRecentWeekExist(String title);

        @Query("DELETE FROM recent_week_for_you")
        void deleteAllRecentWeeks();

        @Query("SELECT * FROM recent_week_for_you WHERE title = :title")
        ForYouDBModel.DayModel getRecentWeekByDate(String title);

        @Query("SELECT * FROM recent_week_for_you ORDER BY title DESC")
        LiveData<List<ForYouDBModel.RecentWeekModel>> getRecentWeeksData();

        @Query("SELECT * FROM recent_week_for_you WHERE title = :title")
        ForYouDBModel.RecentWeekModel getRecentWeekByTitle(String title);
    }

    @Dao
    public interface MonthDao {

        @Insert
        void insertMonth(ForYouDBModel.MonthModel monthModel);

        @Update
        void updateMonth(ForYouDBModel.MonthModel monthModel);

        @Delete
        void deleteMonth(ForYouDBModel.MonthModel monthModel);

        @Query("SELECT EXISTS(SELECT * FROM month_for_you WHERE title = :title)")
        boolean isMonthExist(String title);

        @Query("DELETE FROM month_for_you")
        void deleteAllMonths();

        @Query("SELECT * FROM month_for_you WHERE title = :title")
        ForYouDBModel.MonthModel getMonthByMonthTitle(String title);

        @Query("SELECT * FROM month_for_you ORDER BY title DESC")
        LiveData<List<ForYouDBModel.MonthModel>> getMonthsData();
    }

    @Dao
    public interface RecentYearDao {

        @Insert
        void insertRecentYear(ForYouDBModel.RecentYearModel recentYearModel);

        @Update
        void updateRecentYear(ForYouDBModel.RecentYearModel recentYearModel);

        @Delete
        void deleteRecentYear(ForYouDBModel.RecentYearModel recentYearModel);

        @Query("SELECT EXISTS(SELECT * FROM recent_year_for_you WHERE title = :title)")
        boolean isRecentYearExist(String title);

        @Query("DELETE FROM recent_year_for_you")
        void deleteAllRecentYears();

        @Query("SELECT * FROM recent_year_for_you WHERE title = :title")
        ForYouDBModel.RecentYearModel getRecentYearByTitle(String title);

        @Query("SELECT * FROM recent_year_for_you ORDER BY title DESC")
        LiveData<List<ForYouDBModel.RecentYearModel>> getRecentYearsData();
    }

    @Dao
    public interface YearDao {

        @Insert
        void insertYear(ForYouDBModel.YearModel yearModel);

        @Update
        void updateYear(ForYouDBModel.YearModel yearModel);

        @Delete
        void deleteYear(ForYouDBModel.YearModel yearModel);

        @Query("SELECT EXISTS(SELECT * FROM year_for_you WHERE title = :title)")
        boolean isYearExist(String title);

        @Query("DELETE FROM year_for_you")
        void deleteAllYears();

        @Query("SELECT * FROM year_for_you WHERE title = :title")
        ForYouDBModel.YearModel getYearByTitle(String title);

        @Query("SELECT * FROM year_for_you ORDER BY title DESC")
        LiveData<List<ForYouDBModel.YearModel>> getYearsData();
    }

    @Dao
    public interface AddressDao {

        @Insert
        void insertAddress(ForYouDBModel.AddressModel addressModel);

        @Update
        void updateAddress(ForYouDBModel.AddressModel addressModel);

        @Delete
        void deleteAddress(ForYouDBModel.AddressModel addressModel);

        @Query("SELECT EXISTS(SELECT * FROM address_for_you WHERE address = :address)")
        boolean isAddressExist(String address);

        @Query("DELETE FROM address_for_you")
        void deleteAllAddress();

        @Query("SELECT * FROM address_for_you WHERE address = :address")
        ForYouDBModel.AddressModel getAddressDBModelByAddress(String address);

        @Query("SELECT * FROM address_for_you")
        LiveData<List<ForYouDBModel.AddressModel>> getAddressData();
    }
}
