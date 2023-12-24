package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.foryou;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ForYouRepository {

    private final ForYouDao.DayDao daysDao;
    private final ForYouDao.RecentWeekDao recentWeekDao;
    private final ForYouDao.MonthDao monthDao;
    private final ForYouDao.RecentYearDao recentYearDao;
    private final ForYouDao.YearDao yearDao;
    private final ForYouDao.AddressDao addressDao;

    private final LiveData<List<ForYouDBModel.DayModel>> daysData;
    private final LiveData<List<ForYouDBModel.RecentWeekModel>> recentWeekData;
    private final LiveData<List<ForYouDBModel.MonthModel>> monthData;
    private final LiveData<List<ForYouDBModel.RecentYearModel>> recentYearData;
    private final LiveData<List<ForYouDBModel.YearModel>> yearData;
    private final LiveData<List<ForYouDBModel.AddressModel>> addressData;

    public ForYouRepository(Application application) {
        ForYouDatabase forYouDatabase = ForYouDatabase.getInstance(application);

        daysDao = forYouDatabase.daysDao();
        daysData = daysDao.getDaysData();

        recentWeekDao = forYouDatabase.weeksDao();
        recentWeekData = recentWeekDao.getRecentWeeksData();

        monthDao = forYouDatabase.monthDao();
        monthData = monthDao.getMonthsData();

        recentYearDao = forYouDatabase.recentYearDao();
        recentYearData = recentYearDao.getRecentYearsData();

        yearDao = forYouDatabase.yearDao();
        yearData = yearDao.getYearsData();

        addressDao = forYouDatabase.addressDao();
        addressData = addressDao.getAddressData();
    }


    // For Days =====================

    public LiveData<List<ForYouDBModel.DayModel>> getDaysData() {
        return daysData;
    }

    public boolean isDayExist(String title) {
        return daysDao.isDayExist(title);
    }

    public void insertDay(ForYouDBModel.DayModel forYouDBModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                daysDao.insertDay(forYouDBModel);
            }
        }).start();
    }

    public void updateDay(ForYouDBModel.DayModel forYouDBModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                daysDao.updateDay(forYouDBModel);
            }
        }).start();
    }

    public void deleteDay(ForYouDBModel.DayModel forYouDBModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                daysDao.deleteDay(forYouDBModel);
            }
        }).start();
    }

    public ForYouDBModel.DayModel getDayByDate(String date) {
        return daysDao.getDayByDate(date);
    }

    public void deleteAllDays() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                daysDao.deleteAllDays();
            }
        }).start();
    }


    // For Recent Week =========================

    public LiveData<List<ForYouDBModel.RecentWeekModel>> getRecentWeekData() {
        return recentWeekData;
    }

    public boolean isRecentWeekExist(String weekTitle) {
        return recentWeekDao.isRecentWeekExist(weekTitle);
    }

    public void deleteAllRecentWeeks() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                recentWeekDao.deleteAllRecentWeeks();
            }
        }).start();
    }

    public void insertRecentWeek(ForYouDBModel.RecentWeekModel forYouDBModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                recentWeekDao.insertRecentWeek(forYouDBModel);
            }
        }).start();
    }

    public ForYouDBModel.RecentWeekModel getRecentWeekByTitle(String title) {
        return recentWeekDao.getRecentWeekByTitle(title);
    }

    public void updateRecentWeek(ForYouDBModel.RecentWeekModel weekForYouDBModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                recentWeekDao.updateRecentWeek(weekForYouDBModel);
            }
        }).start();
    }

    public void deleteRecentWeek(ForYouDBModel.RecentWeekModel weekForYouDBModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                recentWeekDao.deleteRecentWeek(weekForYouDBModel);
            }
        }).start();
    }


    // For Month ===============================

    public boolean isMonthExist(String month) {
        return monthDao.isMonthExist(month);
    }

    public LiveData<List<ForYouDBModel.MonthModel>> getMonthsData() {
        return monthData;
    }

    public void insertMonth(ForYouDBModel.MonthModel monthForYouDBModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                monthDao.insertMonth(monthForYouDBModel);
            }
        }).start();
    }

    public ForYouDBModel.MonthModel getMonthByMonthTitle(String title) {
        return monthDao.getMonthByMonthTitle(title);
    }

    public void updateMonth(ForYouDBModel.MonthModel monthForYouDBModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                monthDao.updateMonth(monthForYouDBModel);
            }
        }).start();
    }

    public void deleteMonth(ForYouDBModel.MonthModel monthForYouDBModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                monthDao.deleteMonth(monthForYouDBModel);
            }
        }).start();
    }


    // For Recent Year ===========================

    public LiveData<List<ForYouDBModel.RecentYearModel>> getRecentYearData() {
        return recentYearData;
    }

    public boolean isRecentYearExist(String title) {
        return recentYearDao.isRecentYearExist(title);
    }

    public void deleteRecentYear(ForYouDBModel.RecentYearModel recentYearModelForYouDBModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                recentYearDao.deleteRecentYear(recentYearModelForYouDBModel);
            }
        }).start();
    }

    public void deleteAllRecentYear() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                recentYearDao.deleteAllRecentYears();
            }
        }).start();
    }

    public void insertRecentYear(ForYouDBModel.RecentYearModel recentYearModelForYouDBModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                recentYearDao.insertRecentYear(recentYearModelForYouDBModel);
            }
        }).start();
    }

    public ForYouDBModel.RecentYearModel getRecentYearByTitle(String title) {
        return recentYearDao.getRecentYearByTitle(title);
    }

    public void updateRecentYear(ForYouDBModel.RecentYearModel recentYearModelForYouDBModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                recentYearDao.updateRecentYear(recentYearModelForYouDBModel);
            }
        }).start();
    }


    // For Years ==================================

    public boolean isYearExist(String year) {
        return yearDao.isYearExist(year);
    }

    public void insertYear(ForYouDBModel.YearModel yearModelForYouDBModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                yearDao.insertYear(yearModelForYouDBModel);
            }
        }).start();
    }

    public LiveData<List<ForYouDBModel.YearModel>> getYearsData() {
        return yearData;
    }

    public ForYouDBModel.YearModel getYearByTitle(String title) {
        return yearDao.getYearByTitle(title);
    }

    public void updateYear(ForYouDBModel.YearModel yearModelForYouDBModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                yearDao.updateYear(yearModelForYouDBModel);
            }
        }).start();
    }

    public void deleteYear(ForYouDBModel.YearModel yearModelForYouDBModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                yearDao.deleteYear(yearModelForYouDBModel);
            }
        }).start();
    }


    // For Address ==================================

    public boolean isAddressExist(String address) {
        return addressDao.isAddressExist(address);
    }

    public void insertAddress(ForYouDBModel.AddressModel addressModelForYouDBModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                addressDao.insertAddress(addressModelForYouDBModel);
            }
        }).start();
    }

    public LiveData<List<ForYouDBModel.AddressModel>> getAddressData() {
        return addressData;
    }

    public ForYouDBModel.AddressModel getAddressDBModelByAddress(String address) {
        return addressDao.getAddressDBModelByAddress(address);
    }

    public void updateAddress(ForYouDBModel.AddressModel addressModelForYouDBModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                addressDao.updateAddress(addressModelForYouDBModel);
            }
        }).start();
    }

    public void deleteAddress(ForYouDBModel.AddressModel addressModelForYouDBModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                addressDao.deleteAddress(addressModelForYouDBModel);
            }
        }).start();
    }
}