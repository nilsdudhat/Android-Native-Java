package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.mediastore;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

import androidx.exifinterface.media.ExifInterface;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.database.allmedia.MediaModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.AddressDetailModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.AddressModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.FileModel;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.DateUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.LocationUtils;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util.MathUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MediaLoader {

    public static ArrayList<FileModel> getAllFileModelList(Activity activity) {
        return new ArrayList<>(MediaCursor.getAllMedia(activity));
    }

    public static ArrayList<FileModel> getAllFileList(List<MediaModel> mediaModelArrayList) {
        ArrayList<FileModel> fileModelArrayList = new ArrayList<>();

        for (int i = 0; i < mediaModelArrayList.size(); i++) {
            MediaModel mediaModel = mediaModelArrayList.get(i);

            FileModel fileModel = new FileModel(mediaModel.getFileId(), mediaModel.getPath(), mediaModel.getDateModified(), mediaModel.getFileFormat(), mediaModel.getDuration(), mediaModel.getSize());
            fileModelArrayList.add(fileModel);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH); // your own date format

        Collections.sort(fileModelArrayList, new Comparator<FileModel>() {
            @Override
            public int compare(FileModel fileModel1, FileModel fileModel2) {
                try {
                    return Objects.requireNonNull(simpleDateFormat.parse(fileModel2.getDateModified())).compareTo(simpleDateFormat.parse(fileModel1.getDateModified()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        return fileModelArrayList;
    }

    public static ArrayList<AddressModel> getAddressList(Context context, ArrayList<FileModel> fileModelArrayList) {

        ArrayList<AddressModel> addressModelArrayList = new ArrayList<>();
        List<String> cityList = new ArrayList<>();

        for (int i = 0; i < fileModelArrayList.size(); i++) {
            FileModel fileModel = fileModelArrayList.get(i);

            AddressDetailModel addressDetailModel = null;
            try {
                ExifInterface exifInterface = new ExifInterface(fileModel.getPath());

                // getting lat long values from exif
                String LatLong = LocationUtils.getLatLongFromEXIF(exifInterface);

                if (!LatLong.isEmpty()) {
                    String[] separated = LatLong.split(",");

                    double latitude = Double.parseDouble(separated[0]);
                    double longitude = Double.parseDouble(separated[1]);

                    addressDetailModel = LocationUtils.getAddress(context, latitude, longitude);

                    if (addressDetailModel.getCityName() != null && addressDetailModel.getCountryName() != null &&
                            !addressDetailModel.getCityName().isEmpty() && !addressDetailModel.getCountryName().isEmpty()) {
                        String cityName = addressDetailModel.getCityName() + ", " + addressDetailModel.getCountryName();

                        Log.d("LATLONG--", "Position: " + i + "--- LATLONG: " + LatLong);

                        if (cityList.isEmpty()) {
                            cityList.add(cityName);
                            AddressModel addressModel = new AddressModel();
                            addressModel.setAddress(cityName);

                            ArrayList<FileModel> fileModels = new ArrayList<>();
                            fileModels.add(fileModel);
                            addressModel.setFileModelArrayList(fileModels);

                            addressModelArrayList.add(addressModel);
                        } else {
                            if (!cityList.contains(cityName)) {
                                cityList.add(cityName);

                                AddressModel addressModel = new AddressModel();
                                addressModel.setAddress(cityName);

                                ArrayList<FileModel> fileModels = new ArrayList<>();
                                fileModels.add(fileModel);
                                addressModel.setFileModelArrayList(fileModels);

                                addressModelArrayList.add(addressModel);
                            } else {
                                for (int j = 0; j < addressModelArrayList.size(); j++) {
                                    AddressModel addressModel = addressModelArrayList.get(j);

                                    if (addressModel.getAddress().equals(cityName)) {
                                        ArrayList<FileModel> fileModels = addressModel.getFileModelArrayList();
                                        fileModels.add(fileModel);
                                        addressModel.setFileModelArrayList(fileModels);

                                        addressModelArrayList.set(j, addressModel);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                Log.d("--exif--", "onClick: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return addressModelArrayList;
    }

    public static HashMap<String, HashMap<String, HashMap<String, ArrayList<FileModel>>>> getYearHashMap(ArrayList<MediaModel> mediaModelArrayList) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH); // your own date format

        // Sort the list
        Collections.sort(mediaModelArrayList, new Comparator<MediaModel>() {
            public int compare(MediaModel o1,
                               MediaModel o2) {
                try {
                    return Objects.requireNonNull(simpleDateFormat.parse(o2.getDateModified())).compareTo(simpleDateFormat.parse(o1.getDateModified()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        HashMap<String, HashMap<String, HashMap<String, ArrayList<FileModel>>>> yearHashMap = new HashMap<>();

        for (int i = 0; i < mediaModelArrayList.size(); i++) {
            MediaModel mediaModel = mediaModelArrayList.get(i); // getting MediaModel on i position

            FileModel fileModel = new FileModel(mediaModel.getFileId(),
                    mediaModel.getPath(),
                    mediaModel.getDateModified(),
                    mediaModel.getFileFormat(),
                    mediaModel.getDuration(),
                    mediaModel.getSize()); // creating FileModel with help of MediaModel

            String currentDateFormat = "dd/MM/yyyy - HH:mm:ss"; // date format for end media file

            String year = fileModel.getDateModified();
            String yearFormat = "yyyy"; // year format
            year = DateUtils.convertDateFormat(currentDateFormat, yearFormat, year); // converting date to year format

            String month = fileModel.getDateModified();
            String monthFormat = "MMMM, yyyy"; // month format
            month = DateUtils.convertDateFormat(currentDateFormat, monthFormat, month); // converting date to month format

            String day = fileModel.getDateModified();
            String dayFormat = "dd MMMM, yyyy"; // day format
            day = DateUtils.convertDateFormat(currentDateFormat, dayFormat, day); // converting date to day format

            if (yearHashMap.isEmpty()) { // if yearHashMap is empty

                ArrayList<FileModel> tempFileList = new ArrayList<>(); // creating end array of media files
                tempFileList.add(fileModel); // adding media file to array

                HashMap<String, ArrayList<FileModel>> tempDayHashMap = new HashMap<>(); // creating day hashmap which will have day as key and media files as value
                tempDayHashMap.put(day, tempFileList); // adding day to dayHashMap

                HashMap<String, HashMap<String, ArrayList<FileModel>>> monthHashMap = new HashMap<>(); // creating month hashmap which will have month as key and dash Hashmap as value
                monthHashMap.put(month, tempDayHashMap); // adding month to monthHashMap

                yearHashMap.put(year, monthHashMap); // adding year to yearHashMap
            } else { // if yearHashMap is not empty
                if (yearHashMap.containsKey(year)) { // if year already exist in yearHashMap
                    HashMap<String, HashMap<String, ArrayList<FileModel>>> monthHashMap = yearHashMap.get(year); // getting monthHashMap for given year

                    if (monthHashMap == null) { // if monthHashMap is null
                        monthHashMap = new HashMap<>(); // initiating monthHashMap

                        ArrayList<FileModel> tempFileList = new ArrayList<>(); // creating end media array
                        tempFileList.add(fileModel); // adding media file to end array

                        HashMap<String, ArrayList<FileModel>> tempDayHashMap = new HashMap<>(); // creating dayHashMap
                        tempDayHashMap.put(day, tempFileList); // adding day to dayHashMap

                        monthHashMap.put(month, tempDayHashMap); // adding month to monthHashMap
                    } else { // if monthHashMap is not null
                        if (monthHashMap.containsKey(month)) { // if month is exist in monthHashMap
                            HashMap<String, ArrayList<FileModel>> dayHashMap = monthHashMap.get(month); // getting dayHashMap for given month

                            if (dayHashMap == null) { // if dayHashMap is null
                                dayHashMap = new HashMap<>(); // initiating dayHashMap

                                ArrayList<FileModel> tempFileList = new ArrayList<>(); // creating end media array
                                tempFileList.add(fileModel); // adding media file to end array

                                dayHashMap.put(day, tempFileList); // adding/replacing day in dayHashMap
                            } else { // if dayHashMap is not null
                                ArrayList<FileModel> tempFileList;
                                if (dayHashMap.containsKey(day)) { // if day exist in dayHashMap
                                    tempFileList = dayHashMap.get(day); // getting end media array from dayHashMap

                                    if (tempFileList == null) { // if end media array is null
                                        tempFileList = new ArrayList<>(); // creating end media array
                                    }
                                } else { // if day is not exist in dashHashMap
                                    tempFileList = new ArrayList<>(); // initiating end media array
                                }
                                tempFileList.add(fileModel); // adding media file to end array
                                dayHashMap.put(day, tempFileList); // adding/replacing day to dayHashMap
                            }
                            monthHashMap.put(month, dayHashMap); // replacing month to monthHashMap
                        } else { // if month not exist in monthHashMap
                            ArrayList<FileModel> tempFileList = new ArrayList<>(); // creating end media array
                            tempFileList.add(fileModel); // adding media file to end media array

                            HashMap<String, ArrayList<FileModel>> tempDayHashMap = new HashMap<>(); // creating dayHashMap
                            tempDayHashMap.put(day, tempFileList); // adding day to dayHashMap

                            monthHashMap.put(month, tempDayHashMap); // adding month to monthHashMap
                        }
                    }
                    yearHashMap.put(year, monthHashMap); // replacing year to yearHashMap
                } else { // if year not exist in yearHashMap
                    ArrayList<FileModel> tempFileList = new ArrayList<>(); // creating end media array
                    tempFileList.add(fileModel); // adding media file to end media array

                    HashMap<String, ArrayList<FileModel>> tempDayHashMap = new HashMap<>(); // creating dayHashMap
                    tempDayHashMap.put(day, tempFileList); // adding day to dayHashMap

                    HashMap<String, HashMap<String, ArrayList<FileModel>>> tempMonthHashMap = new HashMap<>(); // creating month to monthHashMap
                    tempMonthHashMap.put(month, tempDayHashMap); // adding month to monthHashMap

                    yearHashMap.put(year, tempMonthHashMap); // adding year to yearHashMap
                }
            }
        }

        return yearHashMap; // returning required yearHashMap with HashMap<year, YearHashMap<month, MonthHashMap<day, DayHashMap<String, ArrayList<FileModel>>>>>
    }

    public static HashMap<String, HashMap<String, ArrayList<FileModel>>> getMonthHashMap(ArrayList<MediaModel> mediaModelArrayList) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss"); // your own date format

        // Sort the list
        Collections.sort(mediaModelArrayList, new Comparator<MediaModel>() {
            public int compare(MediaModel o1,
                               MediaModel o2) {
                try {
                    return Objects.requireNonNull(simpleDateFormat.parse(o2.getDateModified())).compareTo(simpleDateFormat.parse(o1.getDateModified()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        HashMap<String, HashMap<String, ArrayList<FileModel>>> monthHashMap = new HashMap<>();

        for (int i = 0; i < mediaModelArrayList.size(); i++) {
            MediaModel mediaModel = mediaModelArrayList.get(i);

            FileModel fileModel = new FileModel(mediaModel.getFileId(),
                    mediaModel.getPath(),
                    mediaModel.getDateModified(),
                    mediaModel.getFileFormat(),
                    mediaModel.getDuration(),
                    mediaModel.getSize());

            String currentDateFormat = "dd/MM/yyyy - HH:mm:ss";

            String month = fileModel.getDateModified();
            String monthFormat = "MMMM, yyyy";
            month = DateUtils.convertDateFormat(currentDateFormat, monthFormat, month);

            String day = fileModel.getDateModified();
            String dayFormat = "dd MMMM, yyyy";
            day = DateUtils.convertDateFormat(currentDateFormat, dayFormat, day);

            if (monthHashMap.isEmpty()) {

                ArrayList<FileModel> tempFileList = new ArrayList<>();
                tempFileList.add(fileModel);

                HashMap<String, ArrayList<FileModel>> tempDayHashMap = new HashMap<>();
                tempDayHashMap.put(day, tempFileList);

                monthHashMap.put(month, tempDayHashMap);
            } else {
                if (monthHashMap.containsKey(month)) {
                    HashMap<String, ArrayList<FileModel>> tempDayHashMap = monthHashMap.get(month);

                    if (tempDayHashMap != null) {
                        ArrayList<FileModel> tempFileList;
                        if (tempDayHashMap.containsKey(day)) {
                            tempFileList = tempDayHashMap.get(day);

                            if (tempFileList == null) {
                                tempFileList = new ArrayList<>();
                            }
                        } else {
                            tempFileList = new ArrayList<>();
                        }
                        tempFileList.add(fileModel);
                        tempDayHashMap.put(day, tempFileList);
                        monthHashMap.put(month, tempDayHashMap);
                    }
                } else {
                    ArrayList<FileModel> tempFileList = new ArrayList<>();
                    tempFileList.add(fileModel);

                    HashMap<String, ArrayList<FileModel>> tempDayHashMap = new HashMap<>();
                    tempDayHashMap.put(day, tempFileList);

                    monthHashMap.put(month, tempDayHashMap);
                }
            }
        }

        return monthHashMap;
    }

    public static HashMap<String, ArrayList<FileModel>> getDayHashMap(ArrayList<MediaModel> mediaModelArrayList) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH); // your own date format

        // Sort the list
        Collections.sort(mediaModelArrayList, new Comparator<MediaModel>() {
            public int compare(MediaModel o1,
                               MediaModel o2) {
                try {
                    return Objects.requireNonNull(simpleDateFormat.parse(o2.getDateModified())).compareTo(simpleDateFormat.parse(o1.getDateModified()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        HashMap<String, ArrayList<FileModel>> dayHashMap = new HashMap<>();

        for (int i = 0; i < mediaModelArrayList.size(); i++) {

            MediaModel mediaModel = mediaModelArrayList.get(i);

            FileModel fileModel = new FileModel(mediaModel.getFileId(),
                    mediaModel.getPath(),
                    mediaModel.getDateModified(),
                    mediaModel.getFileFormat(),
                    mediaModel.getDuration(),
                    mediaModel.getSize());

            String day = mediaModelArrayList.get(i).getDateModified();

            String currentDateFormat = "dd/MM/yyyy - HH:mm:ss";
            String dayFormat = "dd MMMM, yyyy";

            day = DateUtils.convertDateFormat(currentDateFormat, dayFormat, day);

            ArrayList<FileModel> tempFileList;
            if (dayHashMap.containsKey(day)) {
                tempFileList = new ArrayList<>(Objects.requireNonNull(dayHashMap.get(day)));
            } else {
                tempFileList = new ArrayList<>();
            }
            tempFileList.add(fileModel);
            dayHashMap.put(day, tempFileList);
        }

        return dayHashMap;
    }

    public static ArrayList<FileModel> getAddressForYouList(Activity activity, ArrayList<FileModel> fileModelArrayList) {
        FaceDetector faceDetector = new FaceDetector.Builder(activity)
                .setTrackingEnabled(false)
                .setLandmarkType(com.google.android.gms.vision.face.FaceDetector.ALL_CLASSIFICATIONS)
                .setClassificationType(com.google.android.gms.vision.face.FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmapFactoryOptions.inSampleSize = 16;

        ArrayList<FileModel> imageList = new ArrayList<>();

        for (int i = 0; i < fileModelArrayList.size(); i++) {
            FileModel fileModel = fileModelArrayList.get(i);
//            Log.d("--year--", "getYearImageList: " + fileModel.getDateModified());

            Bitmap bitmap = BitmapFactory.decodeFile(fileModel.getPath(), bitmapFactoryOptions);

            if (bitmap != null) {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<Face> faces = faceDetector.detect(frame);

                if (faces.size() != 0) {
                    Log.d("--address--", "path: " + fileModel.getPath());
                    imageList.add(fileModel);
                }
                bitmap.recycle();
            }
        }

        return imageList;
    }

    public static ArrayList<FileModel> getForYouRandomImageList(ArrayList<FileModel> imageList) {
        ArrayList<FileModel> forYouList = new ArrayList<>();

        int forYouSize = MathUtils.getForYouSize(imageList.size());

        ArrayList<Integer> randomIndexList = new ArrayList<>();

        if (forYouSize != 0) {
            for (int i = 0; i < imageList.size(); i++) {
                if (forYouList.size() == forYouSize) {
                    Collections.sort(forYouList, new Comparator<FileModel>() {
                        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

                        @Override
                        public int compare(FileModel lhs, FileModel rhs) {
                            try {
                                return Objects.requireNonNull(dateFormat.parse(lhs.getDateModified())).compareTo(dateFormat.parse(rhs.getDateModified()));
                            } catch (ParseException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                    });
                    for (int j = 0; j < forYouList.size(); j++) {
                        Log.d("--for_you--", "date: " + forYouList.get(j).getDateModified() + "-----path:" + forYouList.get(j).getPath());
                    }
                } else {
                    int randomIndex = MathUtils.getRandomNumber(0, imageList.size() - 1);

                    if (imageList.isEmpty()) {
                        FileModel fileModel = imageList.get(randomIndex);

                        forYouList.add(fileModel);
                        randomIndexList.add(randomIndex);
                    } else {
                        if (!randomIndexList.contains(randomIndex)) {
                            FileModel fileModel = imageList.get(randomIndex);

                            forYouList.add(fileModel);
                            randomIndexList.add(randomIndex);
                        }
                    }
                }
            }
        }

        return forYouList;
    }

    public static ArrayList<String> getYearsForYouList(ArrayList<FileModel> fileModels) {
        ArrayList<String> tempList = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 0);
        String currentYear = dateFormat.format(cal.getTime());

        for (int i = 0; i < fileModels.size(); i++) {
            FileModel fileModel = fileModels.get(i);

            String date = fileModel.getDateModified();
            String year = DateUtils.convertDateFormat("dd/MM/yyyy - HH:mm:ss", "yyyy", date);

            if (!currentYear.equals(year)) {
                if (tempList.isEmpty()) {
                    tempList.add(year);
                } else {
                    if (!tempList.contains(year)) {
                        tempList.add(year);
                    }
                }
            }
        }

        return tempList;
    }

    public static ArrayList<FileModel> getMonthImageList(Activity activity, String month, ArrayList<FileModel> fileModelArrayList) {
        FaceDetector faceDetector = new FaceDetector.Builder(activity)
                .setTrackingEnabled(false)
                .setLandmarkType(com.google.android.gms.vision.face.FaceDetector.ALL_CLASSIFICATIONS)
                .setClassificationType(com.google.android.gms.vision.face.FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmapFactoryOptions.inSampleSize = 16;

        ArrayList<FileModel> monthImageList = new ArrayList<>();

        String camera_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + File.separator + "Camera";

        for (int i = 0; i < fileModelArrayList.size(); i++) {
            FileModel fileModel = fileModelArrayList.get(i);

            if (fileModel.getPath().startsWith(camera_path)) {
                if (fileModel.getDateModified().contains(month)) {
                    Bitmap bitmap = BitmapFactory.decodeFile(fileModel.getPath(), bitmapFactoryOptions);

                    if (bitmap != null) {
                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                        SparseArray<Face> faces = faceDetector.detect(frame);

                        if (faces.size() != 0) {
                            Log.d("--recent_month--", "path: " + fileModel.getPath());
                            monthImageList.add(fileModel);
                        }
                        bitmap.recycle();
                    }
                }
            }
        }

        return monthImageList;
    }

    public static ArrayList<FileModel> getRecentYearImageList(Activity activity, ArrayList<FileModel> fileModelArrayList) {
        ArrayList<FileModel> tempImageList = new ArrayList<>();

        ArrayList<String> recent12MonthsList = new ArrayList<>(DateUtils.getRecent12MonthsList());

        for (int i = 0; i < recent12MonthsList.size(); i++) {
            ArrayList<FileModel> monthList = new ArrayList<>(MediaLoader.getMonthImageList(activity, recent12MonthsList.get(i), fileModelArrayList));
            tempImageList.addAll(monthList);
        }

        return tempImageList;
    }

    public static ArrayList<FileModel> getDayImageList(Activity activity, String day, ArrayList<FileModel> fileModelArrayList) {
        FaceDetector faceDetector = new FaceDetector.Builder(activity)
                .setTrackingEnabled(false)
                .setLandmarkType(com.google.android.gms.vision.face.FaceDetector.ALL_CLASSIFICATIONS)
                .setClassificationType(com.google.android.gms.vision.face.FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmapFactoryOptions.inSampleSize = 16;

        ArrayList<FileModel> dayImageList = new ArrayList<>();

        String camera_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + File.separator + "Camera";

        for (int i = 0; i < fileModelArrayList.size(); i++) {
            FileModel fileModel = fileModelArrayList.get(i);

            if (fileModel.getPath().startsWith(camera_path)) {
                if (fileModel.getDateModified().startsWith(day)) {
                    Bitmap bitmap = BitmapFactory.decodeFile(fileModel.getPath(), bitmapFactoryOptions);

                    if (bitmap != null) {
                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                        SparseArray<Face> faces = faceDetector.detect(frame);
                        if (faces.size() != 0) {
                            Log.d("--date_modified--", "getYesterdayImageList: " + fileModel.getPath());
                            dayImageList.add(fileModel);
                        }
                        bitmap.recycle();
                    }
                }
            }
        }

        Log.d("--date_modified--", "scan for day: " + day + " done");

        return dayImageList;
    }

    public static ArrayList<FileModel> getRecentWeekImageList(Activity activity, ArrayList<FileModel> fileModelArrayList) {
        ArrayList<FileModel> tempImageList = new ArrayList<>();

        ArrayList<String> recent7DaysList = new ArrayList<>(DateUtils.getRecent7DaysList());

        for (int i = 0; i < recent7DaysList.size(); i++) {
            ArrayList<FileModel> dayList = new ArrayList<>(MediaLoader.getDayImageList(activity, recent7DaysList.get(i), fileModelArrayList));
            tempImageList.addAll(dayList);
        }

        return tempImageList;
    }

    public static ArrayList<FileModel> getYearImageList(Activity activity, String year, ArrayList<FileModel> fileModelArrayList) {
        FaceDetector faceDetector = new FaceDetector.Builder(activity)
                .setTrackingEnabled(false)
                .setLandmarkType(com.google.android.gms.vision.face.FaceDetector.ALL_CLASSIFICATIONS)
                .setClassificationType(com.google.android.gms.vision.face.FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmapFactoryOptions.inSampleSize = 16;

        ArrayList<FileModel> yearImageList = new ArrayList<>();

        String camera_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + File.separator + "Camera";

        for (int i = 0; i < fileModelArrayList.size(); i++) {
            FileModel fileModel = fileModelArrayList.get(i);
//            Log.d("--year--", "getYearImageList: " + fileModel.getDateModified());

            if (fileModel.getPath().startsWith(camera_path)) {
                if (fileModel.getDateModified().contains(year)) {
                    Bitmap bitmap = BitmapFactory.decodeFile(fileModel.getPath(), bitmapFactoryOptions);

                    if (bitmap != null) {
                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                        SparseArray<Face> faces = faceDetector.detect(frame);

                        if (faces.size() != 0) {
                            Log.d("--year--", "path: " + fileModel.getPath());
                            yearImageList.add(fileModel);
                        }
                        bitmap.recycle();
                    }
                }
            }
        }

        return yearImageList;
    }

    public static HashMap<String, ArrayList<FileModel>> getAllAlbumHashMap(ArrayList<FileModel> fileModelList) {
        HashMap<String, ArrayList<FileModel>> tempAlbumHashMap = new HashMap<>();

        if (!fileModelList.isEmpty()) {

            for (int i = 0; i < fileModelList.size(); i++) {
                FileModel fileModel = fileModelList.get(i);

                File file = new File(fileModel.getPath());
                File parentFile = file.getParentFile();

                if (!tempAlbumHashMap.containsKey(Objects.requireNonNull(parentFile).getName())) {
                    ArrayList<FileModel> modelList = new ArrayList<>();
                    modelList.add(fileModel);

                    tempAlbumHashMap.put(parentFile.getName(), modelList);
                } else {
                    ArrayList<FileModel> modelList = tempAlbumHashMap.get(parentFile.getName());
                    if ((modelList == null) || modelList.isEmpty()) {
                        modelList = new ArrayList<>();
                    }
                    modelList.add(fileModel);
                    tempAlbumHashMap.put(parentFile.getName(), modelList);
                }
            }
        }

        Object[] albumNameList = tempAlbumHashMap.keySet().toArray();
        String[] strDirectories = {"WhatsApp", "Telegram"};

        for (Object o : albumNameList) {
            String albumName = (String) o;

            for (String strDirectory : strDirectories) {
                if (albumName.contains(strDirectory)) {
                    if (tempAlbumHashMap.containsKey(strDirectory)) {
                        ArrayList<FileModel> modelList = tempAlbumHashMap.get(strDirectory);

                        if (modelList == null) {
                            modelList = new ArrayList<>();
                        }

                        ArrayList<FileModel> fileModels = tempAlbumHashMap.get(albumName);

                        if (fileModels == null) {
                            fileModels = new ArrayList<>();
                        }

                        modelList.addAll(fileModels);

                        tempAlbumHashMap.put(strDirectory, modelList);
                        tempAlbumHashMap.remove(albumName);
                        break;
                    } else {
                        ArrayList<FileModel> fileModels = tempAlbumHashMap.get(albumName);

                        if (fileModels == null) {
                            fileModels = new ArrayList<>();
                        }

                        tempAlbumHashMap.put(strDirectory, fileModels);
                        tempAlbumHashMap.remove(albumName);
                        break;
                    }
                }
            }
        }

        return tempAlbumHashMap;
    }

    public static ArrayList<FileModel> getVideosList(ArrayList<FileModel> fileModelList) {
        ArrayList<FileModel> videosList = new ArrayList<>();

        for (int i = 0; i < fileModelList.size(); i++) {
            FileModel fileModel = fileModelList.get(i);

            if (fileModel.getFileFormat().startsWith("video")) {
                videosList.add(fileModel);
            }
        }

        return videosList;
    }

    public static ArrayList<FileModel> getListFromStringMatch(String folder, ArrayList<FileModel> fileModelArrayList) {
        ArrayList<FileModel> tempCameraList = new ArrayList<>();

        for (int i = 0; i < fileModelArrayList.size(); i++) {
            FileModel fileModel = fileModelArrayList.get(i);

            if (fileModel.getPath().contains(folder)) {
                tempCameraList.add(fileModel);
            }
        }

        return tempCameraList;
    }
}
