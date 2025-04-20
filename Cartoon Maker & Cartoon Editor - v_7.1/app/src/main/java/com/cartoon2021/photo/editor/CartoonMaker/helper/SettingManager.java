package com.cartoon2021.photo.editor.CartoonMaker.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingManager {
    public static final int LAYER_000_FRAME = 0;
    public static final int LAYER_001_SHAPE = 1;
    public static final int LAYER_002_SMILEY = 2;
    public static final int LAYER_003_EYES = 3;
    public static final int LAYER_004_EYEBROW = 4;
    public static final int LAYER_005_MOUTH = 5;
    public static final int LAYER_006_NOSE = 6;
    public static final int LAYER_007_HAIR = 7;
    public static final int LAYER_008_GOGGLE = 8;
    public static final int LAYER_009_HAT = 9;
    public static final int LAYER_010_DRESS = 10;
    public static final int LAYER_011_HANDS = 11;
    public static final int LAYER_012_BACKG = 12;
    private static SettingManager sInstance;
    private String PREF_NAME = "setting";
    private SharedPreferences mPref;

    public static SettingManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SettingManager(context);
        }
        return sInstance;
    }

    private SettingManager(Context context) {
        this.mPref = context.getSharedPreferences("setting", 0);
    }

    public int getSelectedLayer(int i) {
        return this.mPref.getInt("save" + String.valueOf(i) + "selected_layer", 2);
    }

    public synchronized void setSelectedLayer(int i, int i2) {
        this.mPref.edit().putInt("save" + String.valueOf(i) + "selected_layer", i2).apply();
    }

    public int getLayerItem(int i, int i2) {
        return this.mPref.getInt("save" + String.valueOf(i) + "layer_" + String.valueOf(i2), i2 == 12 ? 0 : i + 1);
    }
}
