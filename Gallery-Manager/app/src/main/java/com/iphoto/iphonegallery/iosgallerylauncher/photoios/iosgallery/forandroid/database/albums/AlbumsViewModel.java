package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AlbumsViewModel extends AndroidViewModel {

    private final AlbumRepository albumRepository;

    private final LiveData<List<AlbumDBModel.FavoriteDBModel>> favoritesData;
    private final LiveData<List<AlbumDBModel.AddressDBModel>> addressData;

    public AlbumsViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository(application);

        // for favorites
        favoritesData = albumRepository.getFavoritesData();

        // for address
        addressData = albumRepository.getAddressData();
    }


    // for Favorite =========================

    public LiveData<List<AlbumDBModel.FavoriteDBModel>> getAllFavorites() {
        return favoritesData;
    }

    public void deleteAllFavorites() {
        albumRepository.deleteAllFavorites();
    }

    public void insertFavorite(AlbumDBModel.FavoriteDBModel albumDBModel) {
        albumRepository.insertFavorite(albumDBModel);
    }

    public void updateFavorite(AlbumDBModel.FavoriteDBModel albumDBModel) {
        albumRepository.updateFavorite(albumDBModel);
    }

    public void deleteFavorite(AlbumDBModel.FavoriteDBModel albumDBModel) {
        albumRepository.deleteFavorite(albumDBModel);
    }

    public boolean isFavoriteExist(String path) {
        return albumRepository.isFavoriteExist(path);
    }

    public AlbumDBModel.FavoriteDBModel getFavoriteByPath(String path) {
        return albumRepository.getFavoriteByPath(path);
    }

//    public List<FavoriteModel> getListByParentName(String parentName) {
//        return favoriteRepository.getListByParentName(parentName);
//    }


    // for Address ============================

    public LiveData<List<AlbumDBModel.AddressDBModel>> getAllAddressData() {
        return addressData;
    }

    public void deleteAllAddress() {
        albumRepository.deleteAllAddress();
    }

    public void insertAddress(AlbumDBModel.AddressDBModel albumDBModel) {
        albumRepository.insertAddress(albumDBModel);
    }

    public void updateAddress(AlbumDBModel.AddressDBModel albumDBModel) {
        albumRepository.updateAddress(albumDBModel);
    }

    public void deleteAddress(AlbumDBModel.AddressDBModel albumDBModel) {
        albumRepository.deleteAddress(albumDBModel);
    }

    public void deleteAddressDuplicates() {
        albumRepository.deleteAddressDuplicates();
    }

    public boolean isCityExist(String city) {
        return albumRepository.isCityExist(city);
    }

    public boolean isFileModelWithAddressExist(String path) {
        return albumRepository.isFileModelWithAddressExist(path);
    }

    public AlbumDBModel.AddressDBModel getAddressByPath(String path) {
        return albumRepository.getAddressByPath(path);
    }
}