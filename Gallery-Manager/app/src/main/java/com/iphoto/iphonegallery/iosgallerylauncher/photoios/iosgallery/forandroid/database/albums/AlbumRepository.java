package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AlbumRepository {

    private final AlbumDao.FavoriteDao favoriteDao;
    private final AlbumDao.AddressDao addressDao;

    private final LiveData<List<AlbumDBModel.FavoriteDBModel>> favoriteMediaData;
    private final LiveData<List<AlbumDBModel.AddressDBModel>> addressData;

    AlbumRepository(Application application) {
        AlbumDatabase albumDatabase = AlbumDatabase.getInstance(application);

        // for favorites
        favoriteDao = albumDatabase.favoriteDao();
        favoriteMediaData = favoriteDao.getAllFavoritesData();

        // for address

        addressDao = albumDatabase.addressDao();
        addressData = addressDao.getAllAddressData();
    }


    // For Favorites ==========================

    public void insertFavorite(AlbumDBModel.FavoriteDBModel albumDBModel) {
        favoriteDao.insertFavorite(albumDBModel);
    }

    public void updateFavorite(AlbumDBModel.FavoriteDBModel albumDBModel) {
        favoriteDao.updateFavorite(albumDBModel);
    }

    public void deleteFavorite(AlbumDBModel.FavoriteDBModel albumDBModel) {
        favoriteDao.deleteFavorite(albumDBModel);
    }

    public void deleteAllFavorites() {
        favoriteDao.deleteAllFavorites();
    }

    public boolean isFavoriteExist(String path) {
        return favoriteDao.isFavoriteExist(path);
    }

    public AlbumDBModel.FavoriteDBModel getFavoriteByPath(String path) {
        return favoriteDao.getFavoriteByPath(path);
    }

    public LiveData<List<AlbumDBModel.FavoriteDBModel>> getFavoritesData() {
        return favoriteMediaData;
    }

    //    public List<AlbumDBModel.FavoriteDBModel> getListByParentName(String parentName) {
//        return favoriteDao.getListByParentName(parentName);
//    }


    // For Address ====================

    public LiveData<List<AlbumDBModel.AddressDBModel>> getAddressData() {
        return addressData;
    }

    public void updateAddress(AlbumDBModel.AddressDBModel albumDBModel) {
        addressDao.updateAddress(albumDBModel);
    }

    public void deleteAddress(AlbumDBModel.AddressDBModel albumDBModel) {
        addressDao.deleteAddress(albumDBModel);
    }

    public void deleteAddressDuplicates() {
        addressDao.deleteAddressDuplicates();
    }

    public void deleteAllAddress() {
        addressDao.deleteAllAddress();
    }

    public boolean isCityExist(String city) {
        return addressDao.isCityExist(city);
    }

    public boolean isFileModelWithAddressExist(String path) {
        return addressDao.isFileModelWithAddressExist(path) != 0;
    }

    public AlbumDBModel.AddressDBModel getAddressByPath(String path) {
        return addressDao.getAddressByPath(path);
    }

    public void insertAddress(AlbumDBModel.AddressDBModel albumDBModel) {
        addressDao.insertAddress(albumDBModel);
    }
}