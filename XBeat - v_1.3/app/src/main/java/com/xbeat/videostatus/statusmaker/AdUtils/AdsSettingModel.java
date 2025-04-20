package com.xbeat.videostatus.statusmaker.AdUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdsSettingModel {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("counter")
        @Expose
        private String counter;
        @SerializedName("native-counter")
        @Expose
        private String nativeCounter;
        @SerializedName("facebook-ad")
        @Expose
        private String facebookAd;
        @SerializedName("google-ad")
        @Expose
        private String googleAd;
        @SerializedName("app-next")
        @Expose
        private String appNext;
        @SerializedName("facebook-banner")
        @Expose
        private String facebookBanner;
        @SerializedName("facebook-native")
        @Expose
        private String facebookNative;
        @SerializedName("facebook-int")
        @Expose
        private String facebookInt;
        @SerializedName("google-banner")
        @Expose
        private String googleBanner;
        @SerializedName("google-native")
        @Expose
        private String googleNative;
        @SerializedName("google-int")
        @Expose
        private String googleInt;
        @SerializedName("google-reward")
        @Expose
        private String googleReward;
        @SerializedName("app-next-banner")
        @Expose
        private String appNextBanner;
        @SerializedName("app-next-int")
        @Expose
        private String appNextInt;
        @SerializedName("app-next-native")
        @Expose
        private String appNextNative;

        @SerializedName("is_qureka")
        @Expose
        private String isQureka;
        @SerializedName("qureka-id")
        @Expose
        private String qurekaId;

        public String getIsQureka() {
            return isQureka;
        }

        public void setIsQureka(String isQureka) {
            this.isQureka = isQureka;
        }

        public String getQurekaId() {
            return qurekaId;
        }

        public void setQurekaId(String qurekaId) {
            this.qurekaId = qurekaId;
        }
        public String getCounter() {
            return counter;
        }

        public void setCounter(String counter) {
            this.counter = counter;
        }

        public String getNativeCounter() {
            return nativeCounter;
        }

        public void setNativeCounter(String nativeCounter) {
            this.nativeCounter = nativeCounter;
        }

        public String getFacebookAd() {
            return facebookAd;
        }

        public void setFacebookAd(String facebookAd) {
            this.facebookAd = facebookAd;
        }

        public String getGoogleAd() {
            return googleAd;
        }

        public void setGoogleAd(String googleAd) {
            this.googleAd = googleAd;
        }

        public String getAppNext() {
            return appNext;
        }

        public void setAppNext(String appNext) {
            this.appNext = appNext;
        }

        public String getFacebookBanner() {
            return facebookBanner;
        }

        public void setFacebookBanner(String facebookBanner) {
            this.facebookBanner = facebookBanner;
        }

        public String getFacebookNative() {
            return facebookNative;
        }

        public void setFacebookNative(String facebookNative) {
            this.facebookNative = facebookNative;
        }

        public String getFacebookInt() {
            return facebookInt;
        }

        public void setFacebookInt(String facebookInt) {
            this.facebookInt = facebookInt;
        }

        public String getGoogleBanner() {
            return googleBanner;
        }

        public void setGoogleBanner(String googleBanner) {
            this.googleBanner = googleBanner;
        }

        public String getGoogleNative() {
            return googleNative;
        }

        public void setGoogleNative(String googleNative) {
            this.googleNative = googleNative;
        }

        public String getGoogleInt() {
            return googleInt;
        }

        public void setGoogleInt(String googleInt) {
            this.googleInt = googleInt;
        }

        public String getGoogleReward() {
            return googleReward;
        }

        public void setGoogleReward(String googleReward) {
            this.googleReward = googleReward;
        }

        public String getAppNextBanner() {
            return appNextBanner;
        }

        public void setAppNextBanner(String appNextBanner) {
            this.appNextBanner = appNextBanner;
        }

        public String getAppNextInt() {
            return appNextInt;
        }

        public void setAppNextInt(String appNextInt) {
            this.appNextInt = appNextInt;
        }

        public String getAppNextNative() {
            return appNextNative;
        }

        public void setAppNextNative(String appNextNative) {
            this.appNextNative = appNextNative;
        }
    }
}