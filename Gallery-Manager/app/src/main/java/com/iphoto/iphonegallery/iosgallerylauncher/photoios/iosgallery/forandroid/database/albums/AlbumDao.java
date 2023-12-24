package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public class AlbumDao {

    @Dao
    public interface FavoriteDao {

        @Insert
        void insertFavorite(AlbumDBModel.FavoriteDBModel albumDBModel);

        @Update
        void updateFavorite(AlbumDBModel.FavoriteDBModel albumDBModel);

        @Delete
        void deleteFavorite(AlbumDBModel.FavoriteDBModel albumDBModel);

        @Query("SELECT EXISTS(SELECT * FROM favorite_table WHERE path = :path)")
        boolean isFavoriteExist(String path);

        @Query("DELETE FROM favorite_table")
        void deleteAllFavorites();

        @Query("SELECT * FROM favorite_table WHERE path = :path")
        AlbumDBModel.FavoriteDBModel getFavoriteByPath(String path);

//    @Query("SELECT * FROM media_table WHERE parentName = :parentName")
//    List<MediaModel> getListByParentName(String parentName);

        @Query("SELECT * FROM favorite_table ORDER BY dateModified DESC")
        LiveData<List<AlbumDBModel.FavoriteDBModel>> getAllFavoritesData();
    }

    @Dao
    public interface AddressDao {

        @Insert
        void insertAddress(AlbumDBModel.AddressDBModel albumDBModel);

        @Update
        void updateAddress(AlbumDBModel.AddressDBModel albumDBModel);

        @Delete
        void deleteAddress(AlbumDBModel.AddressDBModel albumDBModel);

        @Query("DELETE FROM address_table WHERE id NOT IN (SELECT MIN(id) FROM address_table GROUP BY path)")
        void deleteAddressDuplicates();

        @Query("SELECT EXISTS(SELECT * FROM address_table WHERE address = :city)")
        boolean isCityExist(String city);

        @Query("SELECT EXISTS(SELECT * FROM address_table WHERE path = :path)")
        int isFileModelWithAddressExist(String path);

        @Query("DELETE FROM address_table")
        void deleteAllAddress();

        @Query("SELECT * FROM address_table WHERE path = :path")
        AlbumDBModel.AddressDBModel getAddressByPath(String path);

        @Query("SELECT * FROM address_table ORDER BY id ASC")
        LiveData<List<AlbumDBModel.AddressDBModel>> getAllAddressData();
    }
}