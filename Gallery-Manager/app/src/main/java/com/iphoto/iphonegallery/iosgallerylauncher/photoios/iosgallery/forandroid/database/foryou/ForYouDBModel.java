package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.foryou;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class ForYouDBModel {

    @Entity(tableName = "day_for_you")
    public static class DayModel {

        @PrimaryKey(autoGenerate = true)
        private int id;

        String title;
        int fileCount;
        String pathList;

        public DayModel(String title, int fileCount, String pathList) {
            this.title = title;
            this.fileCount = fileCount;
            this.pathList = pathList;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getFileCount() {
            return fileCount;
        }

        public void setFileCount(int fileCount) {
            this.fileCount = fileCount;
        }

        public String getPathList() {
            return pathList;
        }

        public void setPathList(String pathList) {
            this.pathList = pathList;
        }
    }

    @Entity(tableName = "recent_week_for_you")
    public static class RecentWeekModel {

        @PrimaryKey(autoGenerate = true)
        private int id;

        String title;
        int fileCount;
        String pathList;

        public RecentWeekModel(String title, int fileCount, String pathList) {
            this.title = title;
            this.fileCount = fileCount;
            this.pathList = pathList;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getFileCount() {
            return fileCount;
        }

        public void setFileCount(int fileCount) {
            this.fileCount = fileCount;
        }

        public String getPathList() {
            return pathList;
        }

        public void setPathList(String pathList) {
            this.pathList = pathList;
        }
    }

    @Entity(tableName = "month_for_you")
    public static class MonthModel {

        @PrimaryKey(autoGenerate = true)
        private int id;

        String title;
        int fileCount;
        String pathList;

        public MonthModel(String title, int fileCount, String pathList) {
            this.title = title;
            this.fileCount = fileCount;
            this.pathList = pathList;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getFileCount() {
            return fileCount;
        }

        public void setFileCount(int fileCount) {
            this.fileCount = fileCount;
        }

        public String getPathList() {
            return pathList;
        }

        public void setPathList(String pathList) {
            this.pathList = pathList;
        }
    }

    @Entity(tableName = "recent_year_for_you")
    public static class RecentYearModel {

        @PrimaryKey(autoGenerate = true)
        private int id;

        String title;
        int fileCount;
        String pathList;

        public RecentYearModel(String title, int fileCount, String pathList) {
            this.title = title;
            this.fileCount = fileCount;
            this.pathList = pathList;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getFileCount() {
            return fileCount;
        }

        public void setFileCount(int fileCount) {
            this.fileCount = fileCount;
        }

        public String getPathList() {
            return pathList;
        }

        public void setPathList(String pathList) {
            this.pathList = pathList;
        }
    }

    @Entity(tableName = "year_for_you")
    public static class YearModel {

        @PrimaryKey(autoGenerate = true)
        private int id;

        String title;
        int fileCount;
        String pathList;

        public YearModel(String title, int fileCount, String pathList) {
            this.title = title;
            this.fileCount = fileCount;
            this.pathList = pathList;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPathList() {
            return pathList;
        }

        public void setPathList(String pathList) {
            this.pathList = pathList;
        }
    }

    @Entity(tableName = "address_for_you")
    public static class AddressModel {

        @PrimaryKey(autoGenerate = true)
        private int id;

        String address;
        int fileCount;
        String pathList;

        public AddressModel(String address, int fileCount, String pathList) {
            this.address = address;
            this.fileCount = fileCount;
            this.pathList = pathList;
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

        public int getFileCount() {
            return fileCount;
        }

        public void setFileCount(int fileCount) {
            this.fileCount = fileCount;
        }

        public String getPathList() {
            return pathList;
        }

        public void setPathList(String pathList) {
            this.pathList = pathList;
        }
    }
}
