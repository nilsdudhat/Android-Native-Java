package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.albums;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

public class AlbumDBModel implements Serializable {

    @Entity(tableName = "favorite_table")
    public static class FavoriteDBModel implements Serializable {

        @PrimaryKey(autoGenerate = true)
        private int id;

        String fileId;
        String path;
        String dateModified;
        String fileFormat;
        String duration;
        String size;

        public FavoriteDBModel(String fileId, String path, String dateModified, String fileFormat, String duration, String size) {
            this.fileId = fileId;
            this.path = path;
            this.dateModified = dateModified;
            this.fileFormat = fileFormat;
            this.duration = duration;
            this.size = size;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getDateModified() {
            return dateModified;
        }

        public void setDateModified(String dateModified) {
            this.dateModified = dateModified;
        }

        public String getFileFormat() {
            return fileFormat;
        }

        public void setFileFormat(String fileFormat) {
            this.fileFormat = fileFormat;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }

    @Entity(tableName = "address_table")
    public static class AddressDBModel {

        @PrimaryKey(autoGenerate = true)
        private int id;

        String address;
        String path;
        String fileModel;
        String addressDetailModel;

        public AddressDBModel(String address, String path, String fileModel, String addressDetailModel) {
            this.address = address;
            this.path = path;
            this.fileModel = fileModel;
            this.addressDetailModel = addressDetailModel;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getFileModel() {
            return fileModel;
        }

        public void setFileModel(String fileModel) {
            this.fileModel = fileModel;
        }

        public String getAddressDetailModel() {
            return addressDetailModel;
        }

        public void setAddressDetailModel(String addressDetailModel) {
            this.addressDetailModel = addressDetailModel;
        }
    }
}