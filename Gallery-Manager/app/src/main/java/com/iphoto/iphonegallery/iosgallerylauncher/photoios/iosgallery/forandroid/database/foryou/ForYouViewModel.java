package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.foryou;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ForYouViewModel extends AndroidViewModel {

    private final ForYouRepository forYouRepository;

    private final LiveData<List<ForYouDBModel.DayModel>> dayData;
    private final LiveData<List<ForYouDBModel.RecentWeekModel>> weekData;
    private final LiveData<List<ForYouDBModel.MonthModel>> monthData;
    private final LiveData<List<ForYouDBModel.RecentYearModel>> recentYearData;
    private final LiveData<List<ForYouDBModel.YearModel>> yearData;
    private final LiveData<List<ForYouDBModel.AddressModel>> addressData;

    public ForYouViewModel(@NonNull Application application) {
        super(application);

        forYouRepository = new ForYouRepository(application);
        dayData = forYouRepository.getDaysData();
        weekData = forYouRepository.getRecentWeekData();
        monthData = forYouRepository.getMonthsData();
        recentYearData = forYouRepository.getRecentYearData();
        yearData = forYouRepository.getYearsData();
        addressData = forYouRepository.getAddressData();
    }


    // For Days =====================================================

    public boolean isDayExist(String title) {
        return forYouRepository.isDayExist(title);
    }

    public void insertDay(ForYouDBModel.DayModel forYouDBModel) {
        forYouRepository.insertDay(forYouDBModel);
    }

    public void updateDay(ForYouDBModel.DayModel forYouDBModel) {
        forYouRepository.updateDay(forYouDBModel);
    }

    public void deleteDay(ForYouDBModel.DayModel forYouDBModel) {
        forYouRepository.deleteDay(forYouDBModel);
    }

    public ForYouDBModel.DayModel getDayByDate(String date) {
        return forYouRepository.getDayByDate(date);
    }

    public void deleteAllDays() {
        forYouRepository.deleteAllDays();
    }

    public LiveData<List<ForYouDBModel.DayModel>> getAllDays() {
        return dayData;
    }


    // For Recent Week ============================

    public LiveData<List<ForYouDBModel.RecentWeekModel>> getAllRecentWeeks() {
        return weekData;
    }

    public void insertRecentWeek(ForYouDBModel.RecentWeekModel forYouDBModel) {
        forYouRepository.insertRecentWeek(forYouDBModel);
    }

    public void updateRecentWeek(ForYouDBModel.RecentWeekModel weekForYouDBModel) {
        forYouRepository.updateRecentWeek(weekForYouDBModel);
    }

    public void deleteRecentWeek(ForYouDBModel.RecentWeekModel weekForYouDBModel) {
        forYouRepository.deleteRecentWeek(weekForYouDBModel);
    }

    public void deleteAllRecentWeek() {
        forYouRepository.deleteAllRecentWeeks();
    }

    public boolean isRecentWeekExist(String weekTitle) {
        return forYouRepository.isRecentWeekExist(weekTitle);
    }

    public ForYouDBModel.RecentWeekModel getRecentWeekByTitle(String title) {
        return forYouRepository.getRecentWeekByTitle(title);
    }


    // For Month ==================================

    public LiveData<List<ForYouDBModel.MonthModel>> getAllMonths() {
        return monthData;
    }

    public void insertMonth(ForYouDBModel.MonthModel monthForYouDBModel) {
        forYouRepository.insertMonth(monthForYouDBModel);
    }

    public void updateMonth(ForYouDBModel.MonthModel monthForYouDBModel) {
        forYouRepository.updateMonth(monthForYouDBModel);
    }

    public void deleteMonth(ForYouDBModel.MonthModel monthForYouDBModel) {
        forYouRepository.deleteMonth(monthForYouDBModel);
    }

    public boolean isMonthExist(String month) {
        return forYouRepository.isMonthExist(month);
    }

    public ForYouDBModel.MonthModel getMonthByMonthTitle(String title) {
        return forYouRepository.getMonthByMonthTitle(title);
    }


    // For Recent Year ===============================

    public LiveData<List<ForYouDBModel.RecentYearModel>> getAllRecentYear() {
        return recentYearData;
    }

    public void insertRecentYear(ForYouDBModel.RecentYearModel recentYearModelForYouDBModel) {
        forYouRepository.insertRecentYear(recentYearModelForYouDBModel);
    }

    public void updateRecentYear(ForYouDBModel.RecentYearModel recentYearModelForYouDBModel) {
        forYouRepository.updateRecentYear(recentYearModelForYouDBModel);
    }

    public boolean isRecentYearExist(String title) {
        return forYouRepository.isRecentYearExist(title);
    }

    public void deleteRecentYear(ForYouDBModel.RecentYearModel recentYearModelForYouDBModel) {
        forYouRepository.deleteRecentYear(recentYearModelForYouDBModel);
    }

    public void deleteAllRecentYear() {
        forYouRepository.deleteAllRecentYear();
    }

    public ForYouDBModel.RecentYearModel getRecentYearByTitle(String title) {
        return forYouRepository.getRecentYearByTitle(title);
    }


    // For Years ====================================

    public LiveData<List<ForYouDBModel.YearModel>> getAllYears() {
        return yearData;
    }

    public boolean isYearExist(String year) {
        return forYouRepository.isYearExist(year);
    }

    public void insertYear(ForYouDBModel.YearModel yearModelForYouDBModel) {
        forYouRepository.insertYear(yearModelForYouDBModel);
    }

    public ForYouDBModel.YearModel getYearByTitle(String title) {
        return forYouRepository.getYearByTitle(title);
    }

    public void updateYear(ForYouDBModel.YearModel yearModelForYouDBModel) {
        forYouRepository.updateYear(yearModelForYouDBModel);
    }

    public void deleteYear(ForYouDBModel.YearModel yearModelForYouDBModel) {
        forYouRepository.deleteYear(yearModelForYouDBModel);
    }


    // For Address ====================================

    public LiveData<List<ForYouDBModel.AddressModel>> getAllAddress() {
        return addressData;
    }

    public boolean isAddressExist(String address) {
        return forYouRepository.isAddressExist(address);
    }

    public void insertAddress(ForYouDBModel.AddressModel addressModelForYouDBModel) {
        forYouRepository.insertAddress(addressModelForYouDBModel);
    }

    public ForYouDBModel.AddressModel getAddressDBModelByAddress(String address) {
        return forYouRepository.getAddressDBModelByAddress(address);
    }

    public void updateAddress(ForYouDBModel.AddressModel addressModelForYouDBModel) {
        forYouRepository.updateAddress(addressModelForYouDBModel);
    }

    public void deleteAddress(ForYouDBModel.AddressModel addressModelForYouDBModel) {
        forYouRepository.deleteAddress(addressModelForYouDBModel);
    }
}
