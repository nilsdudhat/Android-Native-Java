package com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.offlinevideoplayer.video.player.hdvideoplayer.mediaplayer.hdplayer.MyApplication;


public class PreferenceUtil {

    public static final String LAST_SLEEP_TIMER_VALUE = "last_sleep_timer_value";
    public static final String SLEEP_TIMER_FINISH_SONG = "sleep_timer_finish_music";
    public static final String NEXT_SLEEP_TIMER_ELAPSED_REALTIME = "next_sleep_timer_elapsed_real_time";
    public static final String REORDER_TAB = "tab_selection";
    public static final String EQSWITCH = "eqswitch";
    public static final String BAND_LEVEL = "level";
    public static final String SAVE_PRESET = "preset";
    public static final int GAIN_MAX = 100;
    public static final String SAVE_EQ = "Equalizers";
    public static final String BASS_BOOST = "BassBoost";
    public static final String VIRTUAL_BOOST = "VirtualBoost";
    public static final String LOUD_BOOST = "Loud";
    public static final String PRESET_BOOST = "PresetReverb";
    public static final String PRESET_POS = "spinner_position";
    public static final short BASSBOOST_STRENGTH = 1000;
    public static final short Virtualizer_STRENGTH = 1000;
    public static final String VIDEOURL = "videourl";
    public static final String VIDEOPOSITION = "videoposition";
    public static final String AUTOPLAYNEXT = "autoplayon";
    public static final String BATTERYLOCK = "batterylock";
    public static final String GENERAL_THEME = "general_theme";
    private static final String LAST_SPEED = "last_speed";
    private static final String LAST_BRIGHTNESS = "last_brightness";
    private static final String SORT_ORDER = "sort_order";
    private static final String FOLDER_SORT_ORDER = "folder_sort_order";
    private static final String FOLDER_VIEW_TYPE = "folder_view_type";
    private static final String VIEW_TYPE = "view_type";
    private static final String List_VIEW_TYPE = "list_view_type";
    private static final String THEME = "theme";
    private static final String LOCK = "lock";
    private static final String PIN_LOCK = "pin_lock";
    private static final String LOCK_VIDEO = "lock_lock";
    private static final String RECYCLE_VIDEO = "recycle_lock";
    private static final String REPEAT_ONE_VIDEO = "repeat_one";
    private static final String ORIENTATION = "orientation";
    private static final String RESUMEVID = "resumevideo";
    private static final String RESBOOl = "resumebool";
    private static final String BLOCKADS = "block_ads";
    private static PreferenceUtil sInstance;
    private final SharedPreferences mPreferences;

    public PreferenceUtil(@NonNull Context context) {
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferenceUtil getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new PreferenceUtil(context.getApplicationContext());
        }
        return sInstance;
    }

    public static PreferenceUtil getInstance() {
        return sInstance;
    }

    public static int getThemeResFromPrefValue1(String themePrefValue) {
        switch (themePrefValue) {
            case "dark":
                return 1;
            case "light":
                return 0;
        }
        return 0;
    }

    public void saveLastBrightness(float f) {
        this.mPreferences.edit().putFloat(LAST_BRIGHTNESS, f).apply();
    }

    public boolean getBlockads() {
        return this.mPreferences.getBoolean(BLOCKADS, true);
    }

    public void setBlockads(Boolean b) {
        this.mPreferences.edit().putBoolean(BLOCKADS, b).apply();
    }

    public boolean getAutoplaynext() {
        return this.mPreferences.getBoolean(AUTOPLAYNEXT, true);
    }

    public void setAutoplaynext(Boolean b) {
        this.mPreferences.edit().putBoolean(AUTOPLAYNEXT, b).apply();
    }

    public boolean getBatterylock() {
        return this.mPreferences.getBoolean(BATTERYLOCK, true);
    }

    public void setBatterylock(Boolean b) {
        this.mPreferences.edit().putBoolean(BATTERYLOCK, b).apply();
    }

    public float getLastBrightness() {
        return this.mPreferences.getFloat(LAST_BRIGHTNESS, 0.5f);
    }

    public void saveLastSpeed(float f) {
        this.mPreferences.edit().putFloat(LAST_SPEED, f).apply();
    }

    public float getLastSpeed() {
        return this.mPreferences.getFloat(LAST_SPEED, 1.0f);
    }

    public void saveSortOrder(int x) {
        this.mPreferences.edit().putInt(SORT_ORDER, x).apply();
    }

    public int getSortOrder() {
        return this.mPreferences.getInt(SORT_ORDER, 0);
    }

    public void saveOrientation(int i) {
        this.mPreferences.edit().putInt(ORIENTATION, i).apply();
    }

    public int getOrientation() {
        return this.mPreferences.getInt(ORIENTATION, 2);
    }

    public int getResumestatus() {
        return this.mPreferences.getInt(RESUMEVID, 2);
    }

    public void saveResumestatus(int i) {
        this.mPreferences.edit().putInt(RESUMEVID, i).apply();
    }

    public void saveFolderSortOrder(int x) {
        this.mPreferences.edit().putInt(FOLDER_SORT_ORDER, x).apply();
    }

    public int getFolderSortOrder() {
        return this.mPreferences.getInt(FOLDER_SORT_ORDER, 0);
    }

    public void saveFolderAsc(Boolean aBoolean) {
        this.mPreferences.edit().putBoolean(FOLDER_VIEW_TYPE, aBoolean).apply();
    }

    public void saveResumBool(Boolean aBoolean) {
        this.mPreferences.edit().putBoolean(RESBOOl, aBoolean).apply();
    }

    public boolean getResumBool() {
        return this.mPreferences.getBoolean(RESBOOl, true);
    }

    public boolean getFolderAsc() {
        return this.mPreferences.getBoolean(FOLDER_VIEW_TYPE, true);
    }

    public void saveListAsc(Boolean aBoolean) {
        this.mPreferences.edit().putBoolean(List_VIEW_TYPE, aBoolean).apply();
    }

    public boolean getListAsc() {
        return this.mPreferences.getBoolean(List_VIEW_TYPE, true);
    }

    public void saveRepeatOne(Boolean aBoolean) {
        this.mPreferences.edit().putBoolean(REPEAT_ONE_VIDEO, aBoolean).apply();
    }

    public boolean getRepeatOne() {
        return this.mPreferences.getBoolean(REPEAT_ONE_VIDEO, false);
    }

    public void saveViewType(Boolean x) {
        this.mPreferences.edit().putBoolean(VIEW_TYPE, x).apply();
    }

    public boolean getViewType() {
        return this.mPreferences.getBoolean(VIEW_TYPE, true);
    }

    public int getTheme() {
        return this.mPreferences.getInt(THEME, 11);
    }

    public void setTheme(int x) {
        this.mPreferences.edit().putInt(THEME, x).apply();
    }

    public boolean getLock() {
        return this.mPreferences.getBoolean(LOCK, false);
    }

    public void setLock(Boolean x) {
        this.mPreferences.edit().putBoolean(LOCK, x).apply();
    }

    public String getVideoURL() {
        return mPreferences.getString(VIDEOURL, "");
    }

    public void setVideoURL(String videoURL) {
        mPreferences.edit().putString(VIDEOURL, videoURL).apply();
    }

    public String getLockVideo() {
        return mPreferences.getString(LOCK_VIDEO, "");
    }

    public void setLockVideo(String videoURL) {
        mPreferences.edit().putString(LOCK_VIDEO, videoURL).apply();
    }

    public String getRecycleVideo() {
        return mPreferences.getString(RECYCLE_VIDEO, "");
    }

    public void setRecycleVideo(String videoURL) {
        mPreferences.edit().putString(RECYCLE_VIDEO, videoURL).apply();
    }

    public int getVideoPosition() {
        return mPreferences.getInt(VIDEOPOSITION, 0);
    }

    public void setVideoPosition(int videoPosition) {
        mPreferences.edit().putInt(VIDEOPOSITION, videoPosition).apply();
    }

    public void setVideoPosition(String pinLock) {
        mPreferences.edit().putString(PIN_LOCK, pinLock).apply();
    }

    public SharedPreferences saveEq() {
        return MyApplication.getEqPref();
    }

    public void eqSwitch(Boolean torf) {
        SharedPreferences.Editor editor = saveEq().edit();
        editor.putBoolean(PreferenceUtil.EQSWITCH, torf);
        editor.apply();
    }

    public boolean geteqSwitch() {
        return saveEq().getBoolean(PreferenceUtil.EQSWITCH, true);
    }

    public int getPresetPos() {
        return MyApplication.getmPreferences().getInt(PRESET_POS, 0);
    }

    public void savePresetPos(int position) {
        SharedPreferences.Editor editor = MyApplication.getmPreferences().edit();
        editor.putInt(PRESET_POS, position);
        editor.apply();
    }

    public void setResumeVideotime(Context context, Long time, String videoname) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong("r_" + videoname, time);
        editor.apply();
    }

    public long getresumeVideotime(Context context, String video_name) {
        return mPreferences.getLong("r_" + video_name, 0);
    }

    public void setIsPlayVideo(Context context, Boolean isBooleanplay, String videoname) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean("isplay" + videoname, isBooleanplay);
        editor.apply();
    }

    public boolean getIsPlayVideo(Context context, String video_name) {
        return mPreferences.getBoolean("isplay" + video_name, false);
    }

    public String getPinLock() {
        return mPreferences.getString(PIN_LOCK, "");
    }

    public void setGeneralTheme(String theme) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(GENERAL_THEME, theme);
        editor.apply();
    }


    public int getLastSleepTimerValue() {
        return mPreferences.getInt(LAST_SLEEP_TIMER_VALUE, 30);
    }

    public void setLastSleepTimerValue(final int value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(LAST_SLEEP_TIMER_VALUE, value);
        editor.apply();
    }

    public boolean getSleepTimerFinishMusic() {
        return mPreferences.getBoolean(SLEEP_TIMER_FINISH_SONG, false);
    }

    public void setSleepTimerFinishMusic(final boolean value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(SLEEP_TIMER_FINISH_SONG, value);
        editor.apply();
    }

    public long getNextSleepTimerElapsedRealTime() {
        return mPreferences.getLong(NEXT_SLEEP_TIMER_ELAPSED_REALTIME, -1);
    }

    public void setNextSleepTimerElapsedRealtime(final long value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(NEXT_SLEEP_TIMER_ELAPSED_REALTIME, value);
        editor.apply();
    }
}
