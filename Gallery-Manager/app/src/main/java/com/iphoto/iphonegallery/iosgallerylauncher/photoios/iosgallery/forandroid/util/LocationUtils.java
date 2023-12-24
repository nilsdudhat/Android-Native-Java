package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.exifinterface.media.ExifInterface;

import com.google.gson.Gson;
import com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.model.AddressDetailModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtils {

    public static String getLatLongFromEXIF(ExifInterface exif) {

        String LATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        String LATITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
        String LONGITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        String LONGITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

        // your Final lat Long Values
        Float Latitude, Longitude;

        if ((LATITUDE != null)
                && (LATITUDE_REF != null)
                && (LONGITUDE != null)
                && (LONGITUDE_REF != null)) {

            if (LATITUDE_REF.equals("N")) {
                Latitude = convertToDegree(LATITUDE);
            } else {
                Latitude = 0 - convertToDegree(LATITUDE);
            }

            if (LONGITUDE_REF.equals("E")) {
                Longitude = convertToDegree(LONGITUDE);
            } else {
                Longitude = 0 - convertToDegree(LONGITUDE);
            }

            return Latitude + "," + Longitude;
        } else {
            return "";
        }
    }

    public static AddressDetailModel getAddress(Context context, double lat, double lng) {
        AddressDetailModel addressDetailModel = new AddressDetailModel();

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

            if (!addresses.isEmpty()) {

                Log.d("--list_address--", "getAddress: " + "list_address" + new Gson().toJson(addresses));
                Address obj = addresses.get(0);

                if (obj.getAddressLine(0) != null && !obj.getAddressLine(0).isEmpty()) {
                    addressDetailModel.setAddressLine(obj.getAddressLine(0));
                }
                if (obj.getSubLocality() != null && !obj.getLocality().isEmpty()) {
                    addressDetailModel.setAreaName(obj.getSubLocality());
                }
                if (obj.getSubAdminArea() != null && !obj.getSubAdminArea().isEmpty()) {
                    addressDetailModel.setCityName(obj.getSubAdminArea());
                }
                if (obj.getAdminArea() != null && !obj.getAdminArea().isEmpty()) {
                    addressDetailModel.setStateName(obj.getAdminArea());
                }
                if (obj.getCountryName() != null && !obj.getCountryName().isEmpty()) {
                    addressDetailModel.setCountryName(obj.getCountryName());
                }
                if (obj.getPostalCode() != null && !obj.getPostalCode().isEmpty()) {
                    addressDetailModel.setPostalCode(obj.getPostalCode());
                }

                Log.d("--address--", "Address" + new Gson().toJson(addressDetailModel));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressDetailModel;
    }

    private static Float convertToDegree(String stringDMS) {
        float result;

        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = Double.valueOf(stringD[0]);
        Double D1 = Double.valueOf(stringD[1]);
        double FloatD = D0 / D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = Double.valueOf(stringM[0]);
        Double M1 = Double.valueOf(stringM[1]);
        double FloatM = M0 / M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = Double.valueOf(stringS[0]);
        Double S1 = Double.valueOf(stringS[1]);
        double FloatS = S0 / S1;

        result = (float) (FloatD + (FloatM / 60) + (FloatS / 3600));

        return result;
    }

    public static AddressDetailModel getAddressDetailModel(Activity activity, String path) {

        AddressDetailModel addressDetailModel = null;
        try {
            ExifInterface exifInterface = new ExifInterface(path);

            // getting lat long values from exif
            String LatLong = LocationUtils.getLatLongFromEXIF(exifInterface);

            if (!LatLong.isEmpty()) {
                String[] separated = LatLong.split(",");

                double latitude = Double.parseDouble(separated[0]);
                double longitude = Double.parseDouble(separated[1]);

                addressDetailModel = LocationUtils.getAddress(activity, latitude, longitude);

                return addressDetailModel;
            }
        } catch (IOException e) {
            Log.d("--exif--", "onClick: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public static String getAddressForPlaces(AddressDetailModel addressDetailModel) {
        String address = "";

        if (addressDetailModel.getAreaName() != null && !addressDetailModel.getAreaName().isEmpty()) {
            address = address + addressDetailModel.getAreaName();
        }

        if (addressDetailModel.getCityName() != null && !addressDetailModel.getCityName().isEmpty()) {
            if (address.isEmpty()) {
                address = address + addressDetailModel.getCityName();
            } else {
                address = address + ", " + addressDetailModel.getCityName();
            }
        }

        if (addressDetailModel.getStateName() != null && !addressDetailModel.getStateName().isEmpty()) {
            if (address.isEmpty()) {
                address = address + addressDetailModel.getStateName();
            } else {
                address = address + ", " + addressDetailModel.getStateName();
            }
        }

        if (addressDetailModel.getCountryName() != null && !addressDetailModel.getCountryName().isEmpty()) {
            if (address.isEmpty()) {
                address = address + addressDetailModel.getCountryName();
            } else {
                address = address + ", " + addressDetailModel.getCountryName();
            }
        }

        return address;
    }

    public static String getAddressForYou(Activity activity, String path) {

        String address = "";

        AddressDetailModel addressDetailModel = null;
        try {
            ExifInterface exifInterface = new ExifInterface(path);

            // getting lat long values from exif
            String LatLong = LocationUtils.getLatLongFromEXIF(exifInterface);

            if (!LatLong.isEmpty()) {
                String[] separated = LatLong.split(",");

                double latitude = Double.parseDouble(separated[0]);
                double longitude = Double.parseDouble(separated[1]);

                addressDetailModel = LocationUtils.getAddress(activity, latitude, longitude);

                if (addressDetailModel.getAreaName() != null && !addressDetailModel.getAreaName().isEmpty()) {
                    address = address + addressDetailModel.getAreaName();
                }

                if (addressDetailModel.getCityName() != null && !addressDetailModel.getCityName().isEmpty()) {
                    if (address.isEmpty()) {
                        address = address + addressDetailModel.getCityName();
                    } else {
                        address = address + ", " + addressDetailModel.getCityName();
                    }
                }

                if (addressDetailModel.getStateName() != null && !addressDetailModel.getStateName().isEmpty()) {
                    if (address.isEmpty()) {
                        address = address + addressDetailModel.getStateName();
                    } else {
                        address = address + ", " + addressDetailModel.getStateName();
                    }
                }

                if (addressDetailModel.getCountryName() != null && !addressDetailModel.getCountryName().isEmpty()) {
                    if (address.isEmpty()) {
                        address = address + addressDetailModel.getCountryName();
                    } else {
                        address = address + ", " + addressDetailModel.getCountryName();
                    }
                }
            }
        } catch (IOException e) {
            Log.d("--exif--", "onClick: " + e.getMessage());
            e.printStackTrace();
        }

        return address;
    }
}
