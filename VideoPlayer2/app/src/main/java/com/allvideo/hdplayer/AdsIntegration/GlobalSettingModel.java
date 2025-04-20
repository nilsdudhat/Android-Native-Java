package com.allvideo.hdplayer.AdsIntegration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GlobalSettingModel {
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
        @SerializedName("bg-color")
        @Expose
        private String bgColor;
        @SerializedName("font-color")
        @Expose
        private String fontColor;
        @SerializedName("forcefully-update")
        @Expose
        private String forcefullyUpdate;
        @SerializedName("forcefully-update-package")
        @Expose
        private String forcefullyUpdatePackage;

        public String getBgColor() {
            return bgColor;
        }

        public void setBgColor(String bgColor) {
            this.bgColor = bgColor;
        }

        public String getFontColor() {
            return fontColor;
        }

        public void setFontColor(String fontColor) {
            this.fontColor = fontColor;
        }

        public String getForcefullyUpdate() {
            return forcefullyUpdate;
        }

        public void setForcefullyUpdate(String forcefullyUpdate) {
            this.forcefullyUpdate = forcefullyUpdate;
        }

        public String getForcefullyUpdatePackage() {
            return forcefullyUpdatePackage;
        }

        public void setForcefullyUpdatePackage(String forcefullyUpdatePackage) {
            this.forcefullyUpdatePackage = forcefullyUpdatePackage;
        }
    }
}